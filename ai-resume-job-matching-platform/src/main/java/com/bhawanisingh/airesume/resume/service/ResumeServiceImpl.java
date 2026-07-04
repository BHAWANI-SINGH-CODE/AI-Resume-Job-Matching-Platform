package com.bhawanisingh.airesume.resume.service;

import com.bhawanisingh.airesume.auth.entity.User;
import com.bhawanisingh.airesume.auth.repository.UserRepository;
import com.bhawanisingh.airesume.common.exception.ResourceNotFoundException;
import com.bhawanisingh.airesume.common.util.FileUtil;
import com.bhawanisingh.airesume.resume.dto.ResumeResponse;
import com.bhawanisingh.airesume.resume.dto.ResumeUploadResponse;
import com.bhawanisingh.airesume.resume.entity.Resume;
import com.bhawanisingh.airesume.resume.enums.ResumeStatus;
import com.bhawanisingh.airesume.resume.repository.ResumeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class ResumeServiceImpl implements ResumeService {

    private final ResumeRepository resumeRepository;
    private final UserRepository userRepository;
    private final FileUtil fileUtil;

    @Value("${app.file.upload-dir}")
    private String uploadDir;

    @Override
    public ResumeUploadResponse uploadResume(MultipartFile file, String userEmail) {

        User user = getUserByEmail(userEmail);

        String extension = fileUtil.validateAndGetExtension(file);
        String storedFileName = fileUtil.generateStoredFileName(extension);
        String savedFilePath = fileUtil.saveFile(file, uploadDir, storedFileName);

        boolean isFirstResume =
                resumeRepository.countByUserAndStatus(user, ResumeStatus.ACTIVE) == 0;

        Resume resume = Resume.builder()
                .originalFileName(file.getOriginalFilename())
                .storedFileName(storedFileName)
                .filePath(savedFilePath)
                .fileType(file.getContentType() != null ? file.getContentType() : extension)
                .fileSize(file.getSize())
                .primaryResume(isFirstResume)
                .status(ResumeStatus.ACTIVE)
                .user(user)
                .build();

        Resume savedResume = resumeRepository.save(resume);

        return ResumeUploadResponse.builder()
                .resumeId(savedResume.getId())
                .fileName(savedResume.getOriginalFileName())
                .fileType(savedResume.getFileType())
                .fileSize(savedResume.getFileSize())
                .primaryResume(savedResume.getPrimaryResume())
                .message("Resume uploaded successfully")
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ResumeResponse> getMyResumes(String userEmail) {

        User user = getUserByEmail(userEmail);

        List<Resume> resumes = resumeRepository.findByUserAndStatusOrderByCreatedAtDesc(
                user,
                ResumeStatus.ACTIVE
        );

        return resumes.stream()
                .map(this::mapToResumeResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ResumeResponse getResumeById(UUID resumeId, String userEmail) {

        User user = getUserByEmail(userEmail);
        Resume resume = getActiveResumeByIdAndUser(resumeId, user);

        return mapToResumeResponse(resume);
    }

    @Override
    @Transactional
    public void deleteResume(UUID resumeId, String userEmail) {

        Resume resume = resumeRepository.findByIdAndUserEmailAndStatus(
                        resumeId,
                        userEmail,
                        ResumeStatus.ACTIVE
                )
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Resume not found with id: " + resumeId
                ));

        User user = resume.getUser();
        boolean wasPrimary = Boolean.TRUE.equals(resume.getPrimaryResume());

        // soft delete in DB
        resume.setStatus(ResumeStatus.DELETED);
        resume.setPrimaryResume(false);
        resumeRepository.save(resume);

        // physical file delete attempt
        deleteResumeFileIfExists(resume.getFilePath());

        // if deleted resume was primary, assign another active resume as primary
        if (wasPrimary) {
            List<Resume> activeResumes =
                    resumeRepository.findByUserAndStatusOrderByCreatedAtDesc(
                            user,
                            ResumeStatus.ACTIVE
                    );

            if (!activeResumes.isEmpty()) {
                Resume nextPrimary = activeResumes.get(0);
                nextPrimary.setPrimaryResume(true);
                resumeRepository.save(nextPrimary);
            }
        }
    }

    @Override
    public ResumeResponse markAsPrimary(UUID resumeId, String userEmail) {

        User user = getUserByEmail(userEmail);
        Resume targetResume = getActiveResumeByIdAndUser(resumeId, user);

        resumeRepository.findByUserAndPrimaryResumeTrueAndStatus(user, ResumeStatus.ACTIVE)
                .ifPresent(existingPrimary -> {
                    existingPrimary.setPrimaryResume(false);
                    resumeRepository.save(existingPrimary);
                });

        targetResume.setPrimaryResume(true);
        Resume savedResume = resumeRepository.save(targetResume);

        return mapToResumeResponse(savedResume);
    }

    // =========================================================
    // Helper Methods
    // =========================================================

    private User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found with email: " + email));
    }

    private Resume getActiveResumeByIdAndUser(UUID resumeId, User user) {
        Resume resume = resumeRepository.findByIdAndUser(resumeId, user)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Resume not found with id: " + resumeId));

        if (resume.getStatus() != ResumeStatus.ACTIVE) {
            throw new ResourceNotFoundException("Resume not found with id: " + resumeId);
        }

        return resume;
    }

    private ResumeResponse mapToResumeResponse(Resume resume) {
        return ResumeResponse.builder()
                .id(resume.getId())
                .originalFileName(resume.getOriginalFileName())
                .storedFileName(resume.getStoredFileName())
                .filePath(resume.getFilePath())
                .fileType(resume.getFileType())
                .fileSize(resume.getFileSize())
                .primaryResume(resume.getPrimaryResume())
                .status(resume.getStatus())
                .createdAt(resume.getCreatedAt())
                .updatedAt(resume.getUpdatedAt())
                .build();
    }

    private void deleteResumeFileIfExists(String filePath) {
        if (filePath == null || filePath.isBlank()) {
            return;
        }

        try {
            Path path = Paths.get(filePath).toAbsolutePath().normalize();
            Files.deleteIfExists(path);
        } catch (Exception ex) {
            // intentionally swallow exception for now
            // DB delete should not fail just because physical file delete failed
        }
    }
}
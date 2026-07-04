package com.bhawanisingh.airesume.resume.service;

import com.bhawanisingh.airesume.resume.dto.ResumeResponse;
import com.bhawanisingh.airesume.resume.dto.ResumeUploadResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface ResumeService {

    ResumeUploadResponse uploadResume(MultipartFile file, String userEmail);

    List<ResumeResponse> getMyResumes(String userEmail);

    ResumeResponse getResumeById(UUID resumeId, String userEmail);

    void deleteResume(UUID resumeId, String userEmail);

    ResumeResponse markAsPrimary(UUID resumeId, String userEmail);
}
package com.bhawanisingh.airesume.resume.controller;

import com.bhawanisingh.airesume.common.response.ApiResponse;
import com.bhawanisingh.airesume.resume.dto.ResumeResponse;
import com.bhawanisingh.airesume.resume.dto.ResumeUploadResponse;
import com.bhawanisingh.airesume.resume.service.ResumeService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/resumes")
@RequiredArgsConstructor
public class ResumeController {

    private final ResumeService resumeService;

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<ResumeUploadResponse>> uploadResume(
            @RequestParam("file") MultipartFile file,
            Authentication authentication
    ) {
        String userEmail = authentication.getName();

        ResumeUploadResponse response = resumeService.uploadResume(file, userEmail);

        return ResponseEntity.ok(
                ApiResponse.success("Resume uploaded successfully", response)
        );
    }

    @GetMapping("/my")
    public ResponseEntity<ApiResponse<List<ResumeResponse>>> getMyResumes(
            Authentication authentication
    ) {
        String userEmail = authentication.getName();

        List<ResumeResponse> resumes = resumeService.getMyResumes(userEmail);

        return ResponseEntity.ok(
                ApiResponse.success("Resumes fetched successfully", resumes)
        );
    }

    @GetMapping("/{resumeId}")
    public ResponseEntity<ApiResponse<ResumeResponse>> getResumeById(
            @PathVariable UUID resumeId,
            Authentication authentication
    ) {
        String userEmail = authentication.getName();

        ResumeResponse resume = resumeService.getResumeById(resumeId, userEmail);

        return ResponseEntity.ok(
                ApiResponse.success("Resume fetched successfully", resume)
        );
    }

    @DeleteMapping("/{resumeId}")
    public ResponseEntity<ApiResponse<String>> deleteResume(
            @PathVariable UUID resumeId,
            Authentication authentication
    ) {
        String userEmail = authentication.getName();

        resumeService.deleteResume(resumeId, userEmail);

        return ResponseEntity.ok(
                ApiResponse.success("Resume deleted successfully", "Deleted")
        );
    }

    @PatchMapping("/{resumeId}/primary")
    public ResponseEntity<ApiResponse<ResumeResponse>> markResumeAsPrimary(
            @PathVariable UUID resumeId,
            Authentication authentication
    ) {
        String userEmail = authentication.getName();

        ResumeResponse updatedResume = resumeService.markAsPrimary(resumeId, userEmail);

        return ResponseEntity.ok(
                ApiResponse.success("Primary resume updated successfully", updatedResume)
        );
    }

    @GetMapping("/{resumeId}/download")
    public ResponseEntity<Resource> downloadResume(
            @PathVariable UUID resumeId,
            Authentication authentication
    ) throws MalformedURLException {

        String userEmail = authentication.getName();

        ResumeResponse resume = resumeService.getResumeById(resumeId, userEmail);

        Path filePath = Paths.get(resume.getFilePath()).toAbsolutePath().normalize();
        Resource resource = new UrlResource(filePath.toUri());

        if (!resource.exists() || !resource.isReadable()) {
            throw new RuntimeException("Resume file not found on server");
        }

        String contentType = resume.getFileType();
        if (contentType == null || contentType.isBlank()) {
            contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        ContentDisposition.attachment()
                                .filename(resume.getOriginalFileName())
                                .build()
                                .toString()
                )
                .body(resource);
    }
}
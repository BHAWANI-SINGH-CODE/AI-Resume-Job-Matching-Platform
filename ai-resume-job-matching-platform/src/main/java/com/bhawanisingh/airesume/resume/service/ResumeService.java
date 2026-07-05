package com.bhawanisingh.airesume.resume.service;

import com.bhawanisingh.airesume.resume.dto.ResumeResponse;
import com.bhawanisingh.airesume.resume.dto.ResumeUploadResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


public interface ResumeService {

    ResumeUploadResponse uploadResume(MultipartFile file, String userEmail);

    List<ResumeResponse> getMyResumes(String userEmail);

    ResumeResponse getResumeById(Long resumeId, String userEmail);

    void deleteResume(Long resumeId, String userEmail);

    ResumeResponse markAsPrimary(Long resumeId, String userEmail);
}
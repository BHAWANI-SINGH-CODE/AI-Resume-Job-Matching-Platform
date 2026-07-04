package com.bhawanisingh.airesume.resume.dto;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResumeUploadResponse {

    private UUID resumeId;
    private String fileName;
    private String fileType;
    private Long fileSize;
    private Boolean primaryResume;
    private String message;
}
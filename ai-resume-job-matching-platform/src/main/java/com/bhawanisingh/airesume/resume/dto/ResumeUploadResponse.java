package com.bhawanisingh.airesume.resume.dto;

import lombok.*;



@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResumeUploadResponse {

    private Long resumeId;
    private String fileName;
    private String fileType;
    private Long fileSize;
    private Boolean primaryResume;
    private String message;
}
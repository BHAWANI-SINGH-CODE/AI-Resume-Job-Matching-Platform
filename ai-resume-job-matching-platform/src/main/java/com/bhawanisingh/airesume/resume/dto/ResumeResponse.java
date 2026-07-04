package com.bhawanisingh.airesume.resume.dto;

import com.bhawanisingh.airesume.resume.enums.ResumeStatus;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResumeResponse {

    private UUID id;
    private String originalFileName;
    private String storedFileName;
    private String filePath;
    private String fileType;
    private Long fileSize;
    private Boolean primaryResume;
    private ResumeStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
package com.bhawanisingh.airesume.application.dto.response;

import com.bhawanisingh.airesume.application.entity.ApplicationStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class JobApplicationSummaryResponse {

    private Long applicationId;
    private ApplicationStatus status;
    private String coverLetter;
    private LocalDateTime appliedAt;

    private Long candidateId;
    private String candidateName;
    private String candidateEmail;

    private Long jobId;
    private String jobTitle;

    private Long resumeId;
    private String resumeFileName;
}
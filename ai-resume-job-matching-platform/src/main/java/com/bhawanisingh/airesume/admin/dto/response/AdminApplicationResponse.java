package com.bhawanisingh.airesume.admin.dto.response;

import com.bhawanisingh.airesume.application.entity.ApplicationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminApplicationResponse {

    private Long id;
    private String coverLetter;
    private ApplicationStatus status;
    private LocalDateTime appliedAt;

    private Long candidateId;
    private String candidateName;
    private String candidateEmail;

    private Long jobId;
    private String jobTitle;
    private String companyName;
    private Long jobPostedByUserId;

    private Long resumeId;
}
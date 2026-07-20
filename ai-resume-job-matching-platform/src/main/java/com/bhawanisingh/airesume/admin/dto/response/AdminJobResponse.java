package com.bhawanisingh.airesume.admin.dto.response;

import com.bhawanisingh.airesume.job.enums.EmploymentType;
import com.bhawanisingh.airesume.job.enums.ExperienceLevel;
import com.bhawanisingh.airesume.job.enums.JobStatus;
import com.bhawanisingh.airesume.job.enums.WorkMode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminJobResponse {

    private Long id;

    private String title;

    private String companyName;

    private String location;

    private String description;

    private String requirements;

    private BigDecimal salaryMin;

    private BigDecimal salaryMax;

    private EmploymentType employmentType;

    private ExperienceLevel experienceLevel;

    private WorkMode workMode;

    private JobStatus status;

    private LocalDate applicationDeadline;

    private String externalApplyUrl;

    private Long postedByUserId;

    private boolean deleted;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
package com.bhawanisingh.airesume.admin.mapper;

import com.bhawanisingh.airesume.admin.dto.response.AdminApplicationResponse;
import com.bhawanisingh.airesume.admin.dto.response.AdminCompanyResponse;
import com.bhawanisingh.airesume.admin.dto.response.AdminJobResponse;
import com.bhawanisingh.airesume.admin.dto.response.AdminUserResponse;
import com.bhawanisingh.airesume.application.entity.Application;
import com.bhawanisingh.airesume.auth.entity.User;
import com.bhawanisingh.airesume.company.entity.Company;
import com.bhawanisingh.airesume.job.entity.Job;

public final class AdminMapper {

    private AdminMapper() {
    }

    public static AdminUserResponse toAdminUserResponse(User user) {
        if (user == null) {
            return null;
        }

        return AdminUserResponse.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .role(user.getRole())
                .status(user.getStatus())
                .emailVerified(user.getEmailVerified())
                .profileImageUrl(user.getProfileImageUrl())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }

    public static AdminCompanyResponse toAdminCompanyResponse(Company company) {
        if (company == null) {
            return null;
        }

        return AdminCompanyResponse.builder()
                .id(company.getId())
                .name(company.getName())
                .website(company.getWebsite())
                .industry(company.getIndustry())
                .location(company.getLocation())
                .description(company.getDescription())
                .logoUrl(company.getLogoUrl())
                .size(company.getSize())
                .foundedYear(company.getFoundedYear())
                .createdByUserId(company.getCreatedByUserId())
                .deleted(company.isDeleted())
                .createdAt(company.getCreatedAt())
                .updatedAt(company.getUpdatedAt())
                .build();
    }

    public static AdminJobResponse toAdminJobResponse(Job job) {
        if (job == null) {
            return null;
        }

        return AdminJobResponse.builder()
                .id(job.getId())
                .title(job.getTitle())
                .companyName(job.getCompanyName())
                .location(job.getLocation())
                .description(job.getDescription())
                .requirements(job.getRequirements())
                .salaryMin(job.getSalaryMin())
                .salaryMax(job.getSalaryMax())
                .employmentType(job.getEmploymentType())
                .experienceLevel(job.getExperienceLevel())
                .workMode(job.getWorkMode())
                .status(job.getStatus())
                .applicationDeadline(job.getApplicationDeadline())
                .externalApplyUrl(job.getExternalApplyUrl())
                .postedByUserId(job.getPostedByUserId())
                .deleted(job.isDeleted())
                .createdAt(job.getCreatedAt())
                .updatedAt(job.getUpdatedAt())
                .build();
    }

    public static AdminApplicationResponse toAdminApplicationResponse(Application application) {
        if (application == null) {
            return null;
        }

        return AdminApplicationResponse.builder()
                .id(application.getId())
                .coverLetter(application.getCoverLetter())
                .status(application.getStatus())
                .appliedAt(application.getAppliedAt())

                .candidateId(application.getCandidate() != null ? application.getCandidate().getId() : null)
                .candidateName(application.getCandidate() != null ? application.getCandidate().getFullName() : null)
                .candidateEmail(application.getCandidate() != null ? application.getCandidate().getEmail() : null)

                .jobId(application.getJob() != null ? application.getJob().getId() : null)
                .jobTitle(application.getJob() != null ? application.getJob().getTitle() : null)
                .companyName(application.getJob() != null ? application.getJob().getCompanyName() : null)
                .jobPostedByUserId(application.getJob() != null ? application.getJob().getPostedByUserId() : null)

                .resumeId(application.getResume() != null ? application.getResume().getId() : null)
                .build();
    }
}
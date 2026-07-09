package com.bhawanisingh.airesume.application.mapper;

import com.bhawanisingh.airesume.application.dto.response.ApplicationResponse;
import com.bhawanisingh.airesume.application.dto.response.JobApplicationSummaryResponse;
import com.bhawanisingh.airesume.application.entity.Application;
import com.bhawanisingh.airesume.auth.entity.User;
import com.bhawanisingh.airesume.job.entity.Job;
import com.bhawanisingh.airesume.resume.entity.Resume;
import org.springframework.stereotype.Component;

@Component
public class ApplicationMapper {

    public ApplicationResponse toApplicationResponse(Application application) {
        User candidate = application.getCandidate();
        Job job = application.getJob();
        Resume resume = application.getResume();

        return ApplicationResponse.builder()
                .applicationId(application.getId())
                .status(application.getStatus())
                .coverLetter(application.getCoverLetter())
                .appliedAt(application.getAppliedAt())
                .candidateId(candidate != null ? candidate.getId() : null)
                .candidateName(candidate != null ? candidate.getFullName() : null)
                .candidateEmail(candidate != null ? candidate.getEmail() : null)
                .jobId(job != null ? job.getId() : null)
                .jobTitle(job != null ? job.getTitle() : null)
                .companyName(job != null ? job.getCompanyName() : null)
                .resumeId(resume != null ? resume.getId() : null)
                .resumeFileName(resume != null ? resume.getOriginalFileName() : null)
                .build();
    }

    public JobApplicationSummaryResponse toJobApplicationSummaryResponse(Application application) {
        User candidate = application.getCandidate();
        Job job = application.getJob();
        Resume resume = application.getResume();

        return JobApplicationSummaryResponse.builder()
                .applicationId(application.getId())
                .status(application.getStatus())
                .coverLetter(application.getCoverLetter())
                .appliedAt(application.getAppliedAt())
                .candidateId(candidate != null ? candidate.getId() : null)
                .candidateName(candidate != null ? candidate.getFullName() : null)
                .candidateEmail(candidate != null ? candidate.getEmail() : null)
                .jobId(job != null ? job.getId() : null)
                .jobTitle(job != null ? job.getTitle() : null)
                .resumeId(resume != null ? resume.getId() : null)
                .resumeFileName(resume != null ? resume.getOriginalFileName() : null)
                .build();
    }
}
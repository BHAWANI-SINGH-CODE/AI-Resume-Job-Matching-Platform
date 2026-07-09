package com.bhawanisingh.airesume.application.service;

import com.bhawanisingh.airesume.application.dto.request.ApplicationStatusUpdateRequest;
import com.bhawanisingh.airesume.application.dto.request.JobApplicationRequest;
import com.bhawanisingh.airesume.application.dto.response.ApplicationResponse;
import com.bhawanisingh.airesume.application.dto.response.JobApplicationSummaryResponse;
import com.bhawanisingh.airesume.application.dto.response.RecruiterApplicationDashboardResponse;
import com.bhawanisingh.airesume.application.entity.ApplicationStatus;

import java.util.List;

public interface ApplicationService {

    ApplicationResponse applyToJob(Long jobId, JobApplicationRequest request);

    List<ApplicationResponse> getMyApplications();

    ApplicationResponse getMyApplicationById(Long applicationId);

    List<JobApplicationSummaryResponse> getApplicationsByJobId(Long jobId);

    ApplicationResponse updateApplicationStatus(Long applicationId, ApplicationStatusUpdateRequest request);

    List<JobApplicationSummaryResponse> getMyPostedJobApplications();

    List<JobApplicationSummaryResponse> getMyPostedJobApplicationsByStatus(ApplicationStatus status);

    RecruiterApplicationDashboardResponse getMyApplicationDashboard();
}
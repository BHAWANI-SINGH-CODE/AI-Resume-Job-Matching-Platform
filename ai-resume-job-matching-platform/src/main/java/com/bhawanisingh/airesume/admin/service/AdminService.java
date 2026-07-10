package com.bhawanisingh.airesume.admin.service;

import com.bhawanisingh.airesume.admin.dto.request.AdminUserStatusUpdateRequest;
import com.bhawanisingh.airesume.admin.dto.response.AdminApplicationPageResponse;
import com.bhawanisingh.airesume.admin.dto.response.AdminCompanyPageResponse;
import com.bhawanisingh.airesume.admin.dto.response.AdminDashboardSummaryResponse;
import com.bhawanisingh.airesume.admin.dto.response.AdminJobPageResponse;
import com.bhawanisingh.airesume.admin.dto.response.AdminUserPageResponse;
import com.bhawanisingh.airesume.admin.dto.response.AdminUserResponse;
import com.bhawanisingh.airesume.application.entity.ApplicationStatus;
import com.bhawanisingh.airesume.auth.enums.Role;
import com.bhawanisingh.airesume.auth.enums.UserStatus;
import com.bhawanisingh.airesume.job.enums.JobStatus;

public interface AdminService {

    AdminDashboardSummaryResponse getDashboardSummary();

    AdminUserPageResponse getUsers(
            String search,
            Role role,
            UserStatus status,
            int page,
            int size
    );

    AdminUserResponse updateUserStatus(
            Long userId,
            AdminUserStatusUpdateRequest request
    );

    AdminCompanyPageResponse getCompanies(
            String search,
            Boolean deleted,
            int page,
            int size
    );

    void softDeleteCompany(Long companyId);

    void restoreCompany(Long companyId);

    AdminJobPageResponse getJobs(
            String search,
            JobStatus status,
            Boolean deleted,
            int page,
            int size
    );

    void softDeleteJob(Long jobId);

    void restoreJob(Long jobId);

    AdminApplicationPageResponse getApplications(
            String search,
            ApplicationStatus status,
            int page,
            int size
    );
}
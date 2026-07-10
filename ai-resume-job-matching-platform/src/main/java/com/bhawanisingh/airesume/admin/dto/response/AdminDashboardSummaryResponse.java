package com.bhawanisingh.airesume.admin.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminDashboardSummaryResponse {

    private long totalUsers;          // total user accounts
    private long totalAdminAccounts;  // Role.ADMIN
    private long totalCompanyAccounts; // Role.COMPANY
    private long totalCandidateAccounts; // Role.USER

    private long totalCompanies;
    private long totalJobs;
    private long totalApplications;
    private long totalResumes;
    private long totalParsedResumes;
}
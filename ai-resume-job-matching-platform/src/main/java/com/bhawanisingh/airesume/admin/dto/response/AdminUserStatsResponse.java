package com.bhawanisingh.airesume.admin.dto.response;

public class AdminUserStatsResponse {

    private long totalUsers;
    private long totalCandidates;
    private long totalCompanies;
    private long totalAdmins;
    private long activeUsers;
    private long inactiveUsers;
    private long blockedUsers;
    private long pendingVerificationUsers;

    public AdminUserStatsResponse() {
    }

    public AdminUserStatsResponse(long totalUsers,
                                  long totalCandidates,
                                  long totalCompanies,
                                  long totalAdmins,
                                  long activeUsers,
                                  long inactiveUsers,
                                  long blockedUsers,
                                  long pendingVerificationUsers) {
        this.totalUsers = totalUsers;
        this.totalCandidates = totalCandidates;
        this.totalCompanies = totalCompanies;
        this.totalAdmins = totalAdmins;
        this.activeUsers = activeUsers;
        this.inactiveUsers = inactiveUsers;
        this.blockedUsers = blockedUsers;
        this.pendingVerificationUsers = pendingVerificationUsers;
    }

    public long getTotalUsers() {
        return totalUsers;
    }

    public long getTotalCandidates() {
        return totalCandidates;
    }

    public long getTotalCompanies() {
        return totalCompanies;
    }

    public long getTotalAdmins() {
        return totalAdmins;
    }

    public long getActiveUsers() {
        return activeUsers;
    }

    public long getInactiveUsers() {
        return inactiveUsers;
    }

    public long getBlockedUsers() {
        return blockedUsers;
    }

    public long getPendingVerificationUsers() {
        return pendingVerificationUsers;
    }
}
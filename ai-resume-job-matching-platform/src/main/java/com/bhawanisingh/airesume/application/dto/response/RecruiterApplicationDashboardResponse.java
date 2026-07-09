package com.bhawanisingh.airesume.application.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RecruiterApplicationDashboardResponse {

    private long totalApplications;
    private long appliedCount;
    private long reviewedCount;
    private long shortlistedCount;
    private long rejectedCount;
    private long hiredCount;
}
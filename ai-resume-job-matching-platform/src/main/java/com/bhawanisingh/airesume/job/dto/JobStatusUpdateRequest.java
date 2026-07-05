package com.bhawanisingh.airesume.job.dto;

import com.bhawanisingh.airesume.job.enums.JobStatus;
import jakarta.validation.constraints.NotNull;

public class JobStatusUpdateRequest {

    @NotNull(message = "Job status is required")
    private JobStatus status;

    public JobStatusUpdateRequest() {
    }

    public JobStatus getStatus() {
        return status;
    }

    public void setStatus(JobStatus status) {
        this.status = status;
    }
}
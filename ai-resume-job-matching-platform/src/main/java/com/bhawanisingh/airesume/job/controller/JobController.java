package com.bhawanisingh.airesume.job.controller;

import com.bhawanisingh.airesume.common.response.ApiResponse;
import com.bhawanisingh.airesume.job.dto.JobCreateRequest;
import com.bhawanisingh.airesume.job.dto.JobResponse;
import com.bhawanisingh.airesume.job.dto.JobStatusUpdateRequest;
import com.bhawanisingh.airesume.job.dto.JobUpdateRequest;
import com.bhawanisingh.airesume.job.service.JobService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/jobs")
public class JobController {

    private final JobService jobService;

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<JobResponse>> createJob(@Valid @RequestBody JobCreateRequest request,
                                                              Authentication authentication) {
        Long postedByUserId = extractUserId(authentication);
        JobResponse response = jobService.createJob(request, postedByUserId);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Job created successfully", response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<JobResponse>>> getAllOpenJobs() {
        List<JobResponse> jobs = jobService.getAllOpenJobs();
        return ResponseEntity.ok(ApiResponse.success("Open jobs fetched successfully", jobs));
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<JobResponse>>> getAllJobs() {
        List<JobResponse> jobs = jobService.getAllJobs();
        return ResponseEntity.ok(ApiResponse.success("Jobs fetched successfully", jobs));
    }

    @GetMapping("/{jobId}")
    public ResponseEntity<ApiResponse<JobResponse>> getJobById(@PathVariable Long jobId) {
        JobResponse response = jobService.getJobById(jobId);
        return ResponseEntity.ok(ApiResponse.success("Job fetched successfully", response));
    }

    @PutMapping("/{jobId}")
    public ResponseEntity<ApiResponse<JobResponse>> updateJob(@PathVariable Long jobId,
                                                              @Valid @RequestBody JobUpdateRequest request) {
        JobResponse response = jobService.updateJob(jobId, request);
        return ResponseEntity.ok(ApiResponse.success("Job updated successfully", response));
    }

    @PatchMapping("/{jobId}/status")
    public ResponseEntity<ApiResponse<JobResponse>> updateJobStatus(@PathVariable Long jobId,
                                                                    @Valid @RequestBody JobStatusUpdateRequest request) {
        JobResponse response = jobService.updateJobStatus(jobId, request);
        return ResponseEntity.ok(ApiResponse.success("Job status updated successfully", response));
    }

    @DeleteMapping("/{jobId}")
    public ResponseEntity<ApiResponse<Void>> deleteJob(@PathVariable Long jobId) {
        jobService.deleteJob(jobId);
        return ResponseEntity.ok(ApiResponse.success("Job deleted successfully", null));
    }

    private Long extractUserId(Authentication authentication) {
        if (authentication == null || authentication.getName() == null) {
            return null;
        }

        try {
            return Long.parseLong(authentication.getName());
        } catch (NumberFormatException ex) {
            return null;
        }
    }
}
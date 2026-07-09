package com.bhawanisingh.airesume.application.controller;

import com.bhawanisingh.airesume.application.dto.request.ApplicationStatusUpdateRequest;
import com.bhawanisingh.airesume.application.dto.request.JobApplicationRequest;
import com.bhawanisingh.airesume.application.dto.response.ApplicationResponse;
import com.bhawanisingh.airesume.application.dto.response.JobApplicationSummaryResponse;
import com.bhawanisingh.airesume.application.dto.response.RecruiterApplicationDashboardResponse;
import com.bhawanisingh.airesume.application.entity.ApplicationStatus;
import com.bhawanisingh.airesume.application.service.ApplicationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/applications")
@RequiredArgsConstructor
public class ApplicationController {

    private final ApplicationService applicationService;

    @PostMapping("/jobs/{jobId}")
    public ResponseEntity<ApplicationResponse> applyToJob(
            @PathVariable Long jobId,
            @Valid @RequestBody JobApplicationRequest request
    ) {
        ApplicationResponse response = applicationService.applyToJob(jobId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/me")
    public ResponseEntity<List<ApplicationResponse>> getMyApplications() {
        return ResponseEntity.ok(applicationService.getMyApplications());
    }

    @GetMapping("/me/{applicationId}")
    public ResponseEntity<ApplicationResponse> getMyApplicationById(@PathVariable Long applicationId) {
        return ResponseEntity.ok(applicationService.getMyApplicationById(applicationId));
    }

    @GetMapping("/jobs/{jobId}")
    public ResponseEntity<List<JobApplicationSummaryResponse>> getApplicationsByJobId(@PathVariable Long jobId) {
        return ResponseEntity.ok(applicationService.getApplicationsByJobId(jobId));
    }

    @PatchMapping("/{applicationId}/status")
    public ResponseEntity<ApplicationResponse> updateApplicationStatus(
            @PathVariable Long applicationId,
            @Valid @RequestBody ApplicationStatusUpdateRequest request
    ) {
        return ResponseEntity.ok(applicationService.updateApplicationStatus(applicationId, request));
    }

    @GetMapping("/company/me")
    public ResponseEntity<List<JobApplicationSummaryResponse>> getMyPostedJobApplications() {
        return ResponseEntity.ok(applicationService.getMyPostedJobApplications());
    }

    @GetMapping("/company/me/status/{status}")
    public ResponseEntity<List<JobApplicationSummaryResponse>> getMyPostedJobApplicationsByStatus(
            @PathVariable ApplicationStatus status
    ) {
        return ResponseEntity.ok(applicationService.getMyPostedJobApplicationsByStatus(status));
    }

    @GetMapping("/company/me/dashboard")
    public ResponseEntity<RecruiterApplicationDashboardResponse> getMyApplicationDashboard() {
        return ResponseEntity.ok(applicationService.getMyApplicationDashboard());
    }
}
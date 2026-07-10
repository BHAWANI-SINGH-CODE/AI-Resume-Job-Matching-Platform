package com.bhawanisingh.airesume.admin.controller;

import com.bhawanisingh.airesume.admin.dto.request.AdminUserStatusUpdateRequest;
import com.bhawanisingh.airesume.admin.dto.response.AdminApplicationPageResponse;
import com.bhawanisingh.airesume.admin.dto.response.AdminCompanyPageResponse;
import com.bhawanisingh.airesume.admin.dto.response.AdminDashboardSummaryResponse;
import com.bhawanisingh.airesume.admin.dto.response.AdminJobPageResponse;
import com.bhawanisingh.airesume.admin.dto.response.AdminUserPageResponse;
import com.bhawanisingh.airesume.admin.dto.response.AdminUserResponse;
import com.bhawanisingh.airesume.admin.service.AdminService;
import com.bhawanisingh.airesume.application.entity.ApplicationStatus;
import com.bhawanisingh.airesume.auth.enums.Role;
import com.bhawanisingh.airesume.auth.enums.UserStatus;
import com.bhawanisingh.airesume.common.response.ApiResponse;
import com.bhawanisingh.airesume.job.enums.JobStatus;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/dashboard")
    public ResponseEntity<ApiResponse<AdminDashboardSummaryResponse>> getDashboardSummary() {
        return ResponseEntity.ok(
                ApiResponse.success(
                        "Admin dashboard summary fetched successfully",
                        adminService.getDashboardSummary()
                )
        );
    }

    @GetMapping("/users")
    public ResponseEntity<ApiResponse<AdminUserPageResponse>> getUsers(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Role role,
            @RequestParam(required = false) UserStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(
                ApiResponse.success(
                        "Users fetched successfully",
                        adminService.getUsers(search, role, status, page, size)
                )
        );
    }

    @PatchMapping("/users/{userId}/status")
    public ResponseEntity<ApiResponse<AdminUserResponse>> updateUserStatus(
            @PathVariable Long userId,
            @Valid @RequestBody AdminUserStatusUpdateRequest request
    ) {
        return ResponseEntity.ok(
                ApiResponse.success(
                        "User status updated successfully",
                        adminService.updateUserStatus(userId, request)
                )
        );
    }

    @GetMapping("/companies")
    public ResponseEntity<ApiResponse<AdminCompanyPageResponse>> getCompanies(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Boolean deleted,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(
                ApiResponse.success(
                        "Companies fetched successfully",
                        adminService.getCompanies(search, deleted, page, size)
                )
        );
    }

    @PatchMapping("/companies/{companyId}/soft-delete")
    public ResponseEntity<ApiResponse<Void>> softDeleteCompany(@PathVariable Long companyId) {
        adminService.softDeleteCompany(companyId);
        return ResponseEntity.ok(ApiResponse.success("Company soft deleted successfully", null));
    }

    @PatchMapping("/companies/{companyId}/restore")
    public ResponseEntity<ApiResponse<Void>> restoreCompany(@PathVariable Long companyId) {
        adminService.restoreCompany(companyId);
        return ResponseEntity.ok(ApiResponse.success("Company restored successfully", null));
    }

    @GetMapping("/jobs")
    public ResponseEntity<ApiResponse<AdminJobPageResponse>> getJobs(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) JobStatus status,
            @RequestParam(required = false) Boolean deleted,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(
                ApiResponse.success(
                        "Jobs fetched successfully",
                        adminService.getJobs(search, status, deleted, page, size)
                )
        );
    }

    @PatchMapping("/jobs/{jobId}/soft-delete")
    public ResponseEntity<ApiResponse<Void>> softDeleteJob(@PathVariable Long jobId) {
        adminService.softDeleteJob(jobId);
        return ResponseEntity.ok(ApiResponse.success("Job soft deleted successfully", null));
    }

    @PatchMapping("/jobs/{jobId}/restore")
    public ResponseEntity<ApiResponse<Void>> restoreJob(@PathVariable Long jobId) {
        adminService.restoreJob(jobId);
        return ResponseEntity.ok(ApiResponse.success("Job restored successfully", null));
    }

    @GetMapping("/applications")
    public ResponseEntity<ApiResponse<AdminApplicationPageResponse>> getApplications(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) ApplicationStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(
                ApiResponse.success(
                        "Applications fetched successfully",
                        adminService.getApplications(search, status, page, size)
                )
        );
    }
}
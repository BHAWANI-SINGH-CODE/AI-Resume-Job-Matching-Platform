package com.bhawanisingh.airesume.admin.service;

import com.bhawanisingh.airesume.admin.dto.request.AdminUserStatusUpdateRequest;
import com.bhawanisingh.airesume.admin.dto.response.AdminUserResponse;
import com.bhawanisingh.airesume.admin.dto.response.AdminUserStatsResponse;

import java.util.List;

public interface AdminUserManagementService {

    List<AdminUserResponse> getAllUsers();

    AdminUserResponse getUserById(Long userId);

    AdminUserResponse updateUserStatus(Long userId, AdminUserStatusUpdateRequest request);

    AdminUserStatsResponse getUserStats();
}
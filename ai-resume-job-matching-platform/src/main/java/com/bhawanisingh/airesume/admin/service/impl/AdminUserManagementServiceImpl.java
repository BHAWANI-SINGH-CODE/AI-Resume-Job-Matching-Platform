package com.bhawanisingh.airesume.admin.service.impl;

import com.bhawanisingh.airesume.admin.dto.request.AdminUserStatusUpdateRequest;
import com.bhawanisingh.airesume.admin.dto.response.AdminUserResponse;
import com.bhawanisingh.airesume.admin.dto.response.AdminUserStatsResponse;
import com.bhawanisingh.airesume.admin.mapper.AdminUserMapper;
import com.bhawanisingh.airesume.admin.service.AdminUserManagementService;
import com.bhawanisingh.airesume.auth.entity.User;
import com.bhawanisingh.airesume.auth.enums.Role;
import com.bhawanisingh.airesume.auth.enums.UserStatus;
import com.bhawanisingh.airesume.auth.repository.UserRepository;
import com.bhawanisingh.airesume.common.exception.InvalidOperationException;
import com.bhawanisingh.airesume.common.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class AdminUserManagementServiceImpl implements AdminUserManagementService {

    private final UserRepository userRepository;

    public AdminUserManagementServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<AdminUserResponse> getAllUsers() {
        return userRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(AdminUserMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public AdminUserResponse getUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        return AdminUserMapper.toResponse(user);
    }

    @Override
    public AdminUserResponse updateUserStatus(Long userId, AdminUserStatusUpdateRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        if (user.getRole() == Role.ADMIN && request.getStatus() == UserStatus.BLOCKED) {
            throw new InvalidOperationException("Admin user cannot be blocked from this endpoint");
        }

        user.setStatus(request.getStatus());
        User savedUser = userRepository.save(user);

        return AdminUserMapper.toResponse(savedUser);
    }

    @Override
    @Transactional(readOnly = true)
    public AdminUserStatsResponse getUserStats() {
        long totalUsers = userRepository.count();
        long totalCandidates = userRepository.countByRole(Role.USER);
        long totalCompanies = userRepository.countByRole(Role.COMPANY);
        long totalAdmins = userRepository.countByRole(Role.ADMIN);

        long activeUsers = userRepository.countByStatus(UserStatus.ACTIVE);
        long inactiveUsers = userRepository.countByStatus(UserStatus.INACTIVE);
        long blockedUsers = userRepository.countByStatus(UserStatus.BLOCKED);
        long pendingVerificationUsers = userRepository.countByStatus(UserStatus.PENDING_VERIFICATION);

        return new AdminUserStatsResponse(
                totalUsers,
                totalCandidates,
                totalCompanies,
                totalAdmins,
                activeUsers,
                inactiveUsers,
                blockedUsers,
                pendingVerificationUsers
        );
    }
}
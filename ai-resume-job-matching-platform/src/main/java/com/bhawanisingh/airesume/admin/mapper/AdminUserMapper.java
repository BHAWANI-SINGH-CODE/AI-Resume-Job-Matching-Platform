package com.bhawanisingh.airesume.admin.mapper;

import com.bhawanisingh.airesume.admin.dto.response.AdminUserResponse;
import com.bhawanisingh.airesume.auth.entity.User;

public final class AdminUserMapper {

    private AdminUserMapper() {
    }

    public static AdminUserResponse toResponse(User user) {
        return new AdminUserResponse(
                user.getId(),
                user.getFullName(),
                user.getEmail(),
                user.getRole(),
                user.getStatus(),
                user.getEmailVerified(),
                user.getProfileImageUrl(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }
}
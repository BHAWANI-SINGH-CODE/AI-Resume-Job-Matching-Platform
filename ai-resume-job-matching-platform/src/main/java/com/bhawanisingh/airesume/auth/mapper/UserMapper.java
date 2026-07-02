package com.bhawanisingh.airesume.auth.mapper;

import com.bhawanisingh.airesume.auth.dto.UserProfileResponse;
import com.bhawanisingh.airesume.auth.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserProfileResponse toUserProfileResponse(User user) {
        return UserProfileResponse.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .role(user.getRole().name())
                .status(user.getStatus().name())
                .emailVerified(user.getEmailVerified())
                .build();
    }
}
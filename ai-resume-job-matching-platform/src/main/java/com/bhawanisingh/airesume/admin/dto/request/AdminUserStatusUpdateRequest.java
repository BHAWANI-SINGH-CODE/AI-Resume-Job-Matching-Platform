package com.bhawanisingh.airesume.admin.dto.request;

import com.bhawanisingh.airesume.auth.enums.UserStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminUserStatusUpdateRequest {

    @NotNull(message = "Status is required")
    private UserStatus status;
}
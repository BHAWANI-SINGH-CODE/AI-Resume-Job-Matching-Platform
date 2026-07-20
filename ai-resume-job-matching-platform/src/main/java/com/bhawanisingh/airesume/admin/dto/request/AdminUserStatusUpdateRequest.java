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

    public AdminUserStatusUpdateRequest() {
    }

    public UserStatus getStatus() {
        return status;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }
}
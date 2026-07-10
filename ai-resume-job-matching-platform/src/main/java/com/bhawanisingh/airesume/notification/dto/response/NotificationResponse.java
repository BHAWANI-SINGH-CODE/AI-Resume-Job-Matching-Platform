package com.bhawanisingh.airesume.notification.dto.response;

import com.bhawanisingh.airesume.notification.enums.NotificationStatus;
import com.bhawanisingh.airesume.notification.enums.NotificationType;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationResponse {

    private Long id;
    private String title;
    private String message;
    private NotificationType type;
    private NotificationStatus status;
    private LocalDateTime readAt;
    private Long referenceId;
    private String referenceType;
    private LocalDateTime createdAt;
}
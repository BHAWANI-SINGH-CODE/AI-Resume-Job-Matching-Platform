package com.bhawanisingh.airesume.notification.controller;

import com.bhawanisingh.airesume.common.response.ApiResponse;
import com.bhawanisingh.airesume.notification.dto.response.NotificationResponse;
import com.bhawanisingh.airesume.notification.dto.response.UnreadNotificationCountResponse;
import com.bhawanisingh.airesume.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<NotificationResponse>>> getMyNotifications() {
        List<NotificationResponse> notifications = notificationService.getMyNotifications();

        return ResponseEntity.ok(
                ApiResponse.success("Notifications fetched successfully", notifications)
        );
    }

    @GetMapping("/unread-count")
    public ResponseEntity<ApiResponse<UnreadNotificationCountResponse>> getUnreadCount() {
        UnreadNotificationCountResponse response = notificationService.getUnreadCount();

        return ResponseEntity.ok(
                ApiResponse.success("Unread notification count fetched successfully", response)
        );
    }

    @PatchMapping("/{notificationId}/read")
    public ResponseEntity<ApiResponse<NotificationResponse>> markAsRead(
            @PathVariable Long notificationId
    ) {
        NotificationResponse response = notificationService.markAsRead(notificationId);

        return ResponseEntity.ok(
                ApiResponse.success("Notification marked as read successfully", response)
        );
    }

    @PatchMapping("/read-all")
    public ResponseEntity<ApiResponse<Void>> markAllAsRead() {
        notificationService.markAllAsRead();

        return ResponseEntity.ok(
                ApiResponse.success("All notifications marked as read successfully", null)
        );
    }
}
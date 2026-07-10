package com.bhawanisingh.airesume.notification.service;

import com.bhawanisingh.airesume.notification.dto.response.NotificationResponse;
import com.bhawanisingh.airesume.notification.dto.response.UnreadNotificationCountResponse;
import com.bhawanisingh.airesume.notification.entity.Notification;
import com.bhawanisingh.airesume.notification.enums.NotificationType;

import java.util.List;

public interface NotificationService {

    Notification createNotification(
            Long userId,
            String title,
            String message,
            NotificationType type,
            Long referenceId,
            String referenceType
    );

    List<NotificationResponse> getMyNotifications();

    UnreadNotificationCountResponse getUnreadCount();

    NotificationResponse markAsRead(Long notificationId);

    void markAllAsRead();
}
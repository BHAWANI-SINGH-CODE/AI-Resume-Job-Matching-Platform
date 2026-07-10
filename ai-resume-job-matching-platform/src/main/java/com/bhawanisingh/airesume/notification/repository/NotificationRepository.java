package com.bhawanisingh.airesume.notification.repository;

import com.bhawanisingh.airesume.notification.entity.Notification;
import com.bhawanisingh.airesume.notification.enums.NotificationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findByUserIdOrderByCreatedAtDesc(Long userId);

    long countByUserIdAndStatus(Long userId, NotificationStatus status);

    Optional<Notification> findByIdAndUserId(Long notificationId, Long userId);

    List<Notification> findByUserIdAndStatusOrderByCreatedAtDesc(Long userId, NotificationStatus status);
}
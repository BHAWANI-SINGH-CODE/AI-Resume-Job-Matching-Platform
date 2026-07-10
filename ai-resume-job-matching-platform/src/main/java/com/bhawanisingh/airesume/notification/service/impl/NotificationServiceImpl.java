package com.bhawanisingh.airesume.notification.service.impl;

import com.bhawanisingh.airesume.auth.entity.User;
import com.bhawanisingh.airesume.auth.repository.UserRepository;
import com.bhawanisingh.airesume.common.exception.ResourceNotFoundException;
import com.bhawanisingh.airesume.notification.dto.response.NotificationResponse;
import com.bhawanisingh.airesume.notification.dto.response.UnreadNotificationCountResponse;
import com.bhawanisingh.airesume.notification.entity.Notification;
import com.bhawanisingh.airesume.notification.enums.NotificationStatus;
import com.bhawanisingh.airesume.notification.enums.NotificationType;
import com.bhawanisingh.airesume.notification.mapper.NotificationMapper;
import com.bhawanisingh.airesume.notification.repository.NotificationRepository;
import com.bhawanisingh.airesume.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;
    private final UserRepository userRepository;

    @Override
    public Notification createNotification(
            Long userId,
            String title,
            String message,
            NotificationType type,
            Long referenceId,
            String referenceType
    ) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        Notification notification = Notification.builder()
                .title(title)
                .message(message)
                .type(type)
                .status(NotificationStatus.UNREAD)
                .referenceId(referenceId)
                .referenceType(referenceType)
                .user(user)
                .build();

        return notificationRepository.save(notification);
    }

    @Override
    @Transactional(readOnly = true)
    public List<NotificationResponse> getMyNotifications() {
        Long currentUserId = getCurrentUserId();

        return notificationRepository.findByUserIdOrderByCreatedAtDesc(currentUserId)
                .stream()
                .map(notificationMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public UnreadNotificationCountResponse getUnreadCount() {
        Long currentUserId = getCurrentUserId();

        long unreadCount = notificationRepository.countByUserIdAndStatus(
                currentUserId,
                NotificationStatus.UNREAD
        );

        return UnreadNotificationCountResponse.builder()
                .unreadCount(unreadCount)
                .build();
    }

    @Override
    public NotificationResponse markAsRead(Long notificationId) {
        Long currentUserId = getCurrentUserId();

        Notification notification = notificationRepository.findByIdAndUserId(notificationId, currentUserId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Notification not found with id: " + notificationId
                ));

        if (notification.getStatus() == NotificationStatus.UNREAD) {
            notification.setStatus(NotificationStatus.READ);
            notification.setReadAt(LocalDateTime.now());
        }

        Notification savedNotification = notificationRepository.save(notification);
        return notificationMapper.toResponse(savedNotification);
    }

    @Override
    public void markAllAsRead() {
        Long currentUserId = getCurrentUserId();

        List<Notification> unreadNotifications = notificationRepository
                .findByUserIdAndStatusOrderByCreatedAtDesc(currentUserId, NotificationStatus.UNREAD);

        if (unreadNotifications.isEmpty()) {
            return;
        }

        LocalDateTime now = LocalDateTime.now();

        unreadNotifications.forEach(notification -> {
            notification.setStatus(NotificationStatus.READ);
            notification.setReadAt(now);
        });

        notificationRepository.saveAll(unreadNotifications);
    }

    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        User currentUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Authenticated user not found"));

        return currentUser.getId();
    }
}
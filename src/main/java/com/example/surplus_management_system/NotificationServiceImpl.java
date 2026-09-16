package com.example.surplus_management_system;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;

    public NotificationServiceImpl(
            NotificationRepository notificationRepository
    ) {
        this.notificationRepository = notificationRepository;
    }

    @Override
    public NotificationResponse createNotification(
            UUID userId,
            NotificationType type,
            String title,
            String message,
            String referenceType,
            UUID referenceId
    ) {

        if (userId == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }

        if (type == null) {
            throw new IllegalArgumentException(
                    "Notification type cannot be null"
            );
        }

        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException(
                    "Notification title cannot be empty"
            );
        }

        if (message == null || message.isBlank()) {
            throw new IllegalArgumentException(
                    "Notification message cannot be empty"
            );
        }

        Notification notification = Notification.builder()
                .userId(userId)
                .type(type)
                .title(title)
                .message(message)
                .referenceType(referenceType)
                .referenceId(referenceId)
                .isRead(false)
                .build();

        Notification saved =
                notificationRepository.save(notification);

        return mapToResponse(saved);
    }

    @Override
    public NotificationResponse getNotification(UUID id) {

        Notification notification =
                notificationRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Notification not found: " + id
                                )
                        );

        return mapToResponse(notification);
    }

    @Override
    public List<NotificationResponse> getUserNotifications(
            UUID userId
    ) {

        return notificationRepository
                .findByUserIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<NotificationResponse> getUnreadNotifications(
            UUID userId
    ) {

        return notificationRepository
                .findByUserIdAndIsReadOrderByCreatedAtDesc(
                        userId,
                        false
                )
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public long getUnreadCount(UUID userId) {

        return notificationRepository.countByUserIdAndIsRead(
                userId,
                false
        );
    }

    @Override
    public NotificationResponse markAsRead(UUID id) {

        Notification notification =
                notificationRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Notification not found: " + id
                                )
                        );

        if (!Boolean.TRUE.equals(notification.getIsRead())) {
            notification.setIsRead(true);
            notification.setReadAt(LocalDateTime.now());
        }

        Notification saved =
                notificationRepository.save(notification);

        return mapToResponse(saved);
    }

    @Override
    public void markAllAsRead(UUID userId) {

        List<Notification> notifications =
                notificationRepository
                        .findByUserIdAndIsReadOrderByCreatedAtDesc(
                                userId,
                                false
                        );

        LocalDateTime now = LocalDateTime.now();

        for (Notification notification : notifications) {
            notification.setIsRead(true);
            notification.setReadAt(now);
        }

        notificationRepository.saveAll(notifications);
    }

    @Override
    public void deleteNotification(UUID id) {

        if (!notificationRepository.existsById(id)) {
            throw new RuntimeException(
                    "Notification not found: " + id
            );
        }

        notificationRepository.deleteById(id);
    }

    private NotificationResponse mapToResponse(
            Notification notification
    ) {

        return NotificationResponse.builder()
                .id(notification.getId())
                .type(notification.getType())
                .title(notification.getTitle())
                .message(notification.getMessage())
                .referenceType(notification.getReferenceType())
                .referenceId(notification.getReferenceId())
                .isRead(notification.getIsRead())
                .createdAt(notification.getCreatedAt())
                .readAt(notification.getReadAt())
                .build();
    }
}
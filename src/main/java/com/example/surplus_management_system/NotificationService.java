package com.example.surplus_management_system;

import java.util.List;
import java.util.UUID;

public interface NotificationService {

    NotificationResponse createNotification(
            UUID userId,
            NotificationType type,
            String title,
            String message,
            String referenceType,
            UUID referenceId
    );

    NotificationResponse getNotification(UUID id);

    List<NotificationResponse> getUserNotifications(UUID userId);

    List<NotificationResponse> getUnreadNotifications(UUID userId);

    long getUnreadCount(UUID userId);

    NotificationResponse markAsRead(UUID id);

    void markAllAsRead(UUID userId);

    void deleteNotification(UUID id);
}
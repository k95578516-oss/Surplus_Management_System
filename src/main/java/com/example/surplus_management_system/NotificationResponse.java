package com.example.surplus_management_system;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationResponse {

    private UUID id;

    private NotificationType type;

    private String title;

    private String message;

    private String referenceType;

    private UUID referenceId;

    private Boolean isRead;

    private LocalDateTime createdAt;

    private LocalDateTime readAt;
}
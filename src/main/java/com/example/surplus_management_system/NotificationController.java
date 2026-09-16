package com.example.surplus_management_system;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/notifications")
@SecurityRequirement(name = "bearerAuth")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @PostMapping
    public ResponseEntity<NotificationResponse> createNotification(
            @RequestParam UUID userId,
            @RequestParam NotificationType type,
            @RequestParam String title,
            @RequestParam String message,
            @RequestParam(required = false) String referenceType,
            @RequestParam(required = false) UUID referenceId
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        notificationService.createNotification(
                                userId,
                                type,
                                title,
                                message,
                                referenceType,
                                referenceId
                        )
                );
    }

    @GetMapping("/{id}")
    public ResponseEntity<NotificationResponse> getNotification(
            @PathVariable UUID id
    ) {
        return ResponseEntity.ok(
                notificationService.getNotification(id)
        );
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<NotificationResponse>> getUserNotifications(
            @PathVariable UUID userId
    ) {
        return ResponseEntity.ok(
                notificationService.getUserNotifications(userId)
        );
    }

    @GetMapping("/user/{userId}/unread")
    public ResponseEntity<List<NotificationResponse>> getUnreadNotifications(
            @PathVariable UUID userId
    ) {
        return ResponseEntity.ok(
                notificationService.getUnreadNotifications(userId)
        );
    }

    @GetMapping("/user/{userId}/unread/count")
    public ResponseEntity<Long> getUnreadCount(
            @PathVariable UUID userId
    ) {
        return ResponseEntity.ok(
                notificationService.getUnreadCount(userId)
        );
    }

    @PatchMapping("/{id}/read")
    public ResponseEntity<NotificationResponse> markAsRead(
            @PathVariable UUID id
    ) {
        return ResponseEntity.ok(
                notificationService.markAsRead(id)
        );
    }

    @PatchMapping("/user/{userId}/read-all")
    public ResponseEntity<Void> markAllAsRead(
            @PathVariable UUID userId
    ) {
        notificationService.markAllAsRead(userId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNotification(
            @PathVariable UUID id
    ) {
        notificationService.deleteNotification(id);
        return ResponseEntity.noContent().build();
    }
}

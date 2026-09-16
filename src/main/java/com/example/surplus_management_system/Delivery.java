package com.example.surplus_management_system;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "deliveries")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Delivery {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private UUID requestId;

    private Double pickupLatitude;

    private Double pickupLongitude;

    private Double deliveryLatitude;

    private Double deliveryLongitude;

    private Double distanceKm;

    private Integer estimatedDeliveryMinutes;

    private LocalDateTime expectedPickupAt;

    private LocalDateTime expectedDeliveryAt;

    private LocalDateTime lastLocationUpdateAt;

    private LocalDateTime actualPickupAt;

    private LocalDateTime actualDeliveryAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DeliveryStatus status;

    @Enumerated(EnumType.STRING)
    private DeliveryMethod deliveryMethod;

    private String deliveryPartner;

    private String proofUrl;

    @Column(precision = 15, scale = 2)
    private BigDecimal quantityDelivered;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();

        if (status == null) {
            status = DeliveryStatus.REQUESTED;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
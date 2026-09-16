package com.example.surplus_management_system;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeliveryResponse {

    private UUID id;

    private UUID requestId;

    private Double pickupLatitude;

    private Double pickupLongitude;

    private Double deliveryLatitude;

    private Double deliveryLongitude;

    private Double distanceKm;

    private Integer estimatedDeliveryMinutes;

    private LocalDateTime expectedPickupAt;

    private LocalDateTime lastLocationUpdateAt;

    private LocalDateTime expectedDeliveryAt;

    private LocalDateTime actualPickupAt;

    private LocalDateTime actualDeliveryAt;

    private DeliveryStatus status;

    private DeliveryMethod deliveryMethod;

    private String deliveryPartner;

    private String proofUrl;

    private BigDecimal quantityDelivered;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
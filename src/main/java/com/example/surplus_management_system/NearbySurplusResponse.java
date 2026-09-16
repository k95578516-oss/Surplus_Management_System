package com.example.surplus_management_system;

import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NearbySurplusResponse {

    private UUID surplusId;

    private UUID organizationId;

    private String organizationName;

    private String resourceName;

    private ResourceCategory category;

    private BigDecimal availableQuantity;

    private String unit;

    private Double latitude;

    private Double longitude;

    private Double distanceKm;

    private Integer estimatedDeliveryMinutes;

    private SurplusStatus status;
}

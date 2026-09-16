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
public class SurplusResponse {

    private UUID id;

    private UUID providerOrganizationId;

    private String resourceName;

    private ResourceCategory category;

    private BigDecimal quantity;

    private String unit;

    private ConditionType condition;

    private String description;

    private String locationAddress;

    private String city;

    private String state;

    private Double latitude;

    private Double longitude;

    private LocalDateTime availableFrom;

    private LocalDateTime availableUntil;

    private LocalDateTime expiryDate;

    private Boolean isPerishable;

    private Boolean isEmergencyEligible;

    private String imageUrl;

    private SurplusStatus status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}

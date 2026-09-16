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
public class NeedResponse {

    private UUID id;

    private UUID recipientOrganizationId;

    private String resourceName;

    private ResourceCategory category;

    private BigDecimal quantityRequired;

    private String unit;

    private UrgencyLevel urgency;

    private String purpose;

    private String locationAddress;

    private String city;

    private String state;

    private Double latitude;

    private Double longitude;

    private LocalDateTime requiredBy;

    private Boolean isEmergency;

    private NeedStatus status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
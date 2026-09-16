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
public class EmergencyResponse {

    private UUID id;

    private UUID needId;

    private UUID recipientOrganizationId;

    private ResourceCategory category;

    private BigDecimal quantityRequired;

    private String reason;

    private EmergencySeverity severity;

    private LocalDateTime requiredBy;

    private Integer maxDeliveryMinutes;

    private EmergencyStatus status;

    private LocalDateTime createdAt;

    private LocalDateTime resolvedAt;
}

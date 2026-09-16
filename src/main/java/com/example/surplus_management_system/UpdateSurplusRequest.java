package com.example.surplus_management_system;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateSurplusRequest {

    private String resourceName;

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
}
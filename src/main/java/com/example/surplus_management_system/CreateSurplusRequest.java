package com.example.surplus_management_system;

import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateSurplusRequest {

    @NotBlank
    private String resourceName;

    @NotNull
    private ResourceCategory category;

    @NotNull
    @DecimalMin(value = "0.01")
    private BigDecimal quantity;

    @NotBlank
    private String unit;

    @NotNull
    private ConditionType condition;

    @Size(max = 2000)
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
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
public class CreateNeedRequest {

    @NotBlank
    private String resourceName;

    @NotNull
    private ResourceCategory category;

    @NotNull
    @DecimalMin(value = "0.01")
    private BigDecimal quantityRequired;

    @NotBlank
    private String unit;

    @NotNull
    private UrgencyLevel urgency;

    @Size(max = 2000)
    private String purpose;

    private String locationAddress;

    private String city;

    private String state;

    private Double latitude;

    private Double longitude;

    private LocalDateTime requiredBy;

    private Boolean isEmergency;
}
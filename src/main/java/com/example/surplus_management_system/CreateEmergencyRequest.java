package com.example.surplus_management_system;

import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateEmergencyRequest {

    @NotNull
    private UUID needId;

    @NotNull
    private ResourceCategory category;

    @NotNull
    @DecimalMin(value = "0.01")
    private BigDecimal quantityRequired;

    @NotBlank
    @Size(max = 2000)
    private String reason;

    @NotNull
    private EmergencySeverity severity;

    @NotNull
    private LocalDateTime requiredBy;

    private Integer maxDeliveryMinutes;
}

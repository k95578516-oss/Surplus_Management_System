package com.example.surplus_management_system;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateRequestRequest {

    @NotNull
    private UUID matchId;

    @NotNull
    @DecimalMin(value = "0.01")
    private BigDecimal quantityRequested;

    private String message;
}
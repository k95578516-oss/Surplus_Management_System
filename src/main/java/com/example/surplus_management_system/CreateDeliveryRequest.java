package com.example.surplus_management_system;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateDeliveryRequest {

    @NotNull
    private UUID requestId;

    private DeliveryMethod deliveryMethod;

    private LocalDateTime expectedPickupAt;
}
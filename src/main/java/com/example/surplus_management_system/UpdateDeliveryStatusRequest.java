package com.example.surplus_management_system;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateDeliveryStatusRequest {

    @NotNull
    private DeliveryStatus status;
}
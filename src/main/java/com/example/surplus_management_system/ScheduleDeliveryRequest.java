package com.example.surplus_management_system;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ScheduleDeliveryRequest {

    @NotNull
    private LocalDateTime expectedPickupAt;

    @NotNull
    private LocalDateTime expectedDeliveryAt;
}

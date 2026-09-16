package com.example.surplus_management_system;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeliveryETAResponse {

    private Double distanceKm;

    private Integer estimatedDeliveryMinutes;

    private String estimatedArrival;

    private Boolean delayed;
}

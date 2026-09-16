package com.example.surplus_management_system;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeliveryTrackingResponse {

    private Double latitude;

    private Double longitude;

    private DeliveryStatus status;

    private Double distanceRemainingKm;

    private Integer estimatedMinutesRemaining;

    private LocalDateTime recordedAt;
}

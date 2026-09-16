package com.example.surplus_management_system;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MatchResponse {

    private UUID id;

    private UUID surplusId;

    private UUID needId;

    private Double overallScore;

    private Double categoryScore;

    private Double quantityScore;

    private Double distanceScore;

    private String matchReason;

    private Double urgencyScore;

    private Double conditionScore;

    private Double availabilityScore;

    private Double distanceKm;

    private MatchStatus status;

    private LocalDateTime createdAt;

    private LocalDateTime expiresAt;
}
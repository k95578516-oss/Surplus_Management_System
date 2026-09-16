package com.example.surplus_management_system;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MatchScoreResponse {

    private Double overallScore;

    private Double categoryScore;

    private Double quantityScore;

    private Double distanceScore;

    private Double urgencyScore;

    private Double conditionScore;

    private Double availabilityScore;

    private Double distanceKm;
}

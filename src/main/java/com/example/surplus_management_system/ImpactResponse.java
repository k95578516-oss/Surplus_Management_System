package com.example.surplus_management_system;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ImpactResponse {

    private Double totalResourcesRedistributed;

    private Integer organizationsSupported;

    private Integer completedMatches;

    private Integer estimatedBeneficiaries;

    private Double estimatedWasteDivertedKg;
}
package com.example.surplus_management_system;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "impact_records")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ImpactRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private UUID organizationId;

    private UUID surplusId;

    private UUID requestId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ResourceCategory resourceCategory;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal quantityRedistributed;

    private Integer beneficiariesEstimated;

    private Double wasteDivertedKg;

    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
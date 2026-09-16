package com.example.surplus_management_system;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(
        name = "matches",
        indexes = {
                @Index(name = "idx_match_surplus", columnList = "surplusId"),
                @Index(name = "idx_match_need", columnList = "needId"),
                @Index(name = "idx_match_score", columnList = "overallScore")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Match {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private UUID surplusId;

    @Column(nullable = false)
    private UUID needId;

    @Column(nullable = false)
    private Double overallScore;

    private Double categoryScore;

    private Double quantityScore;

    @Column(length = 1000)
    private String matchReason;

    private Double distanceScore;

    private Double urgencyScore;

    private Double conditionScore;

    private Double availabilityScore;

    private Double distanceKm;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MatchStatus status;

    private LocalDateTime createdAt;

    private LocalDateTime expiresAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();

        if (status == null) {
            status = MatchStatus.ACTIVE;
        }
    }
}
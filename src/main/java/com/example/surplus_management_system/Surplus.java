package com.example.surplus_management_system;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "surplus")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Surplus {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private UUID providerOrganizationId;

    @Column(nullable = false)
    private String resourceName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ResourceCategory category;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal quantity;

    @Column(nullable = false)
    private String unit;

    @Enumerated(EnumType.STRING)
    @Column(name = "item_condition", nullable = false)
    private ConditionType condition;



    @Column(length = 2000)
    private String description;

    private String locationAddress;

    private String city;

    private String state;

    private Double latitude;

    private Double longitude;

    private LocalDateTime availableFrom;

    private LocalDateTime availableUntil;

    private LocalDateTime expiryDate;

    private Boolean isPerishable;

    private Boolean isEmergencyEligible;

    private String imageUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SurplusStatus status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();

        if (status == null) {
            status = SurplusStatus.AVAILABLE;
        }

        if (isPerishable == null) {
            isPerishable = false;
        }

        if (isEmergencyEligible == null) {
            isEmergencyEligible = false;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}

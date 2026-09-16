package com.example.surplus_management_system;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "needs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Need {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private UUID recipientOrganizationId;

    @Column(nullable = false)
    private String resourceName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ResourceCategory category;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal quantityRequired;

    @Column(nullable = false)
    private String unit;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UrgencyLevel urgency;

    @Column(length = 2000)
    private String purpose;

    private String locationAddress;

    private String city;

    private String state;

    private Double latitude;

    private Double longitude;

    private LocalDateTime requiredBy;

    private Boolean isEmergency;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NeedStatus status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();

        if (status == null) {
            status = NeedStatus.OPEN;
        }

        if (isEmergency == null) {
            isEmergency = false;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
package com.example.surplus_management_system;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "requests")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Request {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private UUID matchId;

    @Column(nullable = false)
    private UUID surplusId;

    @Column(nullable = false)
    private UUID recipientOrganizationId;

    @Column(nullable = false)
    private UUID providerOrganizationId;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal quantityRequested;

    @Column(length = 1000)
    private String message;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RequestStatus status;

    private LocalDateTime requestedAt;

    private LocalDateTime acceptedAt;

    private LocalDateTime rejectedAt;

    private LocalDateTime completedAt;

    @PrePersist
    protected void onCreate() {
        requestedAt = LocalDateTime.now();

        if (status == null) {
            status = RequestStatus.PENDING;
        }
    }
}
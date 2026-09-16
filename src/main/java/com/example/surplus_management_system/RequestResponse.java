package com.example.surplus_management_system;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RequestResponse {

    private UUID id;

    private UUID matchId;

    private UUID surplusId;

    private UUID recipientOrganizationId;

    private UUID providerOrganizationId;

    private BigDecimal quantityRequested;

    private String message;

    private RequestStatus status;

    private LocalDateTime requestedAt;

    private LocalDateTime acceptedAt;

    private LocalDateTime rejectedAt;

    private LocalDateTime completedAt;
}

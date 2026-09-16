package com.example.surplus_management_system;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VerificationResponse {

    private UUID id;

    private VerificationEntityType entityType;

    private UUID entityId;

    private VerificationType verificationType;

    private VerificationStatus status;

    private UUID verifiedBy;

    private String remarks;

    private String documentUrl;

    private LocalDateTime verifiedAt;

    private LocalDateTime createdAt;
}
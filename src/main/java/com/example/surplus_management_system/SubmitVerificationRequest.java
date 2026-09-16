package com.example.surplus_management_system;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubmitVerificationRequest {

    @NotNull
    private VerificationEntityType entityType;

    @NotNull
    private UUID entityId;

    @NotNull
    private VerificationType verificationType;

    private String documentUrl;

    private String remarks;
}
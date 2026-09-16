package com.example.surplus_management_system;

import java.util.List;
import java.util.UUID;

public interface VerificationService {

    VerificationResponse submitVerification(
            SubmitVerificationRequest request
    );

    VerificationResponse getVerification(UUID id);

    List<VerificationResponse> getVerificationsByEntity(
            VerificationEntityType entityType,
            UUID entityId
    );

    VerificationResponse getLatestVerification(
            VerificationEntityType entityType,
            UUID entityId
    );

    List<VerificationResponse> getVerificationsByStatus(
            VerificationStatus status
    );

    VerificationResponse decideVerification(
            UUID id,
            VerificationDecisionRequest request,
            UUID verifiedBy
    );

    void deleteVerification(UUID id);
}
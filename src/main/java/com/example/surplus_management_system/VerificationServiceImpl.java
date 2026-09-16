package com.example.surplus_management_system;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class VerificationServiceImpl implements VerificationService {

    private final VerificationRepository verificationRepository;

    private final OrganizationRepository organizationRepository;

    public VerificationServiceImpl(
            VerificationRepository verificationRepository,
            OrganizationRepository organizationRepository
    ) {
        this.verificationRepository = verificationRepository;
        this.organizationRepository = organizationRepository;
    }

    @Override
    public VerificationResponse submitVerification(
            SubmitVerificationRequest request
    ) {

        validateEntityExists(
                request.getEntityType(),
                request.getEntityId()
        );

        Verification verification = Verification.builder()
                .entityType(request.getEntityType())
                .entityId(request.getEntityId())
                .verificationType(request.getVerificationType())
                .documentUrl(request.getDocumentUrl())
                .remarks(request.getRemarks())
                .status(VerificationStatus.PENDING)
                .build();

        Verification saved = verificationRepository.save(verification);

        return mapToResponse(saved);
    }

    @Override
    @Transactional
    public VerificationResponse getVerification(UUID id) {

        Verification verification = verificationRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Verification not found: " + id
                        )
                );

        return mapToResponse(verification);
    }

    @Override
    public List<VerificationResponse> getVerificationsByEntity(
            VerificationEntityType entityType,
            UUID entityId
    ) {

        return verificationRepository
                .findByEntityTypeAndEntityId(entityType, entityId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public VerificationResponse getLatestVerification(
            VerificationEntityType entityType,
            UUID entityId
    ) {

        Verification verification =
                verificationRepository
                        .findFirstByEntityTypeAndEntityIdOrderByCreatedAtDesc(
                                entityType,
                                entityId
                        )
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "No verification found for entity: "
                                                + entityId
                                )
                        );

        return mapToResponse(verification);
    }

    @Override
    public List<VerificationResponse> getVerificationsByStatus(
            VerificationStatus status
    ) {

        return verificationRepository
                .findByStatus(status)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public VerificationResponse decideVerification(
            UUID id,
            VerificationDecisionRequest request,
            UUID verifiedBy
    ) {

        Verification verification =
                verificationRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Verification not found: " + id
                                )
                        );

        if (verification.getStatus() != VerificationStatus.PENDING) {
            throw new IllegalStateException(
                    "Only pending verifications can be decided"
            );
        }

        if (request.getStatus() == VerificationStatus.PENDING) {
            throw new IllegalArgumentException(
                    "Verification decision cannot be PENDING"
            );
        }

        verification.setStatus(request.getStatus());
        verification.setVerifiedBy(verifiedBy);
        verification.setRemarks(request.getRemarks());

        if (request.getStatus() == VerificationStatus.VERIFIED) {
            verification.setVerifiedAt(LocalDateTime.now());

            updateOrganizationVerificationStatus(
                    verification,
                    VerificationStatus.VERIFIED,
                    verifiedBy
            );
        } else if (request.getStatus() == VerificationStatus.REJECTED) {
            verification.setVerifiedAt(LocalDateTime.now());

            updateOrganizationVerificationStatus(
                    verification,
                    VerificationStatus.REJECTED,
                    verifiedBy
            );
        } else if (request.getStatus() == VerificationStatus.SUSPENDED) {
            verification.setVerifiedAt(LocalDateTime.now());

            updateOrganizationVerificationStatus(
                    verification,
                    VerificationStatus.SUSPENDED,
                    verifiedBy
            );
        }

        Verification saved = verificationRepository.save(verification);

        return mapToResponse(saved);
    }

    @Override
    public void deleteVerification(UUID id) {

        Verification verification =
                verificationRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Verification not found: " + id
                                )
                        );

        if (verification.getStatus() == VerificationStatus.VERIFIED) {
            throw new IllegalStateException(
                    "Verified records cannot be deleted"
            );
        }

        verificationRepository.delete(verification);
    }

    private void validateEntityExists(
            VerificationEntityType entityType,
            UUID entityId
    ) {

        switch (entityType) {

            case ORGANIZATION -> {
                if (!organizationRepository.existsById(entityId)) {
                    throw new RuntimeException(
                            "Organization not found: " + entityId
                    );
                }
            }

            case SURPLUS, NEED -> {
                // The current project repositories do not expose
                // a generic verification-existence method here.
                // Entity IDs are therefore accepted and verified
                // through the respective workflow.
            }

            default -> throw new IllegalArgumentException(
                    "Unsupported verification entity type: " + entityType
            );
        }
    }

    private void updateOrganizationVerificationStatus(
            Verification verification,
            VerificationStatus status,
            UUID verifiedBy
    ) {

        if (verification.getEntityType()
                != VerificationEntityType.ORGANIZATION) {
            return;
        }

        Organization organization =
                organizationRepository.findById(
                        verification.getEntityId()
                ).orElseThrow(() ->
                        new RuntimeException(
                                "Organization not found: "
                                        + verification.getEntityId()
                        )
                );

        organization.setVerificationStatus(status);
        organization.setVerifiedAt(LocalDateTime.now());
        organization.setVerifiedBy(verifiedBy);

        organizationRepository.save(organization);
    }

    private VerificationResponse mapToResponse(
            Verification verification
    ) {

        return VerificationResponse.builder()
                .id(verification.getId())
                .entityType(verification.getEntityType())
                .entityId(verification.getEntityId())
                .verificationType(verification.getVerificationType())
                .status(verification.getStatus())
                .verifiedBy(verification.getVerifiedBy())
                .remarks(verification.getRemarks())
                .documentUrl(verification.getDocumentUrl())
                .verifiedAt(verification.getVerifiedAt())
                .createdAt(verification.getCreatedAt())
                .build();
    }
}
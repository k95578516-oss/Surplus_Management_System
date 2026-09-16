package com.example.surplus_management_system;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class EmergencyServiceImpl implements EmergencyService {

    private final EmergencyRequestRepository emergencyRequestRepository;
    private final NeedRepository needRepository;

    public EmergencyServiceImpl(
            EmergencyRequestRepository emergencyRequestRepository,
            NeedRepository needRepository
    ) {
        this.emergencyRequestRepository = emergencyRequestRepository;
        this.needRepository = needRepository;
    }

    @Override
    public EmergencyResponse createEmergency(
            CreateEmergencyRequest request
    ) {

        Need need = needRepository.findById(request.getNeedId())
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Need not found"
                        )
                );

        if (!Boolean.TRUE.equals(need.getIsEmergency())) {
            throw new IllegalStateException(
                    "The selected need is not marked as an emergency"
            );
        }

        if (request.getRequiredBy().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException(
                    "Required-by time cannot be in the past"
            );
        }

        if (request.getMaxDeliveryMinutes() != null
                && request.getMaxDeliveryMinutes() <= 0) {

            throw new IllegalArgumentException(
                    "Maximum delivery minutes must be greater than zero"
            );
        }

        if (emergencyRequestRepository
                .findByNeedId(request.getNeedId())
                .isPresent()) {

            throw new IllegalStateException(
                    "An emergency request already exists for this need"
            );
        }

        EmergencyRequest emergencyRequest =
                EmergencyRequest.builder()
                        .needId(request.getNeedId())
                        .recipientOrganizationId(
                                need.getRecipientOrganizationId()
                        )
                        .category(request.getCategory())
                        .quantityRequired(
                                request.getQuantityRequired()
                        )
                        .reason(request.getReason())
                        .severity(request.getSeverity())
                        .requiredBy(request.getRequiredBy())
                        .maxDeliveryMinutes(
                                request.getMaxDeliveryMinutes()
                        )
                        .status(EmergencyStatus.ACTIVE)
                        .build();

        EmergencyRequest saved =
                emergencyRequestRepository.save(
                        emergencyRequest
                );

        return mapToResponse(saved);
    }

    @Override
    public EmergencyResponse getEmergency(UUID id) {

        EmergencyRequest emergencyRequest =
                findEmergency(id);

        return mapToResponse(emergencyRequest);
    }

    @Override
    public List<EmergencyResponse> getAllEmergencies() {

        return emergencyRequestRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<EmergencyResponse> getActiveEmergencies() {

        return emergencyRequestRepository
                .findByStatus(EmergencyStatus.ACTIVE)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<EmergencyResponse> getEmergenciesByStatus(
            EmergencyStatus status
    ) {

        return emergencyRequestRepository
                .findByStatus(status)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<EmergencyResponse> getEmergenciesBySeverity(
            EmergencySeverity severity
    ) {

        return emergencyRequestRepository
                .findBySeverityAndStatus(
                        severity,
                        EmergencyStatus.ACTIVE
                )
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public EmergencyResponse getEmergencyByNeed(UUID needId) {

        EmergencyRequest emergencyRequest =
                emergencyRequestRepository
                        .findByNeedId(needId)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Emergency request not found for need"
                                )
                        );

        return mapToResponse(emergencyRequest);
    }

    @Override
    public EmergencyResponse updateStatus(
            UUID id,
            EmergencyStatusUpdateRequest request
    ) {

        EmergencyRequest emergencyRequest =
                findEmergency(id);

        EmergencyStatus currentStatus =
                emergencyRequest.getStatus();

        EmergencyStatus newStatus =
                request.getStatus();

        validateStatusTransition(
                currentStatus,
                newStatus
        );

        emergencyRequest.setStatus(newStatus);

        if (newStatus == EmergencyStatus.RESOLVED) {
            emergencyRequest.setResolvedAt(
                    LocalDateTime.now()
            );
        }

        EmergencyRequest saved =
                emergencyRequestRepository.save(
                        emergencyRequest
                );

        return mapToResponse(saved);
    }

    @Override
    public void deleteEmergency(UUID id) {

        EmergencyRequest emergencyRequest =
                findEmergency(id);

        if (emergencyRequest.getStatus() == EmergencyStatus.IN_DELIVERY
                || emergencyRequest.getStatus() == EmergencyStatus.RESOLVED) {

            throw new IllegalStateException(
                    "Emergency request cannot be deleted in its current status"
            );
        }

        emergencyRequestRepository.delete(
                emergencyRequest
        );
    }

    private EmergencyRequest findEmergency(UUID id) {

        return emergencyRequestRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Emergency request not found"
                        )
                );
    }

    private void validateStatusTransition(
            EmergencyStatus currentStatus,
            EmergencyStatus newStatus
    ) {

        if (currentStatus == newStatus) {
            throw new IllegalStateException(
                    "Emergency request is already in this status"
            );
        }

        boolean valid = switch (currentStatus) {

            case ACTIVE ->
                    newStatus == EmergencyStatus.MATCHING
                            || newStatus == EmergencyStatus.CANCELLED;

            case MATCHING ->
                    newStatus == EmergencyStatus.ASSIGNED
                            || newStatus == EmergencyStatus.CANCELLED;

            case ASSIGNED ->
                    newStatus == EmergencyStatus.IN_DELIVERY
                            || newStatus == EmergencyStatus.CANCELLED;

            case IN_DELIVERY ->
                    newStatus == EmergencyStatus.RESOLVED;

            case RESOLVED, CANCELLED ->
                    false;
        };

        if (!valid) {
            throw new IllegalStateException(
                    "Invalid emergency status transition: "
                            + currentStatus
                            + " -> "
                            + newStatus
            );
        }
    }

    private EmergencyResponse mapToResponse(
            EmergencyRequest emergencyRequest
    ) {

        return EmergencyResponse.builder()
                .id(emergencyRequest.getId())
                .needId(emergencyRequest.getNeedId())
                .recipientOrganizationId(
                        emergencyRequest.getRecipientOrganizationId()
                )
                .category(emergencyRequest.getCategory())
                .quantityRequired(
                        emergencyRequest.getQuantityRequired()
                )
                .reason(emergencyRequest.getReason())
                .severity(emergencyRequest.getSeverity())
                .requiredBy(emergencyRequest.getRequiredBy())
                .maxDeliveryMinutes(
                        emergencyRequest.getMaxDeliveryMinutes()
                )
                .status(emergencyRequest.getStatus())
                .createdAt(emergencyRequest.getCreatedAt())
                .resolvedAt(emergencyRequest.getResolvedAt())
                .build();
    }
}
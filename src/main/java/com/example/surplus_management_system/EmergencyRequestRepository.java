package com.example.surplus_management_system;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EmergencyRequestRepository
        extends JpaRepository<EmergencyRequest, UUID> {

    Optional<EmergencyRequest> findByNeedId(
            UUID needId
    );

    List<EmergencyRequest> findByStatus(
            EmergencyStatus status
    );

    List<EmergencyRequest> findBySeverityAndStatus(
            EmergencySeverity severity,
            EmergencyStatus status
    );
}
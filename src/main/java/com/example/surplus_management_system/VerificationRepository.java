package com.example.surplus_management_system;


import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface VerificationRepository
        extends JpaRepository<Verification, UUID> {

    List<Verification> findByEntityTypeAndEntityId(
            VerificationEntityType entityType,
            UUID entityId
    );

    List<Verification> findByStatus(
            VerificationStatus status
    );

    Optional<Verification> findFirstByEntityTypeAndEntityIdOrderByCreatedAtDesc(
            VerificationEntityType entityType,
            UUID entityId
    );
}

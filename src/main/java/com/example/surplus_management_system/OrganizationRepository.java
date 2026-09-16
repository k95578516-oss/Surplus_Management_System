package com.example.surplus_management_system;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrganizationRepository extends JpaRepository<Organization, UUID> {

    Optional<Organization> findByUserId(UUID userId);

    List<Organization> findByVerificationStatus(
            VerificationStatus verificationStatus
    );
}

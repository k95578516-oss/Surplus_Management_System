package com.example.surplus_management_system;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface SurplusRepository extends JpaRepository<Surplus, UUID> {

    List<Surplus> findByProviderOrganizationId(UUID organizationId);

    List<Surplus> findByStatus(SurplusStatus status);

    List<Surplus> findByCategoryAndStatus(
            ResourceCategory category,
            SurplusStatus status
    );

    List<Surplus> findByExpiryDateBeforeAndStatus(
            LocalDateTime dateTime,
            SurplusStatus status
    );

    List<Surplus> findByIsEmergencyEligibleTrueAndStatus(
            SurplusStatus status
    );
}
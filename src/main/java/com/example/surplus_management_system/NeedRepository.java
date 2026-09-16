package com.example.surplus_management_system;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface NeedRepository extends JpaRepository<Need, UUID> {

    List<Need> findByRecipientOrganizationId(UUID organizationId);

    List<Need> findByStatus(NeedStatus status);

    List<Need> findByCategoryAndStatus(
            ResourceCategory category,
            NeedStatus status
    );

    List<Need> findByUrgencyAndStatus(
            UrgencyLevel urgency,
            NeedStatus status
    );

    List<Need> findByIsEmergencyTrueAndStatus(
            NeedStatus status
    );
}
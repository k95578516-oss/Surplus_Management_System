package com.example.surplus_management_system;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface RequestRepository extends JpaRepository<Request, UUID> {

    List<Request> findByRecipientOrganizationId(
            UUID organizationId
    );

    List<Request> findByProviderOrganizationId(
            UUID organizationId
    );

    List<Request> findByStatus(
            RequestStatus status
    );

    List<Request> findByMatchId(
            UUID matchId
    );

    List<Request> findBySurplusId(
            UUID surplusId
    );
}
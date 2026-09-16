package com.example.surplus_management_system;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DeliveryRepository extends JpaRepository<Delivery, UUID> {

    Optional<Delivery> findByRequestId(
            UUID requestId
    );

    List<Delivery> findByStatus(
            DeliveryStatus status
    );

    List<Delivery> findByRequestIdIn(
            List<UUID> requestIds
    );
}

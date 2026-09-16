package com.example.surplus_management_system;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DeliveryTrackingRepository
        extends JpaRepository<DeliveryTracking, UUID> {

    List<DeliveryTracking> findByDeliveryIdOrderByRecordedAtDesc(
            UUID deliveryId
    );

    Optional<DeliveryTracking> findFirstByDeliveryIdOrderByRecordedAtDesc(
            UUID deliveryId
    );
}
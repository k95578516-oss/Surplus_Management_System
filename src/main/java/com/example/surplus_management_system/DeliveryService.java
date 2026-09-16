package com.example.surplus_management_system;

import java.util.List;
import java.util.UUID;

public interface DeliveryService {

    DeliveryResponse createDelivery(CreateDeliveryRequest request);

    DeliveryResponse getDelivery(UUID id);

    DeliveryResponse getDeliveryByRequest(UUID requestId);

    List<DeliveryResponse> getAllDeliveries();

    List<DeliveryResponse> getDeliveriesByStatus(DeliveryStatus status);

    DeliveryResponse updateStatus(
            UUID id,
            UpdateDeliveryStatusRequest request
    );

    DeliveryResponse scheduleDelivery(
            UUID id,
            ScheduleDeliveryRequest request
    );

    DeliveryETAResponse getDeliveryETA(UUID id);
}
package com.example.surplus_management_system;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/deliveries")
public class DeliveryController {

    private final DeliveryService deliveryService;

    public DeliveryController(DeliveryService deliveryService) {
        this.deliveryService = deliveryService;
    }

    @PostMapping
    public ResponseEntity<DeliveryResponse> createDelivery(
            @Valid @RequestBody CreateDeliveryRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(deliveryService.createDelivery(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DeliveryResponse> getDelivery(
            @PathVariable UUID id
    ) {
        return ResponseEntity.ok(
                deliveryService.getDelivery(id)
        );
    }

    @GetMapping("/request/{requestId}")
    public ResponseEntity<DeliveryResponse> getDeliveryByRequest(
            @PathVariable UUID requestId
    ) {
        return ResponseEntity.ok(
                deliveryService.getDeliveryByRequest(requestId)
        );
    }

    @GetMapping
    public ResponseEntity<List<DeliveryResponse>> getAllDeliveries() {
        return ResponseEntity.ok(
                deliveryService.getAllDeliveries()
        );
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<DeliveryResponse>> getDeliveriesByStatus(
            @PathVariable DeliveryStatus status
    ) {
        return ResponseEntity.ok(
                deliveryService.getDeliveriesByStatus(status)
        );
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<DeliveryResponse> updateStatus(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateDeliveryStatusRequest request
    ) {
        return ResponseEntity.ok(
                deliveryService.updateStatus(id, request)
        );
    }

    @PatchMapping("/{id}/schedule")
    public ResponseEntity<DeliveryResponse> scheduleDelivery(
            @PathVariable UUID id,
            @Valid @RequestBody ScheduleDeliveryRequest request
    ) {
        return ResponseEntity.ok(
                deliveryService.scheduleDelivery(id, request)
        );
    }

    @GetMapping("/{id}/eta")
    public ResponseEntity<DeliveryETAResponse> getDeliveryETA(
            @PathVariable UUID id
    ) {
        return ResponseEntity.ok(
                deliveryService.getDeliveryETA(id)
        );
    }
}

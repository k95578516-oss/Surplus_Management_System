package com.example.surplus_management_system;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/emergencies")
@SecurityRequirement(name = "bearerAuth")
public class EmergencyController {

    private final EmergencyService emergencyService;

    public EmergencyController(EmergencyService emergencyService) {
        this.emergencyService = emergencyService;
    }

    @PostMapping
    public ResponseEntity<EmergencyResponse> createEmergency(
            @Valid @RequestBody CreateEmergencyRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(emergencyService.createEmergency(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmergencyResponse> getEmergency(
            @PathVariable UUID id
    ) {
        return ResponseEntity.ok(
                emergencyService.getEmergency(id)
        );
    }

    @GetMapping
    public ResponseEntity<List<EmergencyResponse>> getAllEmergencies() {
        return ResponseEntity.ok(
                emergencyService.getAllEmergencies()
        );
    }

    @GetMapping("/active")
    public ResponseEntity<List<EmergencyResponse>> getActiveEmergencies() {
        return ResponseEntity.ok(
                emergencyService.getActiveEmergencies()
        );
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<EmergencyResponse>> getEmergenciesByStatus(
            @PathVariable EmergencyStatus status
    ) {
        return ResponseEntity.ok(
                emergencyService.getEmergenciesByStatus(status)
        );
    }

    @GetMapping("/severity/{severity}")
    public ResponseEntity<List<EmergencyResponse>> getEmergenciesBySeverity(
            @PathVariable EmergencySeverity severity
    ) {
        return ResponseEntity.ok(
                emergencyService.getEmergenciesBySeverity(severity)
        );
    }

    @GetMapping("/need/{needId}")
    public ResponseEntity<EmergencyResponse> getEmergencyByNeed(
            @PathVariable UUID needId
    ) {
        return ResponseEntity.ok(
                emergencyService.getEmergencyByNeed(needId)
        );
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<EmergencyResponse> updateStatus(
            @PathVariable UUID id,
            @Valid @RequestBody EmergencyStatusUpdateRequest request
    ) {
        return ResponseEntity.ok(
                emergencyService.updateStatus(id, request)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmergency(
            @PathVariable UUID id
    ) {
        emergencyService.deleteEmergency(id);
        return ResponseEntity.noContent().build();
    }
}
package com.example.surplus_management_system;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/surplus")
public class SurplusController {

    private final SurplusService surplusService;

    public SurplusController(SurplusService surplusService) {
        this.surplusService = surplusService;
    }

    @PostMapping("/organization/{organizationId}")
    public ResponseEntity<SurplusResponse> createSurplus(
            @PathVariable UUID organizationId,
            @Valid @RequestBody CreateSurplusRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(surplusService.createSurplus(organizationId, request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SurplusResponse> getSurplus(
            @PathVariable UUID id
    ) {
        return ResponseEntity.ok(
                surplusService.getSurplus(id)
        );
    }

    @GetMapping
    public ResponseEntity<List<SurplusResponse>> getAllSurplus() {
        return ResponseEntity.ok(
                surplusService.getAllSurplus()
        );
    }

    @GetMapping("/organization/{organizationId}")
    public ResponseEntity<List<SurplusResponse>> getSurplusByOrganization(
            @PathVariable UUID organizationId
    ) {
        return ResponseEntity.ok(
                surplusService.getSurplusByOrganization(organizationId)
        );
    }

    @GetMapping("/available")
    public ResponseEntity<List<SurplusResponse>> getAvailableSurplus() {
        return ResponseEntity.ok(
                surplusService.getAvailableSurplus()
        );
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<SurplusResponse>> getSurplusByCategory(
            @PathVariable ResourceCategory category
    ) {
        return ResponseEntity.ok(
                surplusService.getSurplusByCategory(category)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<SurplusResponse> updateSurplus(
            @PathVariable UUID id,
            @RequestBody UpdateSurplusRequest request
    ) {
        return ResponseEntity.ok(
                surplusService.updateSurplus(id, request)
        );
    }

    @PatchMapping("/{id}/status/{status}")
    public ResponseEntity<SurplusResponse> updateStatus(
            @PathVariable UUID id,
            @PathVariable SurplusStatus status
    ) {
        return ResponseEntity.ok(
                surplusService.updateStatus(id, status)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSurplus(
            @PathVariable UUID id
    ) {
        surplusService.deleteSurplus(id);

        return ResponseEntity.noContent().build();
    }
}


package com.example.surplus_management_system;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/needs")
@SecurityRequirement(name = "bearerAuth")
public class NeedController {

    private final NeedService needService;

    public NeedController(NeedService needService) {
        this.needService = needService;
    }

    @PostMapping("/organization/{organizationId}")
    public ResponseEntity<NeedResponse> createNeed(
            @PathVariable UUID organizationId,
            @Valid @RequestBody CreateNeedRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(needService.createNeed(organizationId, request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<NeedResponse> getNeed(
            @PathVariable UUID id
    ) {
        return ResponseEntity.ok(
                needService.getNeed(id)
        );
    }

    @GetMapping
    public ResponseEntity<List<NeedResponse>> getAllNeeds() {
        return ResponseEntity.ok(
                needService.getAllNeeds()
        );
    }

    @GetMapping("/organization/{organizationId}")
    public ResponseEntity<List<NeedResponse>> getNeedsByOrganization(
            @PathVariable UUID organizationId
    ) {
        return ResponseEntity.ok(
                needService.getNeedsByOrganization(organizationId)
        );
    }

    @GetMapping("/open")
    public ResponseEntity<List<NeedResponse>> getOpenNeeds() {
        return ResponseEntity.ok(
                needService.getOpenNeeds()
        );
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<NeedResponse>> getNeedsByCategory(
            @PathVariable ResourceCategory category
    ) {
        return ResponseEntity.ok(
                needService.getNeedsByCategory(category)
        );
    }

    @GetMapping("/urgency/{urgency}")
    public ResponseEntity<List<NeedResponse>> getNeedsByUrgency(
            @PathVariable UrgencyLevel urgency
    ) {
        return ResponseEntity.ok(
                needService.getNeedsByUrgency(urgency)
        );
    }

    @GetMapping("/emergency")
    public ResponseEntity<List<NeedResponse>> getEmergencyNeeds() {
        return ResponseEntity.ok(
                needService.getEmergencyNeeds()
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<NeedResponse> updateNeed(
            @PathVariable UUID id,
            @RequestBody UpdateNeedRequest request
    ) {
        return ResponseEntity.ok(
                needService.updateNeed(id, request)
        );
    }

    @PatchMapping("/{id}/status/{status}")
    public ResponseEntity<NeedResponse> updateStatus(
            @PathVariable UUID id,
            @PathVariable NeedStatus status
    ) {
        return ResponseEntity.ok(
                needService.updateStatus(id, status)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNeed(
            @PathVariable UUID id
    ) {
        needService.deleteNeed(id);

        return ResponseEntity.noContent().build();
    }
}


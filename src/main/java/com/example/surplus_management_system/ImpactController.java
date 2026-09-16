package com.example.surplus_management_system;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/impact")
@SecurityRequirement(name = "bearerAuth")
public class ImpactController {

    private final ImpactService impactService;

    public ImpactController(ImpactService impactService) {
        this.impactService = impactService;
    }

    @GetMapping
    public ResponseEntity<ImpactResponse> getOverallImpact() {
        return ResponseEntity.ok(
                impactService.getOverallImpact()
        );
    }

    @GetMapping("/organization/{organizationId}")
    public ResponseEntity<ImpactResponse> getOrganizationImpact(
            @PathVariable UUID organizationId
    ) {
        return ResponseEntity.ok(
                impactService.getOrganizationImpact(organizationId)
        );
    }

    @PostMapping("/record")
    public ResponseEntity<ImpactRecord> createImpactRecord(
            @RequestBody ImpactRecord impactRecord
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        impactService.createImpactRecord(
                                impactRecord
                        )
                );
    }

    @GetMapping("/organization/{organizationId}/records")
    public ResponseEntity<List<ImpactRecord>> getOrganizationImpactRecords(
            @PathVariable UUID organizationId
    ) {
        return ResponseEntity.ok(
                impactService.getOrganizationImpactRecords(
                        organizationId
                )
        );
    }
}


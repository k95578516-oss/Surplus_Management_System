package com.example.surplus_management_system;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/organizations")
@SecurityRequirement(name = "bearerAuth")
public class OrganizationController {

    private final OrganizationService organizationService;

    public OrganizationController(OrganizationService organizationService) {
        this.organizationService = organizationService;
    }

    @PostMapping("/user/{userId}")
    public ResponseEntity<OrganizationResponse> createOrganization(
            @PathVariable UUID userId,
            @Valid @RequestBody CreateOrganizationRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        organizationService.createOrganization(
                                userId,
                                request
                        )
                );
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrganizationResponse> getOrganization(
            @PathVariable UUID id
    ) {
        return ResponseEntity.ok(
                organizationService.getOrganization(id)
        );
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<OrganizationResponse> getOrganizationByUserId(
            @PathVariable UUID userId
    ) {
        return ResponseEntity.ok(
                organizationService.getOrganizationByUserId(userId)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrganizationResponse> updateOrganization(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateOrganizationRequest request
    ) {
        return ResponseEntity.ok(
                organizationService.updateOrganization(
                        id,
                        request
                )
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrganization(
            @PathVariable UUID id
    ) {
        organizationService.deleteOrganization(id);

        return ResponseEntity.noContent().build();
    }
}

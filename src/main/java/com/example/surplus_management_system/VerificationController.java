package com.example.surplus_management_system;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/verifications")
@SecurityRequirement(name = "bearerAuth")
public class VerificationController {

    private final VerificationService verificationService;

    public VerificationController(VerificationService verificationService) {
        this.verificationService = verificationService;
    }

    @PostMapping
    public ResponseEntity<VerificationResponse> submitVerification(
            @Valid @RequestBody SubmitVerificationRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        verificationService.submitVerification(request)
                );
    }

    @GetMapping("/{id}")
    public ResponseEntity<VerificationResponse> getVerification(
            @PathVariable UUID id
    ) {
        return ResponseEntity.ok(
                verificationService.getVerification(id)
        );
    }

    @GetMapping("/entity")
    public ResponseEntity<List<VerificationResponse>> getVerificationsByEntity(
            @RequestParam VerificationEntityType entityType,
            @RequestParam UUID entityId
    ) {
        return ResponseEntity.ok(
                verificationService.getVerificationsByEntity(
                        entityType,
                        entityId
                )
        );
    }

    @GetMapping("/entity/latest")
    public ResponseEntity<VerificationResponse> getLatestVerification(
            @RequestParam VerificationEntityType entityType,
            @RequestParam UUID entityId
    ) {
        return ResponseEntity.ok(
                verificationService.getLatestVerification(
                        entityType,
                        entityId
                )
        );
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<VerificationResponse>> getVerificationsByStatus(
            @PathVariable VerificationStatus status
    ) {
        return ResponseEntity.ok(
                verificationService.getVerificationsByStatus(status)
        );
    }

    @PatchMapping("/{id}/decision")
    public ResponseEntity<VerificationResponse> decideVerification(
            @PathVariable UUID id,
            @Valid @RequestBody VerificationDecisionRequest request,
            @RequestParam UUID verifiedBy
    ) {
        return ResponseEntity.ok(
                verificationService.decideVerification(
                        id,
                        request,
                        verifiedBy
                )
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVerification(
            @PathVariable UUID id
    ) {
        verificationService.deleteVerification(id);
        return ResponseEntity.noContent().build();
    }
}


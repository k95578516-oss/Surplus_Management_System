package com.example.surplus_management_system;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/requests")

public class RequestController {

    private final RequestService requestService;

    public RequestController(RequestService requestService) {
        this.requestService = requestService;
    }

    @PostMapping
    public ResponseEntity<RequestResponse> createRequest(
            @Valid @RequestBody CreateRequestRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(requestService.createRequest(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RequestResponse> getRequest(
            @PathVariable UUID id
    ) {
        return ResponseEntity.ok(
                requestService.getRequest(id)
        );
    }

    @GetMapping
    public ResponseEntity<List<RequestResponse>> getAllRequests() {
        return ResponseEntity.ok(
                requestService.getAllRequests()
        );
    }

    @GetMapping("/recipient/{organizationId}")
    public ResponseEntity<List<RequestResponse>> getRequestsByRecipient(
            @PathVariable UUID organizationId
    ) {
        return ResponseEntity.ok(
                requestService.getRequestsByRecipient(organizationId)
        );
    }

    @GetMapping("/provider/{organizationId}")
    public ResponseEntity<List<RequestResponse>> getRequestsByProvider(
            @PathVariable UUID organizationId
    ) {
        return ResponseEntity.ok(
                requestService.getRequestsByProvider(organizationId)
        );
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<RequestResponse>> getRequestsByStatus(
            @PathVariable RequestStatus status
    ) {
        return ResponseEntity.ok(
                requestService.getRequestsByStatus(status)
        );
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<RequestResponse> updateStatus(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateRequestStatusRequest request
    ) {
        return ResponseEntity.ok(
                requestService.updateStatus(id, request)
        );
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<Void> cancelRequest(
            @PathVariable UUID id
    ) {
        requestService.cancelRequest(id);

        return ResponseEntity.noContent().build();
    }
}

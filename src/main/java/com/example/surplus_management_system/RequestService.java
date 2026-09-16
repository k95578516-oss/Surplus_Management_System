package com.example.surplus_management_system;

import java.util.List;
import java.util.UUID;

public interface RequestService {

    RequestResponse createRequest(
            CreateRequestRequest request
    );

    RequestResponse getRequest(UUID id);

    List<RequestResponse> getAllRequests();

    List<RequestResponse> getRequestsByRecipient(
            UUID organizationId
    );

    List<RequestResponse> getRequestsByProvider(
            UUID organizationId
    );

    List<RequestResponse> getRequestsByStatus(
            RequestStatus status
    );

    RequestResponse updateStatus(
            UUID id,
            UpdateRequestStatusRequest request
    );

    void cancelRequest(UUID id);
}
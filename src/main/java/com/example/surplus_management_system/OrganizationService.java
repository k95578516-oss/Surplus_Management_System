package com.example.surplus_management_system;

import java.util.UUID;

public interface OrganizationService {

    OrganizationResponse createOrganization(
            UUID userId,
            CreateOrganizationRequest request
    );

    OrganizationResponse getOrganization(UUID id);

    OrganizationResponse getOrganizationByUserId(UUID userId);

    OrganizationResponse updateOrganization(
            UUID id,
            UpdateOrganizationRequest request
    );

    void deleteOrganization(UUID id);
}
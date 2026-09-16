package com.example.surplus_management_system;

import java.util.List;
import java.util.UUID;

public interface SurplusService {

    SurplusResponse createSurplus(
            UUID providerOrganizationId,
            CreateSurplusRequest request
    );

    SurplusResponse getSurplus(UUID id);

    List<SurplusResponse> getAllSurplus();

    List<SurplusResponse> getSurplusByOrganization(
            UUID organizationId
    );

    List<SurplusResponse> getAvailableSurplus();

    List<SurplusResponse> getSurplusByCategory(
            ResourceCategory category
    );

    SurplusResponse updateSurplus(
            UUID id,
            UpdateSurplusRequest request
    );

    SurplusResponse updateStatus(
            UUID id,
            SurplusStatus status
    );

    void deleteSurplus(UUID id);
}

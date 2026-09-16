package com.example.surplus_management_system;

import java.util.List;
import java.util.UUID;

public interface NeedService {

    NeedResponse createNeed(
            UUID recipientOrganizationId,
            CreateNeedRequest request
    );

    NeedResponse getNeed(UUID id);

    List<NeedResponse> getAllNeeds();

    List<NeedResponse> getNeedsByOrganization(
            UUID organizationId
    );

    List<NeedResponse> getOpenNeeds();

    List<NeedResponse> getNeedsByCategory(
            ResourceCategory category
    );

    List<NeedResponse> getNeedsByUrgency(
            UrgencyLevel urgency
    );

    List<NeedResponse> getEmergencyNeeds();

    NeedResponse updateNeed(
            UUID id,
            UpdateNeedRequest request
    );

    NeedResponse updateStatus(
            UUID id,
            NeedStatus status
    );

    void deleteNeed(UUID id);
}
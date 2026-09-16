package com.example.surplus_management_system;

import java.util.List;
import java.util.UUID;

public interface EmergencyService {

    EmergencyResponse createEmergency(CreateEmergencyRequest request);

    EmergencyResponse getEmergency(UUID id);

    List<EmergencyResponse> getAllEmergencies();

    List<EmergencyResponse> getActiveEmergencies();

    List<EmergencyResponse> getEmergenciesByStatus(
            EmergencyStatus status
    );

    List<EmergencyResponse> getEmergenciesBySeverity(
            EmergencySeverity severity
    );

    EmergencyResponse getEmergencyByNeed(UUID needId);

    EmergencyResponse updateStatus(
            UUID id,
            EmergencyStatusUpdateRequest request
    );

    void deleteEmergency(UUID id);
}
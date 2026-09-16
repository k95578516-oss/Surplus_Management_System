package com.example.surplus_management_system;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class NeedServiceImpl implements NeedService {

    private final NeedRepository needRepository;

    public NeedServiceImpl(NeedRepository needRepository) {
        this.needRepository = needRepository;
    }

    @Override
    public NeedResponse createNeed(
            UUID recipientOrganizationId,
            CreateNeedRequest request
    ) {

        Need need = Need.builder()
                .recipientOrganizationId(recipientOrganizationId)
                .resourceName(request.getResourceName())
                .category(request.getCategory())
                .quantityRequired(request.getQuantityRequired())
                .unit(request.getUnit())
                .urgency(request.getUrgency())
                .purpose(request.getPurpose())
                .locationAddress(request.getLocationAddress())
                .city(request.getCity())
                .state(request.getState())
                .latitude(request.getLatitude())
                .longitude(request.getLongitude())
                .requiredBy(request.getRequiredBy())
                .isEmergency(request.getIsEmergency())
                .status(NeedStatus.OPEN)
                .build();

        Need savedNeed = needRepository.save(need);

        return mapToResponse(savedNeed);
    }

    @Override
    public NeedResponse getNeed(UUID id) {

        Need need = findNeed(id);

        return mapToResponse(need);
    }

    @Override
    public List<NeedResponse> getAllNeeds() {

        return needRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<NeedResponse> getNeedsByOrganization(
            UUID organizationId
    ) {

        return needRepository
                .findByRecipientOrganizationId(organizationId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<NeedResponse> getOpenNeeds() {

        return needRepository
                .findByStatus(NeedStatus.OPEN)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<NeedResponse> getNeedsByCategory(
            ResourceCategory category
    ) {

        return needRepository
                .findByCategoryAndStatus(
                        category,
                        NeedStatus.OPEN
                )
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<NeedResponse> getNeedsByUrgency(
            UrgencyLevel urgency
    ) {

        return needRepository
                .findByUrgencyAndStatus(
                        urgency,
                        NeedStatus.OPEN
                )
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<NeedResponse> getEmergencyNeeds() {

        return needRepository
                .findByIsEmergencyTrueAndStatus(
                        NeedStatus.OPEN
                )
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public NeedResponse updateNeed(
            UUID id,
            UpdateNeedRequest request
    ) {

        Need need = findNeed(id);

        if (request.getResourceName() != null) {
            need.setResourceName(
                    request.getResourceName()
            );
        }

        if (request.getCategory() != null) {
            need.setCategory(
                    request.getCategory()
            );
        }

        if (request.getQuantityRequired() != null) {
            need.setQuantityRequired(
                    request.getQuantityRequired()
            );
        }

        if (request.getUnit() != null) {
            need.setUnit(
                    request.getUnit()
            );
        }

        if (request.getUrgency() != null) {
            need.setUrgency(
                    request.getUrgency()
            );
        }

        if (request.getPurpose() != null) {
            need.setPurpose(
                    request.getPurpose()
            );
        }

        if (request.getLocationAddress() != null) {
            need.setLocationAddress(
                    request.getLocationAddress()
            );
        }

        if (request.getCity() != null) {
            need.setCity(
                    request.getCity()
            );
        }

        if (request.getState() != null) {
            need.setState(
                    request.getState()
            );
        }

        if (request.getLatitude() != null) {
            need.setLatitude(
                    request.getLatitude()
            );
        }

        if (request.getLongitude() != null) {
            need.setLongitude(
                    request.getLongitude()
            );
        }

        if (request.getRequiredBy() != null) {
            need.setRequiredBy(
                    request.getRequiredBy()
            );
        }

        if (request.getIsEmergency() != null) {
            need.setIsEmergency(
                    request.getIsEmergency()
            );
        }

        Need updatedNeed = needRepository.save(need);

        return mapToResponse(updatedNeed);
    }

    @Override
    public NeedResponse updateStatus(
            UUID id,
            NeedStatus status
    ) {

        Need need = findNeed(id);

        need.setStatus(status);

        Need updatedNeed = needRepository.save(need);

        return mapToResponse(updatedNeed);
    }

    @Override
    public void deleteNeed(UUID id) {

        Need need = findNeed(id);

        needRepository.delete(need);
    }

    private Need findNeed(UUID id) {

        return needRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Need not found with id: " + id
                        )
                );
    }

    private NeedResponse mapToResponse(Need need) {

        return NeedResponse.builder()
                .id(need.getId())
                .recipientOrganizationId(
                        need.getRecipientOrganizationId()
                )
                .resourceName(
                        need.getResourceName()
                )
                .category(
                        need.getCategory()
                )
                .quantityRequired(
                        need.getQuantityRequired()
                )
                .unit(
                        need.getUnit()
                )
                .urgency(
                        need.getUrgency()
                )
                .purpose(
                        need.getPurpose()
                )
                .locationAddress(
                        need.getLocationAddress()
                )
                .city(
                        need.getCity()
                )
                .state(
                        need.getState()
                )
                .latitude(
                        need.getLatitude()
                )
                .longitude(
                        need.getLongitude()
                )
                .requiredBy(
                        need.getRequiredBy()
                )
                .isEmergency(
                        need.getIsEmergency()
                )
                .status(
                        need.getStatus()
                )
                .createdAt(
                        need.getCreatedAt()
                )
                .updatedAt(
                        need.getUpdatedAt()
                )
                .build();
    }
}
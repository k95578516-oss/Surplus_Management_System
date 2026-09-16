package com.example.surplus_management_system;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class SurplusServiceImpl implements SurplusService {

    private final SurplusRepository surplusRepository;

    public SurplusServiceImpl(
            SurplusRepository surplusRepository
    ) {
        this.surplusRepository = surplusRepository;
    }

    @Override
    public SurplusResponse createSurplus(
            UUID providerOrganizationId,
            CreateSurplusRequest request
    ) {

        Surplus surplus = Surplus.builder()
                .providerOrganizationId(providerOrganizationId)
                .resourceName(request.getResourceName())
                .category(request.getCategory())
                .quantity(request.getQuantity())
                .unit(request.getUnit())
                .condition(request.getCondition())
                .description(request.getDescription())
                .locationAddress(request.getLocationAddress())
                .city(request.getCity())
                .state(request.getState())
                .latitude(request.getLatitude())
                .longitude(request.getLongitude())
                .availableFrom(request.getAvailableFrom())
                .availableUntil(request.getAvailableUntil())
                .expiryDate(request.getExpiryDate())
                .isPerishable(request.getIsPerishable())
                .isEmergencyEligible(
                        request.getIsEmergencyEligible()
                )
                .imageUrl(request.getImageUrl())
                .status(SurplusStatus.AVAILABLE)
                .build();

        Surplus savedSurplus =
                surplusRepository.save(surplus);

        return mapToResponse(savedSurplus);
    }

    @Override
    public SurplusResponse getSurplus(UUID id) {

        Surplus surplus = findSurplus(id);

        return mapToResponse(surplus);
    }

    @Override
    public List<SurplusResponse> getAllSurplus() {

        return surplusRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<SurplusResponse> getSurplusByOrganization(
            UUID organizationId
    ) {

        return surplusRepository
                .findByProviderOrganizationId(organizationId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<SurplusResponse> getAvailableSurplus() {

        return surplusRepository
                .findByStatus(SurplusStatus.AVAILABLE)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<SurplusResponse> getSurplusByCategory(
            ResourceCategory category
    ) {

        return surplusRepository
                .findByCategoryAndStatus(
                        category,
                        SurplusStatus.AVAILABLE
                )
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public SurplusResponse updateSurplus(
            UUID id,
            UpdateSurplusRequest request
    ) {

        Surplus surplus = findSurplus(id);

        if (request.getResourceName() != null) {
            surplus.setResourceName(
                    request.getResourceName()
            );
        }

        if (request.getQuantity() != null) {
            surplus.setQuantity(
                    request.getQuantity()
            );
        }

        if (request.getUnit() != null) {
            surplus.setUnit(
                    request.getUnit()
            );
        }

        if (request.getCondition() != null) {
            surplus.setCondition(
                    request.getCondition()
            );
        }

        if (request.getDescription() != null) {
            surplus.setDescription(
                    request.getDescription()
            );
        }

        if (request.getLocationAddress() != null) {
            surplus.setLocationAddress(
                    request.getLocationAddress()
            );
        }

        if (request.getCity() != null) {
            surplus.setCity(
                    request.getCity()
            );
        }

        if (request.getState() != null) {
            surplus.setState(
                    request.getState()
            );
        }

        if (request.getLatitude() != null) {
            surplus.setLatitude(
                    request.getLatitude()
            );
        }

        if (request.getLongitude() != null) {
            surplus.setLongitude(
                    request.getLongitude()
            );
        }

        if (request.getAvailableFrom() != null) {
            surplus.setAvailableFrom(
                    request.getAvailableFrom()
            );
        }

        if (request.getAvailableUntil() != null) {
            surplus.setAvailableUntil(
                    request.getAvailableUntil()
            );
        }

        if (request.getExpiryDate() != null) {
            surplus.setExpiryDate(
                    request.getExpiryDate()
            );
        }

        if (request.getIsPerishable() != null) {
            surplus.setIsPerishable(
                    request.getIsPerishable()
            );
        }

        if (request.getIsEmergencyEligible() != null) {
            surplus.setIsEmergencyEligible(
                    request.getIsEmergencyEligible()
            );
        }

        if (request.getImageUrl() != null) {
            surplus.setImageUrl(
                    request.getImageUrl()
            );
        }

        Surplus updatedSurplus =
                surplusRepository.save(surplus);

        return mapToResponse(updatedSurplus);
    }

    @Override
    public SurplusResponse updateStatus(
            UUID id,
            SurplusStatus status
    ) {

        Surplus surplus = findSurplus(id);

        surplus.setStatus(status);

        Surplus updatedSurplus =
                surplusRepository.save(surplus);

        return mapToResponse(updatedSurplus);
    }

    @Override
    public void deleteSurplus(UUID id) {

        Surplus surplus = findSurplus(id);

        surplusRepository.delete(surplus);
    }

    private Surplus findSurplus(UUID id) {

        return surplusRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Surplus not found with id: " + id
                        )
                );
    }

    private SurplusResponse mapToResponse(
            Surplus surplus
    ) {

        return SurplusResponse.builder()
                .id(surplus.getId())
                .providerOrganizationId(
                        surplus.getProviderOrganizationId()
                )
                .resourceName(
                        surplus.getResourceName()
                )
                .category(
                        surplus.getCategory()
                )
                .quantity(
                        surplus.getQuantity()
                )
                .unit(
                        surplus.getUnit()
                )
                .condition(
                        surplus.getCondition()
                )
                .description(
                        surplus.getDescription()
                )
                .locationAddress(
                        surplus.getLocationAddress()
                )
                .city(
                        surplus.getCity()
                )
                .state(
                        surplus.getState()
                )
                .latitude(
                        surplus.getLatitude()
                )
                .longitude(
                        surplus.getLongitude()
                )
                .availableFrom(
                        surplus.getAvailableFrom()
                )
                .availableUntil(
                        surplus.getAvailableUntil()
                )
                .expiryDate(
                        surplus.getExpiryDate()
                )
                .isPerishable(
                        surplus.getIsPerishable()
                )
                .isEmergencyEligible(
                        surplus.getIsEmergencyEligible()
                )
                .imageUrl(
                        surplus.getImageUrl()
                )
                .status(
                        surplus.getStatus()
                )
                .createdAt(
                        surplus.getCreatedAt()
                )
                .updatedAt(
                        surplus.getUpdatedAt()
                )
                .build();
    }
}
package com.example.surplus_management_system;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class OrganizationServiceImpl implements OrganizationService {

    private final OrganizationRepository organizationRepository;

    public OrganizationServiceImpl(
            OrganizationRepository organizationRepository
    ) {
        this.organizationRepository = organizationRepository;
    }

    @Override
    public OrganizationResponse createOrganization(
            UUID userId,
            CreateOrganizationRequest request
    ) {

        if (organizationRepository.findByUserId(userId).isPresent()) {
            throw new RuntimeException(
                    "Organization already exists for this user"
            );
        }

        Organization organization = Organization.builder()
                .userId(userId)
                .name(request.getName())
                .type(request.getType())
                .registrationNumber(request.getRegistrationNumber())
                .description(request.getDescription())
                .email(request.getEmail())
                .phone(request.getPhone())
                .address(request.getAddress())
                .city(request.getCity())
                .state(request.getState())
                .country(request.getCountry())
                .latitude(request.getLatitude())
                .longitude(request.getLongitude())
                .verificationStatus(VerificationStatus.PENDING)
                .build();

        Organization savedOrganization =
                organizationRepository.save(organization);

        return mapToResponse(savedOrganization);
    }

    @Override
    public OrganizationResponse getOrganization(UUID id) {

        Organization organization =
                organizationRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Organization not found"
                                )
                        );

        return mapToResponse(organization);
    }

    @Override
    public OrganizationResponse getOrganizationByUserId(UUID userId) {

        Organization organization =
                organizationRepository.findByUserId(userId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Organization not found for user"
                                )
                        );

        return mapToResponse(organization);
    }

    @Override
    public OrganizationResponse updateOrganization(
            UUID id,
            UpdateOrganizationRequest request
    ) {

        Organization organization =
                organizationRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Organization not found"
                                )
                        );

        if (request.getName() != null) {
            organization.setName(request.getName());
        }

        if (request.getType() != null) {
            organization.setType(request.getType());
        }

        if (request.getRegistrationNumber() != null) {
            organization.setRegistrationNumber(
                    request.getRegistrationNumber()
            );
        }

        if (request.getDescription() != null) {
            organization.setDescription(
                    request.getDescription()
            );
        }

        if (request.getEmail() != null) {
            organization.setEmail(request.getEmail());
        }

        if (request.getPhone() != null) {
            organization.setPhone(request.getPhone());
        }

        if (request.getAddress() != null) {
            organization.setAddress(request.getAddress());
        }

        if (request.getCity() != null) {
            organization.setCity(request.getCity());
        }

        if (request.getState() != null) {
            organization.setState(request.getState());
        }

        if (request.getCountry() != null) {
            organization.setCountry(request.getCountry());
        }

        if (request.getLatitude() != null) {
            organization.setLatitude(request.getLatitude());
        }

        if (request.getLongitude() != null) {
            organization.setLongitude(request.getLongitude());
        }

        Organization updatedOrganization =
                organizationRepository.save(organization);

        return mapToResponse(updatedOrganization);
    }

    @Override
    public void deleteOrganization(UUID id) {

        Organization organization =
                organizationRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Organization not found"
                                )
                        );

        organizationRepository.delete(organization);
    }

    private OrganizationResponse mapToResponse(
            Organization organization
    ) {

        return OrganizationResponse.builder()
                .id(organization.getId())
                .userId(organization.getUserId())
                .name(organization.getName())
                .type(organization.getType())
                .registrationNumber(
                        organization.getRegistrationNumber()
                )
                .description(organization.getDescription())
                .email(organization.getEmail())
                .phone(organization.getPhone())
                .address(organization.getAddress())
                .city(organization.getCity())
                .state(organization.getState())
                .country(organization.getCountry())
                .latitude(organization.getLatitude())
                .longitude(organization.getLongitude())
                .verificationStatus(
                        organization.getVerificationStatus()
                )
                .verifiedAt(organization.getVerifiedAt())
                .createdAt(organization.getCreatedAt())
                .build();
    }
}
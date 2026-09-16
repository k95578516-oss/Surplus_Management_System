package com.example.surplus_management_system;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrganizationResponse {

    private UUID id;

    private UUID userId;

    private String name;

    private OrganizationType type;

    private String registrationNumber;

    private String description;

    private String email;

    private String phone;

    private String address;

    private String city;

    private String state;

    private String country;

    private Double latitude;

    private Double longitude;

    private VerificationStatus verificationStatus;

    private LocalDateTime verifiedAt;

    private LocalDateTime createdAt;
}
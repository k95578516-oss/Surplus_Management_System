package com.example.surplus_management_system;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateOrganizationRequest {

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
}
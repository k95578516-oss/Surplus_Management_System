package com.example.surplus_management_system;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthResponse {

    private UUID userId;

    private String name;

    private String email;

    private UserRole role;

    private String token;
}
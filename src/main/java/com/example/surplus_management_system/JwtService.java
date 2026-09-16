package com.example.surplus_management_system;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@Service
public class JwtService {

    private final SecretKey secretKey;
    private final long expirationMillis;

    public JwtService(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.expiration}") long expirationMillis
    ) {
        this.secretKey =
                Keys.hmacShaKeyFor(
                        Decoders.BASE64.decode(secret)
                );

        this.expirationMillis = expirationMillis;
    }

    public String generateToken(User user) {

        Instant now = Instant.now();

        return Jwts.builder()
                .subject(user.getId().toString())
                .claim("email", user.getEmail())
                .claim("role", user.getRole().name())
                .issuedAt(Date.from(now))
                .expiration(
                        Date.from(
                                now.plusMillis(
                                        expirationMillis
                                )
                        )
                )
                .signWith(secretKey)
                .compact();
    }

    public UUID extractUserId(String token) {

        String subject =
                extractAllClaims(token)
                        .getSubject();

        return UUID.fromString(subject);
    }

    public boolean isTokenValid(String token) {

        try {
            Claims claims =
                    extractAllClaims(token);

            return claims.getExpiration()
                    .after(new Date());

        } catch (Exception exception) {
            return false;
        }
    }

    public String extractRole(String token) {

        return extractAllClaims(token)
                .get("role", String.class);
    }

    private Claims extractAllClaims(String token) {

        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}


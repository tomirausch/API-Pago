package com.tomas.payments.infrastructure.security;

import java.util.Date;

import javax.crypto.SecretKey;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    private static final Logger logger = LoggerFactory.getLogger(JwtService.class);
    private static final long TOKEN_EXPIRATION_MS = 1000 * 60 * 60; // 1 hour
    private static final String SECRET_ENV_KEY = "jwt_secret_key";
    private static final int MINIMUM_SECRET_LENGTH = 32; // 256 bits

    private final String secret;

    public JwtService() {
        this.secret = System.getenv(SECRET_ENV_KEY);
        validateSecret();
    }

    private void validateSecret() {
        if (secret == null || secret.isEmpty()) {
            logger.error("JWT secret key not configured. Set environment variable: {}", SECRET_ENV_KEY);
            throw new IllegalStateException("JWT secret key not configured. Set environment variable: " + SECRET_ENV_KEY);
        }

        if (secret.length() < MINIMUM_SECRET_LENGTH) {
            logger.error("JWT secret key is too short. Minimum {} characters required, got {}", 
                MINIMUM_SECRET_LENGTH, secret.length());
            throw new IllegalStateException(
                "JWT secret must be at least " + MINIMUM_SECRET_LENGTH + " characters (256 bits) for HMAC-SHA256"
            );
        }

        logger.info("JWT secret key validated successfully");
    }

    public String generateToken(String username) {
        if (username == null || username.isBlank()) {
            logger.warn("Attempted to generate token with null or blank username");
            throw new IllegalArgumentException("Username cannot be null or blank");
        }

        try {
            logger.debug("Generating JWT token for username: {}", username);
            String token = Jwts.builder()
                    .subject(username)
                    .issuedAt(new Date())
                    .expiration(new Date(System.currentTimeMillis() + TOKEN_EXPIRATION_MS))
                    .signWith(getKey())
                    .compact();
            logger.debug("JWT token generated successfully for username: {}", username);
            return token;
        } catch (Exception e) {
            logger.error("Error generating JWT token for username: {}", username, e);
            throw new JwtException("Failed to generate token", e);
        }
    }

    public String extractUsername(String token) {
        if (token == null || token.isBlank()) {
            logger.warn("Attempted to extract username from null or blank token");
            throw new IllegalArgumentException("Token cannot be null or blank");
        }

        try {
            logger.debug("Extracting username from JWT token");
            String username = Jwts.parser()
                    .verifyWith(getKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .getSubject();
            logger.debug("Username extracted successfully from token");
            return username;
        } catch (ExpiredJwtException e) {
            logger.warn("JWT token has expired");
            throw e;
        } catch (JwtException e) {
            logger.warn("Invalid JWT token: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error extracting username from token", e);
            throw new JwtException("Failed to extract username from token", e);
        }
    }

    private SecretKey getKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }
}

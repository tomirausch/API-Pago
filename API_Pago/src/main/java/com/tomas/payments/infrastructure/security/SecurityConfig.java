package com.tomas.payments.infrastructure.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Security configuration for the API.
 *
 * CSRF Protection Decision:
 * ✓ CSRF is disabled for this stateless REST API because:
 *   - API uses JWT tokens (Bearer authentication), not session cookies
 *   - Stateless architecture (SessionCreationPolicy.STATELESS)
 *   - CSRF attacks require session-based authentication to be effective
 *   - CSRF tokens are unnecessary for token-based APIs
 *   - This is standard practice in REST APIs (RFC 6749, OAuth 2.0)
 *
 * Security is maintained through:
 *   - JWT validation on every request
 *   - HTTPS requirement (at deployment)
 *   - Token expiration (1 hour)
 *   - Secure token generation (HMAC-SHA256, minimum 256-bit key)
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private static final String AUTH_PATH = "/auth/**";
    private final JwtFilter jwtFilter;

    public SecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
            // CSRF disabled: stateless API with JWT token-based auth (not session-based)
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(authRequest ->
                authRequest
                    // Public endpoints: authentication routes
                    .requestMatchers(AUTH_PATH).permitAll()
                    // All other endpoints require authentication
                    .anyRequest().authenticated()
                )
            // Stateless API: no session cookies
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            // Add JWT filter before standard authentication filter
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
            .build();
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

}

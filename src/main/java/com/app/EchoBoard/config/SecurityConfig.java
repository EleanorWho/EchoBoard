package com.app.echoboard.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Security configuration class
 * Disable security authentication for development testing
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // Disable CSRF
            .authorizeHttpRequests(authz -> authz
                .requestMatchers("/api/**").permitAll() // Allow all API access
                .requestMatchers("/h2-console/**").permitAll() // Allow H2 console access
                .requestMatchers("/swagger-ui/**").permitAll() // Allow Swagger UI access
                .requestMatchers("/v3/api-docs/**").permitAll() // Allow OpenAPI docs access
                .anyRequest().permitAll() // Allow all other requests
            )
            .headers(headers -> headers
                .frameOptions(frameOptions -> frameOptions.sameOrigin()) // Allow H2 console frame
            );
        
        return http.build();
    }
}
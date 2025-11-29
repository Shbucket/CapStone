package com.shomari.flashcardbe.security;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        ClerkJwtAuthFilter clerkFilter = new ClerkJwtAuthFilter(
                "https://comic-whippet-10.clerk.accounts.dev",
                "https://comic-whippet-10.clerk.accounts.dev/.well-known/jwks.json"
        );

        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/users/**").permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(clerkFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}

package com.jumpstart.loadshedhub.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.*;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.*;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@Configuration @EnableMethodSecurity @RequiredArgsConstructor
public class SecurityConfig {
    private final JwtFilter jwtFilter;
    @Bean SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception { return http.csrf(c->c.disable()).sessionManagement(s->s.sessionCreationPolicy(SessionCreationPolicy.STATELESS)).authorizeHttpRequests(a->a.requestMatchers("/api/auth/**","/swagger-ui/**","/api-docs/**").permitAll().anyRequest().authenticated()).addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class).build(); }
}

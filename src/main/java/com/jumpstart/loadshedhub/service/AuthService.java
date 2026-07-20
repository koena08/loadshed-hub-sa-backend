package com.jumpstart.loadshedhub.service;

import com.jumpstart.loadshedhub.dto.AuthRequestDTO;
import com.jumpstart.loadshedhub.dto.ResponseDTO;
import com.jumpstart.loadshedhub.entity.Role;
import com.jumpstart.loadshedhub.entity.User;
import com.jumpstart.loadshedhub.repository.UserRepository;
import com.jumpstart.loadshedhub.security.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    public AuthService(UserRepository repository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil, AuthenticationManager authenticationManager) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
    }

    public ResponseDTO<String> register(AuthRequestDTO request) {
        Role role = request.getRole() != null ? Role.valueOf(request.getRole()) : Role.ROLE_CITIZEN;

        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(role)
                .build();

        repository.save(user);

        String token = jwtUtil.generateToken(user);
        //generic success factory method
        return ResponseDTO.success("Registration successful", token);
    }

    public ResponseDTO<String> login(AuthRequestDTO request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        User user = repository.findByEmail(request.getEmail()).orElseThrow();
        String token = jwtUtil.generateToken(user);

        return ResponseDTO.success("Login successful", token);
    }
}
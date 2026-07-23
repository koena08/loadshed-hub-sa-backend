package com.jumpstart.loadshedhub.service;

import com.jumpstart.loadshedhub.dto.*;
import com.jumpstart.loadshedhub.entity.*;
import com.jumpstart.loadshedhub.repository.UserRepository;
import com.jumpstart.loadshedhub.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service @RequiredArgsConstructor
public class AuthService {
    private final UserRepository users; private final PasswordEncoder encoder; private final JwtUtil jwt;
    public AuthResponseDTO register(AuthRequestDTO r) { if (users.findByEmail(r.getEmail()).isPresent()) throw new IllegalStateException("Email already registered"); User u=users.save(User.builder().email(r.getEmail()).password(encoder.encode(r.getPassword())).firstName(r.getFirstName()).lastName(r.getLastName()).role(Role.ROLE_CITIZEN).build()); return response(u); }
    public AuthResponseDTO login(AuthRequestDTO r) { User u=users.findByEmail(r.getEmail()).orElseThrow(()->new IllegalStateException("Invalid email or password")); if(!encoder.matches(r.getPassword(),u.getPassword())) throw new IllegalStateException("Invalid email or password"); return response(u); }
    private AuthResponseDTO response(User u) { return new AuthResponseDTO(jwt.generate(u),u.getEmail(),u.getRole().name()); }
}



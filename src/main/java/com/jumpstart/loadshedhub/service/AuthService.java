package com.jumpstart.loadshedhub.service;
import com.jumpstart.loadshedhub.dto.AuthRequestDTO;
import com.jumpstart.loadshedhub.entity.*;
import com.jumpstart.loadshedhub.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.jumpstart.loadshedhub.dto.AuthResponseDTO;
import com.jumpstart.loadshedhub.security.JwtUtil;
@Service @RequiredArgsConstructor
public class AuthService {
 private final UserRepository users; private final PasswordEncoder encoder; private final JwtUtil jwt;
 public AuthResponseDTO register(AuthRequestDTO r){ if(users.findByEmail(r.getEmail()).isPresent()) throw new IllegalStateException("An account with that email already exists"); User user=users.save(User.builder().email(r.getEmail()).password(encoder.encode(r.getPassword())).firstName(r.getFirstName()).lastName(r.getLastName()).role(Role.ROLE_CITIZEN).build()); return new AuthResponseDTO(jwt.generateToken(user),user.getEmail(),user.getRole().name()); }
 public AuthResponseDTO login(AuthRequestDTO r){ User user=users.findByEmail(r.getEmail()).orElseThrow(()->new IllegalStateException("Invalid email or password")); if(!encoder.matches(r.getPassword(),user.getPassword())) throw new IllegalStateException("Invalid email or password"); return new AuthResponseDTO(jwt.generateToken(user),user.getEmail(),user.getRole().name()); }
}

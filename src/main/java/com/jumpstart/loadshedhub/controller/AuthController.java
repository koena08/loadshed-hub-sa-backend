package com.jumpstart.loadshedhub.controller;
import com.jumpstart.loadshedhub.dto.*;
import com.jumpstart.loadshedhub.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/api/auth") @RequiredArgsConstructor
public class AuthController { private final AuthService auth;
 @PostMapping("/register") public ResponseEntity<Response<AuthResponseDTO>> register(@Valid @RequestBody AuthRequestDTO r){ return ResponseEntity.status(HttpStatus.CREATED).body(Response.success("Registration successful",auth.register(r))); }
 @PostMapping("/login") public ResponseEntity<Response<AuthResponseDTO>> login(@Valid @RequestBody AuthRequestDTO r){ return ResponseEntity.ok(Response.success("Login successful",auth.login(r))); }
}

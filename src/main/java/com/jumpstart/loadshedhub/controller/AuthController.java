package com.jumpstart.loadshedhub.controller;

import com.jumpstart.loadshedhub.dto.*;
import com.jumpstart.loadshedhub.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController @RequestMapping("/api/auth") @RequiredArgsConstructor
public class AuthController {
    private final AuthService auth;
    @PostMapping("/register") public ResponseEntity<ResponseDTO<AuthResponseDTO>> register(@Valid @RequestBody AuthRequestDTO r) { return ResponseEntity.status(HttpStatus.CREATED).body(ResponseDTO.success("Registered",auth.register(r))); }
    @PostMapping("/login") public ResponseDTO<AuthResponseDTO> login(@Valid @RequestBody AuthRequestDTO r) { return ResponseDTO.success("Logged in",auth.login(r)); }
}



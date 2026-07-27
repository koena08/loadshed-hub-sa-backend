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

    @PostMapping("/register")
    public ResponseEntity<ResponseDTO<AuthResponseDTO>> register(@Valid @RequestBody AuthRequestDTO r) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseDTO.success("Registered", auth.register(r)));
    }

    @PostMapping("/login")
    public ResponseDTO<AuthResponseDTO> login(@Valid @RequestBody AuthRequestDTO r) {
        return ResponseDTO.success("Logged in", auth.login(r));
    }

    @PostMapping("/forgot-password")
    public ResponseDTO<Void> forgotPassword(@Valid @RequestBody ForgotPasswordRequest r) {
        auth.forgotPassword(r.getEmail());
        // Deliberately generic message: never confirms whether the email is registered.
        return ResponseDTO.success("If that email is registered, a reset link is on its way.", null);
    }

    @PostMapping("/reset-password")
    public ResponseDTO<Void> resetPassword(@Valid @RequestBody ResetPasswordRequest r) {
        auth.resetPassword(r.getToken(), r.getNewPassword());
        return ResponseDTO.success("Password reset. You can now sign in with your new password.", null);
    }
}

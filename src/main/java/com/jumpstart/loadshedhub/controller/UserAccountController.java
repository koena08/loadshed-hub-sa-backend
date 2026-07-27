package com.jumpstart.loadshedhub.controller;

import com.jumpstart.loadshedhub.dto.PasswordUpdateRequest;
import com.jumpstart.loadshedhub.dto.ResponseDTO;
import com.jumpstart.loadshedhub.entity.User;
import com.jumpstart.loadshedhub.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users/me")
@RequiredArgsConstructor
public class UserAccountController {
    private final UserRepository users;
    private final PasswordEncoder passwordEncoder;

    @PutMapping("/password")
    public ResponseDTO<Void> changePassword(@AuthenticationPrincipal User user, @Valid @RequestBody PasswordUpdateRequest request) {
        if (request.getCurrentPassword() == null || !passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Current password is incorrect");
        }
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        users.save(user);
        return ResponseDTO.success("Password changed", null);
    }
}

package com.jumpstart.loadshedhub.service;

import com.jumpstart.loadshedhub.dto.*;
import com.jumpstart.loadshedhub.entity.*;
import com.jumpstart.loadshedhub.repository.PasswordResetTokenRepository;
import com.jumpstart.loadshedhub.repository.UserRepository;
import com.jumpstart.loadshedhub.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository users;
    private final PasswordEncoder encoder;
    private final JwtUtil jwt;
    private final PasswordResetTokenRepository resetTokens;
    private final MailService mailService;

    @Value("${app.frontend-url:http://localhost:5173}")
    private String frontendUrl;

    private static final int MAX_FAILED_ATTEMPTS = 5;
    private static final int LOCKOUT_MINUTES = 15;
    private static final SecureRandom RANDOM = new SecureRandom();

    public AuthResponseDTO register(AuthRequestDTO r) {
        if (users.findByEmail(r.getEmail()).isPresent()) throw new IllegalStateException("Email already registered");
        Role role = "ROLE_BUSINESS_OWNER".equalsIgnoreCase(r.getRequestedRole()) || "BUSINESS_OWNER".equalsIgnoreCase(r.getRequestedRole())
                ? Role.ROLE_BUSINESS_OWNER : Role.ROLE_CITIZEN;
        User u = users.save(User.builder()
                .email(r.getEmail().trim().toLowerCase())
                .password(encoder.encode(r.getPassword()))
                .firstName(r.getFirstName())
                .lastName(r.getLastName())
                .role(role)
                .enabled(true)
                .build());
        return response(u);
    }

    @Transactional
    public AuthResponseDTO login(AuthRequestDTO r) {
        User u = users.findByEmail(r.getEmail().trim().toLowerCase())
                .orElseThrow(() -> new IllegalStateException("Invalid email or password"));

        if (!u.isEnabled()) {
            throw new IllegalStateException("This account has been disabled. Contact support for help.");
        }
        if (!u.isAccountNonLocked()) {
            throw new IllegalStateException("This account is temporarily locked due to failed sign-in attempts. Try again later or reset your password.");
        }
        if (!encoder.matches(r.getPassword(), u.getPassword())) {
            registerFailedAttempt(u);
            throw new IllegalStateException("Invalid email or password");
        }

        if (u.getFailedLoginAttempts() > 0 || u.getLockedUntil() != null) {
            u.setFailedLoginAttempts(0);
            u.setLockedUntil(null);
            users.save(u);
        }
        return response(u);
    }

    private void registerFailedAttempt(User u) {
        u.setFailedLoginAttempts(u.getFailedLoginAttempts() + 1);
        if (u.getFailedLoginAttempts() >= MAX_FAILED_ATTEMPTS) {
            u.setLockedUntil(LocalDateTime.now().plusMinutes(LOCKOUT_MINUTES));
        }
        users.save(u);
    }

    /**
     * Always responds as if it succeeded (no matter whether the email exists) to avoid
     * leaking which addresses are registered. Generates a single-use token valid for 30 minutes.
     */
    @Transactional
    public void forgotPassword(String email) {
        users.findByEmail(email.trim().toLowerCase()).ifPresent(user -> {
            resetTokens.deleteByUserId(user.getId());
            String token = generateToken();
            resetTokens.save(PasswordResetToken.builder()
                    .token(token)
                    .user(user)
                    .expiresAt(LocalDateTime.now().plusMinutes(30))
                    .used(false)
                    .build());
            String resetLink = frontendUrl + "/?token=" + token;
            mailService.sendPasswordResetEmail(user.getEmail(), resetLink);
        });
    }

    @Transactional
    public void resetPassword(String token, String newPassword) {
        PasswordResetToken resetToken = resetTokens.findByToken(token)
                .orElseThrow(() -> new IllegalStateException("This reset link is invalid or has already been used."));
        if (resetToken.isUsed() || resetToken.isExpired()) {
            throw new IllegalStateException("This reset link has expired. Please request a new one.");
        }
        User user = resetToken.getUser();
        user.setPassword(encoder.encode(newPassword));
        user.setFailedLoginAttempts(0);
        user.setLockedUntil(null);
        users.save(user);

        resetToken.setUsed(true);
        resetTokens.save(resetToken);
    }

    private String generateToken() {
        byte[] bytes = new byte[32];
        RANDOM.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    private AuthResponseDTO response(User u) {
        return new AuthResponseDTO(jwt.generate(u), u.getEmail(), u.getRole().name());
    }
}

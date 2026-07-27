package com.jumpstart.loadshedhub.controller;

import com.jumpstart.loadshedhub.dto.AdminStatsDTO;
import com.jumpstart.loadshedhub.dto.PasswordUpdateRequest;
import com.jumpstart.loadshedhub.dto.ResponseDTO;
import com.jumpstart.loadshedhub.dto.RoleUpdateRequest;
import com.jumpstart.loadshedhub.dto.UserStatusRequest;
import com.jumpstart.loadshedhub.entity.Role;
import com.jumpstart.loadshedhub.entity.User;
import com.jumpstart.loadshedhub.exception.ResourceNotFoundException;
import com.jumpstart.loadshedhub.repository.LocationRepository;
import com.jumpstart.loadshedhub.repository.ReportRepository;
import com.jumpstart.loadshedhub.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminUserController {
    private final UserRepository users;
    private final LocationRepository locations;
    private final ReportRepository reports;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/users")
    public ResponseDTO<List<User>> list() {
        return ResponseDTO.success("Users retrieved", users.findAll());
    }

    @GetMapping("/stats")
    public ResponseDTO<AdminStatsDTO> stats() {
        AdminStatsDTO dto = new AdminStatsDTO(
                users.count(),
                users.countByRole(Role.ROLE_CITIZEN),
                users.countByRole(Role.ROLE_BUSINESS_OWNER),
                users.countByRole(Role.ROLE_ADMIN),
                locations.count(),
                locations.countByVerifiedTrue(),
                locations.count() - locations.countByVerifiedTrue(),
                reports.count()
        );
        return ResponseDTO.success("Stats retrieved", dto);
    }

    @PutMapping("/users/{id}/role")
    public ResponseDTO<User> updateRole(@PathVariable Long id, @Valid @RequestBody RoleUpdateRequest request, @AuthenticationPrincipal User requester) {
        if (id.equals(requester.getId())) {
            throw new IllegalStateException("You can't change your own role.");
        }
        User user = users.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found: " + id));
        user.setRole(request.getRole());
        return ResponseDTO.success("User role updated", users.save(user));
    }

    @PutMapping("/users/{id}/status")
    public ResponseDTO<User> updateStatus(@PathVariable Long id, @Valid @RequestBody UserStatusRequest request, @AuthenticationPrincipal User requester) {
        if (id.equals(requester.getId())) {
            throw new IllegalStateException("You can't disable your own account.");
        }
        User user = users.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found: " + id));
        user.setEnabled(request.getEnabled());
        if (Boolean.TRUE.equals(request.getEnabled())) {
            user.setFailedLoginAttempts(0);
            user.setLockedUntil(null);
        }
        return ResponseDTO.success(request.getEnabled() ? "User re-enabled" : "User disabled", users.save(user));
    }

    @DeleteMapping("/users/{id}")
    public ResponseDTO<Void> delete(@PathVariable Long id, @AuthenticationPrincipal User requester) {
        if (id.equals(requester.getId())) {
            throw new IllegalStateException("You can't delete your own account.");
        }
        User user = users.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found: " + id));
        users.delete(user);
        return ResponseDTO.success("User deleted", null);
    }

    @PutMapping("/users/{id}/password")
    public ResponseDTO<Void> resetPassword(@PathVariable Long id, @Valid @RequestBody PasswordUpdateRequest request) {
        User user = users.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found: " + id));
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        user.setFailedLoginAttempts(0);
        user.setLockedUntil(null);
        users.save(user);
        return ResponseDTO.success("Password reset", null);
    }
}

package com.jumpstart.loadshedhub.controller;

import com.jumpstart.loadshedhub.dto.ResponseDTO;
import com.jumpstart.loadshedhub.dto.RoleUpdateRequest;
import com.jumpstart.loadshedhub.entity.Role;
import com.jumpstart.loadshedhub.entity.User;
import com.jumpstart.loadshedhub.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminUserController {
    private final UserRepository users;

    @GetMapping
    public ResponseDTO<List<User>> list() { return ResponseDTO.success("Users retrieved", users.findAll()); }

    @PutMapping("/{id}/role")
    public ResponseDTO<User> updateRole(@PathVariable Long id, @Valid @RequestBody RoleUpdateRequest request) {
        User user = users.findById(id).orElseThrow(() -> new IllegalArgumentException("User not found"));
        user.setRole(request.getRole());
        return ResponseDTO.success("User role updated", users.save(user));
    }

    @DeleteMapping("/{id}")
    public ResponseDTO<Void> delete(@PathVariable Long id) { users.deleteById(id); return ResponseDTO.success("User deleted", null); }
}

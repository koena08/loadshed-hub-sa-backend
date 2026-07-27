package com.jumpstart.loadshedhub.controller;

import com.jumpstart.loadshedhub.dto.*;
import com.jumpstart.loadshedhub.entity.Location;
import com.jumpstart.loadshedhub.entity.User;
import com.jumpstart.loadshedhub.service.LocationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController @RequestMapping("/api/locations") @RequiredArgsConstructor
public class LocationController {
    private final LocationService service;

    @GetMapping
    public ResponseDTO<Page<Location>> list(@RequestParam(required=false) String name, @PageableDefault(size=20, sort="name") Pageable pageable) {
        return ResponseDTO.success("Locations retrieved", service.search(name, pageable));
    }

    @GetMapping("/{id}")
    public ResponseDTO<Location> get(@PathVariable Long id) {
        return ResponseDTO.success("Location retrieved", service.get(id));
    }

    // Hubs registered by the signed-in business owner (or admin), for their "My Hubs" dashboard.
    @GetMapping("/mine")
    @PreAuthorize("hasAnyRole('ADMIN','BUSINESS_OWNER')")
    public ResponseDTO<List<Location>> mine(@AuthenticationPrincipal User user) {
        return ResponseDTO.success("Your hubs retrieved", service.findMine(user.getId()));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','BUSINESS_OWNER')")
    public ResponseDTO<Location> create(@Valid @RequestBody LocationRequest request, @AuthenticationPrincipal User user) {
        return ResponseDTO.success("Location created", service.create(request, user));
    }

    // Admins may edit any hub; business owners may only edit hubs they registered (enforced in service).
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','BUSINESS_OWNER')")
    public ResponseDTO<Location> update(@PathVariable Long id, @Valid @RequestBody LocationRequest request, @AuthenticationPrincipal User user) {
        return ResponseDTO.success("Location updated", service.update(id, request, user));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','BUSINESS_OWNER')")
    public ResponseDTO<Void> delete(@PathVariable Long id, @AuthenticationPrincipal User user) {
        service.delete(id, user);
        return ResponseDTO.success("Location deleted", null);
    }

    // Admin-only moderation: approve/verify a community-submitted hub.
    @PatchMapping("/{id}/verify")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseDTO<Location> verify(@PathVariable Long id, @RequestBody Map<String, Boolean> body) {
        boolean verified = Boolean.TRUE.equals(body.getOrDefault("verified", true));
        return ResponseDTO.success(verified ? "Location verified" : "Location unverified", service.setVerified(id, verified));
    }
}

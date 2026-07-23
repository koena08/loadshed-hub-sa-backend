package com.jumpstart.loadshedhub.controller;

import com.jumpstart.loadshedhub.dto.*;
import com.jumpstart.loadshedhub.entity.Location;
import com.jumpstart.loadshedhub.service.LocationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController @RequestMapping("/api/locations") @RequiredArgsConstructor
public class LocationController {
    private final LocationService service;
    @GetMapping public Response<Page<Location>> list(@RequestParam(required=false) String name, @PageableDefault(size=20, sort="name") Pageable pageable) { return Response.success("Locations retrieved", service.search(name, pageable)); }
    @GetMapping("/{id}") public Response<Location> get(@PathVariable Long id) { return Response.success("Location retrieved", service.get(id)); }
    @PostMapping @PreAuthorize("hasRole('ADMIN')") public Response<Location> create(@Valid @RequestBody LocationRequest request) { return Response.success("Location created", service.create(request)); }
    @PutMapping("/{id}") @PreAuthorize("hasRole('ADMIN')") public Response<Location> update(@PathVariable Long id, @Valid @RequestBody LocationRequest request) { return Response.success("Location updated", service.update(id, request)); }
    @DeleteMapping("/{id}") @PreAuthorize("hasRole('ADMIN')") public Response<Void> delete(@PathVariable Long id) { service.delete(id); return Response.success("Location deleted", null); }
}

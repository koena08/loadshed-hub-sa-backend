package com.jumpstart.loadshedhub.controller;

import com.jumpstart.loadshedhub.dto.LocationRequest;
import com.jumpstart.loadshedhub.dto.Response;
import com.jumpstart.loadshedhub.entity.Location;
import com.jumpstart.loadshedhub.service.LocationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/locations")
@RequiredArgsConstructor
public class LocationController {

    private final LocationService locationService;

    @PostMapping
    public ResponseEntity<Response<Location>> createLocation(@Valid @RequestBody LocationRequest dto) {
        Location location = locationService.createLocation(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Response.success("Location created successfully", location));
    }

    @GetMapping
    public ResponseEntity<Response<List<Location>>> getAllLocations() {
        List<Location> locations = locationService.getAllLocations();
        return ResponseEntity.ok(Response.success("Locations retrieved successfully", locations));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response<Location>> getLocationById(@PathVariable Long id) {
        Location location = locationService.getLocationById(id);
        return ResponseEntity.ok(Response.success("Location retrieved successfully", location));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Response<Location>> updateLocation(
            @PathVariable Long id,
            @Valid @RequestBody LocationRequest dto) {
        Location location = locationService.updateLocation(id, dto);
        return ResponseEntity.ok(Response.success("Location updated successfully", location));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Response<Void>> deleteLocation(@PathVariable Long id) {
        locationService.deleteLocation(id);
        return ResponseEntity.ok(Response.success("Location deleted successfully", null));
    }
}
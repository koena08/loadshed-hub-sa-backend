package com.jumpstart.loadshedhub.controller;

import com.jumpstart.loadshedhub.dto.AmenityRequest;
import com.jumpstart.loadshedhub.dto.ResponseDTO;
import com.jumpstart.loadshedhub.entity.Amenity;
import com.jumpstart.loadshedhub.exception.ResourceNotFoundException;
import com.jumpstart.loadshedhub.repository.AmenityRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/amenities")
@RequiredArgsConstructor
public class AmenityController {
    private final AmenityRepository amenities;

    // Public: needed to populate the amenity checklist on the hub registration form.
    @GetMapping
    public ResponseDTO<List<Amenity>> list() {
        return ResponseDTO.success("Amenities retrieved", amenities.findAll());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseDTO<Amenity> create(@Valid @RequestBody AmenityRequest request) {
        amenities.findByNameIgnoreCase(request.getName()).ifPresent(a -> {
            throw new IllegalStateException("An amenity with that name already exists.");
        });
        return ResponseDTO.success("Amenity created", amenities.save(Amenity.builder().name(request.getName()).build()));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseDTO<Amenity> update(@PathVariable Long id, @Valid @RequestBody AmenityRequest request) {
        Amenity amenity = amenities.findById(id).orElseThrow(() -> new ResourceNotFoundException("Amenity not found: " + id));
        amenity.setName(request.getName());
        return ResponseDTO.success("Amenity updated", amenities.save(amenity));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseDTO<Void> delete(@PathVariable Long id) {
        Amenity amenity = amenities.findById(id).orElseThrow(() -> new ResourceNotFoundException("Amenity not found: " + id));
        amenities.delete(amenity);
        return ResponseDTO.success("Amenity deleted", null);
    }
}

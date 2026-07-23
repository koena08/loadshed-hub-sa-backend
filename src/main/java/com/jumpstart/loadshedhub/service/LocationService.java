package com.jumpstart.loadshedhub.service;

import com.jumpstart.loadshedhub.dto.LocationRequest;
import com.jumpstart.loadshedhub.entity.*;
import com.jumpstart.loadshedhub.exception.ResourceNotFoundException;
import com.jumpstart.loadshedhub.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.HashSet;

@Service @RequiredArgsConstructor @Transactional
public class LocationService {
    private final LocationRepository locations;
    private final AmenityRepository amenities;
    @Transactional(readOnly=true) public Page<Location> search(String name, Pageable pageable) { return name == null || name.isBlank() ? locations.findAll(pageable) : locations.findByNameContainingIgnoreCase(name, pageable); }
    @Transactional(readOnly=true) public Location get(Long id) { return locations.findById(id).orElseThrow(() -> new ResourceNotFoundException("Location not found: " + id)); }
    public Location create(LocationRequest r) { return locations.save(map(new Location(), r)); }
    public Location update(Long id, LocationRequest r) { return locations.save(map(get(id), r)); }
    public void delete(Long id) { locations.delete(get(id)); }
    private Location map(Location l, LocationRequest r) { l.setName(r.getName()); l.setAddress(r.getAddress()); l.setLatitude(r.getLatitude()); l.setLongitude(r.getLongitude());
        l.setOperatingHours(r.getOperatingHours());
        l.setLoadReduction(Boolean.TRUE.equals(r.getLoadReduction()));
        l.setScheduleNote(r.getScheduleNote()); l.setAmenities(new HashSet<>(r.getAmenityIds() == null ? java.util.List.of() : amenities.findAllById(r.getAmenityIds()))); return l; }
}

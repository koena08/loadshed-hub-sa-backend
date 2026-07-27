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
import java.util.List;

@Service @RequiredArgsConstructor @Transactional
public class LocationService {
    private final LocationRepository locations;
    private final AmenityRepository amenities;

    @Transactional(readOnly=true)
    public Page<Location> search(String name, Pageable pageable) {
        return name == null || name.isBlank() ? locations.findAll(pageable) : locations.findByNameContainingIgnoreCase(name, pageable);
    }

    @Transactional(readOnly=true)
    public Location get(Long id) {
        return locations.findById(id).orElseThrow(() -> new ResourceNotFoundException("Location not found: " + id));
    }

    @Transactional(readOnly=true)
    public List<Location> findMine(Long ownerId) {
        return locations.findByOwnerIdOrderByIdDesc(ownerId);
    }

    public Location create(LocationRequest r, User owner) {
        Location location = map(new Location(), r);
        location.setOwner(owner);
        // Admins register pre-verified hubs; community/business owner submissions need review.
        location.setVerified(owner != null && owner.getRole() == Role.ROLE_ADMIN);
        return locations.save(location);
    }

    public Location update(Long id, LocationRequest r, User requester) {
        Location existing = get(id);
        assertCanManage(existing, requester);
        return locations.save(map(existing, r));
    }

    public void delete(Long id, User requester) {
        Location existing = get(id);
        assertCanManage(existing, requester);
        locations.delete(existing);
    }

    public Location setVerified(Long id, boolean verified) {
        Location location = get(id);
        location.setVerified(verified);
        return locations.save(location);
    }

    @Transactional(readOnly = true)
    public long countVerified() {
        return locations.countByVerifiedTrue();
    }

    @Transactional(readOnly = true)
    public long countAll() {
        return locations.count();
    }

    /** Admins can manage any hub; business owners may only manage hubs they registered. */
    private void assertCanManage(Location location, User requester) {
        if (requester == null) throw new IllegalStateException("Authentication required.");
        if (requester.getRole() == Role.ROLE_ADMIN) return;
        if (location.getOwner() != null && location.getOwner().getId().equals(requester.getId())) return;
        throw new IllegalStateException("You can only manage hubs you registered.");
    }

    private Location map(Location l, LocationRequest r) {
        l.setName(r.getName());
        l.setAddress(r.getAddress());
        l.setLatitude(r.getLatitude());
        l.setLongitude(r.getLongitude());
        l.setOperatingHours(r.getOperatingHours());
        l.setLoadReduction(Boolean.TRUE.equals(r.getLoadReduction()));
        l.setScheduleNote(r.getScheduleNote());
        l.setAmenities(new HashSet<>(r.getAmenityIds() == null ? List.of() : amenities.findAllById(r.getAmenityIds())));
        return l;
    }
}

package com.jumpstart.loadshedhub.service;

import com.jumpstart.loadshedhub.dto.LocationRequest;
import com.jumpstart.loadshedhub.entity.Location;
import com.jumpstart.loadshedhub.exception.ResourceNotFoundException;
import com.jumpstart.loadshedhub.repository.LocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LocationService {

    private final LocationRepository locationRepository;

    public Location createLocation(LocationRequest dto) {
        Location location = Location.builder()
                .name(dto.getName())
                .address(dto.getAddress())
                .latitude(dto.getLatitude())
                .longitude(dto.getLongitude())
                .build();
        return locationRepository.save(location);
    }

    public List<Location> getAllLocations() {
        return locationRepository.findAll();
    }

    public Location getLocationById(Long id) {
        return locationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Location not found with id: " + id));
    }

    public Location updateLocation(Long id, LocationRequest dto) {
        Location location = getLocationById(id);
        location.setName(dto.getName());
        location.setAddress(dto.getAddress());
        location.setLatitude(dto.getLatitude());
        location.setLongitude(dto.getLongitude());
        return locationRepository.save(location);
    }

    public void deleteLocation(Long id) {
        Location location = getLocationById(id);
        locationRepository.delete(location);
    }
}
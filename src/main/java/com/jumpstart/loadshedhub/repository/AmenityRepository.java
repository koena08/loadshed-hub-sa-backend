package com.jumpstart.loadshedhub.repository;

import com.jumpstart.loadshedhub.entity.Amenity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface AmenityRepository extends JpaRepository<Amenity, Long> { Optional<Amenity> findByNameIgnoreCase(String name); }

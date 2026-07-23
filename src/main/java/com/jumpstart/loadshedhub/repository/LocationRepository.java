package com.jumpstart.loadshedhub.repository;

import com.jumpstart.loadshedhub.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {
    org.springframework.data.domain.Page<Location> findByNameContainingIgnoreCase(String name, org.springframework.data.domain.Pageable pageable);
}
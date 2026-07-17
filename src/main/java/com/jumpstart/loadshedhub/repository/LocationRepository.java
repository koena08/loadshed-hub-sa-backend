package com.jumpstart.loadshedhub.repository;

import com.jumpstart.loadshedhub.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {
}
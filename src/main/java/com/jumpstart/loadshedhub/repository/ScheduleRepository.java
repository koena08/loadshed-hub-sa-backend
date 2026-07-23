package com.jumpstart.loadshedhub.repository;

import com.jumpstart.loadshedhub.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    List<Schedule> findByLocationId(Long locationId);
    List<Schedule> findByLocationIdAndUtilityType(Long locationId, String utilityType);
}
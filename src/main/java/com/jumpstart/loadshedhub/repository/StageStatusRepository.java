package com.jumpstart.loadshedhub.repository;

import com.jumpstart.loadshedhub.entity.StageStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StageStatusRepository extends JpaRepository<StageStatus, Long> {
}

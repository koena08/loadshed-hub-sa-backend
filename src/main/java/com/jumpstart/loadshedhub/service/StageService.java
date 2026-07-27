package com.jumpstart.loadshedhub.service;

import com.jumpstart.loadshedhub.entity.StageStatus;
import com.jumpstart.loadshedhub.entity.User;
import com.jumpstart.loadshedhub.repository.StageStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class StageService {
    private static final Long SINGLETON_ID = 1L;

    private final StageStatusRepository stageStatusRepository;

    @Transactional(readOnly = true)
    public StageStatus getCurrent() {
        return stageStatusRepository.findById(SINGLETON_ID).orElseGet(() -> StageStatus.builder()
                .id(SINGLETON_ID)
                .stage(0)
                .note(null)
                .updatedAt(LocalDateTime.now())
                .updatedBy("system")
                .build());
    }

    public StageStatus update(int stage, String note, User admin) {
        StageStatus status = stageStatusRepository.findById(SINGLETON_ID).orElse(StageStatus.builder().id(SINGLETON_ID).build());
        status.setStage(stage);
        status.setNote(note);
        status.setUpdatedAt(LocalDateTime.now());
        status.setUpdatedBy(admin.getEmail());
        return stageStatusRepository.save(status);
    }
}

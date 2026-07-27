package com.jumpstart.loadshedhub.controller;

import com.jumpstart.loadshedhub.dto.ResponseDTO;
import com.jumpstart.loadshedhub.dto.StageUpdateRequest;
import com.jumpstart.loadshedhub.entity.StageStatus;
import com.jumpstart.loadshedhub.entity.User;
import com.jumpstart.loadshedhub.service.StageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class StageController {
    private final StageService stageService;

    // Public: every visitor, signed in or not, should see the current national stage.
    @GetMapping("/api/stage")
    public ResponseDTO<StageStatus> current() {
        return ResponseDTO.success("Current stage retrieved", stageService.getCurrent());
    }

    @PutMapping("/api/admin/stage")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseDTO<StageStatus> update(@Valid @RequestBody StageUpdateRequest request, @AuthenticationPrincipal User admin) {
        return ResponseDTO.success("Stage updated", stageService.update(request.getStage(), request.getNote(), admin));
    }
}

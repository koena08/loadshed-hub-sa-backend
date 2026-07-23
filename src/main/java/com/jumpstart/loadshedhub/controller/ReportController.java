package com.jumpstart.loadshedhub.controller;

import com.jumpstart.loadshedhub.dto.ReportRequestDTO;
import com.jumpstart.loadshedhub.dto.ResponseDTO;
import com.jumpstart.loadshedhub.entity.Report;
import com.jumpstart.loadshedhub.entity.User;
import com.jumpstart.loadshedhub.service.ReportService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {
    private final ReportService reportService;

    @PostMapping
    @PreAuthorize("hasAnyRole('CITIZEN','ADMIN')")
    public ResponseEntity<ResponseDTO<Report>> create(@Valid @RequestBody ReportRequestDTO dto, @AuthenticationPrincipal User user) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseDTO.success("Check-in submitted", reportService.createReport(dto, user)));
    }

    @GetMapping("/location/{locationId}")
    public ResponseDTO<List<Report>> getByLocation(@PathVariable Long locationId) {
        return ResponseDTO.success("Reports retrieved", reportService.getReportsByLocation(locationId));
    }

    @GetMapping("/location/{locationId}/recent")
    public ResponseDTO<List<Report>> getRecent(@PathVariable Long locationId) {
        return ResponseDTO.success("Recent reports retrieved", reportService.getRecentReports(locationId));
    }

    @GetMapping("/mine")
    @PreAuthorize("hasAnyRole('CITIZEN','ADMIN')")
    public ResponseDTO<List<Report>> mine(@AuthenticationPrincipal User user) {
        return ResponseDTO.success("Your reports retrieved", reportService.getMyReports(user));
    }

    @PutMapping("/{reportId}")
    @PreAuthorize("hasAnyRole('CITIZEN','ADMIN')")
    public ResponseDTO<Report> update(@PathVariable Long reportId, @Valid @RequestBody ReportRequestDTO dto, @AuthenticationPrincipal User user) {
        return ResponseDTO.success("Report updated", reportService.updateReport(reportId, dto, user));
    }

    @DeleteMapping("/{reportId}")
    @PreAuthorize("hasAnyRole('CITIZEN','ADMIN')")
    public ResponseDTO<Void> delete(@PathVariable Long reportId, @AuthenticationPrincipal User user) {
        reportService.deleteReport(reportId, user);
        return ResponseDTO.success("Report deleted", null);
    }
}



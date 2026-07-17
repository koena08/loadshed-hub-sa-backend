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
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    /**
     * POST /api/reports
     * Submit a new crowd-sourced check-in at a hub.
     */
    @PostMapping
    public ResponseEntity<ResponseDTO<Report>> createReport(
            @Valid @RequestBody ReportRequestDTO dto,
            @AuthenticationPrincipal User user) {

        Report report = reportService.createReport(dto, user);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ResponseDTO.success(report, "Check-in submitted successfully! 🇿🇦"));
    }

    /**
     * GET /api/reports/location/{locationId}
     * Get all reports for a specific hub.
     */
    @GetMapping("/location/{locationId}")
    public ResponseEntity<ResponseDTO<List<Report>>> getReportsByLocation(
            @PathVariable Long locationId) {

        List<Report> reports = reportService.getReportsByLocation(locationId);
        return ResponseEntity.ok(
                ResponseDTO.success(reports, "Reports retrieved for location " + locationId)
        );
    }

    /**
     * GET /api/reports/location/{locationId}/recent
     * Get fresh reports (last 4 hours) for a hub — "Is power ON right now?"
     */
    @GetMapping("/location/{locationId}/recent")
    public ResponseEntity<ResponseDTO<List<Report>>> getRecentReports(
            @PathVariable Long locationId) {

        List<Report> reports = reportService.getRecentReports(locationId);
        return ResponseEntity.ok(
                ResponseDTO.success(reports, "Recent reports (last 4 hours)")
        );
    }

    /**
     * GET /api/reports/mine
     * Get all reports submitted by the currently logged-in user.
     */
    @GetMapping("/mine")
    public ResponseEntity<ResponseDTO<List<Report>>> getMyReports(
            @AuthenticationPrincipal User user) {

        List<Report> reports = reportService.getMyReports(user);
        return ResponseEntity.ok(
                ResponseDTO.success(reports, "Your reports retrieved")
        );
    }

    /**
     * PUT /api/reports/{reportId}
     * Edit a report (within 30-minute window, owner only).
     */
    @PutMapping("/{reportId}")
    public ResponseEntity<ResponseDTO<Report>> updateReport(
            @PathVariable Long reportId,
            @Valid @RequestBody ReportRequestDTO dto,
            @AuthenticationPrincipal User user) {

        Report report = reportService.updateReport(reportId, dto, user);
        return ResponseEntity.ok(
                ResponseDTO.success(report, "Report updated successfully")
        );
    }

    /**
     * DELETE /api/reports/{reportId}
     * Delete a report (owner only).
     */
    @DeleteMapping("/{reportId}")
    public ResponseEntity<ResponseDTO<Void>> deleteReport(
            @PathVariable Long reportId,
            @AuthenticationPrincipal User user) {

        reportService.deleteReport(reportId, user);
        return ResponseEntity.ok(
                ResponseDTO.success(null, "Report deleted successfully")
        );
    }
}
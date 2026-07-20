package com.jumpstart.loadshedhub.controller;

import com.jumpstart.loadshedhub.dto.ReportRequestDTO;
import com.jumpstart.loadshedhub.dto.Response;
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

    @PostMapping
    public ResponseEntity<Response<Report>> createReport(
            @Valid @RequestBody ReportRequestDTO dto,
            @AuthenticationPrincipal User user) {
        Report report = reportService.createReport(dto, user);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Response.success("Check-in submitted successfully", report));
    }

    @GetMapping("/location/{locationId}")
    public ResponseEntity<Response<List<Report>>> getReportsByLocation(@PathVariable Long locationId) {
        List<Report> reports = reportService.getReportsByLocation(locationId);
        return ResponseEntity.ok(Response.success("Reports retrieved for location " + locationId, reports));
    }

    @GetMapping("/location/{locationId}/recent")
    public ResponseEntity<Response<List<Report>>> getRecentReports(@PathVariable Long locationId) {
        List<Report> reports = reportService.getRecentReports(locationId);
        return ResponseEntity.ok(Response.success("Recent reports from the last 4 hours", reports));
    }

    @GetMapping("/mine")
    public ResponseEntity<Response<List<Report>>> getMyReports(@AuthenticationPrincipal User user) {
        List<Report> reports = reportService.getMyReports(user);
        return ResponseEntity.ok(Response.success("Your reports retrieved", reports));
    }

    @PutMapping("/{reportId}")
    public ResponseEntity<Response<Report>> updateReport(
            @PathVariable Long reportId,
            @Valid @RequestBody ReportRequestDTO dto,
            @AuthenticationPrincipal User user) {
        Report report = reportService.updateReport(reportId, dto, user);
        return ResponseEntity.ok(Response.success("Report updated successfully", report));
    }

    @DeleteMapping("/{reportId}")
    public ResponseEntity<Response<Void>> deleteReport(
            @PathVariable Long reportId,
            @AuthenticationPrincipal User user) {
        reportService.deleteReport(reportId, user);
        return ResponseEntity.ok(Response.success("Report deleted successfully", null));
    }
}

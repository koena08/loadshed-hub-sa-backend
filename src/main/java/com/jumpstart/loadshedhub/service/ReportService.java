package com.jumpstart.loadshedhub.service;

import com.jumpstart.loadshedhub.dto.ReportRequestDTO;
import com.jumpstart.loadshedhub.entity.Location;
import com.jumpstart.loadshedhub.entity.Report;
import com.jumpstart.loadshedhub.entity.User;
import com.jumpstart.loadshedhub.exception.NotFoundException;
import com.jumpstart.loadshedhub.repository.LocationRepository;
import com.jumpstart.loadshedhub.repository.ReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ReportService {

    private final ReportRepository reportRepository;
    private final LocationRepository locationRepository;

    private static final int MAX_REPORTS_PER_LOCATION_PER_DAY = 5;
    private static final int REPORT_FRESHNESS_HOURS = 4;

    /**
     * Submit a new crowd-sourced check-in.
     * Prevents spam by limiting reports per user per location per day.
     */
    public Report createReport(ReportRequestDTO dto, User user) {

        // 1. Validate location exists
        Location location = locationRepository.findById(dto.getLocationId())
                .orElseThrow(() -> new NotFoundException(
                        "Location not found with id: " + dto.getLocationId()
                ));

        // 2. Anti-spam: limit reports per user per location per day
        LocalDateTime todayStart = LocalDate.now().atStartOfDay();
        long reportsToday = reportRepository.countUserReportsToday(
                location.getId(), user.getId(), todayStart
        );

        if (reportsToday >= MAX_REPORTS_PER_LOCATION_PER_DAY) {
            throw new IllegalStateException(
                    "You've reached the maximum reports for this location today. " +
                            "Please try again tomorrow."
            );
        }

        // 3. Build and save
        Report report = Report.builder()
                .powerStatus(dto.getPowerStatus().toUpperCase())
                .wifiStatus(dto.getWifiStatus() != null ? dto.getWifiStatus().toUpperCase() : null)
                .crowdLevel(dto.getCrowdLevel() != null ? dto.getCrowdLevel().toUpperCase() : null)
                .safetyRating(dto.getSafetyRating() != null ? dto.getSafetyRating().toUpperCase() : null)
                .comment(dto.getComment())
                .user(user)
                .location(location)
                .build();

        return reportRepository.save(report);
    }

    /**
     * Get all reports for a location (latest first).
     */
    @Transactional(readOnly = true)
    public List<Report> getReportsByLocation(Long locationId) {
        Location location = locationRepository.findById(locationId)
                .orElseThrow(() -> new NotFoundException(
                        "Location not found with id: " + locationId
                ));
        return reportRepository.findByLocationOrderByCreatedAtDesc(location);
    }

    /**
     * Get recent reports (within freshness window) for a location.
     */
    @Transactional(readOnly = true)
    public List<Report> getRecentReports(Long locationId) {
        Location location = locationRepository.findById(locationId)
                .orElseThrow(() -> new NotFoundException(
                        "Location not found with id: " + locationId
                ));
        LocalDateTime since = LocalDateTime.now().minusHours(REPORT_FRESHNESS_HOURS);
        return reportRepository.findRecentReportsByLocation(location, since);
    }

    /**
     * Get all reports submitted by a specific user.
     */
    @Transactional(readOnly = true)
    public List<Report> getMyReports(User user) {
        return reportRepository.findByUserOrderByCreatedAtDesc(user);
    }

    /**
     * Delete a report (only the owner can delete).
     */
    public void deleteReport(Long reportId, User user) {
        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new NotFoundException(
                        "Report not found with id: " + reportId
                ));

        if (!report.getUser().getId().equals(user.getId())) {
            throw new IllegalStateException(
                    "You can only delete your own reports."
            );
        }

        reportRepository.delete(report);
    }

    /**
     * Update a report (only the owner, within a time window).
     */
    public Report updateReport(Long reportId, ReportRequestDTO dto, User user) {
        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new NotFoundException(
                        "Report not found with id: " + reportId
                ));


        if (!report.getUser().getId().equals(user.getId())) {
            throw new IllegalStateException(
                    "You can only edit your own reports."
            );
        }


        if (report.getCreatedAt().isBefore(LocalDateTime.now().minusMinutes(30))) {
            throw new IllegalStateException(
                    "Reports can only be edited within 30 minutes of submission."
            );
        }

       
        report.setPowerStatus(dto.getPowerStatus().toUpperCase());
        report.setWifiStatus(dto.getWifiStatus() != null ? dto.getWifiStatus().toUpperCase() : null);
        report.setCrowdLevel(dto.getCrowdLevel() != null ? dto.getCrowdLevel().toUpperCase() : null);
        report.setSafetyRating(dto.getSafetyRating() != null ? dto.getSafetyRating().toUpperCase() : null);
        report.setComment(dto.getComment());

        return reportRepository.save(report);
    }
}
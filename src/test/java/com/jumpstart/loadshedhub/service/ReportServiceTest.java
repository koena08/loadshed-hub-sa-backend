package com.jumpstart.loadshedhub.service;

import com.jumpstart.loadshedhub.dto.ReportRequestDTO;
import com.jumpstart.loadshedhub.entity.Location;
import com.jumpstart.loadshedhub.entity.Report;
import com.jumpstart.loadshedhub.entity.User;
import com.jumpstart.loadshedhub.exception.ResourceNotFoundException;
import com.jumpstart.loadshedhub.repository.LocationRepository;
import com.jumpstart.loadshedhub.repository.ReportRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReportServiceTest {

    @Mock private ReportRepository reportRepository;
    @Mock private LocationRepository locationRepository;
    @InjectMocks private ReportService reportService;

    private User user;
    private Location location;
    private ReportRequestDTO request;

    @BeforeEach
    void setUp() {
        user = User.builder().id(10L).email("citizen@example.com").build();
        location = Location.builder().id(20L).name("Sandton Library").build();
        request = ReportRequestDTO.builder()
                .locationId(20L).powerStatus("on").wifiStatus("available")
                .crowdLevel("low").safetyRating("safe").comment("Working normally")
                .build();
    }

    @Test
    void createsNormalisedReportForExistingLocation() {
        when(locationRepository.findById(20L)).thenReturn(Optional.of(location));
        when(reportRepository.countUserReportsToday(eq(20L), eq(10L), any(LocalDateTime.class))).thenReturn(0L);
        when(reportRepository.save(any(Report.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Report result = reportService.createReport(request, user);

        assertEquals("ON", result.getPowerStatus());
        assertEquals("AVAILABLE", result.getWifiStatus());
        assertEquals(location, result.getLocation());
        assertEquals(user, result.getUser());
        verify(reportRepository).save(any(Report.class));
    }

    @Test
    void rejectsReportForMissingLocation() {
        when(locationRepository.findById(20L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> reportService.createReport(request, user));

        verify(reportRepository, never()).save(any());
    }

    @Test
    void rejectsSixthReportForTheSameLocationOnTheSameDay() {
        when(locationRepository.findById(20L)).thenReturn(Optional.of(location));
        when(reportRepository.countUserReportsToday(eq(20L), eq(10L), any(LocalDateTime.class))).thenReturn(5L);

        assertThrows(IllegalStateException.class, () -> reportService.createReport(request, user));

        verify(reportRepository, never()).save(any());
    }
}

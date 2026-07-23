package com.jumpstart.loadshedhub.controller;

import com.jumpstart.loadshedhub.dto.Response;
import com.jumpstart.loadshedhub.dto.ScheduleRequestDTO;
import com.jumpstart.loadshedhub.entity.Schedule;
import com.jumpstart.loadshedhub.service.ScheduleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;

    @GetMapping("/api/locations/{locationId}/schedules")
    public Response<List<Schedule>> getForLocation(
            @PathVariable Long locationId,
            @RequestParam(required = false) String utilityType) {
        List<Schedule> schedules = (utilityType == null || utilityType.isBlank())
                ? scheduleService.getSchedulesForLocation(locationId)
                : scheduleService.getSchedulesForLocationByUtility(locationId, utilityType);
        return Response.success("Schedules retrieved", schedules);
    }

    @GetMapping("/api/schedules/{id}")
    public Response<Schedule> get(@PathVariable Long id) {
        return Response.success("Schedule retrieved", scheduleService.get(id));
    }

    @PostMapping("/api/schedules")
    @PreAuthorize("hasRole('ADMIN')")
    public Response<Schedule> create(@Valid @RequestBody ScheduleRequestDTO dto) {
        return Response.success("Schedule created", scheduleService.create(dto));
    }

    @PutMapping("/api/schedules/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Response<Schedule> update(@PathVariable Long id, @Valid @RequestBody ScheduleRequestDTO dto) {
        return Response.success("Schedule updated", scheduleService.update(id, dto));
    }

    @DeleteMapping("/api/schedules/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Response<Void> delete(@PathVariable Long id) {
        scheduleService.delete(id);
        return Response.success("Schedule deleted", null);
    }
}
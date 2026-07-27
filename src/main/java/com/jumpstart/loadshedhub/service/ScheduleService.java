package com.jumpstart.loadshedhub.service;

import com.jumpstart.loadshedhub.dto.ScheduleRequestDTO;
import com.jumpstart.loadshedhub.entity.Location;
import com.jumpstart.loadshedhub.entity.Schedule;
import com.jumpstart.loadshedhub.exception.ResourceNotFoundException;
import com.jumpstart.loadshedhub.repository.LocationRepository;
import com.jumpstart.loadshedhub.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final LocationRepository locationRepository;

    @Transactional(readOnly = true)
    public List<Schedule> getSchedulesForLocation(Long locationId) {
        locationRepository.findById(locationId)
                .orElseThrow(() -> new ResourceNotFoundException("Location not found: " + locationId));
        return scheduleRepository.findByLocationId(locationId);
    }

    @Transactional(readOnly = true)
    public List<Schedule> getSchedulesForLocationByUtility(Long locationId, String utilityType) {
        locationRepository.findById(locationId)
                .orElseThrow(() -> new ResourceNotFoundException("Location not found: " + locationId));
        return scheduleRepository.findByLocationIdAndUtilityType(locationId, utilityType.toUpperCase());
    }

    @Transactional(readOnly = true)
    public Schedule get(Long id) {
        return scheduleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Schedule not found: " + id));
    }

    public Schedule create(ScheduleRequestDTO dto) {
        Location location = locationRepository.findById(dto.getLocationId())
                .orElseThrow(() -> new ResourceNotFoundException("Location not found: " + dto.getLocationId()));
        return scheduleRepository.save(map(new Schedule(), dto, location));
    }

    public Schedule update(Long id, ScheduleRequestDTO dto) {
        Schedule schedule = get(id);
        Location location = locationRepository.findById(dto.getLocationId())
                .orElseThrow(() -> new ResourceNotFoundException("Location not found: " + dto.getLocationId()));
        return scheduleRepository.save(map(schedule, dto, location));
    }

    public void delete(Long id) {
        scheduleRepository.delete(get(id));
    }

    private Schedule map(Schedule schedule, ScheduleRequestDTO dto, Location location) {
        schedule.setLocation(location);
        schedule.setUtilityType(dto.getUtilityType().toUpperCase());
        schedule.setDayOfWeek(dto.getDayOfWeek().toUpperCase());
        schedule.setOpenTime(dto.getOpenTime());
        schedule.setCloseTime(dto.getCloseTime());
        schedule.setGeneratorBackup(dto.isGeneratorBackup());
        schedule.setMinStageForGenerator(dto.getMinStageForGenerator());
        schedule.setNotes(dto.getNotes());
        return schedule;
    }
}
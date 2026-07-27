package com.jumpstart.loadshedhub.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalTime;

@Getter @Setter
public class ScheduleRequestDTO {
    @NotNull(message = "Location ID is required")
    private Long locationId;

    @NotNull(message = "Utility type is required (ELECTRICITY or WATER)")
    private String utilityType;

    @NotNull(message = "Day of week is required")
    private String dayOfWeek;

    @NotNull(message = "Open time is required")
    private LocalTime openTime;

    @NotNull(message = "Close time is required")
    private LocalTime closeTime;

    private boolean generatorBackup;
    private Integer minStageForGenerator;
    private String notes;
}
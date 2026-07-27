package com.jumpstart.loadshedhub.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReportRequestDTO {

    @NotBlank(message = "Power status cannot be empty")
    private String powerStatus;

    private String wifiStatus;
    private String waterStatus;
    private String parkingStatus;
    private String crowdLevel;
    private String safetyRating;

    @Size(max = 500, message = "Comment must be under 500 characters")
    private String comment;

    @NotNull(message = "Location ID is required")
    private Long locationId;
}

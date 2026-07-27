package com.jumpstart.loadshedhub.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class StageUpdateRequest {
    @NotNull
    @Min(0) @Max(8)
    private Integer stage;

    private String note;
}

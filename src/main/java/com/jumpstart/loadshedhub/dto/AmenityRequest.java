package com.jumpstart.loadshedhub.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AmenityRequest {
    @NotBlank(message = "Amenity name is required")
    @Size(max = 50)
    private String name;
}

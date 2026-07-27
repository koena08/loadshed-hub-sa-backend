package com.jumpstart.loadshedhub.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import java.util.Set;

//Defines exact JSON structure the React frontend must send
//when attempting to create or update a location
@Data
public class LocationRequest {
    @NotBlank(message = "Location name is required")
    private String name;
    private String address;

    //Gps coordinates for map plotting on the frontend
    private Double latitude;
    private Double longitude;

    private String operatingHours;
    private Boolean loadReduction = false;
    private String scheduleNote;

    //List of IDs representing available amenities
    private Set<Long> amenityIds;
}

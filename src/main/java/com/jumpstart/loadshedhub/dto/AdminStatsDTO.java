package com.jumpstart.loadshedhub.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AdminStatsDTO {
    private long totalUsers;
    private long totalCitizens;
    private long totalBusinessOwners;
    private long totalAdmins;
    private long totalLocations;
    private long verifiedLocations;
    private long pendingLocations;
    private long totalReports;
}

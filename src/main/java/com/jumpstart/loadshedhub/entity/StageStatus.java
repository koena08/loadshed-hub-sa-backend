package com.jumpstart.loadshedhub.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;

import java.time.LocalDateTime;

//Single row (id is always 1) representing the current national load-shedding stage.
//Kept as its own tiny table rather than a config file so admins can update it at runtime.
@Entity
@Table(name = "stage_status")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class StageStatus {
    @Id
    private Long id;

    @Min(0) @Max(8)
    @Column(nullable = false)
    private int stage;

    // Optional free-text note, e.g. "Stage likely to increase at 16:00" or a schedule window.
    @Column(length = 255)
    private String note;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    // Email of the admin who last changed it, for a simple audit trail.
    private String updatedBy;
}

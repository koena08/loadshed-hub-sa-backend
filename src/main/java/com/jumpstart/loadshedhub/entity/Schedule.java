package com.jumpstart.loadshedhub.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.time.LocalTime;

@Entity
@Table(name = "schedules")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id", nullable = false)
    @com.fasterxml.jackson.annotation.JsonIgnore
    private Location location;

    @NotNull
    @Column(name = "utility_type", nullable = false, length = 20)
    private String utilityType; // ELECTRICITY or WATER

    @NotNull
    @Column(name = "day_of_week", nullable = false, length = 20)
    private String dayOfWeek; // e.g. MONDAY, or "MON-FRI" shorthand

    @NotNull
    @Column(name = "open_time", nullable = false)
    private LocalTime openTime;

    @NotNull
    @Column(name = "close_time", nullable = false)
    private LocalTime closeTime;

    // Electricity-specific: irrelevant/null for WATER rows
    @Column(name = "generator_backup", nullable = false)
    @Builder.Default
    private boolean generatorBackup = false;

    @Column(name = "min_stage_for_generator")
    private Integer minStageForGenerator; // e.g. 3 = generator kicks in at Stage 3+

    @Column(length = 255)
    private String notes;
}
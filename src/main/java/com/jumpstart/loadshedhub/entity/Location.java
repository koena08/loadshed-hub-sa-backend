package com.jumpstart.loadshedhub.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "locations")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(length = 255)
    private String address;

    private Double latitude;
    private Double longitude;

    @Column(nullable = false)
    private boolean verified = false;
}
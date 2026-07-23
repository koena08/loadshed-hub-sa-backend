package com.jumpstart.loadshedhub.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.HashSet;
import java.util.Set;

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
    @Builder.Default
    private boolean verified = false;

    @ManyToMany
    @JoinTable(name="location_amenities", joinColumns=@JoinColumn(name="location_id"), inverseJoinColumns=@JoinColumn(name="amenity_id"))
    @Builder.Default
    private Set<Amenity> amenities = new HashSet<>();
}



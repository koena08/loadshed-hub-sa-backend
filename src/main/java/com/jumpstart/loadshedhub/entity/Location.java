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

    @Column(length = 100)
    private String operatingHours;

    @Column(nullable = false)
    @Builder.Default
    private boolean loadReduction = false;

    @Column(length = 255)
    private String scheduleNote;

    @Column(nullable = false)
    @Builder.Default
    private boolean verified = false;

    //the business owner (or admin) who registered this hub; null for legacy/seeded data.
    //Eager (not lazy) because open-in-view is disabled: serializing this after the request's
    //Hibernate session has closed would otherwise throw LazyInitializationException.
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "owner_id")
    @com.fasterxml.jackson.annotation.JsonIgnoreProperties({"password", "authorities", "hibernateLazyInitializer", "handler"})
    private User owner;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name="location_amenities", joinColumns=@JoinColumn(name="location_id"), inverseJoinColumns=@JoinColumn(name="amenity_id"))
    @Builder.Default
    private Set<Amenity> amenities = new HashSet<>();
}






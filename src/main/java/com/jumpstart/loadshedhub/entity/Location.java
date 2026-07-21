package com.jumpstart.loadshedhub.entity;

import jakarta.persistence.*;
import lombok.*;
<<<<<<< HEAD
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import java.util.HashSet;
import java.util.Set;
=======
>>>>>>> bce1c30216c5b82041fddfb22f56fd1f90b24ccf

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
<<<<<<< HEAD
    @NotBlank
=======
>>>>>>> bce1c30216c5b82041fddfb22f56fd1f90b24ccf
    private String name;

    @Column(length = 255)
    private String address;

<<<<<<< HEAD
    @DecimalMin("-90.0") @DecimalMax("90.0")
    private Double latitude;
    @DecimalMin("-180.0") @DecimalMax("180.0")
    private Double longitude;

    @Column(nullable = false)
    @Builder.Default
    private boolean verified = false;

    @ManyToMany
    @JoinTable(name="location_amenities", joinColumns=@JoinColumn(name="location_id"), inverseJoinColumns=@JoinColumn(name="amenity_id"))
    @Builder.Default
    private Set<Amenity> amenities = new HashSet<>();
}
=======
    private Double latitude;
    private Double longitude;

    @Column(nullable = false)
    private boolean verified = false;
}
>>>>>>> bce1c30216c5b82041fddfb22f56fd1f90b24ccf

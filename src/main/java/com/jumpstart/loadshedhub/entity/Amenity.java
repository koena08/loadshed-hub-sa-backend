package com.jumpstart.loadshedhub.entity;

import jakarta.persistence.*;
import lombok.*;
import jakarta.validation.constraints.NotBlank;

@Entity @Table(name="amenities") @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Amenity {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @NotBlank @Column(nullable=false, unique=true, length=50) private String name;
}

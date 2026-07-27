package com.jumpstart.loadshedhub.entity;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

//mapped directly to the DB "users" table
//implements UserDetails
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;

    //email acts as the username for login, must be unique.
    @Column(unique = true, nullable = false)
    private String email;

    //stores the hashed BCrypt password
    @Column(nullable = false)
    @JsonIgnore
    private String password;

    //maps our custom Role enum as a simple string in the database
    @Enumerated(EnumType.STRING)
    private Role role;

    //admin can disable (ban) an account without deleting it
    @Column(nullable = false)
    @Builder.Default
    private boolean enabled = true;

    //number of consecutive failed login attempts, used for basic brute-force protection
    @Column(nullable = false)
    @Builder.Default
    @JsonIgnore
    private int failedLoginAttempts = 0;

    @JsonIgnore
    private LocalDateTime lockedUntil;

    @Column(updatable = false)
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    //returning 'true' means the account is fully active and not banned
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return lockedUntil == null || lockedUntil.isBefore(LocalDateTime.now());
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}

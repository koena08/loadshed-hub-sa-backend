package com.jumpstart.loadshedhub.config;

import com.jumpstart.loadshedhub.entity.*;
import com.jumpstart.loadshedhub.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import java.util.Set;
import java.util.HashSet;

@Component
@RequiredArgsConstructor
public class JpaTestDataSeeder implements CommandLineRunner {
    private final AmenityRepository amenities;
    private final LocationRepository locations;
    private final UserRepository users;
    private final ReportRepository reports;
    private final StageStatusRepository stageStatusRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.seed-data:true}")
    private boolean enabled;

    @Override
    public void run(String... args) {
        if (!enabled) return;

        Amenity power = amenity("POWER");
        Amenity wifi = amenity("WIFI");
        Amenity safe = amenity("SAFE");
        Amenity parking = amenity("PARKING");

        Location sandton = location("Sandton Library", "Alice Lane, Sandton", -26.1076, 28.0567, true);
        Location rosebank = location("BP Garage Rosebank", "Oxford Road, Rosebank", -26.1456, 28.0366, true);
        Location braamfontein = location("Braamfontein Study Hub", "Juta Street, Braamfontein", -26.1929, 28.0341, false);
        sandton.setAmenities(new HashSet<>(Set.of(power, wifi, safe)));
        rosebank.setAmenities(new HashSet<>(Set.of(power, parking)));
        braamfontein.setAmenities(new HashSet<>(Set.of(wifi)));
        locations.save(sandton); locations.save(rosebank); locations.save(braamfontein);

        User citizen = user("citizen@example.com", "Test", "Citizen", Role.ROLE_CITIZEN);
        User owner = user("owner@example.com", "Test", "Owner", Role.ROLE_BUSINESS_OWNER);
        User admin = user("admin@example.com", "Test", "Admin", Role.ROLE_ADMIN);

        if (locations.findAll().stream().noneMatch(l -> l.getOwner() != null)) {
            Location ownerHub = location("Melville Coffee & Coworking", "7th Street, Melville", -26.1793, 28.0009, false);
            ownerHub.setOwner(owner);
            ownerHub.setAmenities(new HashSet<>(Set.of(power, wifi)));
            locations.save(ownerHub);
        }

        if (reports.count() == 0) {
            reports.save(Report.builder().powerStatus("ON").wifiStatus("AVAILABLE").crowdLevel("LOW").safetyRating("SAFE").comment("Power and Wi-Fi are working.").user(citizen).location(sandton).build());
            reports.save(Report.builder().powerStatus("OFF").wifiStatus("UNAVAILABLE").crowdLevel("HIGH").safetyRating("SAFE").comment("Power is currently off.").user(citizen).location(rosebank).build());
            reports.save(Report.builder().powerStatus("ON").wifiStatus("AVAILABLE").crowdLevel("MEDIUM").safetyRating("SAFE").comment("Quiet study area.").user(admin).location(braamfontein).build());
        }

        if (stageStatusRepository.count() == 0) {
            stageStatusRepository.save(StageStatus.builder()
                    .id(1L)
                    .stage(2)
                    .note("Sample data - update this from the admin console")
                    .updatedAt(java.time.LocalDateTime.now())
                    .updatedBy(admin.getEmail())
                    .build());
        }
    }

    private Amenity amenity(String name) { return amenities.findByNameIgnoreCase(name).orElseGet(() -> amenities.save(Amenity.builder().name(name).build())); }
    private Location location(String name, String address, double lat, double lon, boolean verified) { return locations.findAll().stream().filter(l -> l.getName().equalsIgnoreCase(name)).findFirst().orElseGet(() -> locations.save(Location.builder().name(name).address(address).latitude(lat).longitude(lon).verified(verified).build())); }
    private User user(String email, String first, String last, Role role) { return users.findByEmail(email).orElseGet(() -> users.save(User.builder().email(email).firstName(first).lastName(last).role(role).password(passwordEncoder.encode("Password123")).build())); }
}

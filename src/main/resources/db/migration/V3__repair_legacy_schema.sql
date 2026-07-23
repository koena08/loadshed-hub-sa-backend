-- Repairs databases created before the Location/Amenity merge.
CREATE TABLE IF NOT EXISTS amenities (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS location_amenities (
    location_id BIGINT NOT NULL,
    amenity_id BIGINT NOT NULL,
    PRIMARY KEY (location_id, amenity_id),
    FOREIGN KEY (location_id) REFERENCES locations(id),
    FOREIGN KEY (amenity_id) REFERENCES amenities(id)
);

INSERT IGNORE INTO amenities(name) VALUES ('POWER'), ('WIFI'), ('SAFE'), ('PARKING');

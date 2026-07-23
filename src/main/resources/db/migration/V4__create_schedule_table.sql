CREATE TABLE schedules (
                           id BIGINT AUTO_INCREMENT PRIMARY KEY,
                           location_id BIGINT NOT NULL,
                           utility_type VARCHAR(20) NOT NULL,
                           day_of_week VARCHAR(20) NOT NULL,
                           open_time TIME NOT NULL,
                           close_time TIME NOT NULL,
                           generator_backup BOOLEAN NOT NULL DEFAULT FALSE,
                           min_stage_for_generator INT,
                           notes VARCHAR(255),
                           FOREIGN KEY (location_id) REFERENCES locations(id)
);
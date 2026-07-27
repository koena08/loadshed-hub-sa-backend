-- Run only against your local loadshed_hub database when you want a clean manual test.
SET FOREIGN_KEY_CHECKS = 0;
DELETE FROM reports;
DELETE FROM location_amenities;
DELETE FROM locations;
DELETE FROM amenities;
DELETE FROM users;
SET FOREIGN_KEY_CHECKS = 1;
-- Restart the app. Flyway V2 will not re-run automatically; use DROP DATABASE/CREATE DATABASE
-- if you need the seed migrations to execute again.

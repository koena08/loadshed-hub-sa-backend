# Local test checklist

1. Create the database once: `CREATE DATABASE loadshed_hub CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;`
2. Start with Java 21: `mvnw.cmd spring-boot:run`.
3. Import `postman/LoadShedHub-Main.postman_collection.json`.
4. Run requests 01 and 02 first. Request 02 stores the JWT in the collection variable `token`.
5. Run request 07 to create a report for seeded location `1`.

Hibernate/JPA creates or updates `users`, `locations`, `amenities`, `location_amenities`, and `reports` from the entity mappings. Foreign keys, non-null columns, unique email/amenity names, and validation constraints are generated from those mappings. New users must be created through `/api/auth/register`, which BCrypt-hashes their passwords. Location create/update/delete is restricted to `ROLE_ADMIN`; report operations are available to authenticated citizens and admins.

For a clean manual reset, review and run `DEV_RESET.sql`. To replay migrations from scratch, drop and recreate the database, then restart the app.

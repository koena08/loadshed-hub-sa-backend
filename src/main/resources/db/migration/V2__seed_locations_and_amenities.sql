INSERT INTO amenities(name) VALUES ('POWER'), ('WIFI'), ('SAFE'), ('PARKING');
INSERT INTO locations(name,address,latitude,longitude,verified) VALUES
 ('Sandton Library','Cnr Alice Lane, Sandton',-26.1076,28.0567,TRUE),
 ('BP Garage Rosebank','Oxford Road, Rosebank',-26.1456,28.0366,TRUE),
 ('Braamfontein Study Hub','Juta Street, Braamfontein',-26.1929,28.0341,FALSE);
INSERT INTO location_amenities(location_id,amenity_id) SELECT l.id,a.id FROM locations l CROSS JOIN amenities a WHERE l.name='Sandton Library' AND a.name IN ('POWER','WIFI','SAFE');
INSERT INTO location_amenities(location_id,amenity_id) SELECT l.id,a.id FROM locations l CROSS JOIN amenities a WHERE l.name='BP Garage Rosebank' AND a.name IN ('POWER','PARKING');
INSERT INTO location_amenities(location_id,amenity_id) SELECT l.id,a.id FROM locations l CROSS JOIN amenities a WHERE l.name='Braamfontein Study Hub' AND a.name IN ('WIFI');

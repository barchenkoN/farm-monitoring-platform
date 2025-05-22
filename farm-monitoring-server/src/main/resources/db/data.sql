-- Insert farm data
INSERT INTO farm (name, location, size_hectares) 
VALUES ('Green Valley Farm', 'Kyiv Oblast', 500.00);

-- Insert field data
INSERT INTO field (farm_id, name, size_hectares, crop_type) 
VALUES 
(1, 'North Field', 120.50, 'Wheat'),
(1, 'South Field', 85.75, 'Corn'),
(1, 'East Field', 95.25, 'Sunflower'),
(1, 'West Field', 110.30, 'Soybean');

-- Insert sensor data
INSERT INTO sensor (field_id, name, type, latitude, longitude, battery_level, status) 
VALUES 
(1, 'Sensor-T1', 'TEMPERATURE', 50.4501, 30.5234, 85, 'ACTIVE'),
(1, 'Sensor-M1', 'MOISTURE', 50.4502, 30.5235, 90, 'ACTIVE'),
(2, 'Sensor-T2', 'TEMPERATURE', 50.4601, 30.5334, 80, 'ACTIVE'),
(2, 'Sensor-M2', 'MOISTURE', 50.4602, 30.5335, 95, 'ACTIVE'),
(3, 'Sensor-T3', 'TEMPERATURE', 50.4701, 30.5434, 75, 'ACTIVE'),
(3, 'Sensor-M3', 'MOISTURE', 50.4702, 30.5435, 88, 'ACTIVE'),
(4, 'Sensor-T4', 'TEMPERATURE', 50.4801, 30.5534, 70, 'ACTIVE'),
(4, 'Sensor-M4', 'MOISTURE', 50.4802, 30.5535, 92, 'ACTIVE');

-- Insert some initial sensor readings (using PostgreSQL-compatible timestamp syntax)
INSERT INTO sensor_reading (sensor_id, reading_type, reading_value, unit, timestamp) 
VALUES 
(1, 'TEMPERATURE', 22.5, 'Celsius', NOW() - INTERVAL '1 HOUR'),
(1, 'TEMPERATURE', 23.1, 'Celsius', NOW() - INTERVAL '30 MINUTES'),
(1, 'TEMPERATURE', 23.8, 'Celsius', NOW()),
(2, 'MOISTURE', 42.3, 'Percent', NOW() - INTERVAL '1 HOUR'),
(2, 'MOISTURE', 41.8, 'Percent', NOW() - INTERVAL '30 MINUTES'),
(2, 'MOISTURE', 40.5, 'Percent', NOW()),
(3, 'TEMPERATURE', 21.7, 'Celsius', NOW() - INTERVAL '1 HOUR'),
(3, 'TEMPERATURE', 22.4, 'Celsius', NOW() - INTERVAL '30 MINUTES'),
(3, 'TEMPERATURE', 22.9, 'Celsius', NOW()),
(4, 'MOISTURE', 38.2, 'Percent', NOW() - INTERVAL '1 HOUR'),
(4, 'MOISTURE', 39.1, 'Percent', NOW() - INTERVAL '30 MINUTES'),
(4, 'MOISTURE', 40.3, 'Percent', NOW());
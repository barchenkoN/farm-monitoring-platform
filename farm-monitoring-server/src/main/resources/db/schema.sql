CREATE TABLE IF NOT EXISTS farm (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    location VARCHAR(255) NOT NULL,
    size_hectares DECIMAL(10, 2) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS field (
    id SERIAL PRIMARY KEY,
    farm_id INTEGER NOT NULL,
    name VARCHAR(255) NOT NULL,
    size_hectares DECIMAL(10, 2) NOT NULL,
    crop_type VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (farm_id) REFERENCES farm(id)
);

CREATE TABLE IF NOT EXISTS sensor (
    id SERIAL PRIMARY KEY,
    field_id INTEGER NOT NULL,
    name VARCHAR(255) NOT NULL,
    type VARCHAR(50) NOT NULL,
    latitude DECIMAL(10, 8),
    longitude DECIMAL(11, 8),
    battery_level INTEGER,
    status VARCHAR(50) DEFAULT 'ACTIVE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (field_id) REFERENCES field(id)
);

CREATE TABLE IF NOT EXISTS sensor_reading (
    id SERIAL PRIMARY KEY,
    sensor_id INTEGER NOT NULL,
    reading_type VARCHAR(50) NOT NULL,
    reading_value DECIMAL(10, 2) NOT NULL,
    unit VARCHAR(20) NOT NULL,
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (sensor_id) REFERENCES sensor(id)
);

CREATE INDEX IF NOT EXISTS idx_sensor_reading_sensor_id ON sensor_reading(sensor_id);
CREATE INDEX IF NOT EXISTS idx_sensor_reading_timestamp ON sensor_reading(timestamp); 
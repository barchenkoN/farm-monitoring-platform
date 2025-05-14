package com.farmmonitoring.server.repository;

import com.farmmonitoring.server.model.SensorReading;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SensorReadingRepository extends JpaRepository<SensorReading, Long> {
    List<SensorReading> findBySensorId(Long sensorId);
    
    List<SensorReading> findBySensorIdAndTimestampBetween(
            Long sensorId, LocalDateTime startTime, LocalDateTime endTime);
    
    @Query("SELECT sr FROM SensorReading sr WHERE sr.sensor.id = ?1 ORDER BY sr.timestamp DESC LIMIT 1")
    SensorReading findLatestBySensorId(Long sensorId);
} 
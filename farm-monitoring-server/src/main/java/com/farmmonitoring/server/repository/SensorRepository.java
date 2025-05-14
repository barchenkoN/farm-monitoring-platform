package com.farmmonitoring.server.repository;

import com.farmmonitoring.server.model.Sensor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SensorRepository extends JpaRepository<Sensor, Long> {
    List<Sensor> findByFieldId(Long fieldId);
    List<Sensor> findByType(Sensor.SensorType type);
} 
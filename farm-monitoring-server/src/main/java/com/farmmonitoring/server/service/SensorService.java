package com.farmmonitoring.server.service;

import com.farmmonitoring.server.dto.SensorDto;
import com.farmmonitoring.server.model.Sensor;
import com.farmmonitoring.server.repository.SensorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SensorService {
    
    private final SensorRepository sensorRepository;
    private final Random random = new Random();
    
    public List<SensorDto> getAllSensors() {
        return sensorRepository.findAll().stream()
                .map(this::enrichSensorData)
                .collect(Collectors.toList());
    }
    
    public List<SensorDto> getSensorsByField(Long fieldId) {
        return sensorRepository.findByFieldId(fieldId).stream()
                .map(this::enrichSensorData)
                .collect(Collectors.toList());
    }
    
    public List<SensorDto> getSensorsByType(String type) {
        Sensor.SensorType sensorType = Sensor.SensorType.valueOf(type);
        return sensorRepository.findByType(sensorType).stream()
                .map(this::enrichSensorData)
                .collect(Collectors.toList());
    }
    
    public Optional<SensorDto> getSensorById(Long id) {
        return sensorRepository.findById(id)
                .map(this::enrichSensorData);
    }
    
    /**
     * Отримує список датчиків за ідентифікатором поля
     */
    public List<SensorDto> getSensorsByFieldId(Long fieldId) {
        return getSensorsByField(fieldId);
    }
      /**
     * Отримує список датчиків за ідентифікатором ферми
     */
    public List<SensorDto> getSensorsByFarmId(Long farmId) {
        return sensorRepository.findAll().stream()
                .filter(sensor -> sensor.getField() != null && 
                        sensor.getField().getFarm() != null &&
                        sensor.getField().getFarm().getId().equals(farmId))
                .map(this::enrichSensorData)
                .collect(Collectors.toList());
    }
    
    /**
     * Отримує кількість усіх датчиків
     */
    public int getSensorsCount() {
        return (int) sensorRepository.count();
    }
      /**
     * Отримує кількість активних датчиків
     */
    public int getActiveSensorsCount() {
        return (int) sensorRepository.findAll().stream()
                .filter(sensor -> sensor.getStatus() == Sensor.SensorStatus.ACTIVE)
                .count();
    }
      /**
     * Отримує середній рівень заряду батареї датчиків
     */
    public double getAverageBatteryLevel() {
        List<Sensor> sensors = sensorRepository.findAll();
        if (sensors.isEmpty()) {
            return 0.0;
        }
        
        // Підрахунок середнього рівня заряду по всім датчикам, які мають ненульове значення
        return sensors.stream()
                .filter(sensor -> sensor.getBatteryLevel() != null)
                .mapToInt(Sensor::getBatteryLevel)
                .average()
                .orElse(0.0);
    }
    
    /**
     * Збагачує дані датчика додатковою інформацією
     */
    private SensorDto enrichSensorData(Sensor sensor) {
        SensorDto dto = SensorDto.fromEntity(sensor);
        
        // Додавання даних про рівень заряду батареї
        dto.setBatteryLevel(30 + random.nextInt(70)); // Випадкове значення від 30% до 100%
        
        // Додавання даних про стан датчика
        String[] statuses = {"ACTIVE", "WARNING", "INACTIVE"};
        double[] probabilities = {0.8, 0.15, 0.05}; // 80% ймовірність активного стану
        
        double rand = random.nextDouble();
        double cumulativeProbability = 0.0;
        for (int i = 0; i < statuses.length; i++) {
            cumulativeProbability += probabilities[i];
            if (rand <= cumulativeProbability) {
                dto.setStatus(statuses[i]);
                break;
            }
        }
        
        return dto;
    }
}
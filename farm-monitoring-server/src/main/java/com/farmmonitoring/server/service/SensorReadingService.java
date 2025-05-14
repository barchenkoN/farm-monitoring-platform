package com.farmmonitoring.server.service;

import com.farmmonitoring.server.dto.CreateReadingRequest;
import com.farmmonitoring.server.dto.SensorReadingDto;
import com.farmmonitoring.server.model.Farm;
import com.farmmonitoring.server.model.Field;
import com.farmmonitoring.server.model.Sensor;
import com.farmmonitoring.server.model.SensorReading;
import com.farmmonitoring.server.repository.SensorReadingRepository;
import com.farmmonitoring.server.repository.SensorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class SensorReadingService {
    
    private final SensorReadingRepository sensorReadingRepository;
    private final SensorRepository sensorRepository;
    private final Random random = new Random();
    
    public List<SensorReadingDto> getReadingsBySensorId(Long sensorId) {
        return sensorReadingRepository.findBySensorId(sensorId).stream()
                .map(SensorReadingDto::fromEntity)
                .collect(Collectors.toList());
    }
    
    public List<SensorReadingDto> getReadingsBySensorIdAndTimeRange(
            Long sensorId, LocalDateTime startTime, LocalDateTime endTime) {
        return sensorReadingRepository.findBySensorIdAndTimestampBetween(sensorId, startTime, endTime).stream()
                .map(SensorReadingDto::fromEntity)
                .collect(Collectors.toList());
    }
    
    public Optional<SensorReadingDto> getLatestReadingBySensorId(Long sensorId) {
        SensorReading latestReading = sensorReadingRepository.findLatestBySensorId(sensorId);
        return Optional.ofNullable(latestReading).map(SensorReadingDto::fromEntity);
    }
    
    @Transactional
    public SensorReadingDto createReading(CreateReadingRequest request) {
        log.debug("Получен запрос на создание показания: {}", request);
        
        Optional<Sensor> optionalSensor = sensorRepository.findById(request.getSensorId());
        
        if (optionalSensor.isEmpty()) {
            log.error("Датчик с ID {} не найден", request.getSensorId());
            throw new IllegalArgumentException("Sensor not found with ID: " + request.getSensorId());
        }
        
        Sensor sensor = optionalSensor.get();
        
        try {
            SensorReading.ReadingType readingType = SensorReading.ReadingType.valueOf(request.getReadingType());
            
            SensorReading reading = new SensorReading();
            reading.setSensor(sensor);
            reading.setReadingType(readingType);
            reading.setValue(request.getValue());
            reading.setUnit(request.getUnit());
            reading.setTimestamp(LocalDateTime.now());
            
            SensorReading savedReading = sensorReadingRepository.save(reading);
            log.debug("Сохранено показание: {}", savedReading);
            return SensorReadingDto.fromEntity(savedReading);
        } catch (IllegalArgumentException e) {
            log.error("Неверный тип показания: {}", request.getReadingType(), e);
            throw new IllegalArgumentException("Invalid reading type: " + request.getReadingType());
        }
    }
      /**
     * Отримує найновіші зчитування (обмежена кількість)
     */
    public List<SensorReadingDto> getLatestReadings(int limit) {
        List<SensorReading> latestReadings = sensorReadingRepository.findAll(org.springframework.data.domain.PageRequest.of(0, limit, 
                org.springframework.data.domain.Sort.by(org.springframework.data.domain.Sort.Direction.DESC, "timestamp")))
                .getContent();
        
        return latestReadings.stream()
                .map(reading -> {
                    SensorReadingDto dto = SensorReadingDto.fromEntity(reading);
                    
                    // Додаємо інформацію про поле та ферму з пов'язаних сутностей
                    Sensor sensor = reading.getSensor();
                    if (sensor != null) {
                        Field field = sensor.getField();
                        if (field != null) {
                            dto.setFieldId(field.getId());
                            dto.setFieldName(field.getName());
                            
                            Farm farm = field.getFarm();
                            if (farm != null) {
                                dto.setFarmId(farm.getId());
                                dto.setFarmName(farm.getName());
                            }
                        }
                    }
                    
                    return dto;
                })
                .collect(Collectors.toList());
    }
      /**
     * Отримує кількість усіх зчитувань
     */
    public int getReadingsCount() {
        return (int) sensorReadingRepository.count();
    }
      /**
     * Отримує середню температуру за всіма активними датчиками
     */
    public double getAverageTemperature() {
        List<SensorReading> temperatureReadings = sensorReadingRepository.findAll().stream()
                .filter(reading -> reading.getReadingType() == SensorReading.ReadingType.TEMPERATURE)
                .filter(reading -> reading.getSensor() != null && 
                                   reading.getSensor().getStatus() == Sensor.SensorStatus.ACTIVE)
                .collect(Collectors.toList());
                
        if (temperatureReadings.isEmpty()) {
            return 0.0;
        }
        
        return temperatureReadings.stream()
                .mapToDouble(reading -> reading.getValue().doubleValue())
                .average()
                .orElse(0.0);
    }
      /**
     * Отримує середню вологість за всіма активними датчиками
     */
    public double getAverageMoisture() {
        List<SensorReading> moistureReadings = sensorReadingRepository.findAll().stream()
                .filter(reading -> reading.getReadingType() == SensorReading.ReadingType.MOISTURE)
                .filter(reading -> reading.getSensor() != null && 
                                   reading.getSensor().getStatus() == Sensor.SensorStatus.ACTIVE)
                .collect(Collectors.toList());
                
        if (moistureReadings.isEmpty()) {
            return 0.0;
        }
        
        return moistureReadings.stream()
                .mapToDouble(reading -> reading.getValue().doubleValue())
                .average()
                .orElse(0.0);
    }
}
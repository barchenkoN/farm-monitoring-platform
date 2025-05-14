package com.farmmonitoring.server.controller;

import com.farmmonitoring.server.dto.CreateReadingRequest;
import com.farmmonitoring.server.dto.SensorReadingDto;
import com.farmmonitoring.server.service.SensorReadingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping({"/api/readings", "/readings"})
@RequiredArgsConstructor
@Slf4j
public class SensorReadingController {
    
    private final SensorReadingService sensorReadingService;
    
    @GetMapping("/sensor/{sensorId}")
    public ResponseEntity<List<SensorReadingDto>> getReadingsBySensorId(@PathVariable Long sensorId) {
        log.debug("Запрос на получение всех показаний для датчика с ID: {}", sensorId);
        return ResponseEntity.ok(sensorReadingService.getReadingsBySensorId(sensorId));
    }
    
    @GetMapping("/sensor/{sensorId}/latest")
    public ResponseEntity<SensorReadingDto> getLatestReadingBySensorId(@PathVariable Long sensorId) {
        log.debug("Запрос на получение последнего показания для датчика с ID: {}", sensorId);
        return sensorReadingService.getLatestReadingBySensorId(sensorId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/sensor/{sensorId}/range")
    public ResponseEntity<List<SensorReadingDto>> getReadingsByTimeRange(
            @PathVariable Long sensorId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to) {
        log.debug("Запрос на получение показаний для датчика с ID: {} в интервале от {} до {}", sensorId, from, to);
        return ResponseEntity.ok(
                sensorReadingService.getReadingsBySensorIdAndTimeRange(sensorId, from, to));
    }
    
    @PostMapping
    public ResponseEntity<SensorReadingDto> createReading(@Valid @RequestBody CreateReadingRequest request) {
        log.debug("Получен POST запрос на создание показания: {}", request);
        try {
            SensorReadingDto result = sensorReadingService.createReading(request);
            log.debug("Успешно создано показание: {}", result);
            return ResponseEntity.status(HttpStatus.CREATED).body(result);
        } catch (IllegalArgumentException e) {
            log.error("Ошибка при создании показания: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            log.error("Непредвиденная ошибка при создании показания", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
} 
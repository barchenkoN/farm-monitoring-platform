package com.farmmonitoring.server.controller;
import com.farmmonitoring.server.dto.SensorDto;
import com.farmmonitoring.server.service.SensorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping({"/api/sensors", "/sensors"})
public class SensorController {
    
    private final SensorService sensorService;
    
    public SensorController(SensorService sensorService) {
        this.sensorService = sensorService;
    }
    
    @GetMapping
    public ResponseEntity<List<SensorDto>> getAllSensors() {
        return ResponseEntity.ok(sensorService.getAllSensors());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<SensorDto> getSensorById(@PathVariable Long id) {
        return sensorService.getSensorById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/field/{fieldId}")
    public ResponseEntity<List<SensorDto>> getSensorsByField(@PathVariable Long fieldId) {
        return ResponseEntity.ok(sensorService.getSensorsByField(fieldId));
    }
    
    @GetMapping("/type/{type}")
    public ResponseEntity<List<SensorDto>> getSensorsByType(@PathVariable String type) {
        try {
            return ResponseEntity.ok(sensorService.getSensorsByType(type));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
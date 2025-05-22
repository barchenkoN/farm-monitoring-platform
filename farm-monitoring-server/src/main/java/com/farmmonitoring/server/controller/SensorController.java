package com.farmmonitoring.server.controller;
import com.farmmonitoring.server.dto.SensorDto;
import com.farmmonitoring.server.service.SensorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping({"/api/sensors", "/sensors"})
@Tag(name = "Датчики", description = "API для керування датчиками")
public class SensorController {
    
    private final SensorService sensorService;
    
    public SensorController(SensorService sensorService) {
        this.sensorService = sensorService;
    }
    
    @Operation(summary = "Отримати список всіх датчиків", description = "Повертає список всіх датчиків у системі")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Успішне отримання списку датчиків",
                     content = @Content(mediaType = "application/json",
                     schema = @Schema(implementation = SensorDto.class)))
    })
    @GetMapping
    public ResponseEntity<List<SensorDto>> getAllSensors() {
        return ResponseEntity.ok(sensorService.getAllSensors());
    }
      @Operation(summary = "Отримати датчик за ID", description = "Повертає датчик за вказаним ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Успішне отримання датчика"),
        @ApiResponse(responseCode = "404", description = "Датчик не знайдено")
    })
    @GetMapping("/{id}")
    public ResponseEntity<SensorDto> getSensorById(
            @Parameter(description = "ID датчика") @PathVariable Long id) {
        return sensorService.getSensorById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @Operation(summary = "Отримати датчики за ID поля", description = "Повертає список всіх датчиків на вказаному полі")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Успішне отримання списку датчиків")
    })
    @GetMapping("/field/{fieldId}")
    public ResponseEntity<List<SensorDto>> getSensorsByField(
            @Parameter(description = "ID поля") @PathVariable Long fieldId) {
        return ResponseEntity.ok(sensorService.getSensorsByField(fieldId));
    }
    
    @Operation(summary = "Отримати датчики за типом", description = "Повертає список всіх датчиків вказаного типу")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Успішне отримання списку датчиків"),
        @ApiResponse(responseCode = "400", description = "Неправильний тип датчика")
    })
    @GetMapping("/type/{type}")
    public ResponseEntity<List<SensorDto>> getSensorsByType(
            @Parameter(description = "Тип датчика (TEMPERATURE, MOISTURE)") @PathVariable String type) {
        try {
            return ResponseEntity.ok(sensorService.getSensorsByType(type));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
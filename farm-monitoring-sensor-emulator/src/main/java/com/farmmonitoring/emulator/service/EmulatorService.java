package com.farmmonitoring.emulator.service;

import com.farmmonitoring.emulator.config.EmulatorConfig;
import com.farmmonitoring.emulator.model.ReadingRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmulatorService {
    
    private final EmulatorConfig config;
    private final DataGeneratorService dataGeneratorService;
    private final RestTemplate restTemplate;
    
    private final Map<Long, String> sensorTypes = new HashMap<>();
    
    @Scheduled(fixedDelayString = "${emulator.intervalMilliseconds}")
    public void emulateReadings() {
        for (Long sensorId : config.getSensors()) {
            String sensorType = getSensorType(sensorId);
            
            try {
                ReadingRequest request;
                if ("TEMPERATURE".equals(sensorType)) {
                    request = ReadingRequest.builder()
                            .sensorId(sensorId)
                            .readingType(sensorType)
                            .value(dataGeneratorService.generateTemperature())
                            .unit(dataGeneratorService.getTemperatureUnit())
                            .build();
                } else {
                    request = ReadingRequest.builder()
                            .sensorId(sensorId)
                            .readingType(sensorType)
                            .value(dataGeneratorService.generateMoisture())
                            .unit(dataGeneratorService.getMoistureUnit())
                            .build();
                }
                
                String url = config.getServerUrl() + "/readings";
                
                // Используем более простой способ отправки данных
                Object response = restTemplate.postForObject(url, request, Object.class);
                log.info("Sent reading for sensor {}: {}, response: {}", 
                         sensorId, request, response != null ? "success" : "failure");
            } catch (Exception e) {
                log.error("Error sending reading for sensor {}: {}", sensorId, e.getMessage());
            }
        }
    }
    
    private String getSensorType(Long sensorId) {
        if (!sensorTypes.containsKey(sensorId)) {
            // Even-numbered sensors are temperature, odd are moisture
            sensorTypes.put(sensorId, sensorId % 2 == 0 ? "TEMPERATURE" : "MOISTURE");
        }
        return sensorTypes.get(sensorId);
    }
} 
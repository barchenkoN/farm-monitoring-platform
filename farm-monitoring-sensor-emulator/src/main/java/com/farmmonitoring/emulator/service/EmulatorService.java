package com.farmmonitoring.emulator.service;

import com.farmmonitoring.emulator.config.EmulatorConfig;
import com.farmmonitoring.emulator.model.ReadingRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import jakarta.annotation.PostConstruct;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmulatorService {
    
    private final EmulatorConfig config;
    private final DataGeneratorService dataGeneratorService;
    private final RestTemplate restTemplate;
    
    private final Map<Long, String> sensorTypes = new HashMap<>();
    
    @PostConstruct
    public void init() {
        List<Long> sensorIds = config.getSensors();
        log.info("Initializing emulator for sensors: {}", sensorIds);
        
        // Randomly assign sensor types (TEMPERATURE or MOISTURE)
        for (Long sensorId : sensorIds) {
            // Assigning a type randomly, but making sure we have a mix of both types
            String type = (sensorId % 2 == 0) ? "TEMPERATURE" : "MOISTURE";
            sensorTypes.put(sensorId, type);
            log.info("Sensor {} assigned type: {}", sensorId, type);
        }
    }
    
    @Scheduled(fixedRateString = "${emulator.intervalMilliseconds}")
    public void sendReadings() {
        List<Long> sensorIds = config.getSensors();
        if (sensorIds.isEmpty()) {
            log.warn("No sensors configured. Check your application.properties file.");
            return;
        }
        
        for (Long sensorId : sensorIds) {
            try {
                String type = sensorTypes.get(sensorId);
                ReadingRequest request;
                
                if ("TEMPERATURE".equals(type)) {
                    request = ReadingRequest.builder()
                            .sensorId(sensorId)
                            .readingType(type)
                            .value(dataGeneratorService.generateTemperature())
                            .unit(dataGeneratorService.getTemperatureUnit())
                            .build();
                } else {
                    request = ReadingRequest.builder()
                            .sensorId(sensorId)
                            .readingType(type)
                            .value(dataGeneratorService.generateMoisture())
                            .unit(dataGeneratorService.getMoistureUnit())
                            .build();
                }
                
                String url = config.getServerUrl() + "/readings";
                log.debug("Sending reading to {}: {}", url, request);
                
                try {
                    restTemplate.postForObject(url, request, Object.class);
                    log.info("Successfully sent reading for sensor {}: {} {}", 
                             sensorId, request.getValue(), request.getUnit());
                } catch (RestClientException e) {
                    log.error("Failed to send reading for sensor {}: {}", sensorId, e.getMessage());
                }
            } catch (Exception e) {
                log.error("Error processing sensor {}: {}", sensorId, e.getMessage());
            }
        }
    }
}
package com.farmmonitoring.emulator.config;

import lombok.Data;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
@ConfigurationProperties(prefix = "emulator")
@Data
public class EmulatorConfig {
    @Getter
    private String serverUrl;
    private long intervalMilliseconds;
    private String sensors; // Зберігаємо як рядок
    private TemperatureConfig temperature;
    private MoistureConfig moisture;
    
    // Метод для отримання ID датчиків як списку
    public List<Long> getSensors() {
        if (sensors == null || sensors.isEmpty()) {
            return List.of();
        }
        return Arrays.stream(sensors.split(","))
                .map(String::trim)
                .map(Long::parseLong)
                .collect(Collectors.toList());
    }
    
    public void setSensors(String sensors) {
        this.sensors = sensors;
    }

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }
    
    public long getIntervalMilliseconds() {
        return intervalMilliseconds;
    }
    
    public void setIntervalMilliseconds(long intervalMilliseconds) {
        this.intervalMilliseconds = intervalMilliseconds;
    }
    
    public TemperatureConfig getTemperature() {
        return temperature;
    }
    
    public void setTemperature(TemperatureConfig temperature) {
        this.temperature = temperature;
    }
    
    public MoistureConfig getMoisture() {
        return moisture;
    }
    
    public void setMoisture(MoistureConfig moisture) {
        this.moisture = moisture;
    }
    
    @Data
    public static class TemperatureConfig {
        private double min;
        private double max;
        
        public double getMin() {
            return min;
        }
        
        public void setMin(double min) {
            this.min = min;
        }
        
        public double getMax() {
            return max;
        }
        
        public void setMax(double max) {
            this.max = max;
        }
    }
    
    @Data
    public static class MoistureConfig {
        private double min;
        private double max;
        
        public double getMin() {
            return min;
        }
        
        public void setMin(double min) {
            this.min = min;
        }
        
        public double getMax() {
            return max;
        }
        
        public void setMax(double max) {
            this.max = max;
        }
    }
} 
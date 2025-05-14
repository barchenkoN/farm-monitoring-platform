package com.farmmonitoring.emulator.service;

import com.farmmonitoring.emulator.config.EmulatorConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class DataGeneratorService {
    
    private final EmulatorConfig config;
    private final Random random = new Random();
    
    public BigDecimal generateTemperature() {
        double min = config.getTemperature().getMin();
        double max = config.getTemperature().getMax();
        double value = min + (max - min) * random.nextDouble();
        return BigDecimal.valueOf(value).setScale(1, RoundingMode.HALF_UP);
    }
    
    public BigDecimal generateMoisture() {
        double min = config.getMoisture().getMin();
        double max = config.getMoisture().getMax();
        double value = min + (max - min) * random.nextDouble();
        return BigDecimal.valueOf(value).setScale(1, RoundingMode.HALF_UP);
    }
    
    public String getTemperatureUnit() {
        return "Celsius";
    }
    
    public String getMoistureUnit() {
        return "Percent";
    }
} 
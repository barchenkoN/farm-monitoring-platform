package com.farmmonitoring.emulator.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReadingRequest {
    private Long sensorId;
    private String readingType;
    private BigDecimal value;
    private String unit;
} 
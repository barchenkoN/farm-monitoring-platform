package com.farmmonitoring.server.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Request for creating a new sensor reading
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateReadingRequest {
    @NotNull
    private Long sensorId;
    
    @NotNull
    private String readingType;
    
    @NotNull
    private BigDecimal value;
    
    @NotNull
    private String unit;
}

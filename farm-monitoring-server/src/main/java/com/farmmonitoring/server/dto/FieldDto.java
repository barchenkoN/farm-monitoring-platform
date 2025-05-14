package com.farmmonitoring.server.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FieldDto {
    private Long id;
    private String name;
    private Long farmId;
    private String farmName;
    private double area;
    private String soilType;
    private String currentCrop;
    private String status;
    private String coordinates;
    private Integer sensorsCount;
}

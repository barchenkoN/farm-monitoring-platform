package com.farmmonitoring.server.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FarmDto {
    private Long id;
    private String name;
    private String location;
    private String ownerName;
    private String contactInfo;
    private double totalArea;
    private String description;
    private String status;
}

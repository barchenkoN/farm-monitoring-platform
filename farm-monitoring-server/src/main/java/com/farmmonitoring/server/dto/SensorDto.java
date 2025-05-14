package com.farmmonitoring.server.dto;

import com.farmmonitoring.server.model.Sensor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SensorDto {
    private Long id;
    private Long fieldId;
    private String name;
    private String type;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private Integer batteryLevel;
    private String status;
    private LocalDateTime createdAt;
    
    public static SensorDto fromEntity(Sensor sensor) {
        SensorDto dto = new SensorDto();
        dto.setId(sensor.getId());
        dto.setFieldId(sensor.getField().getId());
        dto.setName(sensor.getName());
        dto.setType(sensor.getType().name());
        dto.setLatitude(sensor.getLatitude());
        dto.setLongitude(sensor.getLongitude());
        dto.setBatteryLevel(sensor.getBatteryLevel());
        dto.setStatus(sensor.getStatus().name());
        dto.setCreatedAt(sensor.getCreatedAt());
        return dto;
    }
    
    // Геттери і сеттери, які мав би згенерувати Lombok, але вони не працюють
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getFieldId() {
        return fieldId;
    }

    public void setFieldId(Long fieldId) {
        this.fieldId = fieldId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public BigDecimal getLatitude() {
        return latitude;
    }

    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }

    public BigDecimal getLongitude() {
        return longitude;
    }

    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }

    public Integer getBatteryLevel() {
        return batteryLevel;
    }

    public void setBatteryLevel(Integer batteryLevel) {
        this.batteryLevel = batteryLevel;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    // Статичний builder метод для заміни @Builder анотації
    public static SensorDtoBuilder builder() {
        return new SensorDtoBuilder();
    }
    
    public static class SensorDtoBuilder {
        private Long id;
        private Long fieldId;
        private String name;
        private String type;
        private BigDecimal latitude;
        private BigDecimal longitude;
        private Integer batteryLevel;
        private String status;
        private LocalDateTime createdAt;
        
        public SensorDtoBuilder id(Long id) {
            this.id = id;
            return this;
        }
        
        public SensorDtoBuilder fieldId(Long fieldId) {
            this.fieldId = fieldId;
            return this;
        }
        
        public SensorDtoBuilder name(String name) {
            this.name = name;
            return this;
        }
        
        public SensorDtoBuilder type(String type) {
            this.type = type;
            return this;
        }
        
        public SensorDtoBuilder latitude(BigDecimal latitude) {
            this.latitude = latitude;
            return this;
        }
        
        public SensorDtoBuilder longitude(BigDecimal longitude) {
            this.longitude = longitude;
            return this;
        }
        
        public SensorDtoBuilder batteryLevel(Integer batteryLevel) {
            this.batteryLevel = batteryLevel;
            return this;
        }
        
        public SensorDtoBuilder status(String status) {
            this.status = status;
            return this;
        }
        
        public SensorDtoBuilder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }
        
        public SensorDto build() {
            SensorDto dto = new SensorDto();
            dto.setId(this.id);
            dto.setFieldId(this.fieldId);
            dto.setName(this.name);
            dto.setType(this.type);
            dto.setLatitude(this.latitude);
            dto.setLongitude(this.longitude);
            dto.setBatteryLevel(this.batteryLevel);
            dto.setStatus(this.status);
            dto.setCreatedAt(this.createdAt);
            return dto;
        }
    }
} 
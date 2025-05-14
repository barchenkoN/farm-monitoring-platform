package com.farmmonitoring.server.dto;

import com.farmmonitoring.server.model.SensorReading;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO for sensor reading data transfer
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SensorReadingDto {
    
    private Long id;
    private Long sensorId;
    private String sensorName;
    private String readingType;
    private BigDecimal value;
    private String unit;
    private LocalDateTime timestamp;
    // Additional fields for analytics and reports
    private Long farmId;
    private String farmName;
    private Long fieldId;
    private String fieldName;
    
    /**
     * Creates DTO from entity
     * 
     * @param entity sensor reading entity
     * @return sensor reading DTO
     */
    public static SensorReadingDto fromEntity(SensorReading entity) {
        SensorReadingDto dto = new SensorReadingDto();
        dto.setId(entity.getId());
        dto.setSensorId(entity.getSensor().getId());
        dto.setSensorName(entity.getSensor().getName());
        dto.setReadingType(entity.getReadingType().name());
        dto.setValue(entity.getValue());
        dto.setUnit(entity.getUnit());
        dto.setTimestamp(entity.getTimestamp());
        
        // Додавання інформації про поле (якщо доступно)
        if (entity.getSensor().getField() != null) {
            dto.setFieldId(entity.getSensor().getField().getId());
            dto.setFieldName(entity.getSensor().getField().getName());
            
            // Додавання інформації про ферму (якщо доступно)
            if (entity.getSensor().getField().getFarm() != null) {
                dto.setFarmId(entity.getSensor().getField().getFarm().getId());
                dto.setFarmName(entity.getSensor().getField().getFarm().getName());
            }
        }
        
        return dto;
    }
    
    // Геттери і сеттери
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSensorId() {
        return sensorId;
    }

    public void setSensorId(Long sensorId) {
        this.sensorId = sensorId;
    }
    
    public String getSensorName() {
        return sensorName;
    }

    public void setSensorName(String sensorName) {
        this.sensorName = sensorName;
    }

    public String getReadingType() {
        return readingType;
    }

    public void setReadingType(String readingType) {
        this.readingType = readingType;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }
    
    /**
     * Допоміжний метод для встановлення значення з double
     * @param value Значення для встановлення
     */
    public void setValue(double value) {
        this.value = BigDecimal.valueOf(value);
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
    
    public Long getFarmId() {
        return farmId;
    }

    public void setFarmId(Long farmId) {
        this.farmId = farmId;
    }

    public String getFarmName() {
        return farmName;
    }

    public void setFarmName(String farmName) {
        this.farmName = farmName;
    }

    public Long getFieldId() {
        return fieldId;
    }

    public void setFieldId(Long fieldId) {
        this.fieldId = fieldId;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }
    
    // Внутрішній клас Builder для SensorReadingDto
    public static class SensorReadingDtoBuilder {
        private Long id;
        private Long sensorId;
        private String sensorName;
        private String readingType;
        private BigDecimal value;
        private String unit;
        private LocalDateTime timestamp;
        private Long farmId;
        private String farmName;
        private Long fieldId;
        private String fieldName;
        
        public SensorReadingDtoBuilder id(Long id) {
            this.id = id;
            return this;
        }
        
        public SensorReadingDtoBuilder sensorId(Long sensorId) {
            this.sensorId = sensorId;
            return this;
        }
        
        public SensorReadingDtoBuilder sensorName(String sensorName) {
            this.sensorName = sensorName;
            return this;
        }
        
        public SensorReadingDtoBuilder readingType(String readingType) {
            this.readingType = readingType;
            return this;
        }
        
        public SensorReadingDtoBuilder value(BigDecimal value) {
            this.value = value;
            return this;
        }
        
        public SensorReadingDtoBuilder value(double value) {
            this.value = BigDecimal.valueOf(value);
            return this;
        }
        
        public SensorReadingDtoBuilder unit(String unit) {
            this.unit = unit;
            return this;
        }
        
        public SensorReadingDtoBuilder timestamp(LocalDateTime timestamp) {
            this.timestamp = timestamp;
            return this;
        }
        
        public SensorReadingDtoBuilder farmId(Long farmId) {
            this.farmId = farmId;
            return this;
        }
        
        public SensorReadingDtoBuilder farmName(String farmName) {
            this.farmName = farmName;
            return this;
        }
        
        public SensorReadingDtoBuilder fieldId(Long fieldId) {
            this.fieldId = fieldId;
            return this;
        }
        
        public SensorReadingDtoBuilder fieldName(String fieldName) {
            this.fieldName = fieldName;
            return this;
        }
        
        public SensorReadingDto build() {
            return new SensorReadingDto(
                this.id, 
                this.sensorId, 
                this.sensorName, 
                this.readingType, 
                this.value, 
                this.unit, 
                this.timestamp, 
                this.farmId, 
                this.farmName, 
                this.fieldId, 
                this.fieldName
            );
        }
    }
}
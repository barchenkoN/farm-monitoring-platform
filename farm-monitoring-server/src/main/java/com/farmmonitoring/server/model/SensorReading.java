package com.farmmonitoring.server.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "sensor_reading")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SensorReading {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sensor_id")
    private Sensor sensor;
    
    @Column(name = "reading_type")
    @Enumerated(EnumType.STRING)
    private ReadingType readingType;
    
    @Column(name = "reading_value")
    private BigDecimal value;
    
    private String unit;
    
    private LocalDateTime timestamp;
    
    public enum ReadingType {
        TEMPERATURE, MOISTURE
    }
}
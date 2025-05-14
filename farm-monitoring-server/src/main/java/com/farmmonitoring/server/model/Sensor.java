package com.farmmonitoring.server.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "sensor")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Sensor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "field_id")
    private Field field;
    
    private String name;
    
    @Enumerated(EnumType.STRING)
    private SensorType type;
    
    private BigDecimal latitude;
    
    private BigDecimal longitude;
    
    @Column(name = "battery_level")
    private Integer batteryLevel;
    
    @Enumerated(EnumType.STRING)
    private SensorStatus status;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @OneToMany(mappedBy = "sensor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SensorReading> readings = new ArrayList<>();
    
    public enum SensorType {
        TEMPERATURE, MOISTURE
    }
    
    public enum SensorStatus {
        ACTIVE, INACTIVE, MAINTENANCE
    }
} 
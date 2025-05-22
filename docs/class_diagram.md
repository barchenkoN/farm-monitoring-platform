# Діаграма класів системи моніторингу фермерських операцій

## Основні класи моделі даних

### Модель (Model)

```
+-------------------+
|       Farm        |
+-------------------+
| -id: Long         |
| -name: String     |
| -location: String |
| -sizeHectares: BigDecimal |
+-------------------+
| +getId(): Long    |
| +getName(): String|
| +setName(String)  |
| +getLocation(): String |
| +setLocation(String) |
| +getSizeHectares(): BigDecimal |
| +setSizeHectares(BigDecimal) |
+-------------------+
         ^
         |
         | 1
         |
         | *
+-------------------+
|      Field        |
+-------------------+
| -id: Long         |
| -farm: Farm       |
| -name: String     |
| -area: BigDecimal |
| -soilType: String |
| -currentCrop: String |
| -status: String   |
+-------------------+
| +getId(): Long    |
| +getFarm(): Farm  |
| +setFarm(Farm)    |
| +getName(): String|
| +setName(String)  |
| +getArea(): BigDecimal |
| +setArea(BigDecimal) |
| +getSoilType(): String |
| +setSoilType(String) |
| +getCurrentCrop(): String |
| +setCurrentCrop(String) |
| +getStatus(): String |
| +setStatus(String) |
+-------------------+
         ^
         |
         | 1
         |
         | *
+-------------------+
|      Sensor       |
+-------------------+
| -id: Long         |
| -field: Field     |
| -name: String     |
| -type: SensorType |
| -latitude: BigDecimal |
| -longitude: BigDecimal |
| -batteryLevel: Integer |
| -status: SensorStatus |
+-------------------+
| +getId(): Long    |
| +getField(): Field|
| +setField(Field)  |
| +getName(): String|
| +setName(String)  |
| +getType(): SensorType |
| +setType(SensorType) |
| +getLatitude(): BigDecimal |
| +setLatitude(BigDecimal) |
| +getLongitude(): BigDecimal |
| +setLongitude(BigDecimal) |
| +getBatteryLevel(): Integer |
| +setBatteryLevel(Integer) |
| +getStatus(): SensorStatus |
| +setStatus(SensorStatus) |
+-------------------+
         ^
         |
         | 1
         |
         | *
+-------------------+
|   SensorReading   |
+-------------------+
| -id: Long         |
| -sensor: Sensor   |
| -readingType: ReadingType |
| -value: BigDecimal|
| -unit: String     |
| -timestamp: LocalDateTime |
+-------------------+
| +getId(): Long    |
| +getSensor(): Sensor |
| +setSensor(Sensor)|
| +getReadingType(): ReadingType |
| +setReadingType(ReadingType) |
| +getValue(): BigDecimal |
| +setValue(BigDecimal) |
| +getUnit(): String |
| +setUnit(String) |
| +getTimestamp(): LocalDateTime |
| +setTimestamp(LocalDateTime) |
+-------------------+
```

## Репозиторії

```
+-------------------+
| FarmRepository    |
+-------------------+
| +findAll(): List<Farm> |
| +findById(Long): Optional<Farm> |
| +save(Farm): Farm |
| +delete(Farm): void |
+-------------------+

+-------------------+
| FieldRepository   |
+-------------------+
| +findAll(): List<Field> |
| +findById(Long): Optional<Field> |
| +findByFarmId(Long): List<Field> |
| +save(Field): Field |
| +delete(Field): void |
+-------------------+

+-------------------+
| SensorRepository  |
+-------------------+
| +findAll(): List<Sensor> |
| +findById(Long): Optional<Sensor> |
| +findByFieldId(Long): List<Sensor> |
| +findByType(SensorType): List<Sensor> |
| +save(Sensor): Sensor |
| +delete(Sensor): void |
+-------------------+

+-------------------+
| SensorReadingRepository |
+-------------------+
| +findAll(): List<SensorReading> |
| +findById(Long): Optional<SensorReading> |
| +findBySensorId(Long): List<SensorReading> |
| +findBySensorIdAndTimestampBetween(Long, LocalDateTime, LocalDateTime): List<SensorReading> |
| +save(SensorReading): SensorReading |
| +delete(SensorReading): void |
+-------------------+
```

## Сервіси

```
+-------------------+
|   FarmService     |
+-------------------+
| -farmRepository: FarmRepository |
+-------------------+
| +getAllFarms(): List<FarmDto> |
| +getFarmById(Long): FarmDto |
| +createFarm(FarmDto): FarmDto |
| +updateFarm(Long, FarmDto): FarmDto |
| +deleteFarm(Long): void |
+-------------------+

+-------------------+
|   FieldService    |
+-------------------+
| -fieldRepository: FieldRepository |
| -farmRepository: FarmRepository |
+-------------------+
| +getAllFields(): List<FieldDto> |
| +getFieldById(Long): FieldDto |
| +getFieldsByFarmId(Long): List<FieldDto> |
| +createField(FieldDto): FieldDto |
| +updateField(Long, FieldDto): FieldDto |
| +deleteField(Long): void |
+-------------------+

+-------------------+
|   SensorService   |
+-------------------+
| -sensorRepository: SensorRepository |
+-------------------+
| +getAllSensors(): List<SensorDto> |
| +getSensorById(Long): SensorDto |
| +getSensorsByFieldId(Long): List<SensorDto> |
| +getSensorsByType(String): List<SensorDto> |
| +createSensor(SensorDto): SensorDto |
| +updateSensor(Long, SensorDto): SensorDto |
| +deleteSensor(Long): void |
+-------------------+

+-------------------+
| SensorReadingService |
+-------------------+
| -sensorReadingRepository: SensorReadingRepository |
| -sensorRepository: SensorRepository |
+-------------------+
| +getAllReadings(): List<SensorReadingDto> |
| +getReadingById(Long): SensorReadingDto |
| +getReadingsBySensorId(Long): List<SensorReadingDto> |
| +getReadingsBySensorIdAndTimeRange(Long, LocalDateTime, LocalDateTime): List<SensorReadingDto> |
| +createReading(CreateReadingRequest): SensorReadingDto |
| +deleteReading(Long): void |
+-------------------+
```

## Контролери

```
+-------------------+
| SensorController  |
+-------------------+
| -sensorService: SensorService |
+-------------------+
| +getAllSensors(): ResponseEntity<List<SensorDto>> |
| +getSensor(Long): ResponseEntity<SensorDto> |
| +createSensor(SensorDto): ResponseEntity<SensorDto> |
| +updateSensor(Long, SensorDto): ResponseEntity<SensorDto> |
| +deleteSensor(Long): ResponseEntity<Void> |
+-------------------+

+-------------------+
| SensorReadingController |
+-------------------+
| -sensorReadingService: SensorReadingService |
+-------------------+
| +getAllReadings(): ResponseEntity<List<SensorReadingDto>> |
| +getReading(Long): ResponseEntity<SensorReadingDto> |
| +getReadingsBySensor(Long): ResponseEntity<List<SensorReadingDto>> |
| +getReadingsByTimeRange(Long, LocalDateTime, LocalDateTime): ResponseEntity<List<SensorReadingDto>> |
| +createReading(CreateReadingRequest): ResponseEntity<SensorReadingDto> |
| +deleteReading(Long): ResponseEntity<Void> |
+-------------------+
```

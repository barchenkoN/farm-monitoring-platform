# ERD Діаграма системи моніторингу фермерських операцій

## Сутності та їх взаємозв'язки

### Farm (Ферма)
- id (PK)
- name
- location
- size_hectares
- owner_name
- contact_info
- description
- status
- created_at
- updated_at

### Field (Поле)
- id (PK)
- farm_id (FK)
- name
- area
- soil_type
- current_crop
- status
- coordinates
- created_at
- updated_at

### Sensor (Датчик)
- id (PK)
- field_id (FK)
- name
- type (TEMPERATURE, MOISTURE)
- latitude
- longitude
- battery_level
- status (ACTIVE, INACTIVE, MAINTENANCE)
- created_at
- updated_at

### SensorReading (Показники датчиків)
- id (PK)
- sensor_id (FK)
- reading_type
- value
- unit
- timestamp

## Взаємозв'язки

1. **Farm - Field**: Один до багатьох (1:N)
   - Одна ферма може мати багато полів
   - Кожне поле належить тільки одній фермі

2. **Field - Sensor**: Один до багатьох (1:N)
   - Одне поле може мати багато датчиків
   - Кожен датчик розташований на одному полі

3. **Sensor - SensorReading**: Один до багатьох (1:N)
   - Один датчик може мати багато показників
   - Кожен показник належить одному конкретному датчику

## Діаграма ERD в нотації Crow's Foot

```
Farm (1)------|<---- (N) Field
Field (1)-----|<---- (N) Sensor
Sensor (1)----|<---- (N) SensorReading
```

## Пояснення зав'язків

- |---- позначає "один"
- |<--- позначає "багато"

Таким чином, одна ферма пов'язана з багатьма полями, одне поле пов'язане з багатьма датчиками, а один датчик пов'язаний з багатьма зчитуваннями показників.

## Додаткова інформація

- Ферма має статус, що вказує на її поточний стан (ACTIVE, INACTIVE, MAINTENANCE)
- Поля мають інформацію про тип ґрунту та поточну культуру, що вирощується
- Датчики мають рівень заряду батареї і статус, що вказує на їхній стан
- Показники датчиків включають тип зчитування, значення, одиницю вимірювання і часову мітку

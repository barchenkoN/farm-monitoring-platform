package com.farmmonitoring.server.controller;

import com.farmmonitoring.server.dto.FarmDto;
import com.farmmonitoring.server.dto.FieldDto;
import com.farmmonitoring.server.dto.SensorDto;
import com.farmmonitoring.server.dto.SensorReadingDto;
import com.farmmonitoring.server.service.FarmService;
import com.farmmonitoring.server.service.FieldService;
import com.farmmonitoring.server.service.SensorReadingService;
import com.farmmonitoring.server.service.SensorService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

@Controller
@RequestMapping("/web")
@RequiredArgsConstructor
public class WebController {
    
    private final SensorService sensorService;
    private final SensorReadingService sensorReadingService;
    private final FarmService farmService;
    private final FieldService fieldService;
    
    @GetMapping
    public String dashboard(Model model, HttpServletRequest request) {
        model.addAttribute("activePage", "dashboard");
        List<SensorDto> sensors = sensorService.getAllSensors();
        List<FarmDto> farms = farmService.getAllFarms();
        
        // Підготовка даних для графіків та метрик
        Map<String, Object> dashboardStats = getDashboardStats();
        
        model.addAttribute("sensors", sensors);
        model.addAttribute("farms", farms);
        model.addAttribute("now", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        model.addAttribute("request", request);
        
        // Додавання статистичних даних в модель
        model.addAllAttributes(dashboardStats);
        
        return "dashboard";
    }
    
    @GetMapping("/sensors")
    public String sensors(Model model) {
        List<SensorDto> sensors = sensorService.getAllSensors();
        List<FarmDto> farms = farmService.getAllFarms();
        List<FieldDto> fields = fieldService.getAllFields();
        
        model.addAttribute("sensors", sensors);
        model.addAttribute("farms", farms);
        model.addAttribute("fields", fields);
        model.addAttribute("now", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        
        return "sensors";
    }
    
    @GetMapping("/sensor/{id}")
    public String sensorDetails(@PathVariable Long id, Model model) {
        Optional<SensorDto> sensor = sensorService.getSensorById(id);
        
        if (sensor.isEmpty()) {
            return "redirect:/web";
        }
        
        List<SensorReadingDto> readings = sensorReadingService.getReadingsBySensorId(id);
        Optional<SensorReadingDto> latestReading = sensorReadingService.getLatestReadingBySensorId(id);
        
        model.addAttribute("sensor", sensor.get());
        model.addAttribute("readings", readings);
        model.addAttribute("latestReading", latestReading.orElse(null));
        model.addAttribute("now", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        
        return "sensor";
    }
    
    @GetMapping("/fields")
    public String fields(Model model) {
        List<FieldDto> fields = fieldService.getAllFields();
        List<FarmDto> farms = farmService.getAllFarms();
        
        model.addAttribute("fields", fields);
        model.addAttribute("farms", farms);
        model.addAttribute("now", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        
        return "fields";
    }
    
    @GetMapping("/field/{id}")
    public String fieldDetails(@PathVariable Long id, Model model) {
        Optional<FieldDto> field = fieldService.getFieldById(id);
        
        if (field.isEmpty()) {
            return "redirect:/web/fields";
        }
        
        List<SensorDto> fieldSensors = sensorService.getSensorsByFieldId(id);
        
        // Отримання показань датчиків для поля
        Map<Long, Double> sensorTemperatures = new HashMap<>();
        Map<Long, Double> sensorMoistures = new HashMap<>();
        
        // Середні значення для поля
        double avgTemperature = 0;
        double avgMoisture = 0;
        int temperatureCount = 0;
        int moistureCount = 0;
        
        // Дата останнього оновлення
        LocalDateTime lastUpdated = null;
        
        // Отримання останніх даних з датчиків
        for (SensorDto sensor : fieldSensors) {
            Optional<SensorReadingDto> latestReading = sensorReadingService.getLatestReadingBySensorId(sensor.getId());
            if (latestReading.isPresent()) {
                SensorReadingDto reading = latestReading.get();
                
                // Оновлення дати останнього оновлення
                if (lastUpdated == null || reading.getTimestamp().isAfter(lastUpdated)) {
                    lastUpdated = reading.getTimestamp();
                }
                
                if ("TEMPERATURE".equals(reading.getReadingType())) {
                    double value = reading.getValue().doubleValue();
                    sensorTemperatures.put(sensor.getId(), value);
                    avgTemperature += value;
                    temperatureCount++;
                } else if ("MOISTURE".equals(reading.getReadingType())) {
                    double value = reading.getValue().doubleValue();
                    sensorMoistures.put(sensor.getId(), value);
                    avgMoisture += value;
                    moistureCount++;
                }
            }
        }
        
        // Розрахунок середніх значень
        if (temperatureCount > 0) {
            avgTemperature /= temperatureCount;
        } else {
            avgTemperature = 0;
        }
        
        if (moistureCount > 0) {
            avgMoisture /= moistureCount;
        } else {
            avgMoisture = 0;
        }
        
        // Підготовка даних для графіків
        List<String> timeLabels = new ArrayList<>();
        List<Double> temperatureValues = new ArrayList<>();
        List<Double> moistureValues = new ArrayList<>();
        
        // Отримання даних за останні 24 години для графіків
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime dayAgo = now.minusDays(1);
        
        // У реальному додатку ми б вибирали дані з бази за цей період
        // Зараз просто генеруємо тестові дані
        for (int i = 0; i < 24; i++) {
            LocalDateTime time = dayAgo.plusHours(i);
            timeLabels.add(time.format(DateTimeFormatter.ofPattern("HH:mm")));
            
            // Випадкові значення навколо середніх для наочності
            double tempVariation = (ThreadLocalRandom.current().nextDouble() - 0.5) * 4.0; // ±2 градуси
            double moistVariation = (ThreadLocalRandom.current().nextDouble() - 0.5) * 10.0; // ±5%
            
            temperatureValues.add(avgTemperature + tempVariation);
            moistureValues.add(avgMoisture + moistVariation);
        }
        
        model.addAttribute("field", field.get());
        model.addAttribute("sensors", fieldSensors);
        model.addAttribute("now", now.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        model.addAttribute("sensorTemperatures", sensorTemperatures);
        model.addAttribute("sensorMoistures", sensorMoistures);
        model.addAttribute("avgTemperature", avgTemperature);
        model.addAttribute("avgMoisture", avgMoisture);
        model.addAttribute("lastUpdated", lastUpdated);
        
        // Дані для графіків
        model.addAttribute("temperatureChartLabels", timeLabels.toString());
        model.addAttribute("temperatureChartValues", temperatureValues.toString());
        model.addAttribute("moistureChartLabels", timeLabels.toString());
        model.addAttribute("moistureChartValues", moistureValues.toString());
        
        return "field";
    }
    
    @GetMapping("/farms")
    public String farms(Model model) {
        List<FarmDto> farms = farmService.getAllFarms();
        
        // Додаткова статистика для кожної ферми
        Map<Long, Integer> fieldsCountMap = new HashMap<>();
        Map<Long, Integer> sensorsCountMap = new HashMap<>();
        
        for (FarmDto farm : farms) {
            int fieldsCount = fieldService.getFieldsByFarmId(farm.getId()).size();
            int sensorsCount = sensorService.getSensorsByFarmId(farm.getId()).size();
            
            fieldsCountMap.put(farm.getId(), fieldsCount);
            sensorsCountMap.put(farm.getId(), sensorsCount);
        }
        
        model.addAttribute("farms", farms);
        model.addAttribute("fieldsCountMap", fieldsCountMap);
        model.addAttribute("sensorsCountMap", sensorsCountMap);
        model.addAttribute("now", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        
        return "farms";
    }
    
    @GetMapping("/farm/{id}")
    public String farmDetails(@PathVariable Long id, Model model) {
        Optional<FarmDto> farm = farmService.getFarmById(id);
        
        if (farm.isEmpty()) {
            return "redirect:/web/farms";
        }
        
        List<FieldDto> farmFields = fieldService.getFieldsByFarmId(id);
        List<SensorDto> farmSensors = sensorService.getSensorsByFarmId(id);
        
        // Статистичні дані для ферми
        double avgTemperature = 0;
        double avgMoisture = 0;
        int temperatureCount = 0;
        int moistureCount = 0;
        double avgBatteryLevel = 0;
        int activeSensorsCount = 0;
        LocalDateTime lastUpdated = null;
        
        // Карта показань датчиків
        Map<Long, Double> lastReadings = new HashMap<>();
        
        // Карта імен полів для датчиків (для відображення в таблиці)
        Map<Long, String> fieldNames = new HashMap<>();
        for (FieldDto field : farmFields) {
            fieldNames.put(field.getId(), field.getName());
        }
        
        // Збір даних з усіх датчиків ферми
        for (SensorDto sensor : farmSensors) {
            // Підрахунок активних датчиків
            if ("ACTIVE".equals(sensor.getStatus())) {
                activeSensorsCount++;
            }
            
            // Сума рівнів заряду батареї
            avgBatteryLevel += sensor.getBatteryLevel();
            
            // Отримання останніх показань з датчиків
            Optional<SensorReadingDto> latestReading = sensorReadingService.getLatestReadingBySensorId(sensor.getId());
            if (latestReading.isPresent()) {
                SensorReadingDto reading = latestReading.get();
                
                // Оновлення дати останнього оновлення
                if (lastUpdated == null || reading.getTimestamp().isAfter(lastUpdated)) {
                    lastUpdated = reading.getTimestamp();
                }
                
                // Збереження останнього показання
                lastReadings.put(sensor.getId(), reading.getValue().doubleValue());
                
                // Обчислення середніх значень
                if ("TEMPERATURE".equals(reading.getReadingType())) {
                    avgTemperature += reading.getValue().doubleValue();
                    temperatureCount++;
                } else if ("MOISTURE".equals(reading.getReadingType())) {
                    avgMoisture += reading.getValue().doubleValue();
                    moistureCount++;
                }
            }
        }
        
        // Розрахунок середніх значень
        if (temperatureCount > 0) {
            avgTemperature /= temperatureCount;
        }
        
        if (moistureCount > 0) {
            avgMoisture /= moistureCount;
        }
        
        if (farmSensors.size() > 0) {
            avgBatteryLevel /= farmSensors.size();
        }
        
        // Відсоток активних датчиків
        double activeSensorsPercentage = farmSensors.size() > 0 
            ? (double) activeSensorsCount / farmSensors.size() * 100 
            : 0;
        
        // Дані для порівняння з попереднім періодом (тренди)
        // Це мають бути реальні дані, зараз підставляємо тестові значення
        double temperatureTrend = 2.5;
        double moistureTrend = -1.8;
        double batteryTrend = -0.7;
        
        model.addAttribute("farm", farm.get());
        model.addAttribute("fields", farmFields);
        model.addAttribute("sensors", farmSensors);
        model.addAttribute("now", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        
        // Додавання статистичних даних в модель
        model.addAttribute("avgTemperature", avgTemperature);
        model.addAttribute("avgMoisture", avgMoisture);
        model.addAttribute("avgBatteryLevel", avgBatteryLevel);
        model.addAttribute("activeSensorsCount", activeSensorsCount);
        model.addAttribute("activeSensorsPercentage", activeSensorsPercentage);
        model.addAttribute("lastUpdated", lastUpdated);
        model.addAttribute("lastReadings", lastReadings);
        model.addAttribute("fieldNames", fieldNames);
        
        // Додавання даних про тренди
        model.addAttribute("temperatureTrend", temperatureTrend);
        model.addAttribute("moistureTrend", moistureTrend);
        model.addAttribute("batteryTrend", batteryTrend);
        
        return "farm";
    }
    
    @GetMapping("/analytics")
    public String analytics(Model model) {
        List<FarmDto> farms = farmService.getAllFarms();
        List<SensorReadingDto> readings = sensorReadingService.getLatestReadings(100);
        
        // Підготовка статистичних даних для аналітики
        Map<String, Object> analyticsData = getAnalyticsData();
        
        model.addAttribute("farms", farms);
        model.addAttribute("readings", readings);
        model.addAttribute("now", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        
        // Додавання аналітичних даних в модель
        model.addAllAttributes(analyticsData);
        
        return "analytics";
    }
    
    @GetMapping("/help")
    public String help(Model model) {
        model.addAttribute("now", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        return "help";
    }
    
    /**
     * Отримує статистичні дані для дашборду
     */
    private Map<String, Object> getDashboardStats() {
        Map<String, Object> stats = new HashMap<>();
        Random random = new Random();
        
        // Кількість різних елементів
        int sensorsCount = sensorService.getSensorsCount();
        int fieldsCount = fieldService.getFieldsCount();
        int farmsCount = farmService.getFarmsCount();
        int readingsCount = sensorReadingService.getReadingsCount();
        
        stats.put("sensorsCount", sensorsCount);
        stats.put("fieldsCount", fieldsCount);
        stats.put("farmsCount", farmsCount);
        stats.put("readingsCount", readingsCount);
        
        // Відсоток активних датчиків
        int activeSensorsCount = sensorService.getActiveSensorsCount();
        double activeSensorsPercentage = sensorsCount > 0 
            ? (double) activeSensorsCount / sensorsCount * 100 
            : 0;
        stats.put("activeSensorsCount", activeSensorsCount);
        stats.put("activeSensorsPercentage", activeSensorsPercentage);
        
        // Середні значення датчиків
        double avgTemperature = sensorReadingService.getAverageTemperature();
        double avgMoisture = sensorReadingService.getAverageMoisture();
        double avgBatteryLevel = sensorService.getAverageBatteryLevel();
        
        stats.put("avgTemperature", avgTemperature);
        stats.put("avgMoisture", avgMoisture);
        stats.put("avgBatteryLevel", avgBatteryLevel);
        
        // Дані для порівняння з попереднім періодом (тренди)
        double temperatureTrend = 2.5;
        double moistureTrend = -1.8;
        double batteryTrend = -2.3;
        
        stats.put("temperatureTrend", temperatureTrend);
        stats.put("moistureTrend", moistureTrend);
        stats.put("batteryTrend", batteryTrend);
        
        // Тенденції кількості зчитувань за кожен день останнього тижня
        List<String> weekDays = new ArrayList<>();
        List<Integer> dailyReadingsCounts = new ArrayList<>();
        
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter dayFormatter = DateTimeFormatter.ofPattern("EE");
        
        for (int i = 6; i >= 0; i--) {
            LocalDateTime day = now.minusDays(i);
            weekDays.add(day.format(dayFormatter));
            
            // Випадкова кількість зчитувань для тестування
            int dailyReadings = readingsCount / 7 + random.nextInt(41) - 20; // ±20 зчитувань
            dailyReadingsCounts.add(dailyReadings);
        }
        
        stats.put("weekDays", weekDays);
        stats.put("dailyReadingsCounts", dailyReadingsCounts);
        
        // Розподіл датчиків за типами (для графіків)
        Map<String, Integer> sensorTypeDistribution = new HashMap<>();
        sensorTypeDistribution.put("TEMPERATURE", (int)(sensorsCount * 0.6)); // Приблизно 60% датчиків температури
        sensorTypeDistribution.put("MOISTURE", (int)(sensorsCount * 0.4)); // Приблизно 40% датчиків вологості
        
        stats.put("sensorTypeDistribution", sensorTypeDistribution);
        
        // Статистика полів за культурами (для графіків)
        Map<String, Integer> cropDistribution = new HashMap<>();
        cropDistribution.put("Пшениця", (int)(fieldsCount * 0.35));
        cropDistribution.put("Кукурудза", (int)(fieldsCount * 0.25));
        cropDistribution.put("Соняшник", (int)(fieldsCount * 0.15));
        cropDistribution.put("Ріпак", (int)(fieldsCount * 0.1));
        cropDistribution.put("Овочеві", (int)(fieldsCount * 0.1));
        cropDistribution.put("Інші", (int)(fieldsCount * 0.05));
        
        stats.put("cropDistribution", cropDistribution);
        
        return stats;
    }
    
    /**
     * Отримує дані для сторінки аналітики з розширеною статистикою
     */
    private Map<String, Object> getAnalyticsData() {
        Map<String, Object> data = new HashMap<>();
        Random random = new Random();
        
        // Середні значення
        double avgTemperature = sensorReadingService.getAverageTemperature();
        double avgMoisture = sensorReadingService.getAverageMoisture();
        double avgBatteryLevel = sensorService.getAverageBatteryLevel();
        int readingsCount = sensorReadingService.getReadingsCount();
        
        data.put("avgTemperature", avgTemperature);
        data.put("avgMoisture", avgMoisture);
        data.put("readingsCount", readingsCount);
        data.put("avgBatteryLevel", avgBatteryLevel);
        
        // Дані для порівняння з попереднім періодом (тренди)
        // Це мають бути реальні дані, зараз підставляємо тестові значення
        data.put("temperatureTrend", 2.5);
        data.put("moistureTrend", -1.8);
        data.put("readingsTrend", 5.7);
        data.put("batteryTrend", -2.3);
        
        // Статистика по фермах
        List<FarmDto> farms = farmService.getAllFarms();
        Map<Long, Double> farmTemperatures = new HashMap<>();
        Map<Long, Double> farmMoistures = new HashMap<>();
        Map<Long, Integer> farmSensorCounts = new HashMap<>();
        Map<Long, Integer> farmFieldCounts = new HashMap<>();
        
        for (FarmDto farm : farms) {
            // Отримуємо датчики для кожної ферми
            List<SensorDto> sensors = sensorService.getSensorsByFarmId(farm.getId());
            int sensorCount = sensors.size();
            
            // Кількість полів для ферми
            int fieldCount = fieldService.getFieldsByFarmId(farm.getId()).size();
            
            // Обчислюємо середню температуру та вологість
            double farmAvgTemp = avgTemperature + (random.nextDouble() * 6 - 3); // ±3 відхилення
            double farmAvgMoisture = avgMoisture + (random.nextDouble() * 10 - 5); // ±5 відхилення
            
            // Додаємо дані до карт
            farmTemperatures.put(farm.getId(), farmAvgTemp);
            farmMoistures.put(farm.getId(), farmAvgMoisture);
            farmSensorCounts.put(farm.getId(), sensorCount);
            farmFieldCounts.put(farm.getId(), fieldCount);
        }
        
        data.put("farmTemperatures", farmTemperatures);
        data.put("farmMoistures", farmMoistures);
        data.put("farmSensorCounts", farmSensorCounts);
        data.put("farmFieldCounts", farmFieldCounts);
        
        // Дані для графіків
        // Генеруємо датові мітки за останні 30 днів
        List<String> dateLabels = new ArrayList<>();
        List<Double> temperatureHistory = new ArrayList<>();
        List<Double> moistureHistory = new ArrayList<>();
        List<Integer> readingsHistory = new ArrayList<>();
        
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM");
        
        for (int i = 29; i >= 0; i--) {
            LocalDateTime date = now.minusDays(i);
            dateLabels.add(date.format(dateFormatter));
            
            // Генеруємо випадкові історичні дані
            // Температури з реалістичними коливаннями
            double dayTempVariation = Math.sin(i * 0.2) * 3; // Синусоїдальні коливання ±3 градуси
            double randomVariation = (random.nextDouble() * 2 - 1); // Випадкові коливання ±1 градус
            temperatureHistory.add(avgTemperature + dayTempVariation + randomVariation);
            
            // Вологість
            double dayMoistVariation = Math.cos(i * 0.2) * 5; // Косинусоїдальні коливання ±5%
            double randomMoistVariation = (random.nextDouble() * 4 - 2); // Випадкові коливання ±2%
            moistureHistory.add(avgMoisture + dayMoistVariation + randomMoistVariation);
            
            // Кількість зчитувань з невеликими коливаннями
            int dailyReadings = (int)(readingsCount / 30 + (random.nextInt(41) - 20)); // ±20 зчитувань
            readingsHistory.add(dailyReadings);
        }
        
        data.put("dateLabels", dateLabels);
        data.put("temperatureHistory", temperatureHistory);
        data.put("moistureHistory", moistureHistory);
        data.put("readingsHistory", readingsHistory);
        
        return data;
    }
}
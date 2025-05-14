# Farm Monitoring Platform

A comprehensive monitoring system for agricultural operations, allowing farmers to monitor temperature, moisture, and other important metrics across their fields.

## Project Structure

The project consists of two main components:

1. **farm-monitoring-server** - Spring Boot application providing REST API and web interface
2. **farm-monitoring-sensor-emulator** - Spring Boot application simulating sensor data

## Features

- Real-time monitoring of field conditions
- Temperature and moisture tracking
- Interactive dashboards and analytics
- Farm and field management
- Sensor status monitoring

## Technologies Used

- Java 17
- Spring Boot 3.1.5
- Spring Data JPA
- H2 Database
- Thymeleaf
- Bootstrap 5
- Chart.js
- Docker

## Getting Started

### Prerequisites

- JDK 17 or later
- Maven 3.6 or later
- Docker (optional)

### Running the Application

#### Using the run script:

```
run-all.bat
```

#### Manual start:

1. Start the server:
```
start-server.bat
```

2. Start the sensor emulator:
```
start-emulator.bat
```

3. Access the web interface at: http://localhost:12345/web

## License

This project is licensed under the MIT License - see the LICENSE file for details. 
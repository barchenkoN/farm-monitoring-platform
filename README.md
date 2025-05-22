# Farm Monitoring Platform

## Description
The Farm Monitoring Platform is a system for monitoring agricultural fields using sensor data. The system consists of two main components:
- **farm-monitoring-server**: The server application that stores and displays sensor data
- **farm-monitoring-sensor-emulator**: An emulator that simulates sensors sending data to the server

## Prerequisites
- Docker and Docker Compose installed on your system

## Running with Docker

### Quick Start
Simply run the provided script to build and start all services:

**Windows:**
```
run-docker.bat
```

**Linux/Mac:**
```
chmod +x run-docker.sh
./run-docker.sh
```

### Manual Start
If you prefer to run the commands manually:

1. **Build and start all services**
```bash
docker-compose build --no-cache
docker-compose up -d
```

2. **Check the running containers**
```bash
docker-compose ps
```

3. **View logs**
```bash
# View all logs
docker-compose logs -f

# View logs for a specific service
docker-compose logs server
docker-compose logs emulator
```

4. **Stop the services**
```bash
docker-compose down
```

5. **Stop the services and remove volumes**
```bash
docker-compose down -v
```

## Accessing the Platform

After starting the services:

1. **Web Interface:**
   - URL: http://localhost:8080/login
   - Login with:
     - Username: `admin`
     - Password: `admin`

2. **API Documentation:**
   - Swagger UI: http://localhost:8080/swagger-ui.html

3. **API Endpoints:**
   - Base URL: http://localhost:8080/api
   - Authentication: Basic Auth with username "admin" and password "admin"

## Features
- Real-time monitoring of field conditions
- Temperature and moisture tracking
- Farm and field management
- Sensor status monitoring
- Authentication via form login and OAuth2 (Google, GitHub)
- API documentation with Swagger/OpenAPI
- Integration tests for API endpoints

## System Architecture
- **PostgreSQL Database**: Stores all farm, field, sensor, and reading data
- **Server Application**: Spring Boot application that provides a web interface and REST API
- **Sensor Emulator**: Simulates IoT sensors sending temperature and moisture readings

## Technologies Used
- Java 17
- Spring Boot 3.1
- Spring Security with OAuth2
- PostgreSQL
- Docker and Docker Compose
- Swagger/OpenAPI
- Thymeleaf (for web interface)
- JUnit for testing

## Documentation
The project includes the following documentation:
- [User Stories](docs/user_stories.md)
- [ER Diagram](docs/erd.md)
- [Class Diagram](docs/class_diagram.md)
- [Test Scenarios](docs/test_scenarios.md)
- [OAuth2 Integration](docs/oauth2_integration.md)

## API Endpoints
- GET /api/sensors - List all sensors
- GET /api/readings/sensor/{id} - Get readings for a specific sensor
- POST /api/readings - Submit a new sensor reading 
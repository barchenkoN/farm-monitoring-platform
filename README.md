# Farm Monitoring Platform

A comprehensive platform for monitoring farm sensors, including temperature and moisture readings.

## System Requirements

- Java 17 (required)
- Maven (included with scripts)
- Windows OS

## Quick Start

### Option 1: All-in-One Script

Run `run-all.bat` to automatically:
1. Check for Java 17
2. Start the server
3. Start the emulator
4. Stop both applications when you press any key

### Option 2: Manual Start

1. Run `setup-jdk.bat` if you don't have Java 17 installed
2. Run `start-server.bat` to start the server
3. Run `start-emulator.bat` in a separate terminal to start the sensor emulator

The server will be accessible at: http://localhost:12345/web

## Project Structure

- **farm-monitoring-server**: The main server application
  - Handles sensor data
  - Provides web interface for monitoring
  - Uses H2 in-memory database
  
- **farm-monitoring-sensor-emulator**: Simulates sensor readings
  - Sends periodic temperature and moisture data to server
  - Configurable sensor readings

## Troubleshooting

### Java Version Issues

The application requires Java 17. If you see errors like:

```
Failed to execute goal org.apache.maven.plugins:maven-compiler-plugin:3.11.0:compile (default-compile) on project farm-monitoring-server: Fatal error compiling: java.lang.ExceptionInInitializerError: com.sun.tools.javac.code.TypeTag :: UNKNOWN
```

Run `setup-jdk.bat` to install the correct Java version.

### Database Issues

If you encounter database issues:

1. Check if the server logged "Inserted initial data" message
2. If not, the schema files might not be loading correctly

### Connection Issues

If the emulator cannot connect to the server:

1. Ensure the server is running first
2. Check that both applications use the same port (12345 by default)
3. Check firewall settings

## Manual Setup

If the scripts don't work, you can manually run the applications:

1. Set Java 17 as your JAVA_HOME
2. Run server:
   ```
   cd farm-monitoring-server
   mvn clean spring-boot:run
   ```
3. Run emulator:
   ```
   cd farm-monitoring-sensor-emulator
   mvn clean spring-boot:run
   ``` 
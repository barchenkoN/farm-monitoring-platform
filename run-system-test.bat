@echo off
echo ============================================
echo   Farm Monitoring Platform - System Test
echo ============================================

echo.
echo Checking Docker installation...
docker --version
if %ERRORLEVEL% neq 0 (
    echo [ERROR] Docker is not installed or not in PATH. Please install Docker Desktop.
    exit /b 1
)

echo.
echo Checking Docker Compose...
docker-compose --version
if %ERRORLEVEL% neq 0 (
    echo [ERROR] Docker Compose is not installed or not in PATH.
    exit /b 1
)

echo.
echo Building and starting all services...
docker-compose down -v
docker-compose build --no-cache
docker-compose up -d

echo.
echo Checking if PostgreSQL is ready...
timeout /t 10 /nobreak

echo.
echo Checking if server is running...
curl -s http://localhost:8080/actuator/health
if %ERRORLEVEL% neq 0 (
    echo [WARN] Server health check failed. Waiting 10 more seconds...
    timeout /t 10 /nobreak
    curl -s http://localhost:8080/actuator/health
    if %ERRORLEVEL% neq 0 (
        echo [ERROR] Server health check failed after retry.
        echo Running logs for troubleshooting:
        docker-compose logs server
    ) else (
        echo [OK] Server is now running.
    )
) else (
    echo [OK] Server is running.
)

echo.
echo Checking if emulator is running...
curl -s http://localhost:8081/actuator/health
if %ERRORLEVEL% neq 0 (
    echo [WARN] Emulator health check failed. Waiting 10 more seconds...
    timeout /t 10 /nobreak
    curl -s http://localhost:8081/actuator/health
    if %ERRORLEVEL% neq 0 (
        echo [ERROR] Emulator health check failed after retry.
        echo Running logs for troubleshooting:
        docker-compose logs emulator
    ) else (
        echo [OK] Emulator is now running.
    )
) else (
    echo [OK] Emulator is running.
)

echo.
echo ============================================
echo   System is running. You can access:
echo.
echo   - Web Interface: http://localhost:8080/login
echo       Username: admin
echo       Password: admin
echo.
echo   - API Documentation: http://localhost:8080/swagger-ui.html
echo.
echo   - Emulator Interface: http://localhost:8081
echo ============================================
echo.
echo Type 'docker-compose down' to stop all services when done.
echo.

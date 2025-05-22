@echo off
echo ============================================
echo   Farm Monitoring Platform - Docker Startup
echo ============================================

echo.
echo Checking Docker installation...
docker --version
if %ERRORLEVEL% neq 0 (
    echo [ERROR] Docker is not installed or not in PATH. Please install Docker Desktop.
    pause
    exit /b 1
)

echo.
echo Checking Docker Compose...
docker-compose --version
if %ERRORLEVEL% neq 0 (
    echo [ERROR] Docker Compose is not installed or not in PATH.
    pause
    exit /b 1
)

echo.
echo Stopping any existing containers...
docker-compose down -v

echo.
echo Building and starting all services...
docker-compose build --no-cache
docker-compose up -d

echo.
echo Waiting for services to start (this may take a minute)...
timeout /t 30 /nobreak

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
echo To view logs, type:
echo   docker-compose logs -f
echo.
echo To stop all services, type:
echo   docker-compose down
echo.
echo Press any key to view logs...
pause > nul
docker-compose logs -f 
@echo off
echo Checking Docker status...

docker info >nul 2>&1
if %errorlevel% neq 0 (
    echo Docker is not running or is not accessible.
    echo.
    echo Please make sure Docker Desktop is installed and running.
    echo.
    echo 1. Check if Docker Desktop is installed.
    echo 2. Start Docker Desktop application if it's not running.
    echo 3. Wait for Docker to fully start (check the icon in the system tray).
    echo 4. Once Docker is running, run run-docker.bat again.
    echo.
    pause
    exit /b 1
) else (
    echo Docker is running and accessible.
    echo You can now run run-docker.bat to start the application.
    pause
) 
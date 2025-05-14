@echo off
setlocal EnableDelayedExpansion
echo Starting Farm Monitoring Sensor Emulator...
cd farm-monitoring-sensor-emulator

REM Load environment variables if .env.bat exists
if exist "..\.env.bat" (
    echo Loading environment variables...
    call "..\.env.bat"
)

echo Checking if server is running on port 12345...
powershell -Command "try { $null = New-Object System.Net.Sockets.TcpClient('localhost', 12345); Write-Host 'Server is running on port 12345.'; exit 0 } catch { Write-Host 'WARNING: Server is not running on port 12345! Emulator might not work correctly.'; exit 1 }"

REM Set Java 17 path directly
SET "JAVA_HOME=C:\Program Files\Eclipse Adoptium\jdk-17.0.15.6-hotspot"
SET "PATH=%JAVA_HOME%\bin;%PATH%"

echo Using Java from: %JAVA_HOME%

echo Starting emulator (sending data to http://localhost:12345/api)...
echo This may take a while, please wait...

REM Run Maven command with Java 17
call mvn -v
call mvn clean spring-boot:run -DskipTests
pause 
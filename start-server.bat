@echo off
setlocal EnableDelayedExpansion
echo Starting Farm Monitoring Server...
cd farm-monitoring-server

REM Load environment variables if .env.bat exists
if exist "..\.env.bat" (
    echo Loading environment variables...
    call "..\.env.bat"
)

REM Kill running Java processes
echo Stopping any running Java processes...
taskkill /F /IM java.exe /T 2>nul
if %ERRORLEVEL% == 0 (
    echo Java processes terminated.
) else (
    echo No Java processes were running.
)

echo Starting server on port 12345...
echo This may take a while, please wait...

REM Set Java 17 path directly
SET "JAVA_HOME=C:\Program Files\Eclipse Adoptium\jdk-17.0.15.6-hotspot"
SET "PATH=%JAVA_HOME%\bin;%PATH%"

echo Using Java from: %JAVA_HOME%

REM Run Maven command with Java 17
call mvn -v
call mvn clean spring-boot:run -DskipTests -Dspring-boot.run.arguments=--server.port=12345
pause
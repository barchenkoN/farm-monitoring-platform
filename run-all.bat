@echo off
setlocal EnableDelayedExpansion

echo =======================================
echo Farm Monitoring Platform - Quick Start
echo =======================================

REM Check if JDK 17 is available
set JDK17_FOUND=false
set "POTENTIAL_JAVA_HOMES[0]=%PROGRAMFILES%\Eclipse Adoptium\jdk-17.0.15.6-hotspot"
set "POTENTIAL_JAVA_HOMES[1]=%PROGRAMFILES%\Eclipse Adoptium\jdk-17"
set "POTENTIAL_JAVA_HOMES[2]=%PROGRAMFILES%\Java\jdk-17"
set "POTENTIAL_JAVA_HOMES[3]=%PROGRAMFILES%\Amazon Corretto\jdk17"
set "POTENTIAL_JAVA_HOMES[4]=%USERPROFILE%\.jdks\jdk-17"

for /L %%i in (0,1,4) do (
    if exist "!POTENTIAL_JAVA_HOMES[%%i]!\bin\java.exe" (
        set "JAVA_HOME=!POTENTIAL_JAVA_HOMES[%%i]!"
        set JDK17_FOUND=true
        echo JDK 17 found at: !JAVA_HOME!
        goto :jdk_check_done
    )
)

:jdk_check_done
if %JDK17_FOUND%==false (
    echo ERROR: JDK 17 was not found. Please run setup-jdk.bat first.
    pause
    exit /b 1
)

REM Kill any running Java processes
echo Stopping any running Java processes...
taskkill /F /IM java.exe /T 2>nul
if %ERRORLEVEL% == 0 (
    echo Java processes terminated.
) else (
    echo No Java processes were running.
)

echo.
echo Starting Farm Monitoring System...
echo.

REM Start the server in a new window
echo Starting server on port 12345...
start "Farm Monitoring Server" cmd /k "call start-server.bat"

REM Wait for the server to start
echo Waiting for server to start...
timeout /t 10 /nobreak > nul

REM Start the emulator in a new window
echo Starting sensor emulator...
start "Farm Monitoring Sensor Emulator" cmd /k "call start-emulator.bat"

echo.
echo Both applications started successfully!
echo Server web interface: http://localhost:12345/web
echo.
echo Press any key to stop all applications...
pause > nul

REM Kill Java processes when done
echo Stopping all Java processes...
taskkill /F /IM java.exe /T 2>nul

echo Done!
pause 
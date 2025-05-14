@echo off
setlocal EnableDelayedExpansion

echo =======================================
echo Farm Monitoring Platform - JDK 17 Setup
echo =======================================

REM Check if JDK 17 is already installed
set JDK17_FOUND=false

REM Check common locations
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
if %JDK17_FOUND%==true (
    echo JDK 17 is already installed.
    echo You can run start-server.bat and start-emulator.bat directly.
    pause
    exit /b 0
)

echo JDK 17 was not found on your system.
echo This application requires JDK 17 to run properly.
echo.
echo Do you want to download and install Eclipse Temurin JDK 17? (Y/N)
set /p INSTALL_CHOICE="> "

if /i "%INSTALL_CHOICE%"=="Y" (
    echo.
    echo Downloading JDK 17...
    
    REM Create temp directory
    if not exist "temp" mkdir temp
    
    REM Download JDK 17 installer
    powershell -Command "Invoke-WebRequest -Uri 'https://github.com/adoptium/temurin17-binaries/releases/download/jdk-17.0.9%2B9/OpenJDK17U-jdk_x64_windows_hotspot_17.0.9_9.msi' -OutFile 'temp\jdk17_installer.msi'"
    
    if %ERRORLEVEL% neq 0 (
        echo Failed to download JDK 17 installer.
        echo Please download and install JDK 17 manually from: https://adoptium.net/temurin/releases/
        pause
        exit /b 1
    )
    
    echo.
    echo Installing JDK 17...
    start /wait msiexec /i "temp\jdk17_installer.msi" /quiet
    
    if %ERRORLEVEL% neq 0 (
        echo Failed to install JDK 17.
        echo Please install JDK 17 manually from the downloaded file: temp\jdk17_installer.msi
        pause
        exit /b 1
    )
    
    echo.
    echo JDK 17 has been installed successfully!
    echo You can now run start-server.bat and start-emulator.bat
    
    REM Cleanup
    rmdir /s /q temp
) else (
    echo.
    echo JDK 17 installation skipped.
    echo Please install JDK 17 manually to run the application.
    echo Download from: https://adoptium.net/temurin/releases/
)

pause 
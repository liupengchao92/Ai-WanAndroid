@echo off
REM Windows batch script for building and running AiCodeApp

REM Set title
title AiCodeApp Installation Script

REM Print header
echo ================================
echo AiCodeApp Installation Script
echo ================================
echo.

REM Clean project
echo [1/4] Cleaning project...
call gradlew clean
if %errorlevel% neq 0 (
    echo Clean failed!
    pause
    exit /b %errorlevel%
)
echo Clean successful!
echo.

REM Build debug version
echo [2/4] Building debug version...
call gradlew assembleDebug
if %errorlevel% neq 0 (
    echo Build failed!
    pause
    exit /b %errorlevel%
)
echo Build successful!
echo.

REM Install to device
echo [3/4] Installing to device...
call gradlew installDebug
if %errorlevel% neq 0 (
    echo Install failed!
    pause
    exit /b %errorlevel%
)
echo Install successful!
echo.

REM Start app
echo [4/4] Starting app...
adb shell am start -n com.gradle.aicodeapp/com.gradle.aicodeapp.MainActivity
if %errorlevel% neq 0 (
    echo Start failed!
    pause
    exit /b %errorlevel%
)
echo App started successfully!
echo.

echo ================================
echo Installation completed!
echo App has been successfully installed and started.
echo ================================

pause
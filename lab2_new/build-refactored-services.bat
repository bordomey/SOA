@echo off
REM Build script for refactored SOA services

echo === Building Refactored SOA Services ===
echo.

echo 1. Building SOAP Movie Service...
cd movie-service
call mvn clean package
if %ERRORLEVEL% NEQ 0 (
    echo ❌ Failed to build movie-service
    exit /b 1
)
cd ..

echo.
echo 2. Building REST Facade...
cd rest-middleware
call mvn clean package
if %ERRORLEVEL% NEQ 0 (
    echo ❌ Failed to build rest-middleware
    exit /b 1
)
cd ..

echo.
echo 3. Building Oscar Service...
cd oscar-service
call mvn clean package
if %ERRORLEVEL% NEQ 0 (
    echo ❌ Failed to build oscar-service
    exit /b 1
)
cd ..

echo.
echo === All services built successfully ===
echo.
echo Next steps:
echo - Run: docker-compose up --build
echo - Test services using the test scripts
echo - Verify integration through HAProxy
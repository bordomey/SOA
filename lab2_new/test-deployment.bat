@echo off

echo Testing Docker Deployment...

REM Check if docker-compose is available
docker-compose version >nul 2>&1
if %errorlevel% neq 0 (
    echo docker-compose could not be found
    exit /b 1
)

REM Check if services are running
echo Checking running services...
docker-compose ps

REM Check logs for any errors
echo Checking for errors in logs...
docker-compose logs | findstr /i error

REM Test Consul availability
echo Testing Consul availability...
curl -s http://localhost:8500/v1/status/leader | findstr ":" >nul && echo Consul is running || echo Consul is not responding

REM Test Movie Service Load Balancer
echo Testing Movie Service Load Balancer...
curl -s http://localhost:8080/actuator/health | findstr "UP" >nul && echo Movie Service LB is responding || echo Movie Service LB is not responding

REM Test Oscar Service Load Balancer
echo Testing Oscar Service Load Balancer...
curl -s http://localhost:8082/api/health 2>nul | findstr "UP" >nul && echo Oscar Service LB is responding || echo Oscar Service LB is not responding

echo Test completed. Check output above for results.
@echo off

echo Starting Movie Service Instance 1 on port 8081...
start "Movie Service 1" cmd /c "java -Dserver.port=8081 -jar movie-service/target/movie-service.jar"

timeout /t 10

echo Starting Movie Service Instance 2 on port 8082...
start "Movie Service 2" cmd /c "java -Dserver.port=8082 -jar movie-service/target/movie-service.jar"

echo Both instances started successfully!
echo Instance 1: https://localhost:8081
echo Instance 2: https://localhost:8082
echo HAProxy Load Balancer: http://localhost:8080
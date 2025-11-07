@echo off
echo Starting Movie Service with HTTPS on port 9192...
start "Movie Service" /D "payara" cmd /c "java -jar payara-micro.jar --deploy ..\movie-service\target\movie-service.war --port 9191 --sslport 9191"

timeout /t 10

echo Starting Oscar Service with HTTPS on port 9292...
start "Oscar Service" /D "payara" cmd /c "java -jar payara-micro.jar --deploy ..\oscar-service\target\oscar-service.war --port 9292 --sslport 9292"

echo Services started with HTTPS!
echo Movie Service: https://localhost:9192/
echo Oscar Service: https://localhost:9292/
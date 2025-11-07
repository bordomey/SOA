@echo off
echo Starting Movie Service on port 8181...
start "Movie Service" /D "payara" cmd /c "java -jar payara-micro.jar --deploy ..\movie-service\target\movie-service.war --contextroot movie-service --port 8181"

timeout /t 10

echo Starting Oscar Service on port 8282...
start "Oscar Service" /D "payara" cmd /c "java -jar payara-micro.jar --deploy ..\oscar-service\target\oscar-service.war --contextroot oscar-service --port 8282"

echo Services started!
echo Movie Service: http://localhost:8181/movie-service/
echo Oscar Service: http://localhost:8282/oscar-service/
@echo off

echo Building Oscar Service...
cd oscar-service
mvn clean package
cd ..

echo Starting Oscar Service Instance 1 on port 9291/9292...
start "Oscar Service 1" /D "payara" cmd /c "java -jar payara-micro.jar --deploy ..\oscar-service\oscar-web\target\oscar-web.war --port 9291 --sslport 9292"

timeout /t 10

echo Starting Oscar Service Instance 2 on port 9391/9392...
start "Oscar Service 2" /D "payara" cmd /c "java -jar payara-micro.jar --deploy ..\oscar-service\oscar-web\target\oscar-web.war --port 9391 --sslport 9392"

echo Both Oscar service instances started successfully!
echo Instance 1: https://localhost:9292/
echo Instance 2: https://localhost:9392/
@echo off
echo Starting Movie Service with HTTPS on port 9192...
start "Movie Service" /D "payara" cmd /c "java -jar payara-micro.jar --deploy ..\movie-service\target\movie-service.war --port 9191 --sslport 9191"

timeout /t 10

echo Starting Oscar Service with HTTPS on port 9292...
start "Oscar Service" /D "payara" cmd /c "java -jar payara-micro.jar --deploy ..\oscar-service\target\oscar-service.war --port 9291 --sslport 9292"

echo Services started with HTTPS!
echo Movie Service: https://localhost:9192/
echo Oscar Service: https://localhost:9292/
java -jar payara-micro.jar --deploy movie-service.war --port 9191 --sslport 9192
java -jar payara-micro.jar --deploy oscar-service.war --port 9291 --sslport 9292
java -Djava.io.tmpdir=/home/studs/s336191/tmp -jar payara-micro.jar --deploy movie-service.war --port 9191 --sslport 9192 --nocluster
java -Djava.io.tmpdir=/home/studs/s336191/tmp -jar payara-micro.jar --deploy oscar-service.war --port 9291 --sslport 9292 --nocluster

ssh -L 9192:localhost:9192 s367309@helios.se.ifmo.ru -p 2222 "java -Djava.io.tmpdir=/home/studs/s336191/tmp -jar payara-micro.jar --deploy movie-service.war --port 9191 --sslport 9192 --nocluster"

ssh -L 9292:localhost:9292 s367309@helios.se.ifmo.ru -p 2222 "java -Djava.io.tmpdir=/home/studs/s336191/tmp -jar payara-micro.jar --deploy oscar-service.war --port 9291 --sslport 9292 --nocluster"
@echo off

echo Building movie-service...
cd movie-service
mvn clean package
cd ..
echo Building oscar-service...
cd oscar-service
mvn clean package
cd ..
echo Starting services with Docker Compose...
docker-compose up -d
echo Services started successfully!
echo Consul UI: http://localhost:8500
echo HAProxy Stats: http://localhost:8404
echo Movie Service Load Balancer: http://localhost:8080
echo Oscar Service Load Balancer: http://localhost:8082
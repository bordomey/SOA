@echo off
echo Building Movie Service...
cd movie-service
mvn clean package
cd ..
echo.
echo Building Oscar Service...
cd oscar-service
mvn clean package
cd ..


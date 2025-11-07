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

echo.
echo Building Test Client...
cd test-client
mvn clean package
cd ..

echo.
echo Setup complete!
echo.
echo To run the services:
echo 1. Generate SSL certificates: generate-keystore.bat
echo 2. Deploy services: deploy-services.bat
echo 3. Run test client: cd test-client && mvn exec:java -Dexec.mainClass="com.lab2.client.TestClient"
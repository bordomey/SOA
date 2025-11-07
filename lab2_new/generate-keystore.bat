@echo off
echo Generating keystore for Movie Service...
keytool -genkeypair -alias movieservice -keyalg RSA -keystore movie-service\src\main\resources\keystore.jks -storepass changeit -keypass changeit -validity 365 -dname "CN=localhost, OU=Lab2, O=University, L=City, ST=State, C=US"

echo Generating keystore for Oscar Service...
keytool -genkeypair -alias oscarservice -keyalg RSA -keystore oscar-service\src\main\resources\keystore.jks -storepass changeit -keypass changeit -validity 365 -dname "CN=localhost, OU=Lab2, O=University, L=City, ST=State, C=US"

echo Keystores generated successfully!
# Use OpenJDK 17 as base image
FROM openjdk:17.0.2-jdk

# Set working directory
WORKDIR /app

# Copy the jar file
COPY target/movie-service.jar app.jar

# Expose port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
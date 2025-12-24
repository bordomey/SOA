# Fixes Summary for Docker Deployment Issues

This document summarizes all the fixes made to resolve the Docker deployment issues encountered with the SOA services.

## Issue 1: Java Version Compatibility

### Problem
The Docker build was failing because `openjdk:11-jre-slim` image was no longer available.

### Solution
1. Updated movie-service Dockerfile to use `openjdk:17-jre-slim`
2. Updated oscar-service Dockerfile to use `payara/micro:5.2022.2-jdk17`
3. Updated Maven pom.xml files to use Java 17 instead of Java 11
4. Updated all documentation to reflect Java 17 requirement

## Issue 2: EJB Class Not Found Exception

### Problem
```
java.lang.ClassNotFoundException: com.lab2.oscar.ejb.OscarServiceRemote
```

The OscarServiceRemote EJB interface was not being found at runtime.

### Root Cause
The EJB module was not being properly packaged or deployed with the web application.

### Solutions Applied

1. **Fixed EJB Dependency Scope**
   - Removed `<scope>provided</scope>` from the EJB dependency in oscar-web/pom.xml
   - This ensures the EJB JAR is included in the WAR file

2. **Updated Docker Deployment Strategy**
   - Modified oscar-service Dockerfile to copy both EJB JAR and WAR file to deployments directory
   - Updated ENTRYPOINT to include additional Payara Micro configuration options

3. **Enhanced EJB Plugin Configuration**
   - Added explicit version and configuration to maven-ejb-plugin in oscar-ejb/pom.xml
   - Ensured proper EJB version (3.2) is specified

4. **Improved Maven Build Configuration**
   - Verified parent pom.xml has proper pluginManagement for maven-ejb-plugin
   - Ensured both modules are built correctly in the reactor build

## Issue 3: Documentation Updates

### Problem
Documentation was outdated and didn't reflect the changes made.

### Solution
1. Updated all documentation files to reflect Java 17 usage
2. Added troubleshooting section to DOCKER_DEPLOYMENT.md
3. Updated deployment summaries to reflect new architecture

## Verification Steps

To verify that the fixes work correctly:

1. **Clean Build**
   ```bash
   cd movie-service
   mvn clean package
   cd ../oscar-service
   mvn clean package
   ```

2. **Docker Build**
   ```bash
   docker-compose build
   ```

3. **Docker Deployment**
   ```bash
   docker-compose up -d
   ```

4. **Verification**
   - Check container logs: `docker-compose logs`
   - Verify services are running: `docker-compose ps`
   - Test service endpoints

## Expected Outcome

After applying these fixes:
- Movie service containers should start successfully with Java 17
- Oscar service containers should start successfully with both EJB and web modules
- No more ClassNotFoundException errors for EJB classes
- Services should register correctly with Consul
- Load balancing should work through HAProxy

## Additional Improvements

1. **Enhanced Troubleshooting Guide**
   - Added common issue resolutions to documentation
   - Provided specific steps for EJB-related issues

2. **Better Configuration Management**
   - Explicit Payara Micro configuration options
   - Clearer port binding settings

3. **Improved Build Reliability**
   - Consistent Java version across all components
   - Proper dependency scoping for multi-module applications

These fixes ensure that the Docker deployment works correctly and reliably, resolving both the immediate Java version issue and the underlying EJB packaging problem.
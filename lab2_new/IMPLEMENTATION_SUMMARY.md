# Implementation Summary for Task 3

This document summarizes the changes made to implement the requirements specified in task3.txt for the SOA lab assignment.

## Changes to "Called" Service (movie-service)

### 1. Spring Boot Configuration
- Converted the movie-service from Payara/JAX-RS to Spring Boot
- Updated pom.xml to use Spring Boot dependencies
- Created Spring Boot application class with `@SpringBootApplication` annotation
- Created controllers to replace JAX-RS resources
- Configured XML support for request/response serialization

### 2. Service Discovery with Consul
- Added Spring Cloud Consul dependencies to pom.xml
- Configured Consul integration in application.properties
- Enabled service discovery with `@EnableDiscoveryClient` annotation
- Configured health checks for service registration

### 3. Load Balancing with HAProxy
- Created HAProxy configuration file (haproxy.cfg)
- Configured load balancing for two movie-service instances
- Set up health checks for backend services
- Created scripts to start multiple service instances

## Changes to "Calling" Service (oscar-service)

### 1. Module Restructuring
- Restructured oscar-service into a multi-module Maven project:
  - Parent module (oscar-service)
  - Web module (oscar-web) - Contains JAX-RS resources
  - EJB module (oscar-ejb) - Contains business logic
- Moved appropriate classes to respective modules
- Configured inter-module dependencies

### 2. EJB Implementation
- Created Stateless EJB (`OscarServiceBean`) to contain business logic
- Implemented Remote interface (`OscarServiceRemote`) for EJB access
- Moved business logic from resource classes to EJB
- Configured EJB pooling with dynamic scaling capabilities

### 3. EJB Pool Configuration
- Created ejb-jar.xml for standard EJB configuration
- Created payara-ejb-jar.xml for Payara-specific pooling settings
- Configured dynamic pool scaling with min/max pool sizes
- Set up pool resize quantities and timeout settings

### 4. Multiple Server Instances
- Created scripts to start multiple oscar-service instances
- Configured different ports for each instance (9291/9292 and 9391/9392)
- Updated HAProxy configuration to load balance between instances

### 5. HAProxy Load Balancing
- Extended HAProxy configuration to include oscar-service load balancing
- Configured frontend/backend for oscar-service instances
- Set up health checks for oscar-service backend

## Compatibility
All changes maintain full compatibility with the existing API implemented in previous lab works. The services continue to:
- Support the same REST endpoints
- Use XML for request/response serialization
- Maintain the same data models and validation rules
- Preserve existing functionality while adding new capabilities

## Deployment
The implementation supports:
- Running multiple instances of both services
- Load balancing through HAProxy
- Service discovery through Consul
- Dynamic scaling of EJB components
- HTTPS communication between services
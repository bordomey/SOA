# Deployment Guide for Refactored SOA Services

## Overview

This guide provides step-by-step instructions for deploying the refactored SOAP-based movie-service with Mule ESB integration and REST facade.

## Prerequisites

- Java 17 JDK installed
- Docker (optional, for containerized deployment)
- Mule ESB runtime installed on Helios server
- Maven 3.6+ for building components

## Deployment Architecture

```
┌─────────────────┐    ┌──────────────────┐    ┌────────────────────┐
│   REST Clients  │───▶│  REST Facade     │───▶│  SOAP Movie        │
│                 │    │  (Port 8081)     │    │  Service (Tomcat)  │
└─────────────────┘    └──────────────────┘    │  (Port 8080)       │
                                                └────────────────────┘
                                                        │
┌─────────────────┐                                     │
│  Oscar Service  │─────────────────────────────────────┘
│  (Unchanged)    │
└─────────────────┘

┌─────────────────┐    ┌──────────────────┐
│   Mule ESB      │───▶│  SOAP Movie      │
│   Integration   │    │  Service         │
│   Flows         │    │                  │
└─────────────────┘    └──────────────────┘
```

## Step-by-Step Deployment

### 1. Build and Deploy SOAP Movie Service

```bash
# Navigate to movie-service directory
cd movie-service

# Build the WAR file
mvn clean package

# Option A: Deploy to standalone Tomcat
# Copy WAR to Tomcat webapps directory
cp target/movie-service.war $TOMCAT_HOME/webapps/

# Start Tomcat
$TOMCAT_HOME/bin/startup.sh

# Option B: Deploy using Docker
docker build -t movie-service-soap .
docker run -d -p 8080:8080 movie-service-soap
```

**Verification:**
```bash
# Check if service is running
curl -f http://localhost:8080/MovieService?wsdl
```

### 2. Deploy REST Facade

```bash
# Navigate to rest-middleware directory
cd ../rest-middleware

# Build the JAR file
mvn clean package

# Run the REST facade
java -jar target/rest-middleware.jar --soap.movie.service.url=http://localhost:8080/MovieService
```

**Configuration Options:**
```bash
# Specify different SOAP service URL
--soap.movie.service.url=https://production-server:8443/MovieService

# Specify different port for REST facade
--server.port=9090
```

### 3. Deploy Mule ESB Configuration

```bash
# Copy Mule configuration files to Mule server
cp ../mule-config/*.xml $MULE_HOME/apps/integration/

# Start Mule ESB with the configuration
$MULE_HOME/bin/mule start

# Or deploy using Mule Management Console
# Import integration-main.xml as a new application
```

**Mule Ports Configuration:**
- Movie Service Integration: Port 8081
- Oscar Service Integration: Port 8082
- HTTPS versions: Ports 8443/9443

### 4. Configure Oscar Service

The oscar-service remains unchanged and should continue to work with the REST facade:

```bash
# Update oscar-service configuration to point to REST facade
# In application.properties or equivalent:
movie.service.base.url=http://localhost:8081/movie-service/api
```

## Testing the Deployment

### 1. Test SOAP Service Directly

```bash
# Test WSDL availability
curl http://localhost:8080/MovieService?wsdl

# Test SOAP operation
curl -X POST http://localhost:8080/MovieService \
  -H "Content-Type: text/xml;charset=UTF-8" \
  -H "SOAPAction: \"\"" \
  --data '<?xml version="1.0" encoding="UTF-8"?>
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:ser="http://soap.movie.lab2.com/">
   <soapenv:Header/>
   <soapenv:Body>
      <ser:getAverageLength/>
   </soapenv:Body>
</soapenv:Envelope>'
```

### 2. Test REST Facade

```bash
# Test REST endpoint (should work same as before)
curl http://localhost:8081/movie-service/api/movies/average-length

# Test movie creation
curl -X POST http://localhost:8081/movie-service/api/movies \
  -H "Content-Type: application/xml" \
  --data '<MovieRequest>
    <name>Test Movie</name>
    <coordinates>
      <x>10.5</x>
      <y>20.3</y>
    </coordinates>
    <length>120</length>
    <operator>
      <name>Test Director</name>
    </operator>
  </MovieRequest>'
```

### 3. Test Mule ESB Integration

```bash
# Test through Mule ESB
curl http://localhost:8081/movie-service/api/movies/average-length

# Test Oscar service integration
curl http://localhost:8082/oscar-service/api/oscar/screenwriters/get-loosers
```

## Monitoring and Troubleshooting

### Log Files

**Tomcat Logs:**
```bash
# Tomcat logs
$TOMCAT_HOME/logs/catalina.out
$TOMCAT_HOME/logs/localhost.*.log

# Application logs
tail -f $TOMCAT_HOME/logs/movie-service.log
```

**REST Facade Logs:**
```bash
# Console output from Spring Boot application
# Or check configured log files
```

**Mule ESB Logs:**
```bash
# Mule logs
$MULE_HOME/logs/mule-app-integration.log
$MULE_HOME/logs/mule-integration-main.log
```

### Health Checks

```bash
# SOAP Service Health
curl -f http://localhost:8080/MovieService?wsdl

# REST Facade Health
curl -f http://localhost:8081/actuator/health

# Mule ESB Health (if configured)
curl -f http://localhost:8081/health
```

## Security Configuration

### Enable HTTPS

**Tomcat SSL Configuration:**
```xml
<!-- In server.xml -->
<Connector port="8443" protocol="org.apache.coyote.http11.Http11NioProtocol"
           maxThreads="200" SSLEnabled="true"
           keystoreFile="/path/to/keystore.jks"
           keystorePass="password"
           keystoreType="JKS"/>
```

**REST Facade SSL:**
```yaml
# In application.yml
server:
  port: 8443
  ssl:
    key-store: classpath:keystore.p12
    key-store-password: password
    key-store-type: PKCS12
```

## Scaling and High Availability

### Load Balancing Options

1. **HAProxy Configuration:**
```haproxy
backend movie_service_backend
    balance roundrobin
    server movie1 localhost:8080 check
    server movie2 192.168.1.10:8080 check
```

2. **Multiple REST Facade Instances:**
```bash
# Run multiple instances on different ports
java -jar rest-middleware.jar --server.port=8081
java -jar rest-middleware.jar --server.port=8082
```

## Rollback Procedure

If issues occur, rollback to previous version:

```bash
# Stop new services
# Restore previous WAR/JAR files
# Restart original services
# Update client configurations to point to old endpoints
```

## Performance Tuning

### JVM Settings for Tomcat
```bash
export JAVA_OPTS="-Xms512m -Xmx2048m -XX:+UseG1GC"
```

### Connection Pooling
Configure database connection pooling in context.xml if using persistence.

## Support and Maintenance

- Monitor logs regularly
- Set up alerting for service downtime
- Regular backup of configuration files
- Keep dependencies updated
- Test failover scenarios periodically

This deployment provides a robust, scalable SOA architecture with backward compatibility maintained through the REST facade layer.
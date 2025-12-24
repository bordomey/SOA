# Docker Deployment Guide

This guide explains how to deploy the SOA services using Docker and Docker Compose.

## Prerequisites

- Docker Engine 20.10 or higher
- Docker Compose 1.29 or higher

## Services Overview

The Docker Compose configuration includes the following services:

1. **Consul** - Service discovery platform
2. **HAProxy** - Load balancer for both services
3. **Movie Service (2 instances)** - Spring Boot microservice
4. **Oscar Service (2 instances)** - EJB-based service running on Payara Micro

## Docker Images

### Movie Service
- Built from `movie-service/Dockerfile`
- Uses OpenJDK 17 JRE slim image
- Runs Spring Boot application on port 8080

### Oscar Service
- Built from `oscar-service/Dockerfile`
- Uses Payara Micro image with Java 17
- Contains both EJB and Web modules
- Runs on ports 8080 (HTTP) and 8443 (HTTPS)

## Network Configuration

All services are connected through a custom bridge network named `soa-network`, which allows them to communicate with each other using service names as hostnames.

## Environment Variables

### Movie Service
- `SERVER_PORT` - Port on which the service runs
- `SPRING_CLOUD_CONSUL_HOST` - Consul hostname for service discovery
- `SPRING_CLOUD_CONSUL_PORT` - Consul port

### Oscar Service
- `MOVIE_SERVICE_BASE_URL` - Base URL for the movie service (used for inter-service communication)

## Ports Mapping

| Service | Host Port | Container Port | Purpose |
|---------|-----------|----------------|---------|
| Consul UI | 8500 | 8500 | Consul web interface |
| Consul DNS | 8600 | 8600 | DNS service |
| HAProxy (Movie LB) | 8080 | 8080 | Movie service load balancer |
| HAProxy (Oscar LB) | 8082 | 8082 | Oscar service load balancer |
| HAProxy Stats | 8404 | 8404 | HAProxy statistics |
| Movie Service 1 | 8081 | 8080 | Direct access to instance 1 |
| Movie Service 2 | 8083 | 8080 | Direct access to instance 2 |
| Oscar Service 1 | 9291/9292 | 8080/8443 | Direct access to instance 1 |
| Oscar Service 2 | 9391/9392 | 8080/8443 | Direct access to instance 2 |

## Deployment Commands

### Build and Deploy All Services
```bash
# Linux/Mac
./build-and-deploy.sh

# Windows
build-and-deploy.bat
```

### Manual Deployment
```bash
# Build the services
cd movie-service && mvn clean package && cd ..
cd oscar-service && mvn clean package && cd ..

# Start all services
docker-compose up -d

# View logs
docker-compose logs -f

# Stop all services
docker-compose down
```

## Access Points

After deployment, the services will be available at:

- **Consul UI**: http://localhost:8500
- **HAProxy Stats**: http://localhost:8404
- **Movie Service Load Balancer**: http://localhost:8080
- **Oscar Service Load Balancer**: http://localhost:8082

## Service Registration

The movie service instances will automatically register with Consul as `movie-service`. You can verify this in the Consul UI.

## Health Checks

HAProxy performs health checks on all backend services:
- Movie services: `/actuator/health` endpoint
- Oscar services: `/api/health` endpoint (if available)

## Scaling Services

To scale services, you can modify the `docker-compose.yml` file or use Docker Compose scaling commands:

```bash
# Scale movie service to 3 instances
docker-compose up -d --scale movie-service=3
```

Note: You may need to update the HAProxy configuration to include additional backend servers.

## Troubleshooting

### Common Issues

1. **Port conflicts**: Make sure the required ports are free on your host machine
2. **Build failures**: Ensure Maven dependencies can be downloaded
3. **Service discovery issues**: Check that Consul is running and services can reach it
4. **Inter-service communication**: Verify that the Oscar service can reach the Movie service

### Useful Commands

```bash
# View running containers
docker-compose ps

# View logs for a specific service
docker-compose logs movie-service-1

# Execute commands in a running container
docker-compose exec movie-service-1 sh

# Restart a specific service
docker-compose restart oscar-service-1
```

## Troubleshooting Common Issues

### ClassNotFoundException for EJB Classes

If you see `ClassNotFoundException` for EJB classes like `OscarServiceRemote`:

1. Ensure both the EJB JAR and WAR files are being copied to the deployments directory
2. Check that the EJB dependency scope is not set to `provided` in the web module
3. Verify that the Maven build is generating both artifacts correctly

### Port Conflicts

If containers fail to start due to port conflicts:

1. Check that the required ports are free on your host machine
2. Modify the port mappings in `docker-compose.yml` if needed
3. Ensure no other instances of the services are running

### Build Failures

If Maven builds fail:

1. Ensure you have internet connectivity to download dependencies
2. Check that Java 17 is installed and configured correctly
3. Clean and rebuild the projects: `mvn clean package`
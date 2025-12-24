# SOA Lab 3 - Advanced Service Configuration

This repository contains the implementation for Lab 3 of the SOA course, which focuses on advanced service configuration including Spring Boot migration, EJB module separation, service discovery, and load balancing.

## Prerequisites

- Java 17 or higher
- Maven 3.6 or higher
- Docker and Docker Compose
- Payara Micro (for manual deployment)

## Project Structure

```
├── movie-service/           # Called service (Spring Boot)
│   ├── src/                 # Source code
│   ├── Dockerfile           # Docker configuration
│   └── target/              # Compiled artifacts
├── oscar-service/           # Calling service (EJB-based)
│   ├── oscar-web/          # Web module (JAX-RS resources)
│   ├── oscar-ejb/          # EJB module (business logic)
│   ├── Dockerfile           # Docker configuration
│   └── target/              # Compiled artifacts
├── haproxy/                # HAProxy configuration
├── docker-compose.yml       # Docker Compose orchestration
├── build-and-deploy.sh     # Build and deployment script (Linux/Mac)
└── build-and-deploy.bat    # Build and deployment script (Windows)
```

## Running the Services

### Option 1: Docker Deployment (Recommended)

```bash
# Build and deploy all services
./build-and-deploy.sh  # Linux/Mac
# OR
build-and-deploy.bat   # Windows
```

> **Troubleshooting Tip**: If you encounter `ClassNotFoundException` errors (like `java.lang.ClassNotFoundException: com.lab2.oscar.ejb.OscarServiceRemote`), check [FIXES_SUMMARY.md](FIXES_SUMMARY.md) for detailed solutions.

### Option 2: Manual Deployment

#### 1. Start Consul for Service Discovery

```bash
consul agent -dev
```

#### 2. Start HAProxy

```bash
haproxy -f haproxy/haproxy.cfg
```

#### 3. Start Movie Service Instances

##### Windows:
```cmd
start-movie-instances.bat
```

##### Linux/Mac:
```bash
./start-movie-instances.sh
```

#### 4. Start Oscar Service Instances

##### Windows:
```cmd
start-oscar-instances.bat
```

##### Linux/Mac:
```bash
./start-oscar-instances.sh
```

## Access Points

- Movie Service Load Balancer: http://localhost:8080
- Oscar Service Load Balancer: http://localhost:8082
- HAProxy Stats: http://localhost:8404
- Consul UI: http://localhost:8500

## Key Features Implemented

1. **Spring Boot Migration**: The movie-service has been migrated from Payara/JAX-RS to Spring Boot
2. **Service Discovery**: Both services register with Consul for automatic service discovery
3. **Load Balancing**: HAProxy distributes requests across multiple service instances
4. **EJB Architecture**: The oscar-service has been restructured into separate web and EJB modules
5. **Dynamic Scaling**: EJB pools are configured for dynamic resizing based on load
6. **Multiple Instances**: Scripts are provided to easily start multiple instances of each service
7. **Docker Deployment**: Full Docker and Docker Compose support for easy deployment
8. **Service Communication**: Well-defined communication patterns between services

## API Endpoints

The services maintain full compatibility with the APIs from previous labs:

### Movie Service
- `GET /api/movies` - Get all movies with filtering and pagination
- `POST /api/movies` - Create a new movie
- `GET /api/movies/{id}` - Get a movie by ID
- `PUT /api/movies/{id}` - Update a movie
- `DELETE /api/movies/{id}` - Delete a movie
- `PATCH /api/movies/honor-by-length/{min-length}/oscars-to-add` - Add Oscars to movies
- `GET /api/movies/average-length` - Get average movie length
- `GET /api/movies/count-by-operator` - Count movies by operator criteria
- `GET /api/movies/filter-by-operator` - Filter movies by operator criteria

### Oscar Service
- `GET /api/oscar/screenwriters/get-loosers` - Get screenwriters with no Oscar wins
- `PATCH /api/oscar/movies/honor-by-length/{min-length}/oscars-to-add` - Add Oscars to movies

All endpoints support both XML and JSON formats.

## Additional Documentation

- [DOCKER_DEPLOYMENT.md](DOCKER_DEPLOYMENT.md) - Detailed instructions for Docker deployment
- [SERVICE_COMMUNICATION.md](SERVICE_COMMUNICATION.md) - Explanation of how services communicate in the Docker environment
- [IMPLEMENTATION_SUMMARY.md](IMPLEMENTATION_SUMMARY.md) - Technical summary of all changes made for Lab 3
- [FIXES_SUMMARY.md](FIXES_SUMMARY.md) - Summary of fixes for Docker deployment issues
- [CHANGES_SUMMARY.md](CHANGES_SUMMARY.md) - Complete summary of all changes made

## Test Scripts

- [test-deployment.sh](test-deployment.sh) - Shell script to verify deployment (Linux/Mac)
- [test-deployment.bat](test-deployment.bat) - Batch script to verify deployment (Windows)
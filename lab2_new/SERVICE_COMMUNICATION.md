# Service Communication in Docker Environment

This document explains how the different services communicate with each other in the Docker deployment.

## Network Architecture

All services are connected through a custom Docker bridge network named `soa-network`. This allows services to communicate with each other using their container names as hostnames.

## Service Communication Flow

### 1. Movie Service Communication

The movie services are Spring Boot applications that:
- Register themselves with Consul for service discovery
- Expose REST APIs for movie management
- Provide health check endpoints at `/actuator/health`

### 2. Oscar Service Communication

The oscar services are EJB-based applications running on Payara Micro that:
- Communicate with movie services via REST APIs
- Are configured to call specific movie service instances
- Expose their own REST APIs for oscar-related operations

### 3. Inter-Service Communication

In the Docker environment, services communicate as follows:

```
Oscar Service 1 ──┐
                  ├──► Movie Service 1
Oscar Service 2 ──┘

Oscar Service 3 ──┐
                  ├──► Movie Service 2
Oscar Service 4 ──┘
```

This configuration ensures that each oscar service instance communicates with a dedicated movie service instance, distributing the load evenly.

## Service Discovery

### Consul Integration

The movie services automatically register with Consul using:
- Service name: `movie-service`
- Health check endpoint: `/actuator/health`
- Registration interval: Every 15 seconds

Other services can discover movie service instances through Consul's DNS interface or HTTP API.

### Load Balancing

HAProxy acts as the load balancer for both services:

1. **Movie Service Load Balancing**
   - Listens on port 8080
   - Distributes requests to movie-service-1 and movie-service-2
   - Performs health checks on `/actuator/health`

2. **Oscar Service Load Balancing**
   - Listens on port 8082
   - Distributes requests to oscar-service-1 and oscar-service-2
   - Performs health checks on `/api/health` (if available)

## Environment Configuration

### Movie Service Environment

```yaml
environment:
  - SERVER_PORT=8080
  - SPRING_CLOUD_CONSUL_HOST=consul
  - SPRING_CLOUD_CONSUL_PORT=8500
```

### Oscar Service Environment

```yaml
environment:
  - MOVIE_SERVICE_BASE_URL=https://movie-service-1:8443  # For instance 1
  - MOVIE_SERVICE_BASE_URL=https://movie-service-2:8443  # For instance 2
```

## SSL/TLS Configuration

### Internal Communication

- Movie services expose HTTP on port 8080
- Oscar services communicate with movie services over HTTPS on port 8443
- Self-signed certificates are used for internal communication

### External Access

- HAProxy terminates SSL for external clients
- Internal services communicate over the secure Docker network

## Health Monitoring

### Movie Services

Health checks are performed on:
- Endpoint: `/actuator/health`
- Method: HTTP GET
- Expected response: HTTP 200

### Oscar Services

Health checks are performed on:
- Endpoint: `/api/health` (if implemented)
- Method: HTTP GET
- Expected response: HTTP 200

## Failover Mechanisms

### Service Failure Detection

HAProxy continuously monitors service health and:
- Removes unhealthy instances from the load balancing pool
- Automatically reintroduces recovered instances
- Maintains service availability during partial outages

### Consul Service Registry

Consul maintains an up-to-date registry of healthy service instances:
- Automatically deregisters failed services
- Enables dynamic service discovery
- Provides health status information to other services

## Configuration Updates

### Runtime Configuration

Services can be configured at runtime through:
- Environment variables in docker-compose.yml
- Consul key-value store (for supported services)
- Volume-mounted configuration files

### Service Discovery Updates

When services are scaled or restarted:
- New instances automatically register with Consul
- HAProxy dynamically updates its backend configuration
- No manual intervention required for basic scaling operations

## Security Considerations

### Network Isolation

- Services are isolated in a private Docker network
- Only necessary ports are exposed to the host
- Inter-service communication is secured through the Docker network

### Certificate Management

- Self-signed certificates are used for internal HTTPS communication
- Certificates are managed through volume mounts
- Certificate rotation requires container restart

## Performance Optimization

### Connection Pooling

Services implement connection pooling for:
- Database connections (where applicable)
- HTTP client connections for inter-service communication
- Efficient resource utilization

### Caching

Services may implement caching strategies for:
- Frequently accessed data
- Computationally expensive operations
- Improved response times

## Monitoring and Observability

### Logging

All services output structured logs that:
- Include timestamp and severity information
- Are captured by Docker logging drivers
- Can be forwarded to centralized logging systems

### Metrics

Services expose metrics through:
- Spring Boot Actuator endpoints (movie services)
- Custom metrics endpoints (where implemented)
- Integration with monitoring systems like Prometheus

### Tracing

Distributed tracing can be implemented through:
- OpenTelemetry instrumentation
- Zipkin or Jaeger integration
- Cross-service request correlation
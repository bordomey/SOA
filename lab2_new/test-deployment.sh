#!/bin/bash

echo "Testing Docker Deployment..."

# Check if docker-compose is available
if ! command -v docker-compose &> /dev/null
then
    echo "docker-compose could not be found"
    exit 1
fi

# Check if services are running
echo "Checking running services..."
docker-compose ps

# Check logs for any errors
echo "Checking for errors in logs..."
docker-compose logs | grep -i error

# Test Consul availability
echo "Testing Consul availability..."
curl -s http://localhost:8500/v1/status/leader | grep -q ":" && echo "Consul is running" || echo "Consul is not responding"

# Test Movie Service Load Balancer
echo "Testing Movie Service Load Balancer..."
curl -s http://localhost:8080/actuator/health | grep -q "UP" && echo "Movie Service LB is responding" || echo "Movie Service LB is not responding"

# Test Oscar Service Load Balancer
echo "Testing Oscar Service Load Balancer..."
curl -s http://localhost:8082/api/health 2>/dev/null | grep -q "UP" && echo "Oscar Service LB is responding" || echo "Oscar Service LB is not responding"

echo "Test completed. Check output above for results."
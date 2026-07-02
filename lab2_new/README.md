# SOA Lab 4 - SOAP service & mule ESB

This repository contains the implementation for Lab 3 of the SOA course, which focuses on advanced service configuration including Spring Boot migration, EJB module separation, service discovery, and load balancing.

## Prerequisites

- Java 17 or higher
- Maven 3.6 or higher
- Docker and Docker Compose
- Payara Micro (for manual deployment)



## Access Points

- Movie Service Load Balancer: http://localhost:8080
- Oscar Service Load Balancer: http://localhost:8082
- HAProxy Stats: http://localhost:8404
- Consul UI: http://localhost:8500

## Key Features Implemented

Переработать сервисы из лабораторной работы #3 следующим образом:

    Первый ("вызываемый") сервис переписать в соответствии с требованиями протокола SOAP.
    Развернуть переработанный сервис на сервере приложений по собственному выбору.
    Оставшийся сервис не модифицировать, не менять его API, протокол и используемый сервер приложений.
    Установить и сконфигурировать на сервере Helios программное обеспечение Mule ESB.
    Настроить интеграцию двух сервисов с использованием установленного программного обеспечения.
    Реализовать дополнительную REST-"прослойку", обеспечивающую возможность доступа к переработанному сервису клиентского приложения без необходимости его модификации. Никакой дополнительной логики, помимо вызовов SOAP-сервиса, разработанная REST-прослойка содержать не должна.

## API Endpoints

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


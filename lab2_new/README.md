# SOA Lab 3 - Advanced Service Configuration

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

## Lab 3 requirements

Изменения в "вызываемом" сервисе:

    Сконфигурировать окружение для работы сервиса на платформе Spring Boot.
    Запустить второй экземпляр сервиса на другом порту. Реализовать балансировку нагрузки между экземплярами с помощью Haproxy.
    Реализовать механизм Service Discovery. Для этого установить Consul и интегрировать свой сервис с ним, автоматически регистрируя в момент запуска.

Изменения в "вызывающем" сервисе:

    Разделить приложение на два модуля -- веб-приложение с веб-сервисом и EJB-jar с бизнес-компонентами.
    Переместить всю логику из класса сервиса в Stateless EJB. В классе сервиса оставить только обращение к методам бизнес-интерфейса. EJB-компонент должен быть доступен удалённо (иметь Remote-интерфейс).
    Сформировать на уровне сервера приложений пул компонентов EJB настраиваемой мощности, динамически расширяемый при увеличении нагрузки.
    Настроить второй экземпляр сервера приложений на другом порту, "поднять" на нём вторую копию веб-сервиса и пула EJB.
    Настроить балансировку нагрузки на оба запущенных узла через Haproxy.

Оба веб-сервиса и клиентское приложение должны сохранить полную совместимость с API, реализованными в рамках предыдущих лабораторных работ.

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

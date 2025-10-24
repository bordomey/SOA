# Movie Management and Oscar Services API

This project contains the OpenAPI specification for two web services as requested in the SOA Lab 1 assignment.

## Overview

The specification includes:

1. **Movie Management Service** - RESTful API for managing a collection of Movie objects
2. **Oscar Service** - Additional operations related to screenwriters and movie awards

## Files

- `openapi-specification.yaml` - Complete OpenAPI 3.0.3 specification
- `swagger-ui.html` - Interactive web documentation using Swagger UI
- `start-server.ps1` - PowerShell script to start a local web server
- `README.md` - This file

## Features Implemented

### Movie Management Service

#### Basic CRUD Operations
- `GET /movies` - Get all movies with filtering, sorting, and pagination
- `POST /movies` - Create a new movie
- `GET /movies/{id}` - Get movie by ID
- `PUT /movies/{id}` - Update movie by ID
- `DELETE /movies/{id}` - Delete movie by ID

#### Additional Operations
- `GET /movies/average-length` - Calculate average movie length
- `GET /movies/count-by-operator` - Count movies with operator greater than specified
- `GET /movies/filter-by-operator` - Get movies with operator greater than specified

### Oscar Service

- `GET /oscar/screenwriters/get-loosers` - Get screenwriters with no Oscar wins
- `PATCH /oscar/movies/honor-by-length/{min-length}/oscars-to-add` - Add Oscars to movies by length

## Data Models

### Movie
- `id` (long): Auto-generated unique identifier > 0
- `name` (String): Non-null, non-empty movie name
- `coordinates` (Coordinates): Non-null coordinates object
- `creationDate` (ZonedDateTime): Auto-generated creation timestamp
- `oscarsCount` (Integer): Non-null, > 0 number of Oscars
- `goldenPalmCount` (Long): Non-null, > 0 number of Golden Palm awards
- `length` (int): Movie length in minutes, > 0
- `genre` (MovieGenre): Optional genre enum
- `operator` (Person): Non-null operator/director information

### Coordinates
- `x` (Float): Non-null X coordinate
- `y` (double): Y coordinate, must be > -26

### Person
- `name` (String): Non-null, non-empty person name
- `birthday` (Date): Optional birthday
- `height` (Long): Optional height in cm, must be > 0 if provided

### MovieGenre Enum
- WESTERN
- MUSICAL
- ADVENTURE
- HORROR
- SCIENCE_FICTION

## Technical Requirements Compliance

✅ **RESTful API Design**: All endpoints follow REST principles
✅ **HTTP Methods**: Operations mapped to appropriate HTTP methods (GET, POST, PUT, DELETE, PATCH)
✅ **XML Format**: All request/response bodies use XML format
✅ **URL Parameters**: All operation parameters passed via URL (path/query parameters)
✅ **Error Handling**: Appropriate HTTP status codes for validation errors
✅ **Filtering & Sorting**: GET /movies supports filtering by any field combination
✅ **Pagination**: Page size and page number support implemented
✅ **Validation**: All class-level constraints properly defined in schema
✅ **Separate URLs**: Additional operations on separate endpoints

## How to View the Documentation

### Option 1: Local Web Server (Recommended)

1. Open PowerShell in the project directory
2. Run the server script:
   ```powershell
   .\start-server.ps1
   ```
3. Open your browser and navigate to: `http://localhost:8000/swagger-ui.html`

### Option 2: Online Swagger Editor

1. Go to https://editor.swagger.io/
2. Copy the contents of `openapi-specification.yaml`
3. Paste it into the editor to view the interactive documentation

### Option 3: VS Code with OpenAPI Extension

1. Install the "OpenAPI (Swagger) Editor" extension in VS Code
2. Open `openapi-specification.yaml`
3. Use the preview feature to view the documentation

## Validation

The OpenAPI specification has been validated for:

- ✅ Valid OpenAPI 3.0.3 syntax
- ✅ All required fields properly defined
- ✅ Constraint validation rules implemented
- ✅ Appropriate HTTP status codes
- ✅ Complete request/response schemas
- ✅ Proper error handling
- ✅ XML content type specification

## API Endpoints Summary

### Movie Management
- **GET** `/movies` - List movies with filtering/pagination
- **POST** `/movies` - Create new movie
- **GET** `/movies/{id}` - Get specific movie
- **PUT** `/movies/{id}` - Update movie
- **DELETE** `/movies/{id}` - Delete movie
- **GET** `/movies/average-length` - Calculate average length
- **GET** `/movies/count-by-operator` - Count by operator comparison
- **GET** `/movies/filter-by-operator` - Filter by operator comparison

### Oscar Service
- **GET** `/oscar/screenwriters/get-loosers` - Get losing screenwriters
- **PATCH** `/oscar/movies/honor-by-length/{min-length}/oscars-to-add` - Award Oscars by length

## Notes

- All auto-generated fields (id, creationDate) are handled automatically by the server
- XML is the required data format for all operations
- Person comparison logic for "greater than" operations should be implemented by the service
- Pagination uses 0-based page numbering
- All validation constraints from the Java classes are preserved in the OpenAPI schema
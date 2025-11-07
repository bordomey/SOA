# Movie Management System - Frontend

A React-based frontend application for managing movies and accessing Oscar-related services. This application provides a user-friendly interface for interacting with the Movie Management and Oscar Services API.

## Features

- **Movie Management**
  - View all movies with pagination, sorting, and filtering
  - Create new movies
  - Edit existing movies
  - Delete movies
  - Filter by name, Oscars count, Golden Palm count, length, genre, operator, and coordinates
  - Sort by any field (ascending/descending)
  - Pagination with configurable page size

- **Movie Statistics**
  - Calculate average movie length
  - Count movies by operator comparison
  - Filter movies by operator comparison

- **Oscar Service**
  - View screenwriters with no Oscar wins
  - Add Oscars to movies based on length threshold

## Prerequisites

- Node.js (v14 or higher)
- npm or yarn

## Installation

1. Install dependencies:
```bash
npm install
```

## Configuration

Create a `.env` file in the root directory to configure the API base URL:

```
REACT_APP_API_URL=http://localhost:8080
```

If not set, the application defaults to `http://localhost:8080`.

## Running the Application

Start the development server:

```bash
npm start
```

The application will open in your browser at `http://localhost:3000`.

## Building for Production

Create a production build:

```bash
npm run build
```

The build folder will contain the optimized production build.

## API Communication

The application communicates with the backend API using XML format. The API service layer handles:
- XML request/response parsing
- Error handling and validation
- HTTP request formatting

## Project Structure

```
src/
  ├── components/          # React components
  │   ├── MovieList.js    # Movie listing with filters and pagination
  │   ├── MovieForm.js    # Create/edit movie form
  │   ├── Statistics.js   # Statistics and analytics
  │   └── OscarService.js # Oscar-related operations
  ├── services/
  │   └── api.js          # API service layer for XML communication
  ├── App.js              # Main application component with routing
  ├── App.css             # Application styles
  ├── index.js            # Application entry point
  └── index.css           # Global styles
```

## Technologies Used

- React 18
- React Router DOM 6
- Axios (HTTP client)
- Fast XML Parser (XML parsing)
- CSS3 (Modern styling with gradients and responsive design)

## Error Handling

The application displays user-friendly error messages for:
- Validation errors from the API
- Network errors
- Server errors
- Invalid input data

All errors are displayed in a prominent error message component with detailed information.


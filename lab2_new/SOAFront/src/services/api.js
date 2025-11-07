import axios from 'axios';
import { XMLParser, XMLBuilder } from 'fast-xml-parser';

const parser = new XMLParser({
  ignoreAttributes: false,
  attributeNamePrefix: "@_",
  textNodeName: "#text",
  parseAttributeValue: true,
  parseTagValue: true,
  trimValues: true,
  parseTrueNumberOnly: false,
});

const builder = new XMLBuilder({
  ignoreAttributes: false,
  attributeNamePrefix: "@_",
  textNodeName: "#text",
  format: true,
  indentBy: "  ",
});

const API_BASE_URL = process.env.REACT_APP_API_URL || 'https://localhost:9192/movie-service/api';
const OSCAR_SERVICE_BASE_URL = process.env.REACT_APP_OSCAR_SERVICE_URL || 'https://localhost:9292/oscar-service/api';

const apiClient = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/xml',
    'Accept': 'application/xml',
  },
  transformRequest: [(data) => {
    if (data && typeof data === 'object') {
      return builder.build(data);
    }
    return data;
  }],
  transformResponse: [(data) => {
    if (!data || data === '') {
      return {};
    }
    if (typeof data === 'string') {
      try {
        const parsed = parser.parse(data);
        return parsed;
      } catch (e) {
        console.error('XML parsing error:', e);
        return data;
      }
    }
    return data;
  }],
});

// Movie Management Service
export const movieService = {
  // Get all movies with filtering, sorting, and pagination
  getMovies: async (params = {}) => {
    const response = await apiClient.get('/movies', { params });
    return response.data;
  },

  // Get movie by ID
  getMovieById: async (id) => {
    const response = await apiClient.get(`/movies/${id}`);
    return response.data;
  },

  // Create a new movie
  createMovie: async (movieData) => {
    const xmlData = {
      MovieRequest: {
        name: movieData.name,
        coordinates: {
          x: parseFloat(movieData.coordinates.x),
          y: parseFloat(movieData.coordinates.y),
        },
        oscarsCount: parseInt(movieData.oscarsCount),
        goldenPalmCount: parseInt(movieData.goldenPalmCount),
        length: parseInt(movieData.length),
        ...(movieData.genre && { genre: movieData.genre }),
        operator: {
          name: movieData.operator.name,
          ...(movieData.operator.birthday && { birthday: movieData.operator.birthday }),
          ...(movieData.operator.height && { height: parseInt(movieData.operator.height) }),
        },
      },
    };
    const response = await apiClient.post('/movies', xmlData);
    return response.data;
  },

  // Update movie by ID
  updateMovie: async (id, movieData) => {
    const xmlData = {
      MovieRequest: {
        name: movieData.name,
        coordinates: {
          x: parseFloat(movieData.coordinates.x),
          y: parseFloat(movieData.coordinates.y),
        },
        oscarsCount: parseInt(movieData.oscarsCount),
        goldenPalmCount: parseInt(movieData.goldenPalmCount),
        length: parseInt(movieData.length),
        ...(movieData.genre && { genre: movieData.genre }),
        operator: {
          name: movieData.operator.name,
          ...(movieData.operator.birthday && { birthday: movieData.operator.birthday }),
          ...(movieData.operator.height && { height: parseInt(movieData.operator.height) }),
        },
      },
    };
    const response = await apiClient.put(`/movies/${id}`, xmlData);
    return response.data;
  },

  // Delete movie by ID
  deleteMovie: async (id) => {
    const response = await apiClient.delete(`/movies/${id}`);
    return response.data || {};
  },
};

// Movie Statistics Service
export const statisticsService = {
  // Get average movie length
  getAverageLength: async () => {
    const response = await apiClient.get('/movies/average-length');
    return response.data;
  },

  // Count movies by operator
  countByOperator: async (operatorParams) => {
    const response = await apiClient.get('/movies/count-by-operator', {
      params: {
        'operator-name': operatorParams.name,
        ...(operatorParams.birthday && { 'operator-birthday': operatorParams.birthday }),
        ...(operatorParams.height && { 'operator-height': operatorParams.height }),
      },
    });
    return response.data;
  },

  // Filter movies by operator
  filterByOperator: async (operatorParams) => {
    const response = await apiClient.get('/movies/filter-by-operator', {
      params: {
        'operator-name': operatorParams.name,
        ...(operatorParams.birthday && { 'operator-birthday': operatorParams.birthday }),
        ...(operatorParams.height && { 'operator-height': operatorParams.height }),
      },
    });
    return response.data;
  },
};

// Oscar Service API Client
const oscarApiClient = axios.create({
  baseURL: OSCAR_SERVICE_BASE_URL,
  headers: {
    'Content-Type': 'application/xml',
    'Accept': 'application/xml',
  },
  transformRequest: [(data) => {
    if (data && typeof data === 'object') {
      return builder.build(data);
    }
    return data;
  }],
  transformResponse: [(data) => {
    if (!data || data === '') {
      return {};
    }
    if (typeof data === 'string') {
      try {
        const parsed = parser.parse(data);
        return parsed;
      } catch (e) {
        console.error('XML parsing error:', e);
        return data;
      }
    }
    return data;
  }],
});

// Oscar Service
export const oscarService = {
  // Get screenwriters with no Oscar wins
  getLoosers: async () => {
    const response = await oscarApiClient.get('/oscar/screenwriters/get-loosers');
    return response.data;
  },

  // Add Oscars to movies by length
  honorByLength: async (minLength, oscarsToAdd) => {
    const response = await oscarApiClient.patch(
      `/oscar/movies/honor-by-length/${minLength}/oscars-to-add`,
      null,
      {
        params: {
          'oscars-to-add': oscarsToAdd,
        },
      }
    );
    return response.data;
  },
};

// Error handler utility
export const handleApiError = (error) => {
  if (error.response) {
    // Server responded with error
    const errorData = error.response.data;
    if (errorData.errorResponse) {
      return {
        error: errorData.errorResponse.error || 'Error',
        message: errorData.errorResponse.message || 'An error occurred',
        status: errorData.errorResponse.status || error.response.status,
      };
    }
    return {
      error: 'Error',
      message: error.response.statusText || 'An error occurred',
      status: error.response.status,
    };
  } else if (error.request) {
    // Request made but no response
    return {
      error: 'Network Error',
      message: 'Could not connect to the server. Please check your connection.',
      status: 0,
    };
  } else {
    // Something else happened
    return {
      error: 'Error',
      message: error.message || 'An unexpected error occurred',
      status: 0,
    };
  }
};


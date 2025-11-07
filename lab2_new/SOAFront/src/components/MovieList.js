import React, { useState, useEffect } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { movieService, handleApiError } from '../services/api';

const MovieList = () => {
  const [movies, setMovies] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [filters, setFilters] = useState({
    name: '',
    oscarsCount: '',
    goldenPalmCount: '',
    length: '',
    genre: '',
    'operator.name': '',
    'coordinates.x': '',
    'coordinates.y': '',
  });
  const [sortField, setSortField] = useState('');
  const [sortDirection, setSortDirection] = useState('asc');
  const [page, setPage] = useState(0);
  const [pageSize, setPageSize] = useState(20);
  const [pagination, setPagination] = useState({
    totalElements: 0,
    totalPages: 0,
    currentPage: 0,
    pageSize: 20,
  });

  const navigate = useNavigate();

  const fetchMovies = async () => {
    setLoading(true);
    setError(null);
    try {
      const params = {
        page,
        size: pageSize,
        ...Object.fromEntries(
          Object.entries(filters).filter(([_, value]) => value !== '')
        ),
      };

      if (sortField) {
        params.sort = [`${sortField},${sortDirection}`];
      }

      const response = await movieService.getMovies(params);
      const data = response.movieListResponse || response;

      // Handle different XML response structures
      const moviesArray = data.movies?.movie || (Array.isArray(data.movies) ? data.movies : []);
      const moviesList = Array.isArray(moviesArray) ? moviesArray : [moviesArray];

      setMovies(moviesList.filter(m => m !== null && m !== undefined));
      setPagination({
        totalElements: data.totalElements || 0,
        totalPages: data.totalPages || 0,
        currentPage: data.currentPage || page,
        pageSize: data.pageSize || pageSize,
      });
    } catch (err) {
      const errorInfo = handleApiError(err);
      setError(`${errorInfo.error}: ${errorInfo.message}`);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchMovies();
  }, [page, pageSize, sortField, sortDirection]);

  const handleFilterChange = (field, value) => {
    setFilters(prev => ({ ...prev, [field]: value }));
    setPage(0);
  };

  const handleSort = (field) => {
    if (sortField === field) {
      setSortDirection(sortDirection === 'asc' ? 'desc' : 'asc');
    } else {
      setSortField(field);
      setSortDirection('asc');
    }
    setPage(0);
  };

  const handleDelete = async (id) => {
    if (!window.confirm('Are you sure you want to delete this movie?')) {
      return;
    }

    try {
      await movieService.deleteMovie(id);
      fetchMovies();
    } catch (err) {
      const errorInfo = handleApiError(err);
      alert(`Error: ${errorInfo.message}`);
    }
  };

  const handleApplyFilters = () => {
    setPage(0);
    fetchMovies();
  };

  const handleClearFilters = () => {
    setFilters({
      name: '',
      oscarsCount: '',
      goldenPalmCount: '',
      length: '',
      genre: '',
      'operator.name': '',
      'coordinates.x': '',
      'coordinates.y': '',
    });
    setSortField('');
    setSortDirection('asc');
    setPage(0);
  };

  const formatDate = (dateString) => {
    if (!dateString) return 'N/A';
    try {
      return new Date(dateString).toLocaleString();
    } catch {
      return dateString;
    }
  };

  const getSortIndicator = (field) => {
    if (sortField !== field) return '';
    return sortDirection === 'asc' ? '↑' : '↓';
  };

  return (
    <div className="card">
      <div className="card-header">
        <h2 className="card-title">Movies</h2>
        <Link to="/movies/new" className="btn btn-primary">
          Add New Movie
        </Link>
      </div>

      {error && <div className="error-message">{error}</div>}

      <div className="filter-section">
        <h3>Filters</h3>
        <div className="filter-row">
          <div className="form-group">
            <label className="form-label">Name</label>
            <input
              type="text"
              className="form-input"
              value={filters.name}
              onChange={(e) => handleFilterChange('name', e.target.value)}
              placeholder="Filter by name"
            />
          </div>
          <div className="form-group">
            <label className="form-label">Oscars Count</label>
            <input
              type="number"
              className="form-input"
              value={filters.oscarsCount}
              onChange={(e) => handleFilterChange('oscarsCount', e.target.value)}
              placeholder="Exact count"
            />
          </div>
          <div className="form-group">
            <label className="form-label">Golden Palm Count</label>
            <input
              type="number"
              className="form-input"
              value={filters.goldenPalmCount}
              onChange={(e) => handleFilterChange('goldenPalmCount', e.target.value)}
              placeholder="Exact count"
            />
          </div>
          <div className="form-group">
            <label className="form-label">Length (minutes)</label>
            <input
              type="number"
              className="form-input"
              value={filters.length}
              onChange={(e) => handleFilterChange('length', e.target.value)}
              placeholder="Filter by length"
            />
          </div>
          <div className="form-group">
            <label className="form-label">Genre</label>
            <select
              className="form-select"
              value={filters.genre}
              onChange={(e) => handleFilterChange('genre', e.target.value)}
            >
              <option value="">All Genres</option>
              <option value="WESTERN">Western</option>
              <option value="MUSICAL">Musical</option>
              <option value="ADVENTURE">Adventure</option>
              <option value="HORROR">Horror</option>
              <option value="SCIENCE_FICTION">Science Fiction</option>
            </select>
          </div>
          <div className="form-group">
            <label className="form-label">Operator Name</label>
            <input
              type="text"
              className="form-input"
              value={filters['operator.name']}
              onChange={(e) => handleFilterChange('operator.name', e.target.value)}
              placeholder="Filter by operator name"
            />
          </div>
          <div className="form-group">
            <label className="form-label">Coordinate X</label>
            <input
              type="number"
              step="any"
              className="form-input"
              value={filters['coordinates.x']}
              onChange={(e) => handleFilterChange('coordinates.x', e.target.value)}
              placeholder="Filter by X coordinate"
            />
          </div>
          <div className="form-group">
            <label className="form-label">Coordinate Y</label>
            <input
              type="number"
              step="any"
              className="form-input"
              value={filters['coordinates.y']}
              onChange={(e) => handleFilterChange('coordinates.y', e.target.value)}
              placeholder="Filter by Y coordinate"
            />
          </div>
        </div>
        <div style={{ display: 'flex', gap: '1rem' }}>
          <button className="btn btn-primary" onClick={handleApplyFilters}>
            Apply Filters
          </button>
          <button className="btn btn-secondary" onClick={handleClearFilters}>
            Clear Filters
          </button>
        </div>
      </div>

      <div className="form-group">
        <label className="form-label">Page Size</label>
        <select
          className="form-select"
          value={pageSize}
          onChange={(e) => {
            setPageSize(parseInt(e.target.value));
            setPage(0);
          }}
          style={{ width: '200px' }}
        >
          <option value="10">10 per page</option>
          <option value="20">20 per page</option>
          <option value="50">50 per page</option>
          <option value="100">100 per page</option>
        </select>
      </div>

      {loading ? (
        <div className="loading">Loading movies...</div>
      ) : movies.length === 0 ? (
        <div className="empty-state">
          <h3>No movies found</h3>
          <p>Try adjusting your filters or add a new movie.</p>
        </div>
      ) : (
        <>
          <table className="table">
            <thead>
              <tr>
                <th onClick={() => handleSort('id')}>
                  ID {getSortIndicator('id')}
                </th>
                <th onClick={() => handleSort('name')}>
                  Name {getSortIndicator('name')}
                </th>
                <th onClick={() => handleSort('length')}>
                  Length {getSortIndicator('length')}
                </th>
                <th onClick={() => handleSort('oscarsCount')}>
                  Oscars {getSortIndicator('oscarsCount')}
                </th>
                <th onClick={() => handleSort('goldenPalmCount')}>
                  Golden Palms {getSortIndicator('goldenPalmCount')}
                </th>
                <th>Genre</th>
                <th>Operator</th>
                <th>Created</th>
                <th>Actions</th>
              </tr>
            </thead>
            <tbody>
              {movies.map((movie) => (
                <tr key={movie.id}>
                  <td>{movie.id}</td>
                  <td>{movie.name}</td>
                  <td>{movie.length} min</td>
                  <td>{movie.oscarsCount}</td>
                  <td>{movie.goldenPalmCount}</td>
                  <td>{movie.genre || 'N/A'}</td>
                  <td>
                    {movie.operator?.name || 'N/A'}
                    {movie.operator?.height && ` (${movie.operator.height} cm)`}
                  </td>
                  <td>{formatDate(movie.creationDate)}</td>
                  <td>
                    <button
                      className="btn btn-small btn-secondary"
                      onClick={() => navigate(`/movies/edit/${movie.id}`)}
                      style={{ marginRight: '0.5rem' }}
                    >
                      Edit
                    </button>
                    <button
                      className="btn btn-small btn-danger"
                      onClick={() => handleDelete(movie.id)}
                    >
                      Delete
                    </button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>

          <div className="pagination">
            <button
              onClick={() => setPage(p => Math.max(0, p - 1))}
              disabled={page === 0}
            >
              Previous
            </button>
            <span className="pagination-info">
              Page {pagination.currentPage + 1} of {pagination.totalPages || 1} 
              ({pagination.totalElements} total)
            </span>
            <button
              onClick={() => setPage(p => p + 1)}
              disabled={page >= (pagination.totalPages - 1)}
            >
              Next
            </button>
          </div>
        </>
      )}
    </div>
  );
};

export default MovieList;


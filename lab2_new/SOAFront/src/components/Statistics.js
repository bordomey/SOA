import React, { useState } from 'react';
import { statisticsService, handleApiError } from '../services/api';

const Statistics = () => {
  const [averageLength, setAverageLength] = useState(null);
  const [countResult, setCountResult] = useState(null);
  const [filteredMovies, setFilteredMovies] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const [operatorFilter, setOperatorFilter] = useState({
    name: '',
    birthday: '',
    height: '',
  });

  const fetchAverageLength = async () => {
    setLoading(true);
    setError(null);
    try {
      const response = await statisticsService.getAverageLength();
      const data = response.averageResponse || response;
      setAverageLength(data.averageLength || data.average || 0);
    } catch (err) {
      const errorInfo = handleApiError(err);
      setError(`${errorInfo.error}: ${errorInfo.message}`);
    } finally {
      setLoading(false);
    }
  };

  const fetchCountByOperator = async () => {
    if (!operatorFilter.name) {
      alert('Operator name is required');
      return;
    }

    setLoading(true);
    setError(null);
    try {
      const response = await statisticsService.countByOperator(operatorFilter);
      const data = response.countResponse || response;
      setCountResult(data.count || 0);
    } catch (err) {
      const errorInfo = handleApiError(err);
      setError(`${errorInfo.error}: ${errorInfo.message}`);
    } finally {
      setLoading(false);
    }
  };

  const fetchFilteredMovies = async () => {
    if (!operatorFilter.name) {
      alert('Operator name is required');
      return;
    }

    setLoading(true);
    setError(null);
    try {
      const response = await statisticsService.filterByOperator(operatorFilter);
      const data = response.movieListResponse || response;
      const moviesArray = data.movies?.movie || (Array.isArray(data.movies) ? data.movies : []);
      const moviesList = Array.isArray(moviesArray) ? moviesArray : [moviesArray];
      setFilteredMovies(moviesList.filter(m => m !== null && m !== undefined));
    } catch (err) {
      const errorInfo = handleApiError(err);
      setError(`${errorInfo.error}: ${errorInfo.message}`);
    } finally {
      setLoading(false);
    }
  };

  const handleOperatorFilterChange = (field, value) => {
    setOperatorFilter(prev => ({ ...prev, [field]: value }));
  };

  return (
    <div>
      <div className="card">
        <div className="card-header">
          <h2 className="card-title">Movie Statistics</h2>
        </div>

        {error && <div className="error-message">{error}</div>}

        <div style={{ marginBottom: '2rem' }}>
          <h3>Average Movie Length</h3>
          <p style={{ marginBottom: '1rem', color: '#666' }}>
            Calculate the average length of all movies in the collection.
          </p>
          <button
            className="btn btn-primary"
            onClick={fetchAverageLength}
            disabled={loading}
          >
            Calculate Average Length
          </button>
          {averageLength !== null && (
            <div style={{ marginTop: '1rem', padding: '1rem', background: '#f8f9fa', borderRadius: '6px' }}>
              <strong>Average Length:</strong> {averageLength.toFixed(2)} minutes
            </div>
          )}
        </div>

        <div style={{ borderTop: '2px solid #f0f0f0', paddingTop: '2rem' }}>
          <h3>Count Movies by Operator</h3>
          <p style={{ marginBottom: '1rem', color: '#666' }}>
            Count movies where the operator is greater than the specified operator.
          </p>
          <div className="form-row">
            <div className="form-group">
              <label className="form-label">Operator Name *</label>
              <input
                type="text"
                className="form-input"
                value={operatorFilter.name}
                onChange={(e) => handleOperatorFilterChange('name', e.target.value)}
                placeholder="Operator name"
              />
            </div>
            <div className="form-group">
              <label className="form-label">Birthday</label>
              <input
                type="date"
                className="form-input"
                value={operatorFilter.birthday}
                onChange={(e) => handleOperatorFilterChange('birthday', e.target.value)}
              />
            </div>
            <div className="form-group">
              <label className="form-label">Height (cm)</label>
              <input
                type="number"
                className="form-input"
                value={operatorFilter.height}
                onChange={(e) => handleOperatorFilterChange('height', e.target.value)}
                min="1"
                placeholder="Height in centimeters"
              />
            </div>
          </div>
          <button
            className="btn btn-primary"
            onClick={fetchCountByOperator}
            disabled={loading}
          >
            Count Movies
          </button>
          {countResult !== null && (
            <div style={{ marginTop: '1rem', padding: '1rem', background: '#f8f9fa', borderRadius: '6px' }}>
              <strong>Count:</strong> {countResult} movie(s)
            </div>
          )}
        </div>

        <div style={{ borderTop: '2px solid #f0f0f0', paddingTop: '2rem', marginTop: '2rem' }}>
          <h3>Filter Movies by Operator</h3>
          <p style={{ marginBottom: '1rem', color: '#666' }}>
            Get all movies where the operator is greater than the specified operator.
          </p>
          <button
            className="btn btn-primary"
            onClick={fetchFilteredMovies}
            disabled={loading}
          >
            Filter Movies
          </button>
          {filteredMovies.length > 0 && (
            <div style={{ marginTop: '1rem' }}>
              <h4>Filtered Movies ({filteredMovies.length})</h4>
              <table className="table">
                <thead>
                  <tr>
                    <th>ID</th>
                    <th>Name</th>
                    <th>Length</th>
                    <th>Oscars</th>
                    <th>Golden Palms</th>
                    <th>Genre</th>
                    <th>Operator</th>
                  </tr>
                </thead>
                <tbody>
                  {filteredMovies.map((movie) => (
                    <tr key={movie.id}>
                      <td>{movie.id}</td>
                      <td>{movie.name}</td>
                      <td>{movie.length} min</td>
                      <td>{movie.oscarsCount}</td>
                      <td>{movie.goldenPalmCount}</td>
                      <td>{movie.genre || 'N/A'}</td>
                      <td>{movie.operator?.name || 'N/A'}</td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          )}
        </div>

        {loading && (
          <div className="loading" style={{ marginTop: '1rem' }}>
            Loading...
          </div>
        )}
      </div>
    </div>
  );
};

export default Statistics;


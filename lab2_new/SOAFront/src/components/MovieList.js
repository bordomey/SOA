import React, { useState, useEffect } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { movieService, handleApiError } from '../services/api';

const SORTABLE_FIELDS = [
  { value: '', label: 'No sorting' },
  { value: 'id', label: 'ID' },
  { value: 'name', label: 'Name' },
  { value: 'length', label: 'Length' },
  { value: 'oscarsCount', label: 'Oscars Count' },
  { value: 'goldenPalmCount', label: 'Golden Palm Count' },
  { value: 'genre', label: 'Genre' },
  { value: 'creationDate', label: 'Creation Date' },
  { value: 'operator.name', label: 'Operator Name' },
  { value: 'operator.height', label: 'Operator Height' },
  { value: 'coordinates.x', label: 'Coordinate X' },
  { value: 'coordinates.y', label: 'Coordinate Y' },
];

const SORT_DIRECTIONS = [
  { value: 'asc', label: 'Ascending (A → Z / smallest first)' },
  { value: 'desc', label: 'Descending (Z → A / largest first)' },
];

const GENRE_OPTIONS = [
  { value: 'WESTERN', label: 'Western' },
  { value: 'MUSICAL', label: 'Musical' },
  { value: 'ADVENTURE', label: 'Adventure' },
  { value: 'HORROR', label: 'Horror' },
  { value: 'SCIENCE_FICTION', label: 'Science Fiction' },
];

const FILTERABLE_FIELDS = [
  { value: 'id', label: 'ID', type: 'number', allowsRange: true, step: 1 },
  { value: 'name', label: 'Name', type: 'string', allowsRange: false },
  { value: 'length', label: 'Length (minutes)', type: 'number', allowsRange: true, step: 1 },
  { value: 'oscarsCount', label: 'Oscars Count', type: 'number', allowsRange: true, step: 1 },
  { value: 'goldenPalmCount', label: 'Golden Palm Count', type: 'number', allowsRange: true, step: 1 },
  { value: 'genre', label: 'Genre', type: 'enum', allowsRange: false, options: GENRE_OPTIONS },
  { value: 'operator.name', label: 'Operator Name', type: 'string', allowsRange: false },
  { value: 'coordinates.x', label: 'Coordinate X', type: 'number', allowsRange: true, step: 'any' },
  { value: 'coordinates.y', label: 'Coordinate Y', type: 'number', allowsRange: true, step: 'any' },
];

const generateRuleId = () => `${Date.now()}-${Math.random().toString(16).slice(2)}`;

const findFieldMeta = (field) => FILTERABLE_FIELDS.find((f) => f.value === field);

const createFilterRule = () => {
  const defaultField = FILTERABLE_FIELDS[0]?.value || '';
  return {
    id: generateRuleId(),
    field: defaultField,
    mode: 'exact',
    value: '',
    range: {
      bounds: '[]',
      start: '',
      end: '',
    },
  };
};

const MovieList = () => {
  const [movies, setMovies] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [filterRules, setFilterRules] = useState([createFilterRule()]);
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
      const filterParams = filterRules.reduce((acc, rule) => {
        if (!rule.field) {
          return acc;
        }

        if (rule.mode === 'range') {
          const { start, end, bounds } = rule.range;
          if (start === '' || end === '') {
            return acc;
          }
          acc[rule.field] = `${bounds[0]}${start},${end}${bounds[1]}`;
        } else if (rule.value !== '' && rule.value !== null && rule.value !== undefined) {
          acc[rule.field] = rule.value;
        }
        return acc;
      }, {});

      const params = {
        page,
        size: pageSize,
        ...filterParams,
      };

      if (sortField) {
        params.sort = [`${sortField},${sortDirection}`];
      }

      const response = await movieService.getMovies(params);
      const data =  response;

      // Handle different XML response structures
      const rawMovies =
        data.movies?.movie ??
        data.movies ??
        data.movie ??
        data.movieListResponse?.movies?.movie ??
        data.movieListResponse?.movies ??
        data.movieListResponse?.movie ??
        [];

      const moviesList = Array.isArray(rawMovies)
        ? rawMovies
        : rawMovies
        ? [rawMovies]
        : [];

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

  const handleFieldChange = (id, newField) => {
    setFilterRules(prevRules =>
      prevRules.map(rule => {
        if (rule.id !== id) return rule;
        const meta = findFieldMeta(newField);
        const allowsRange = meta?.allowsRange ?? false;
        return {
          ...rule,
          field: newField,
          mode: allowsRange ? rule.mode : 'exact',
          value: '',
          range: { bounds: '[]', start: '', end: '' },
        };
      })
    );
    setPage(0);
  };

  const handleModeChange = (id, newMode) => {
    setFilterRules(prevRules =>
      prevRules.map(rule => {
        if (rule.id !== id) return rule;
        const meta = findFieldMeta(rule.field);
        if (newMode === 'range' && !meta?.allowsRange) {
          return rule;
        }
        return {
          ...rule,
          mode: newMode,
        };
      })
    );
    setPage(0);
  };

  const handleExactValueChange = (id, value) => {
    setFilterRules(prevRules =>
      prevRules.map(rule =>
        rule.id === id
          ? {
              ...rule,
              value,
            }
          : rule
      )
    );
    setPage(0);
  };

  const handleRangeValueChange = (id, key, value) => {
    setFilterRules(prevRules =>
      prevRules.map(rule =>
        rule.id === id
          ? {
              ...rule,
              range: {
                ...rule.range,
                [key]: value,
              },
            }
          : rule
      )
    );
    setPage(0);
  };

  const addFilterRule = () => {
    setFilterRules(prev => [...prev, createFilterRule()]);
    setPage(0);
  };

  const removeFilterRule = (id) => {
    setFilterRules(prev => prev.filter(rule => rule.id !== id));
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

  const handleSortFieldSelect = (field) => {
    setSortField(field);
    setPage(0);
  };

  const handleSortDirectionSelect = (direction) => {
    setSortDirection(direction);
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
    setFilterRules([createFilterRule()]);
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
        <p className="helper-text">
          Build custom filters by field, choose exact values or specify ranges like <code>[10,20)</code>.
          You can add multiple filters for the same field if needed.
        </p>

        {filterRules.length === 0 ? (
          <div className="empty-state" style={{ padding: '1rem', marginBottom: '1rem' }}>
            <strong>No filters added.</strong> Click &quot;Add Filter&quot; to begin.
          </div>
        ) : (
          filterRules.map((rule, index) => {
            const fieldMeta = findFieldMeta(rule.field);
            const isRangeMode = rule.mode === 'range';
            const rangeDisabled = !fieldMeta?.allowsRange;
            const numberStep = fieldMeta?.step ?? 1;
            return (
              <div className="card" key={rule.id} style={{ marginBottom: '1rem' }}>
                <div className="card-header" style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                  <span>Filter {index + 1}</span>
                  <button
                    type="button"
                    className="btn btn-small btn-danger"
                    onClick={() => removeFilterRule(rule.id)}
                  >
                    Remove
                  </button>
                </div>
                <div className="card-body">
                  <div className="filter-row" style={{ flexWrap: 'wrap' }}>
                    <div className="form-group">
                      <label className="form-label">Field</label>
                      <select
                        className="form-select"
                        value={rule.field}
                        onChange={(e) => handleFieldChange(rule.id, e.target.value)}
                      >
                        {FILTERABLE_FIELDS.map((field) => (
                          <option key={field.value} value={field.value}>
                            {field.label}
                          </option>
                        ))}
                      </select>
                    </div>

                    <div className="form-group">
                      <label className="form-label">Filter Type</label>
                      <select
                        className="form-select"
                        value={rule.mode}
                        onChange={(e) => handleModeChange(rule.id, e.target.value)}
                      >
                        <option value="exact">Exact</option>
                        <option value="range" disabled={rangeDisabled}>
                          Range
                        </option>
                      </select>
                    </div>

                    {isRangeMode && fieldMeta?.allowsRange ? (
                      <>
                        <div className="form-group">
                          <label className="form-label">Bounds</label>
                          <select
                            className="form-select"
                            value={rule.range.bounds}
                            onChange={(e) => handleRangeValueChange(rule.id, 'bounds', e.target.value)}
                          >
                            <option value="[]">[ min, max ]</option>
                            <option value="()"> ( min, max )</option>
                            <option value="[)">[ min, max )</option>
                            <option value="(]">( min, max ]</option>
                          </select>
                        </div>
                        <div className="form-group">
                          <label className="form-label">Min</label>
                          <input
                            type="number"
                            step={numberStep}
                            className="form-input"
                            value={rule.range.start}
                            onChange={(e) => handleRangeValueChange(rule.id, 'start', e.target.value)}
                            placeholder="Min value"
                          />
                        </div>
                        <div className="form-group">
                          <label className="form-label">Max</label>
                          <input
                            type="number"
                            step={numberStep}
                            className="form-input"
                            value={rule.range.end}
                            onChange={(e) => handleRangeValueChange(rule.id, 'end', e.target.value)}
                            placeholder="Max value"
                          />
                        </div>
                      </>
                    ) : fieldMeta?.type === 'enum' ? (
                      <div className="form-group">
                        <label className="form-label">Value</label>
                        <select
                          className="form-select"
                          value={rule.value}
                          onChange={(e) => handleExactValueChange(rule.id, e.target.value)}
                        >
                          <option value="">Any</option>
                          {fieldMeta.options?.map((option) => (
                            <option key={option.value} value={option.value}>
                              {option.label}
                            </option>
                          ))}
                        </select>
                      </div>
                    ) : (
                      <div className="form-group" style={{ flex: '1 1 200px' }}>
                        <label className="form-label">Value</label>
                        <input
                          type={fieldMeta?.type === 'number' ? 'number' : 'text'}
                          step={fieldMeta?.type === 'number' ? fieldMeta?.step || 1 : undefined}
                          className="form-input"
                          value={rule.value}
                          onChange={(e) => handleExactValueChange(rule.id, e.target.value)}
                          placeholder={`Enter ${fieldMeta?.label?.toLowerCase() || 'value'}`}
                        />
                      </div>
                    )}
                  </div>
                </div>
              </div>
            );
          })
        )}

        <div style={{ marginBottom: '1rem' }}>
          <button type="button" className="btn btn-secondary" onClick={addFilterRule}>
            Add Filter
          </button>
        </div>

        <div style={{ display: 'flex', gap: '1rem', flexWrap: 'wrap' }}>
          <button className="btn btn-primary" onClick={handleApplyFilters}>
            Apply Filters
          </button>
          <button className="btn btn-secondary" onClick={handleClearFilters}>
            Clear Filters
          </button>
        </div>

        <div className="sort-section">
          <h3>Sorting</h3>
          <div className="filter-row">
            <div className="form-group">
              <label className="form-label">Sort Field</label>
              <select
                className="form-select"
                value={sortField}
                onChange={(e) => handleSortFieldSelect(e.target.value)}
              >
                {SORTABLE_FIELDS.map((option) => (
                  <option key={option.value || 'none'} value={option.value}>
                    {option.label}
                  </option>
                ))}
              </select>
            </div>
            <div className="form-group">
              <label className="form-label">Direction</label>
              <select
                className="form-select"
                value={sortDirection}
                onChange={(e) => handleSortDirectionSelect(e.target.value)}
                disabled={!sortField}
              >
                {SORT_DIRECTIONS.map((option) => (
                  <option key={option.value} value={option.value}>
                    {option.label}
                  </option>
                ))}
              </select>
            </div>
          </div>
          <p className="helper-text">
            Matches the `sort` query parameter described in the API specification
            (format `field,direction`).
          </p>
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


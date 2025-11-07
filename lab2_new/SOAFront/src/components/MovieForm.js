import React, { useState, useEffect } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { movieService, handleApiError } from '../services/api';

const MovieForm = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  const isEdit = !!id;

  const [formData, setFormData] = useState({
    name: '',
    coordinates: {
      x: '',
      y: '',
    },
    oscarsCount: '',
    goldenPalmCount: '',
    length: '',
    genre: '',
    operator: {
      name: '',
      birthday: '',
      height: '',
    },
  });

  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [success, setSuccess] = useState(false);

  useEffect(() => {
    if (isEdit) {
      fetchMovie();
    }
  }, [id]);

  const fetchMovie = async () => {
    setLoading(true);
    setError(null);
    try {
      const response = await movieService.getMovieById(id);
      const movie = response.movie || response;
      
      setFormData({
        name: movie.name || '',
        coordinates: {
          x: movie.coordinates?.x || '',
          y: movie.coordinates?.y || '',
        },
        oscarsCount: movie.oscarsCount || '',
        goldenPalmCount: movie.goldenPalmCount || '',
        length: movie.length || '',
        genre: movie.genre || '',
        operator: {
          name: movie.operator?.name || '',
          birthday: movie.operator?.birthday || '',
          height: movie.operator?.height || '',
        },
      });
    } catch (err) {
      const errorInfo = handleApiError(err);
      setError(`${errorInfo.error}: ${errorInfo.message}`);
    } finally {
      setLoading(false);
    }
  };

  const handleChange = (field, value) => {
    if (field.includes('.')) {
      const [parent, child] = field.split('.');
      setFormData(prev => ({
        ...prev,
        [parent]: {
          ...prev[parent],
          [child]: value,
        },
      }));
    } else {
      setFormData(prev => ({
        ...prev,
        [field]: value,
      }));
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError(null);
    setSuccess(false);

    try {
      if (isEdit) {
        await movieService.updateMovie(id, formData);
        setSuccess(true);
        setTimeout(() => {
          navigate('/');
        }, 2000);
      } else {
        await movieService.createMovie(formData);
        setSuccess(true);
        setTimeout(() => {
          navigate('/');
        }, 2000);
      }
    } catch (err) {
      const errorInfo = handleApiError(err);
      setError(`${errorInfo.error}: ${errorInfo.message}`);
    } finally {
      setLoading(false);
    }
  };

  if (loading && isEdit && !formData.name) {
    return <div className="card"><div className="loading">Loading movie data...</div></div>;
  }

  return (
    <div className="card">
      <div className="card-header">
        <h2 className="card-title">{isEdit ? 'Edit Movie' : 'Create New Movie'}</h2>
      </div>

      {error && <div className="error-message">{error}</div>}
      {success && (
        <div className="success-message">
          Movie {isEdit ? 'updated' : 'created'} successfully! Redirecting...
        </div>
      )}

      <form onSubmit={handleSubmit}>
        <div className="form-group">
          <label className="form-label">Movie Name *</label>
          <input
            type="text"
            className="form-input"
            value={formData.name}
            onChange={(e) => handleChange('name', e.target.value)}
            required
            placeholder="Enter movie name"
          />
        </div>

        <div className="form-row">
          <div className="form-group">
            <label className="form-label">Coordinate X *</label>
            <input
              type="number"
              step="any"
              className="form-input"
              value={formData.coordinates.x}
              onChange={(e) => handleChange('coordinates.x', e.target.value)}
              required
              placeholder="X coordinate"
            />
          </div>
          <div className="form-group">
            <label className="form-label">Coordinate Y * (must be > -26)</label>
            <input
              type="number"
              step="any"
              className="form-input"
              value={formData.coordinates.y}
              onChange={(e) => handleChange('coordinates.y', e.target.value)}
              required
              min="-25.999999"
              placeholder="Y coordinate"
            />
          </div>
        </div>

        <div className="form-row">
          <div className="form-group">
            <label className="form-label">Oscars Count * (min: 1)</label>
            <input
              type="number"
              className="form-input"
              value={formData.oscarsCount}
              onChange={(e) => handleChange('oscarsCount', e.target.value)}
              required
              min="1"
              placeholder="Number of Oscars"
            />
          </div>
          <div className="form-group">
            <label className="form-label">Golden Palm Count * (min: 1)</label>
            <input
              type="number"
              className="form-input"
              value={formData.goldenPalmCount}
              onChange={(e) => handleChange('goldenPalmCount', e.target.value)}
              required
              min="1"
              placeholder="Number of Golden Palms"
            />
          </div>
        </div>

        <div className="form-row">
          <div className="form-group">
            <label className="form-label">Length (minutes) * (min: 1)</label>
            <input
              type="number"
              className="form-input"
              value={formData.length}
              onChange={(e) => handleChange('length', e.target.value)}
              required
              min="1"
              placeholder="Movie length in minutes"
            />
          </div>
          <div className="form-group">
            <label className="form-label">Genre</label>
            <select
              className="form-select"
              value={formData.genre}
              onChange={(e) => handleChange('genre', e.target.value)}
            >
              <option value="">Select Genre</option>
              <option value="WESTERN">Western</option>
              <option value="MUSICAL">Musical</option>
              <option value="ADVENTURE">Adventure</option>
              <option value="HORROR">Horror</option>
              <option value="SCIENCE_FICTION">Science Fiction</option>
            </select>
          </div>
        </div>

        <h3 style={{ marginTop: '2rem', marginBottom: '1rem' }}>Operator Information</h3>

        <div className="form-group">
          <label className="form-label">Operator Name *</label>
          <input
            type="text"
            className="form-input"
            value={formData.operator.name}
            onChange={(e) => handleChange('operator.name', e.target.value)}
            required
            placeholder="Enter operator name"
          />
        </div>

        <div className="form-row">
          <div className="form-group">
            <label className="form-label">Birthday</label>
            <input
              type="date"
              className="form-input"
              value={formData.operator.birthday}
              onChange={(e) => handleChange('operator.birthday', e.target.value)}
              placeholder="Operator birthday"
            />
          </div>
          <div className="form-group">
            <label className="form-label">Height (cm)</label>
            <input
              type="number"
              className="form-input"
              value={formData.operator.height}
              onChange={(e) => handleChange('operator.height', e.target.value)}
              min="1"
              placeholder="Height in centimeters"
            />
          </div>
        </div>

        <div style={{ display: 'flex', gap: '1rem', marginTop: '2rem' }}>
          <button
            type="submit"
            className="btn btn-primary"
            disabled={loading}
          >
            {loading ? 'Saving...' : (isEdit ? 'Update Movie' : 'Create Movie')}
          </button>
          <button
            type="button"
            className="btn btn-secondary"
            onClick={() => navigate('/')}
          >
            Cancel
          </button>
        </div>
      </form>
    </div>
  );
};

export default MovieForm;


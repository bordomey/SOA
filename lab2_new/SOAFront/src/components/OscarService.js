import React, { useState } from 'react';
import { oscarService, handleApiError } from '../services/api';

const OscarService = () => {
  const [screenwriters, setScreenwriters] = useState([]);
  const [honorResult, setHonorResult] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const [honorParams, setHonorParams] = useState({
    minLength: '',
    oscarsToAdd: '',
  });

  const fetchLoosers = async () => {
    setLoading(true);
    setError(null);
    try {
      const response = await oscarService.getLoosers();
      const data = response.screenwritersResponse || response;
      const writersArray = data.screenwriters?.screenwriter || 
                          (Array.isArray(data.screenwriters) ? data.screenwriters : []);
      const writersList = Array.isArray(writersArray) ? writersArray : [writersArray];
      setScreenwriters(writersList.filter(w => w !== null && w !== undefined));
    } catch (err) {
      const errorInfo = handleApiError(err);
      setError(`${errorInfo.error}: ${errorInfo.message}`);
    } finally {
      setLoading(false);
    }
  };

  const handleHonorByLength = async () => {
    if (!honorParams.minLength || !honorParams.oscarsToAdd) {
      alert('Please provide both minimum length and number of Oscars to add');
      return;
    }

    setLoading(true);
    setError(null);
    try {
      const response = await oscarService.honorByLength(
        parseInt(honorParams.minLength),
        parseInt(honorParams.oscarsToAdd)
      );
      const data = response.oscarUpdateResponse || response;
      setHonorResult({
        updatedCount: data.updatedMoviesCount || 0,
        message: data.message || 'Oscars added successfully',
      });
    } catch (err) {
      const errorInfo = handleApiError(err);
      setError(`${errorInfo.error}: ${errorInfo.message}`);
    } finally {
      setLoading(false);
    }
  };

  const formatDate = (dateString) => {
    if (!dateString) return 'N/A';
    try {
      return new Date(dateString).toLocaleDateString();
    } catch {
      return dateString;
    }
  };

  return (
    <div>
      <div className="card">
        <div className="card-header">
          <h2 className="card-title">Oscar Service</h2>
        </div>

        {error && <div className="error-message">{error}</div>}

        <div style={{ marginBottom: '2rem' }}>
          <h3>Screenwriters with No Oscar Wins</h3>
          <p style={{ marginBottom: '1rem', color: '#666' }}>
            Retrieve a list of screenwriters whose movies have never won any Oscar awards.
          </p>
          <button
            className="btn btn-primary"
            onClick={fetchLoosers}
            disabled={loading}
          >
            Get Screenwriters
          </button>
          {screenwriters.length > 0 && (
            <div style={{ marginTop: '1rem' }}>
              <h4>Screenwriters ({screenwriters.length})</h4>
              <table className="table">
                <thead>
                  <tr>
                    <th>Name</th>
                    <th>Birthday</th>
                    <th>Height (cm)</th>
                  </tr>
                </thead>
                <tbody>
                  {screenwriters.map((writer, index) => (
                    <tr key={index}>
                      <td>{writer.name || 'N/A'}</td>
                      <td>{formatDate(writer.birthday)}</td>
                      <td>{writer.height || 'N/A'}</td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          )}
          {screenwriters.length === 0 && !loading && (
            <div className="empty-state" style={{ marginTop: '1rem' }}>
              <p>No screenwriters found. Click the button above to fetch data.</p>
            </div>
          )}
        </div>

        <div style={{ borderTop: '2px solid #f0f0f0', paddingTop: '2rem', marginTop: '2rem' }}>
          <h3>Add Oscars to Movies by Length</h3>
          <p style={{ marginBottom: '1rem', color: '#666' }}>
            Add specified number of Oscar awards to all movies with length greater than the minimum specified.
          </p>
          <div className="form-row">
            <div className="form-group">
              <label className="form-label">Minimum Length (minutes) *</label>
              <input
                type="number"
                className="form-input"
                value={honorParams.minLength}
                onChange={(e) => setHonorParams(prev => ({ ...prev, minLength: e.target.value }))}
                min="1"
                placeholder="Minimum length threshold"
                required
              />
            </div>
            <div className="form-group">
              <label className="form-label">Oscars to Add *</label>
              <input
                type="number"
                className="form-input"
                value={honorParams.oscarsToAdd}
                onChange={(e) => setHonorParams(prev => ({ ...prev, oscarsToAdd: e.target.value }))}
                min="1"
                placeholder="Number of Oscars to add"
                required
              />
            </div>
          </div>
          <button
            className="btn btn-success"
            onClick={handleHonorByLength}
            disabled={loading}
          >
            Add Oscars
          </button>
          {honorResult && (
            <div style={{ marginTop: '1rem', padding: '1rem', background: '#f8f9fa', borderRadius: '6px' }}>
              <div className="success-message">
                <strong>{honorResult.message}</strong>
              </div>
              <p style={{ marginTop: '0.5rem' }}>
                <strong>Updated Movies:</strong> {honorResult.updatedCount}
              </p>
            </div>
          )}
        </div>

        {loading && (
          <div className="loading" style={{ marginTop: '1rem' }}>
            Processing...
          </div>
        )}
      </div>
    </div>
  );
};

export default OscarService;


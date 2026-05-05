import { useState } from 'react';
import API from '../api/axios';

export default function Reports() {
  const [reportType, setReportType] = useState('revenue');
  const [startDate, setStartDate] = useState('');
  const [endDate, setEndDate] = useState('');
  const [report, setReport] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  const fetchReport = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError('');
    setReport(null);
    try {
      let data;
      if (reportType === 'usage') {
        const res = await API.get('/reports/usage');
        data = res.data;
      } else {
        const res = await API.get(`/reports/${reportType}`, {
          params: { startDate, endDate }
        });
        data = res.data;
      }
      setReport(data);
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to generate report');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="page">
      <h1>Reports</h1>
      <p className="page-subtitle">Generate revenue, booking, and fleet usage reports</p>

      {error && <div className="alert alert-error">{error}</div>}

      <div className="card" style={{ marginBottom: '20px' }}>
        <h3>Generate Report</h3>
        <form onSubmit={fetchReport}>
          <div className="form-row">
            <div className="form-group">
              <label>Report Type</label>
              <select value={reportType} onChange={(e) => setReportType(e.target.value)}>
                <option value="revenue">Revenue Report</option>
                <option value="bookings">Bookings Report</option>
                <option value="usage">Car Usage Report</option>
              </select>
            </div>
            {reportType !== 'usage' && (
              <>
                <div className="form-group">
                  <label>Start Date</label>
                  <input type="date" value={startDate} onChange={(e) => setStartDate(e.target.value)} required />
                </div>
                <div className="form-group">
                  <label>End Date</label>
                  <input type="date" value={endDate} onChange={(e) => setEndDate(e.target.value)} required />
                </div>
              </>
            )}
          </div>
          <button type="submit" className="btn btn-primary" disabled={loading} style={{ marginTop: '12px' }}>
            {loading ? 'Generating...' : 'Generate'}
          </button>
        </form>
      </div>

      {report && (
        <div className="stats-row">
          {Object.entries(report).map(([key, value]) => (
            <div key={key} className="stat-card">
              <div className="stat-label">
                {key.replace(/([A-Z])/g, ' $1').replace(/^./, s => s.toUpperCase())}
              </div>
              <div className="stat-value">
                {typeof value === 'number' && key.toLowerCase().includes('revenue')
                  ? `₹${value.toLocaleString()}`
                  : String(value)}
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
}

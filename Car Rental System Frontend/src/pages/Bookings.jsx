import { useState, useEffect } from 'react';
import API from '../api/axios';

const STATUS_FLOW = ['CREATED', 'PICKED_UP', 'COMPLETED'];

function BookingStatusFlow({ current }) {
  return (
    <div className="status-flow">
      {STATUS_FLOW.map((step, i) => {
        const currentIdx = STATUS_FLOW.indexOf(current);
        const isCancelled = current === 'CANCELLED';
        let cls = 'status-step';
        if (isCancelled) {
          cls += step === 'CREATED' ? ' done' : '';
        } else if (i < currentIdx) {
          cls += ' done';
        } else if (i === currentIdx) {
          cls += ' current';
        }
        return (
          <span key={step}>
            <span className={cls}>{step.replace('_', ' ')}</span>
            {i < STATUS_FLOW.length - 1 && <span style={{ color: 'var(--text-muted)', margin: '0 2px' }}>→</span>}
          </span>
        );
      })}
      {current === 'CANCELLED' && (
        <span className="status-step" style={{ background: 'var(--error-muted)', color: 'var(--danger)' }}>CANCELLED</span>
      )}
    </div>
  );
}

export default function Bookings() {
  const [bookings, setBookings] = useState([]);
  const [loading, setLoading] = useState(true);
  const [message, setMessage] = useState({ text: '', type: '' });

  const fetchBookings = async () => {
    setLoading(true);
    try {
      const { data } = await API.get('/bookings/me');
      setBookings(data);
    } catch {
      setBookings([]);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => { fetchBookings(); }, []);

  const handleCancel = async (bookingId) => {
    if (!confirm('Are you sure you want to cancel this booking?')) return;
    try {
      await API.delete(`/bookings/${bookingId}`);
      setMessage({ text: 'Booking cancelled successfully', type: 'success' });
      fetchBookings();
    } catch (err) {
      setMessage({ text: err.response?.data?.message || 'Failed to cancel booking', type: 'error' });
    }
  };

  return (
    <div className="page">
      <h1>My Bookings</h1>
      <p className="page-subtitle">Track your booking requests and rental status</p>

      {message.text && (
        <div className={`alert alert-${message.type === 'error' ? 'error' : 'success'}`}>{message.text}</div>
      )}

      {loading ? (
        <div className="loading">Loading bookings...</div>
      ) : bookings.length === 0 ? (
        <div className="empty-state">
          <div className="empty-icon">📋</div>
          <p>No bookings yet. Browse available cars to make a booking request.</p>
        </div>
      ) : (
        <div className="table-wrapper">
          <table>
            <thead>
              <tr>
                <th>ID</th>
                <th>Car</th>
                <th>Dates</th>
                <th>Status Flow</th>
                <th>Action</th>
              </tr>
            </thead>
            <tbody>
              {bookings.map((b) => (
                <tr key={b.id}>
                  <td>#{b.id}</td>
                  <td style={{ fontWeight: 500 }}>{b.carName}</td>
                  <td>
                    <span style={{ fontSize: '12px', color: 'var(--text-secondary)' }}>
                      {b.startDate} → {b.endDate}
                    </span>
                  </td>
                  <td><BookingStatusFlow current={b.status} /></td>
                  <td>
                    {b.status === 'CREATED' && (
                      <button className="btn btn-sm btn-danger" onClick={() => handleCancel(b.id)}>
                        Cancel
                      </button>
                    )}
                    {b.status === 'PICKED_UP' && (
                      <span style={{ fontSize: '12px', color: 'var(--warning)' }}>In progress</span>
                    )}
                    {b.status === 'COMPLETED' && (
                      <span style={{ fontSize: '12px', color: 'var(--success)' }}>Done</span>
                    )}
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}
    </div>
  );
}

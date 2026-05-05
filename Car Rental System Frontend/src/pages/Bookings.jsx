import { useState, useEffect } from 'react';
import API from '../api/axios';

export default function Bookings() {
  const [bookings, setBookings] = useState([]);
  const [loading, setLoading] = useState(true);
  const [message, setMessage] = useState('');

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
    if (!confirm('Cancel this booking?')) return;
    try {
      await API.delete(`/bookings/${bookingId}`);
      setMessage('Booking cancelled');
      fetchBookings();
    } catch (err) {
      setMessage(err.response?.data?.message || 'Failed to cancel');
    }
  };

  return (
    <div className="page">
      <h1>My Bookings</h1>
      {message && <div className="alert alert-info">{message}</div>}
      {loading ? (
        <p>Loading...</p>
      ) : bookings.length === 0 ? (
        <p>No bookings yet.</p>
      ) : (
        <div className="table-wrapper">
          <table>
            <thead>
              <tr>
                <th>ID</th>
                <th>Car</th>
                <th>Start Date</th>
                <th>End Date</th>
                <th>Status</th>
                <th>Action</th>
              </tr>
            </thead>
            <tbody>
              {bookings.map((b) => (
                <tr key={b.id}>
                  <td>{b.id}</td>
                  <td>{b.carName}</td>
                  <td>{b.startDate}</td>
                  <td>{b.endDate}</td>
                  <td>
                    <span className={`badge badge-${b.status === 'CREATED' ? 'info' : b.status === 'COMPLETED' ? 'success' : b.status === 'CANCELLED' ? 'error' : 'warning'}`}>
                      {b.status}
                    </span>
                  </td>
                  <td>
                    {b.status === 'CREATED' && (
                      <button className="btn btn-sm btn-danger" onClick={() => handleCancel(b.id)}>
                        Cancel
                      </button>
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

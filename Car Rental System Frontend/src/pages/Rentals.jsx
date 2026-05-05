import { useState, useEffect } from 'react';
import API from '../api/axios';

export default function Rentals() {
  const [pendingBookings, setPendingBookings] = useState([]);
  const [activeRentals, setActiveRentals] = useState([]);
  const [selectedBookingId, setSelectedBookingId] = useState('');
  const [selectedRentalId, setSelectedRentalId] = useState('');
  const [rental, setRental] = useState(null);
  const [message, setMessage] = useState({ text: '', type: '' });
  const [loading, setLoading] = useState(true);

  const showMsg = (text, type = 'success') => setMessage({ text, type });

  const fetchData = async () => {
    setLoading(true);
    try {
      const [bookingsRes, rentalsRes] = await Promise.all([
        API.get('/bookings/status/CREATED'),
        API.get('/rentals/active'),
      ]);
      setPendingBookings(bookingsRes.data);
      setActiveRentals(rentalsRes.data);
    } catch {
      setPendingBookings([]);
      setActiveRentals([]);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => { fetchData(); }, []);

  const handlePickup = async (e) => {
    e.preventDefault();
    if (!selectedBookingId) return;
    setMessage({ text: '', type: '' });
    setRental(null);
    try {
      const { data } = await API.post(`/rentals/pickup/${selectedBookingId}`);
      setRental(data);
      showMsg(`Car picked up! Rental #${data.id} created.`);
      setSelectedBookingId('');
      fetchData();
    } catch (err) {
      showMsg(err.response?.data?.message || 'Pickup failed', 'error');
    }
  };

  const handleReturn = async (e) => {
    e.preventDefault();
    if (!selectedRentalId) return;
    setMessage({ text: '', type: '' });
    setRental(null);
    try {
      const { data } = await API.post(`/rentals/return/${selectedRentalId}`);
      setRental(data);
      showMsg(`Car returned! Total cost: ₹${data.totalCost} (rentalDays × pricePerDay)`);
      setSelectedRentalId('');
      fetchData();
    } catch (err) {
      showMsg(err.response?.data?.message || 'Return failed', 'error');
    }
  };

  return (
    <div className="page">
      <h1>Rental Management</h1>
      <p className="page-subtitle">Process car pickups and returns. Cost is auto-calculated at return.</p>

      {message.text && (
        <div className={`alert alert-${message.type === 'error' ? 'error' : 'success'}`}>{message.text}</div>
      )}

      {loading ? (
        <div className="loading">Loading...</div>
      ) : (
        <div className="card-grid" style={{ marginBottom: '20px' }}>
          <div className="card">
            <h3>1. Pickup Car</h3>
            <p style={{ marginBottom: '12px' }}>Select a pending booking request to process pickup.</p>
            <form onSubmit={handlePickup}>
              <div className="form-group">
                <label>Pending Booking Requests</label>
                {pendingBookings.length === 0 ? (
                  <p style={{ fontSize: '12px', color: 'var(--text-muted)', marginTop: '4px' }}>No pending bookings</p>
                ) : (
                  <select
                    value={selectedBookingId}
                    onChange={(e) => setSelectedBookingId(e.target.value)}
                    required
                  >
                    <option value="">Select a booking...</option>
                    {pendingBookings.map((b) => (
                      <option key={b.id} value={b.id}>
                        #{b.id} — {b.carName} ({b.startDate} → {b.endDate})
                      </option>
                    ))}
                  </select>
                )}
              </div>
              <button
                type="submit"
                className="btn btn-primary"
                style={{ marginTop: '8px' }}
                disabled={!selectedBookingId || pendingBookings.length === 0}
              >
                Process Pickup
              </button>
            </form>
          </div>

          <div className="card">
            <h3>2. Return Car</h3>
            <p style={{ marginBottom: '12px' }}>Select an active rental to process return.</p>
            <form onSubmit={handleReturn}>
              <div className="form-group">
                <label>Active Rentals</label>
                {activeRentals.length === 0 ? (
                  <p style={{ fontSize: '12px', color: 'var(--text-muted)', marginTop: '4px' }}>No active rentals</p>
                ) : (
                  <select
                    value={selectedRentalId}
                    onChange={(e) => setSelectedRentalId(e.target.value)}
                    required
                  >
                    <option value="">Select a rental...</option>
                    {activeRentals.map((r) => (
                      <option key={r.id} value={r.id}>
                        Rental #{r.id} — Booking #{r.bookingId} (picked up: {new Date(r.pickupTime).toLocaleDateString()})
                      </option>
                    ))}
                  </select>
                )}
              </div>
              <button
                type="submit"
                className="btn btn-success"
                style={{ marginTop: '8px' }}
                disabled={!selectedRentalId || activeRentals.length === 0}
              >
                Process Return
              </button>
            </form>
          </div>
        </div>
      )}

      <div className="stats-row">
        <div className="stat-card">
          <div className="stat-label">Pending Pickups</div>
          <div className="stat-value">{pendingBookings.length}</div>
        </div>
        <div className="stat-card">
          <div className="stat-label">Active Rentals</div>
          <div className="stat-value">{activeRentals.length}</div>
        </div>
      </div>

      {rental && (
        <div className="card" style={{ marginTop: '16px' }}>
          <h3>Rental Details</h3>
          <div className="detail-grid">
            <div>
              <strong>Rental ID</strong>
              <span className="detail-value">#{rental.id}</span>
            </div>
            <div>
              <strong>Booking ID</strong>
              <span className="detail-value">#{rental.bookingId}</span>
            </div>
            <div>
              <strong>Pickup Time</strong>
              <span className="detail-value">{rental.pickupTime ? new Date(rental.pickupTime).toLocaleString() : '—'}</span>
            </div>
            <div>
              <strong>Return Time</strong>
              <span className="detail-value">{rental.returnTime ? new Date(rental.returnTime).toLocaleString() : 'Not returned yet'}</span>
            </div>
            <div>
              <strong>Total Cost</strong>
              <span className="detail-value" style={{ color: rental.totalCost != null ? 'var(--success)' : 'var(--text-secondary)', fontWeight: 700, fontSize: '18px' }}>
                {rental.totalCost != null ? `₹${rental.totalCost}` : 'Pending return'}
              </span>
            </div>
          </div>
          {rental.totalCost != null && (
            <div style={{ marginTop: '12px', fontSize: '12px', color: 'var(--text-secondary)' }}>
              Formula: totalCost = rentalDays × pricePerDay
            </div>
          )}
        </div>
      )}
    </div>
  );
}

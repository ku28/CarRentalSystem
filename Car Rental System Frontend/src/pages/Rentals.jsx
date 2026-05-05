import { useState } from 'react';
import API from '../api/axios';

export default function Rentals() {
  const [bookingId, setBookingId] = useState('');
  const [rentalId, setRentalId] = useState('');
  const [rental, setRental] = useState(null);
  const [message, setMessage] = useState('');

  const handlePickup = async (e) => {
    e.preventDefault();
    setMessage('');
    setRental(null);
    try {
      const { data } = await API.post(`/rentals/pickup/${bookingId}`);
      setRental(data);
      setMessage('Car picked up successfully!');
    } catch (err) {
      setMessage(err.response?.data?.message || 'Pickup failed');
    }
  };

  const handleReturn = async (e) => {
    e.preventDefault();
    setMessage('');
    setRental(null);
    try {
      const { data } = await API.post(`/rentals/return/${rentalId}`);
      setRental(data);
      setMessage('Car returned successfully!');
    } catch (err) {
      setMessage(err.response?.data?.message || 'Return failed');
    }
  };

  const handleLookup = async (e) => {
    e.preventDefault();
    setMessage('');
    try {
      const { data } = await API.get(`/rentals/booking/${bookingId}`);
      setRental(data);
    } catch (err) {
      setMessage(err.response?.data?.message || 'Rental not found');
      setRental(null);
    }
  };

  return (
    <div className="page">
      <h1>Rental Management</h1>
      {message && <div className="alert alert-info">{message}</div>}

      <div className="card-grid">
        <div className="card">
          <h3>Pickup Car</h3>
          <form onSubmit={handlePickup}>
            <div className="form-group">
              <label>Booking ID</label>
              <input
                type="number"
                value={bookingId}
                onChange={(e) => setBookingId(e.target.value)}
                required
              />
            </div>
            <button type="submit" className="btn btn-primary">Pickup</button>
            <button type="button" className="btn" onClick={handleLookup} style={{ marginLeft: '8px' }}>
              Lookup Rental
            </button>
          </form>
        </div>

        <div className="card">
          <h3>Return Car</h3>
          <form onSubmit={handleReturn}>
            <div className="form-group">
              <label>Rental ID</label>
              <input
                type="number"
                value={rentalId}
                onChange={(e) => setRentalId(e.target.value)}
                required
              />
            </div>
            <button type="submit" className="btn btn-primary">Return</button>
          </form>
        </div>
      </div>

      {rental && (
        <div className="card" style={{ marginTop: '20px' }}>
          <h3>Rental Details</h3>
          <div className="detail-grid">
            <div><strong>Rental ID:</strong> {rental.id}</div>
            <div><strong>Booking ID:</strong> {rental.bookingId}</div>
            <div><strong>Pickup Time:</strong> {rental.pickupTime ? new Date(rental.pickupTime).toLocaleString() : '—'}</div>
            <div><strong>Return Time:</strong> {rental.returnTime ? new Date(rental.returnTime).toLocaleString() : '—'}</div>
            <div><strong>Total Cost:</strong> {rental.totalCost != null ? `$${rental.totalCost}` : '—'}</div>
          </div>
        </div>
      )}
    </div>
  );
}

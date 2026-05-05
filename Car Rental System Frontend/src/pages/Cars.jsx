import { useState, useEffect } from 'react';
import { useAuth } from '../context/AuthContext';
import { Link } from 'react-router-dom';
import API from '../api/axios';

export default function Cars() {
  const [cars, setCars] = useState([]);
  const [page, setPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);
  const [search, setSearch] = useState({ brand: '', category: '', status: '' });
  const [loading, setLoading] = useState(true);
  const [bookingCar, setBookingCar] = useState(null);
  const [bookingForm, setBookingForm] = useState({ startDate: '', endDate: '' });
  const [message, setMessage] = useState({ text: '', type: '' });
  const { user } = useAuth();

  const fetchCars = async (p = 0) => {
    setLoading(true);
    try {
      const params = { page: p, size: 12 };
      if (search.brand) params.brand = search.brand;
      if (search.category) params.category = search.category;
      if (search.status) params.availabilityStatus = search.status;
      const { data } = await API.get('/cars/search', { params });
      setCars(data.content || []);
      setTotalPages(data.totalPages || 0);
      setPage(p);
    } catch {
      setCars([]);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => { fetchCars(); }, []);

  const handleSearch = (e) => {
    e.preventDefault();
    fetchCars(0);
  };

  const handleBook = async (e) => {
    e.preventDefault();
    setMessage({ text: '', type: '' });
    try {
      await API.post('/bookings', {
        carId: bookingCar.id,
        startDate: bookingForm.startDate,
        endDate: bookingForm.endDate,
      });
      setMessage({ text: `Booking request sent for ${bookingCar.brand} ${bookingCar.model}. An admin will process your pickup.`, type: 'success' });
      setBookingCar(null);
      setBookingForm({ startDate: '', endDate: '' });
      fetchCars(page);
    } catch (err) {
      setMessage({ text: err.response?.data?.message || 'Booking failed', type: 'error' });
    }
  };

  const estimateCost = () => {
    if (!bookingForm.startDate || !bookingForm.endDate || !bookingCar) return null;
    const start = new Date(bookingForm.startDate);
    const end = new Date(bookingForm.endDate);
    const days = Math.max(1, Math.ceil((end - start) / (1000 * 60 * 60 * 24)));
    const cost = days * bookingCar.pricePerDay;
    return { days, cost };
  };

  const estimate = estimateCost();

  const statusBadge = (status) => {
    const map = { AVAILABLE: 'success', BOOKED: 'info', RENTED: 'warning', MAINTENANCE: 'error' };
    return <span className={`badge badge-${map[status] || 'info'}`}>{status}</span>;
  };

  return (
    <div className="page">
      <h1>Browse Cars</h1>
      <p className="page-subtitle">Find and book from our fleet of vehicles</p>

      <form className="search-bar" onSubmit={handleSearch}>
        <input
          type="text"
          placeholder="Search by brand..."
          value={search.brand}
          onChange={(e) => setSearch({ ...search, brand: e.target.value })}
        />
        <input
          type="text"
          placeholder="Category..."
          value={search.category}
          onChange={(e) => setSearch({ ...search, category: e.target.value })}
        />
        <select
          value={search.status}
          onChange={(e) => setSearch({ ...search, status: e.target.value })}
        >
          <option value="">All Statuses</option>
          <option value="AVAILABLE">Available</option>
          <option value="BOOKED">Booked</option>
          <option value="RENTED">Rented</option>
          <option value="MAINTENANCE">Maintenance</option>
        </select>
        <button type="submit" className="btn btn-primary">Search</button>
      </form>

      {message.text && (
        <div className={`alert alert-${message.type === 'error' ? 'error' : 'success'}`}>
          {message.text}
        </div>
      )}

      {loading ? (
        <div className="loading">Loading cars...</div>
      ) : cars.length === 0 ? (
        <div className="empty-state">
          <div className="empty-icon">🚗</div>
          <p>No cars found matching your search.</p>
        </div>
      ) : (
        <>
          <div className="car-grid">
            {cars.map((car) => (
              <div key={car.id} className="car-card">
                <div className="car-card-header">
                  <div className="car-card-title">{car.brand} {car.model}</div>
                  <span className="car-card-category">{car.category}</span>
                </div>
                <div className="car-card-price">
                  ₹{car.pricePerDay} <span>/day</span>
                </div>
                <div className="car-card-footer">
                  {statusBadge(car.availabilityStatus)}
                  {user && car.availabilityStatus === 'AVAILABLE' ? (
                    <button className="btn btn-sm btn-primary" onClick={() => setBookingCar(car)}>
                      Request Booking
                    </button>
                  ) : !user && car.availabilityStatus === 'AVAILABLE' ? (
                    <Link to="/login" className="btn btn-sm">Login to Book</Link>
                  ) : null}
                </div>
              </div>
            ))}
          </div>

          {totalPages > 1 && (
            <div className="pagination">
              <button className="btn btn-sm" disabled={page === 0} onClick={() => fetchCars(page - 1)}>← Previous</button>
              <span>Page {page + 1} of {totalPages}</span>
              <button className="btn btn-sm" disabled={page >= totalPages - 1} onClick={() => fetchCars(page + 1)}>Next →</button>
            </div>
          )}
        </>
      )}

      {bookingCar && (
        <div className="modal-overlay" onClick={() => setBookingCar(null)}>
          <div className="modal" onClick={(e) => e.stopPropagation()}>
            <h3>Request Booking</h3>
            <p className="modal-subtitle">
              {bookingCar.brand} {bookingCar.model} — ₹{bookingCar.pricePerDay}/day
            </p>
            <form onSubmit={handleBook}>
              <div className="form-group">
                <label>Start Date</label>
                <input
                  type="date"
                  value={bookingForm.startDate}
                  onChange={(e) => setBookingForm({ ...bookingForm, startDate: e.target.value })}
                  min={new Date().toISOString().split('T')[0]}
                  required
                />
              </div>
              <div className="form-group">
                <label>End Date</label>
                <input
                  type="date"
                  value={bookingForm.endDate}
                  onChange={(e) => setBookingForm({ ...bookingForm, endDate: e.target.value })}
                  min={bookingForm.startDate || new Date().toISOString().split('T')[0]}
                  required
                />
              </div>
              {estimate && (
                <div className="card" style={{ marginTop: '12px', background: 'var(--bg)' }}>
                  <div style={{ display: 'flex', justifyContent: 'space-between', fontSize: '13px' }}>
                    <span style={{ color: 'var(--text-secondary)' }}>{estimate.days} day{estimate.days > 1 ? 's' : ''} × ₹{bookingCar.pricePerDay}</span>
                    <span style={{ fontWeight: 700 }}>₹{estimate.cost.toFixed(2)}</span>
                  </div>
                  <div style={{ fontSize: '11px', color: 'var(--text-muted)', marginTop: '4px' }}>
                    Estimated cost • Final cost calculated at return
                  </div>
                </div>
              )}
              <div className="modal-actions">
                <button type="submit" className="btn btn-primary" style={{ flex: 1 }}>Send Booking Request</button>
                <button type="button" className="btn" onClick={() => setBookingCar(null)}>Cancel</button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
}

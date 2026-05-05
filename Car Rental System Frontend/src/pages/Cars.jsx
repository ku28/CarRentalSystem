import { useState, useEffect } from 'react';
import { useAuth } from '../context/AuthContext';
import API from '../api/axios';

export default function Cars() {
  const [cars, setCars] = useState([]);
  const [page, setPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);
  const [search, setSearch] = useState({ brand: '', category: '' });
  const [loading, setLoading] = useState(true);
  const [bookingCar, setBookingCar] = useState(null);
  const [bookingForm, setBookingForm] = useState({ startDate: '', endDate: '' });
  const [message, setMessage] = useState('');
  const { user } = useAuth();

  const fetchCars = async (p = 0) => {
    setLoading(true);
    try {
      const params = { page: p, size: 10 };
      if (search.brand) params.brand = search.brand;
      if (search.category) params.category = search.category;
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
    setMessage('');
    try {
      await API.post('/bookings', {
        carId: bookingCar.id,
        startDate: bookingForm.startDate,
        endDate: bookingForm.endDate,
      });
      setMessage('Booking created successfully!');
      setBookingCar(null);
      setBookingForm({ startDate: '', endDate: '' });
      fetchCars(page);
    } catch (err) {
      setMessage(err.response?.data?.message || 'Booking failed');
    }
  };

  return (
    <div className="page">
      <h1>Available Cars</h1>
      <form className="search-bar" onSubmit={handleSearch}>
        <input
          type="text"
          placeholder="Brand..."
          value={search.brand}
          onChange={(e) => setSearch({ ...search, brand: e.target.value })}
        />
        <input
          type="text"
          placeholder="Category..."
          value={search.category}
          onChange={(e) => setSearch({ ...search, category: e.target.value })}
        />
        <button type="submit" className="btn btn-primary">Search</button>
      </form>

      {message && <div className="alert alert-info">{message}</div>}

      {loading ? (
        <p>Loading cars...</p>
      ) : cars.length === 0 ? (
        <p>No cars found.</p>
      ) : (
        <>
          <div className="table-wrapper">
            <table>
              <thead>
                <tr>
                  <th>Brand</th>
                  <th>Model</th>
                  <th>Category</th>
                  <th>Price/Day</th>
                  <th>Status</th>
                  {user && <th>Action</th>}
                </tr>
              </thead>
              <tbody>
                {cars.map((car) => (
                  <tr key={car.id}>
                    <td>{car.brand}</td>
                    <td>{car.model}</td>
                    <td>{car.category}</td>
                    <td>${car.pricePerDay}</td>
                    <td>
                      <span className={`badge badge-${car.availabilityStatus === 'AVAILABLE' ? 'success' : 'warning'}`}>
                        {car.availabilityStatus}
                      </span>
                    </td>
                    {user && (
                      <td>
                        {car.availabilityStatus === 'AVAILABLE' && (
                          <button
                            className="btn btn-sm btn-primary"
                            onClick={() => setBookingCar(car)}
                          >
                            Book
                          </button>
                        )}
                      </td>
                    )}
                  </tr>
                ))}
              </tbody>
            </table>
          </div>

          <div className="pagination">
            <button
              className="btn btn-sm"
              disabled={page === 0}
              onClick={() => fetchCars(page - 1)}
            >
              Previous
            </button>
            <span>Page {page + 1} of {totalPages}</span>
            <button
              className="btn btn-sm"
              disabled={page >= totalPages - 1}
              onClick={() => fetchCars(page + 1)}
            >
              Next
            </button>
          </div>
        </>
      )}

      {bookingCar && (
        <div className="modal-overlay" onClick={() => setBookingCar(null)}>
          <div className="modal" onClick={(e) => e.stopPropagation()}>
            <h3>Book {bookingCar.brand} {bookingCar.model}</h3>
            <p>Price: ${bookingCar.pricePerDay}/day</p>
            <form onSubmit={handleBook}>
              <div className="form-group">
                <label>Start Date</label>
                <input
                  type="date"
                  value={bookingForm.startDate}
                  onChange={(e) => setBookingForm({ ...bookingForm, startDate: e.target.value })}
                  required
                />
              </div>
              <div className="form-group">
                <label>End Date</label>
                <input
                  type="date"
                  value={bookingForm.endDate}
                  onChange={(e) => setBookingForm({ ...bookingForm, endDate: e.target.value })}
                  required
                />
              </div>
              <div className="modal-actions">
                <button type="submit" className="btn btn-primary">Confirm Booking</button>
                <button type="button" className="btn" onClick={() => setBookingCar(null)}>Cancel</button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
}

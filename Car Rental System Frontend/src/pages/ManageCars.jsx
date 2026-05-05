import { useState, useEffect } from 'react';
import API from '../api/axios';

export default function ManageCars() {
  const [cars, setCars] = useState([]);
  const [page, setPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);
  const [loading, setLoading] = useState(true);
  const [message, setMessage] = useState({ text: '', type: '' });
  const [editCar, setEditCar] = useState(null);
  const [form, setForm] = useState({
    brand: '', model: '', category: '', pricePerDay: '', availabilityStatus: 'AVAILABLE'
  });

  const fetchCars = async (p = 0) => {
    setLoading(true);
    try {
      const { data } = await API.get('/cars', { params: { page: p, size: 10 } });
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

  const resetForm = () => {
    setForm({ brand: '', model: '', category: '', pricePerDay: '', availabilityStatus: 'AVAILABLE' });
    setEditCar(null);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setMessage({ text: '', type: '' });
    try {
      const payload = { ...form, pricePerDay: parseFloat(form.pricePerDay) };
      if (editCar) {
        await API.put(`/cars/${editCar.id}`, payload);
        setMessage({ text: 'Car updated successfully', type: 'success' });
      } else {
        await API.post('/cars', payload);
        setMessage({ text: 'Car added to fleet', type: 'success' });
      }
      resetForm();
      fetchCars(page);
    } catch (err) {
      setMessage({ text: err.response?.data?.message || 'Operation failed', type: 'error' });
    }
  };

  const handleEdit = (car) => {
    setEditCar(car);
    setForm({
      brand: car.brand,
      model: car.model,
      category: car.category,
      pricePerDay: car.pricePerDay.toString(),
      availabilityStatus: car.availabilityStatus,
    });
    window.scrollTo({ top: 0, behavior: 'smooth' });
  };

  const handleDelete = async (id) => {
    if (!confirm('Delete this car from the fleet?')) return;
    try {
      await API.delete(`/cars/${id}`);
      setMessage({ text: 'Car deleted', type: 'success' });
      fetchCars(page);
    } catch (err) {
      setMessage({ text: err.response?.data?.message || 'Delete failed', type: 'error' });
    }
  };

  const statusBadge = (status) => {
    const map = { AVAILABLE: 'success', BOOKED: 'info', RENTED: 'warning', MAINTENANCE: 'error' };
    return <span className={`badge badge-${map[status] || 'info'}`}>{status}</span>;
  };

  return (
    <div className="page">
      <h1>Manage Cars</h1>
      <p className="page-subtitle">Add, edit, or remove vehicles from the fleet</p>

      {message.text && (
        <div className={`alert alert-${message.type === 'error' ? 'error' : 'success'}`}>{message.text}</div>
      )}

      <form className="card form-card" onSubmit={handleSubmit}>
        <h3>{editCar ? `Edit: ${editCar.brand} ${editCar.model}` : 'Add New Car'}</h3>
        <div className="form-row">
          <div className="form-group">
            <label>Brand</label>
            <input placeholder="e.g. Toyota" value={form.brand} onChange={(e) => setForm({ ...form, brand: e.target.value })} required />
          </div>
          <div className="form-group">
            <label>Model</label>
            <input placeholder="e.g. Camry" value={form.model} onChange={(e) => setForm({ ...form, model: e.target.value })} required />
          </div>
          <div className="form-group">
            <label>Category</label>
            <input placeholder="e.g. Sedan" value={form.category} onChange={(e) => setForm({ ...form, category: e.target.value })} required />
          </div>
          <div className="form-group">
            <label>Price/Day (₹)</label>
            <input type="number" step="0.01" min="0" placeholder="50.00" value={form.pricePerDay} onChange={(e) => setForm({ ...form, pricePerDay: e.target.value })} required />
          </div>
          <div className="form-group">
            <label>Status</label>
            <select value={form.availabilityStatus} onChange={(e) => setForm({ ...form, availabilityStatus: e.target.value })}>
              <option value="AVAILABLE">Available</option>
              <option value="BOOKED">Booked</option>
              <option value="RENTED">Rented</option>
              <option value="MAINTENANCE">Maintenance</option>
            </select>
          </div>
        </div>
        <div className="form-actions">
          <button type="submit" className="btn btn-primary">{editCar ? 'Update Car' : 'Add Car'}</button>
          {editCar && <button type="button" className="btn" onClick={resetForm}>Cancel</button>}
        </div>
      </form>

      {loading ? (
        <div className="loading">Loading fleet...</div>
      ) : (
        <>
          <div className="table-wrapper">
            <table>
              <thead>
                <tr>
                  <th>ID</th>
                  <th>Brand</th>
                  <th>Model</th>
                  <th>Category</th>
                  <th>Price/Day</th>
                  <th>Status</th>
                  <th>Actions</th>
                </tr>
              </thead>
              <tbody>
                {cars.map((car) => (
                  <tr key={car.id}>
                    <td>#{car.id}</td>
                    <td style={{ fontWeight: 500 }}>{car.brand}</td>
                    <td>{car.model}</td>
                    <td>{car.category}</td>
                    <td>₹{car.pricePerDay}</td>
                    <td>{statusBadge(car.availabilityStatus)}</td>
                    <td>
                      <button className="btn btn-sm" onClick={() => handleEdit(car)}>Edit</button>
                      <button className="btn btn-sm btn-danger" onClick={() => handleDelete(car.id)}>Delete</button>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
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
    </div>
  );
}

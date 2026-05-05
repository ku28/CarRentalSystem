import { useState } from 'react';
import API from '../api/axios';

export default function Payments() {
  const [rentalId, setRentalId] = useState('');
  const [payment, setPayment] = useState(null);
  const [message, setMessage] = useState({ text: '', type: '' });

  const handleSimulatePayment = async (e) => {
    e.preventDefault();
    setMessage({ text: '', type: '' });
    setPayment(null);
    try {
      const { data } = await API.post('/payments', { rentalId: parseInt(rentalId) });
      setPayment(data);
      setMessage({ text: `Payment of ₹${data.amount} processed successfully!`, type: 'success' });
    } catch (err) {
      setMessage({ text: err.response?.data?.message || 'Payment failed', type: 'error' });
    }
  };

  const handleLookup = async () => {
    setMessage({ text: '', type: '' });
    if (!rentalId) return;
    try {
      const { data } = await API.get(`/payments/rental/${rentalId}`);
      setPayment(data);
    } catch (err) {
      setMessage({ text: err.response?.data?.message || 'No payment found', type: 'error' });
      setPayment(null);
    }
  };

  return (
    <div className="page">
      <h1>Payment Management</h1>
      <p className="page-subtitle">Process payments for completed rentals. Ensure the car has been returned before processing.</p>

      {message.text && (
        <div className={`alert alert-${message.type === 'error' ? 'error' : 'success'}`}>{message.text}</div>
      )}

      <div className="card" style={{ marginBottom: '16px' }}>
        <h3>Process Payment</h3>
        <p style={{ marginBottom: '12px' }}>Enter the rental ID to process or lookup a payment.</p>
        <form onSubmit={handleSimulatePayment}>
          <div className="form-group">
            <label>Rental ID</label>
            <input
              type="number"
              placeholder="e.g. 1"
              value={rentalId}
              onChange={(e) => setRentalId(e.target.value)}
              required
            />
          </div>
          <div style={{ display: 'flex', gap: '8px', marginTop: '8px' }}>
            <button type="submit" className="btn btn-primary">Process Payment</button>
            <button type="button" className="btn" onClick={handleLookup}>Lookup</button>
          </div>
        </form>
      </div>

      {payment && (
        <div className="card">
          <h3>Payment Details</h3>
          <div className="detail-grid">
            <div>
              <strong>Payment ID</strong>
              <span className="detail-value">#{payment.id}</span>
            </div>
            <div>
              <strong>Rental ID</strong>
              <span className="detail-value">#{payment.rentalId}</span>
            </div>
            <div>
              <strong>Amount</strong>
              <span className="detail-value" style={{ color: 'var(--success)', fontWeight: 700, fontSize: '18px' }}>
                ₹{payment.amount}
              </span>
            </div>
            <div>
              <strong>Status</strong>
              <span className={`badge badge-${payment.status === 'COMPLETED' ? 'success' : payment.status === 'FAILED' ? 'error' : 'warning'}`}>
                {payment.status}
              </span>
            </div>
            <div>
              <strong>Payment Date</strong>
              <span className="detail-value">{new Date(payment.paymentDate).toLocaleString()}</span>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}

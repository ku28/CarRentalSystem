import { useState } from 'react';
import API from '../api/axios';

export default function Payments() {
  const [rentalId, setRentalId] = useState('');
  const [payment, setPayment] = useState(null);
  const [message, setMessage] = useState('');

  const handleSimulatePayment = async (e) => {
    e.preventDefault();
    setMessage('');
    setPayment(null);
    try {
      const { data } = await API.post('/payments', { rentalId: parseInt(rentalId) });
      setPayment(data);
      setMessage('Payment processed successfully!');
    } catch (err) {
      setMessage(err.response?.data?.message || 'Payment failed');
    }
  };

  const handleLookup = async (e) => {
    e.preventDefault();
    setMessage('');
    try {
      const { data } = await API.get(`/payments/rental/${rentalId}`);
      setPayment(data);
    } catch (err) {
      setMessage(err.response?.data?.message || 'Payment not found');
      setPayment(null);
    }
  };

  return (
    <div className="page">
      <h1>Payment Management</h1>
      {message && <div className="alert alert-info">{message}</div>}

      <div className="card">
        <h3>Process Payment</h3>
        <form onSubmit={handleSimulatePayment}>
          <div className="form-group">
            <label>Rental ID</label>
            <input
              type="number"
              value={rentalId}
              onChange={(e) => setRentalId(e.target.value)}
              required
            />
          </div>
          <button type="submit" className="btn btn-primary">Process Payment</button>
          <button type="button" className="btn" onClick={handleLookup} style={{ marginLeft: '8px' }}>
            Lookup Payment
          </button>
        </form>
      </div>

      {payment && (
        <div className="card" style={{ marginTop: '20px' }}>
          <h3>Payment Details</h3>
          <div className="detail-grid">
            <div><strong>Payment ID:</strong> {payment.id}</div>
            <div><strong>Rental ID:</strong> {payment.rentalId}</div>
            <div><strong>Amount:</strong> ${payment.amount}</div>
            <div><strong>Status:</strong> <span className={`badge badge-${payment.status === 'COMPLETED' ? 'success' : 'warning'}`}>{payment.status}</span></div>
            <div><strong>Payment Date:</strong> {new Date(payment.paymentDate).toLocaleString()}</div>
          </div>
        </div>
      )}
    </div>
  );
}

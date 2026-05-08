import { Link } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';

export default function Home() {
  const { user, isAdmin } = useAuth();

  return (
    <div className="page home-page">
      <div className="hero">
        <h1>Car Rental System</h1>
        <p>Browse available vehicles, request bookings, and manage your rentals — all in one place.</p>
        <div className="hero-actions">
          <Link to="/cars" className="btn btn-primary">Browse Cars</Link>
          {!user && <Link to="/register" className="btn">Create Account</Link>}
          {user && <Link to="/bookings" className="btn">My Bookings</Link>}
        </div>
      </div>

      <div className="card-grid">
        <div className="card">
          <h3>🚗 Browse Fleet</h3>
          <p>View all available cars with search and filters. See pricing, category, and availability at a glance.</p>
        </div>
        <div className="card">
          <h3>📋 Request Booking</h3>
          <p>Select dates and send a booking request. The system reserves the vehicle and an admin processes your pickup.</p>
        </div>
        <div className="card">
          <h3>💰 Transparent Pricing</h3>
          <p>Cost = rental days × price per day. Get an estimate before booking, final cost calculated at return.</p>
        </div>
      </div>

      {isAdmin && (
        <div className="admin-section">
          <h2>Admin Dashboard</h2>
          <p className="page-subtitle">Manage the system</p>
          <div className="card-grid">
            <Link to="/admin/cars" className="card card-link">
              <h3>🚗 Manage Cars</h3>
              <p>Add, edit, or remove vehicles from the fleet</p>
            </Link>
            <Link to="/admin/rentals" className="card card-link">
              <h3>🔑 Rentals</h3>
              <p>Process car pickups and returns</p>
            </Link>
            <Link to="/admin/payments" className="card card-link">
              <h3>💳 Payments</h3>
              <p>Process and manage payments</p>
            </Link>
            <Link to="/admin/reports" className="card card-link">
              <h3>📊 Reports</h3>
              <p>Revenue, bookings, and usage statistics</p>
            </Link>
            <Link to="/admin/users" className="card card-link">
              <h3>👥 Users</h3>
              <p>View registered users</p>
            </Link>
          </div>
        </div>
      )}
    </div>
  );
}

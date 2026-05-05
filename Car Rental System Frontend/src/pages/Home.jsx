import { Link } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';

export default function Home() {
  const { user, isAdmin } = useAuth();

  return (
    <div className="page home-page">
      <div className="hero">
        <h1>Car Rental Management System</h1>
        <p>Browse available cars, make bookings, and manage your rentals.</p>
        <div className="hero-actions">
          <Link to="/cars" className="btn btn-primary">Browse Cars</Link>
          {!user && <Link to="/register" className="btn">Get Started</Link>}
        </div>
      </div>

      <div className="card-grid">
        <div className="card">
          <h3>🚗 Browse Cars</h3>
          <p>Search and filter available vehicles by brand, model, or category.</p>
        </div>
        <div className="card">
          <h3>📅 Book Easily</h3>
          <p>Select your dates and book a car in seconds.</p>
        </div>
        <div className="card">
          <h3>💳 Simple Payments</h3>
          <p>Straightforward pricing with no hidden fees.</p>
        </div>
      </div>

      {isAdmin && (
        <div className="admin-section">
          <h2>Admin Quick Links</h2>
          <div className="card-grid">
            <Link to="/admin/cars" className="card card-link">
              <h3>Manage Cars</h3>
              <p>Add, edit, or remove vehicles</p>
            </Link>
            <Link to="/admin/rentals" className="card card-link">
              <h3>Rentals</h3>
              <p>Process pickups and returns</p>
            </Link>
            <Link to="/admin/payments" className="card card-link">
              <h3>Payments</h3>
              <p>Process and view payments</p>
            </Link>
            <Link to="/admin/reports" className="card card-link">
              <h3>Reports</h3>
              <p>Revenue and usage statistics</p>
            </Link>
          </div>
        </div>
      )}
    </div>
  );
}

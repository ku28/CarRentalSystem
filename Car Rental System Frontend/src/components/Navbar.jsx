import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';

export default function Navbar() {
  const { user, logout, isAdmin } = useAuth();
  const navigate = useNavigate();

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  return (
    <nav className="navbar">
      <div className="navbar-brand">
        <Link to="/">CarRental</Link>
      </div>
      <div className="navbar-links">
        <Link to="/cars">Cars</Link>
        {user && (
          <>
            <Link to="/bookings">My Bookings</Link>
            <Link to="/notifications">Notifications</Link>
            {isAdmin && (
              <>
                <Link to="/admin/cars">Manage Cars</Link>
                <Link to="/admin/rentals">Rentals</Link>
                <Link to="/admin/payments">Payments</Link>
                <Link to="/admin/reports">Reports</Link>
                <Link to="/admin/users">Users</Link>
              </>
            )}
          </>
        )}
      </div>
      <div className="navbar-auth">
        {user ? (
          <>
            <span className="navbar-user">{user.name}</span>
            <button onClick={handleLogout} className="btn btn-sm">Logout</button>
          </>
        ) : (
          <>
            <Link to="/login" className="btn btn-sm">Login</Link>
            <Link to="/register" className="btn btn-sm btn-primary">Register</Link>
          </>
        )}
      </div>
    </nav>
  );
}

import { BrowserRouter, Routes, Route } from 'react-router-dom';
import { AuthProvider } from './context/AuthContext';
import Navbar from './components/Navbar';
import ProtectedRoute from './components/ProtectedRoute';
import Home from './pages/Home';
import Login from './pages/Login';
import Register from './pages/Register';
import Cars from './pages/Cars';
import Bookings from './pages/Bookings';
import Notifications from './pages/Notifications';
import ManageCars from './pages/ManageCars';
import Rentals from './pages/Rentals';
import Payments from './pages/Payments';
import Reports from './pages/Reports';
import Users from './pages/Users';

export default function App() {
  return (
    <AuthProvider>
      <BrowserRouter>
        <Navbar />
        <main className="container">
          <Routes>
            <Route path="/" element={<Home />} />
            <Route path="/login" element={<Login />} />
            <Route path="/register" element={<Register />} />
            <Route path="/cars" element={<Cars />} />
            <Route
              path="/bookings"
              element={<ProtectedRoute><Bookings /></ProtectedRoute>}
            />
            <Route
              path="/notifications"
              element={<ProtectedRoute><Notifications /></ProtectedRoute>}
            />
            <Route
              path="/admin/cars"
              element={<ProtectedRoute adminOnly><ManageCars /></ProtectedRoute>}
            />
            <Route
              path="/admin/rentals"
              element={<ProtectedRoute adminOnly><Rentals /></ProtectedRoute>}
            />
            <Route
              path="/admin/payments"
              element={<ProtectedRoute adminOnly><Payments /></ProtectedRoute>}
            />
            <Route
              path="/admin/reports"
              element={<ProtectedRoute adminOnly><Reports /></ProtectedRoute>}
            />
            <Route
              path="/admin/users"
              element={<ProtectedRoute adminOnly><Users /></ProtectedRoute>}
            />
          </Routes>
        </main>
      </BrowserRouter>
    </AuthProvider>
  );
}

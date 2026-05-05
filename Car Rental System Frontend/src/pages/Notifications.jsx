import { useState, useEffect } from 'react';
import API from '../api/axios';

export default function Notifications() {
  const [notifications, setNotifications] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchNotifications = async () => {
      try {
        const { data } = await API.get('/users/me/notifications');
        setNotifications(data);
      } catch {
        setNotifications([]);
      } finally {
        setLoading(false);
      }
    };
    fetchNotifications();
  }, []);

  return (
    <div className="page">
      <h1>Notifications</h1>
      <p className="page-subtitle">System messages and booking updates</p>

      {loading ? (
        <div className="loading">Loading notifications...</div>
      ) : notifications.length === 0 ? (
        <div className="empty-state">
          <div className="empty-icon">🔔</div>
          <p>No notifications yet. You'll receive updates when your bookings are processed.</p>
        </div>
      ) : (
        <div className="notification-list">
          {notifications.map((n) => (
            <div key={n.id} className="notification-item">
              <p>{n.message}</p>
              <small>{new Date(n.createdAt).toLocaleString()}</small>
            </div>
          ))}
        </div>
      )}
    </div>
  );
}

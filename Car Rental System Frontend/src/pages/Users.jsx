import { useState, useEffect } from 'react';
import API from '../api/axios';

export default function Users() {
  const [users, setUsers] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchUsers = async () => {
      try {
        const { data } = await API.get('/users');
        setUsers(data);
      } catch {
        setUsers([]);
      } finally {
        setLoading(false);
      }
    };
    fetchUsers();
  }, []);

  return (
    <div className="page">
      <h1>Users</h1>
      <p className="page-subtitle">All registered users in the system</p>

      {loading ? (
        <div className="loading">Loading users...</div>
      ) : users.length === 0 ? (
        <div className="empty-state">
          <div className="empty-icon">👥</div>
          <p>No users found.</p>
        </div>
      ) : (
        <>
          <div className="stats-row">
            <div className="stat-card">
              <div className="stat-label">Total Users</div>
              <div className="stat-value">{users.length}</div>
            </div>
            <div className="stat-card">
              <div className="stat-label">Admins</div>
              <div className="stat-value">{users.filter(u => u.role === 'ROLE_ADMIN').length}</div>
            </div>
            <div className="stat-card">
              <div className="stat-label">Regular Users</div>
              <div className="stat-value">{users.filter(u => u.role === 'ROLE_USER').length}</div>
            </div>
          </div>

          <div className="table-wrapper">
            <table>
              <thead>
                <tr>
                  <th>ID</th>
                  <th>Name</th>
                  <th>Email</th>
                  <th>Role</th>
                </tr>
              </thead>
              <tbody>
                {users.map((u) => (
                  <tr key={u.id}>
                    <td>#{u.id}</td>
                    <td style={{ fontWeight: 500 }}>{u.name}</td>
                    <td style={{ color: 'var(--text-secondary)' }}>{u.email}</td>
                    <td>
                      <span className={`badge badge-${u.role === 'ROLE_ADMIN' ? 'warning' : 'info'}`}>
                        {u.role === 'ROLE_ADMIN' ? 'Admin' : 'User'}
                      </span>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </>
      )}
    </div>
  );
}

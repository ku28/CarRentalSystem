import { useEffect } from 'react';
import { useNavigate, useSearchParams } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';

export default function OAuth2Callback() {
  const [searchParams] = useSearchParams();
  const { login } = useAuth();
  const navigate = useNavigate();

  useEffect(() => {
    const token = searchParams.get('token');
    const userId = searchParams.get('userId');
    const name = searchParams.get('name');
    const email = searchParams.get('email');
    const role = searchParams.get('role');

    if (token && userId) {
      login(
        { id: parseInt(userId), name, email, role },
        token
      );
      navigate('/', { replace: true });
    } else {
      navigate('/login', { replace: true });
    }
  }, [searchParams, login, navigate]);

  return (
    <div className="loading">Signing you in...</div>
  );
}

import { useState } from 'react';
import { useNavigate, Navigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';

export default function LoginPage() {
  const { user, login } = useAuth();
  const navigate = useNavigate();

  const [form, setForm] = useState({ username: '', password: '' });
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);

  // Si ya está autenticado, redirige al dashboard
  if (user) return <Navigate to="/admin/dashboard" replace />;

  const handleChange = (e) =>
    setForm((p) => ({ ...p, [e.target.name]: e.target.value }));

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setLoading(true);
    try {
      await login(form.username, form.password);
      navigate('/admin/dashboard');
    } catch (err) {
      const msg = err.response?.data?.detail ?? 'Credenciales incorrectas.';
      setError(msg);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="login-page">
      <div className="login-card">
        <div className="login-card__logo">
          <div className="login-card__badge">RR</div>
          <div className="login-card__title">Repartos Rápidos</div>
          <div className="login-card__sub">Acceso al panel de operadores</div>
        </div>

        {error && <div className="login-error">{error}</div>}

        <form className="login-form" onSubmit={handleSubmit}>
          <div className="form-group">
            <label>Usuario</label>
            <input
              name="username"
              value={form.username}
              onChange={handleChange}
              placeholder="tu_usuario"
              autoComplete="username"
              required
            />
          </div>
          <div className="form-group">
            <label>Contraseña</label>
            <input
              type="password"
              name="password"
              value={form.password}
              onChange={handleChange}
              placeholder="••••••••"
              autoComplete="current-password"
              required
            />
          </div>
          <button type="submit" className="btn btn-primary" disabled={loading} style={{ marginTop: 8 }}>
            {loading ? 'Ingresando…' : 'Ingresar'}
          </button>
        </form>

        <div style={{ textAlign: 'center', marginTop: 20 }}>
          <a href="/" style={{ fontSize: 13, color: 'var(--rr-gray-500)' }}>
            ← Rastrear un paquete
          </a>
        </div>
      </div>
    </div>
  );
}

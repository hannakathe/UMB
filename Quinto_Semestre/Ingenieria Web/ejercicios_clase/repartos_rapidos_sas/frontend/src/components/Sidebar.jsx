import { NavLink, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';

const ICONS = {
  dashboard: '▦',
  envios: '📦',
  nuevo: '＋',
  repartidores: '🏍',
  clientes: '👥',
  reportes: '📊',
  config: '⚙',
};

export default function Sidebar() {
  const { user, logout } = useAuth();
  const navigate = useNavigate();

  const handleLogout = async () => {
    await logout();
    navigate('/login');
  };

  return (
    <aside className="sidebar">
      <div className="sidebar__logo">
        <div className="sidebar__logo-badge">RR</div>
        <span>Repartos Rápidos</span>
      </div>

      <nav className="sidebar__nav">
        <NavLink to="/admin/dashboard" className={({ isActive }) => `sidebar__link${isActive ? ' active' : ''}`}>
          <span className="sidebar__icon">{ICONS.dashboard}</span> Dashboard
        </NavLink>
        <NavLink to="/admin/envios/nuevo" className={({ isActive }) => `sidebar__link${isActive ? ' active' : ''}`}>
          <span className="sidebar__icon">{ICONS.nuevo}</span> Nuevo Envío
        </NavLink>
        <NavLink to="/admin/envios" end className={({ isActive }) => `sidebar__link${isActive ? ' active' : ''}`}>
          <span className="sidebar__icon">{ICONS.envios}</span> Envíos
        </NavLink>
        <NavLink to="/admin/repartidores" className={({ isActive }) => `sidebar__link${isActive ? ' active' : ''}`}>
          <span className="sidebar__icon">{ICONS.repartidores}</span> Repartidores
        </NavLink>
        <NavLink to="/admin/reportes" className={({ isActive }) => `sidebar__link${isActive ? ' active' : ''}`}>
          <span className="sidebar__icon">{ICONS.reportes}</span> Reportes
        </NavLink>
      </nav>

      <div style={{ padding: '12px 16px', borderTop: '1px solid var(--rr-gray-200)' }}>
        <div style={{ fontSize: 12, color: 'var(--rr-gray-500)', marginBottom: 8 }}>
          {user?.full_name || user?.username}
          <br />
          <span style={{ textTransform: 'capitalize' }}>{user?.role}</span>
        </div>
        <button className="btn btn-outline btn-sm" onClick={handleLogout} style={{ width: '100%' }}>
          Cerrar sesión
        </button>
      </div>
    </aside>
  );
}

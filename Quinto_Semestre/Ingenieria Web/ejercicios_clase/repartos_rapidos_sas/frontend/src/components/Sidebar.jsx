import { NavLink, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';

// Navegación según rol
const ADMIN_LINKS = [
  { to: '/admin/dashboard',     icon: '▦',  label: 'Dashboard'      },
  { to: '/admin/envios/nuevo',  icon: '＋',  label: 'Nuevo Envío'    },
  { to: '/admin/envios',        icon: '📦', label: 'Todos los Envíos', end: true },
  { to: '/admin/repartidores',  icon: '🏍', label: 'Repartidores'   },
  { to: '/admin/reportes',      icon: '📊', label: 'Reportes'        },
];

const OPERATOR_LINKS = [
  { to: '/admin/dashboard',     icon: '▦',  label: 'Dashboard'      },
  { to: '/admin/envios/nuevo',  icon: '＋',  label: 'Nuevo Envío'    },
  { to: '/admin/mis-envios',    icon: '📦', label: 'Mis Envíos'     },
  { to: '/admin/borradores',    icon: '📝', label: 'Borradores'     },
];

export default function Sidebar() {
  const { user, logout } = useAuth();
  const navigate = useNavigate();
  const isAdmin = user?.role === 'admin';
  const links = isAdmin ? ADMIN_LINKS : OPERATOR_LINKS;

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

      {/* Rol badge */}
      <div style={{
        margin: '10px 12px 4px',
        padding: '6px 10px',
        borderRadius: 6,
        background: isAdmin ? '#fff5f5' : '#f0f4ff',
        border: `1px solid ${isAdmin ? '#ffcdd2' : '#c7d2fe'}`,
        fontSize: 11,
        fontWeight: 600,
        color: isAdmin ? 'var(--rr-red)' : '#4338ca',
        textAlign: 'center',
        letterSpacing: '.4px',
        textTransform: 'uppercase',
      }}>
        {isAdmin ? '⚙ Administrador' : '👤 Operador'}
      </div>

      <nav className="sidebar__nav">
        {links.map(({ to, icon, label, end }) => (
          <NavLink
            key={to}
            to={to}
            end={end}
            className={({ isActive }) => `sidebar__link${isActive ? ' active' : ''}`}
          >
            <span className="sidebar__icon">{icon}</span>
            {label}
          </NavLink>
        ))}

        {/* Separador visual */}
        <div style={{ borderTop: '1px solid var(--rr-gray-200)', margin: '10px 16px' }} />

        {/* Acceso al rastreo público */}
        <a
          href="/"
          target="_blank"
          rel="noreferrer"
          className="sidebar__link"
        >
          <span className="sidebar__icon">🔍</span>
          Rastreo público
        </a>
      </nav>

      <div style={{ padding: '12px 16px', borderTop: '1px solid var(--rr-gray-200)' }}>
        <div style={{ fontSize: 12, color: 'var(--rr-gray-700)', marginBottom: 8, lineHeight: 1.4 }}>
          <strong>{user?.full_name || user?.username}</strong>
          <br />
          <span style={{ color: 'var(--rr-gray-500)', fontSize: 11 }}>{user?.email}</span>
        </div>
        <button
          className="btn btn-outline btn-sm"
          onClick={handleLogout}
          style={{ width: '100%' }}
        >
          Cerrar sesión
        </button>
      </div>
    </aside>
  );
}

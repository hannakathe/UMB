import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';

export default function Topbar() {
  const { user } = useAuth();
  const navigate = useNavigate();
  const [query, setQuery] = useState('');

  const initials = user?.full_name
    ? user.full_name.split(' ').map((w) => w[0]).join('').slice(0, 2).toUpperCase()
    : (user?.username?.[0] ?? 'U').toUpperCase();

  const handleSearch = (e) => {
    e.preventDefault();
    if (query.trim()) navigate(`/admin/envios?search=${encodeURIComponent(query.trim())}`);
  };

  return (
    <header className="topbar">
      <form className="topbar__search" onSubmit={handleSearch}>
        <span className="topbar__search-icon">🔍</span>
        <input
          type="search"
          placeholder="Buscar guía, cliente, repartidor…"
          value={query}
          onChange={(e) => setQuery(e.target.value)}
        />
      </form>
      <div className="topbar__spacer" />
      <div className="topbar__user">
        <span>{user?.full_name || user?.username}</span>
        <span style={{ fontSize: 12, color: 'var(--rr-gray-500)', textTransform: 'capitalize' }}>
          · {user?.role}
        </span>
        <div className="topbar__avatar">{initials}</div>
      </div>
    </header>
  );
}

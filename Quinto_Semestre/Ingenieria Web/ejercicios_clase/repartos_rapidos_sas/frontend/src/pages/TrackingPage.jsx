import { useState } from 'react';
import { useNavigate } from 'react-router-dom';

export default function TrackingPage() {
  const [query, setQuery] = useState('');
  const navigate = useNavigate();

  const handleSubmit = (e) => {
    e.preventDefault();
    const num = query.trim().toUpperCase();
    if (num) navigate(`/rastrear/${num}`);
  };

  return (
    <div className="tracking-page">
      {/* Header público */}
      <header style={{
        position: 'fixed', top: 0, left: 0, right: 0,
        background: '#fff', borderBottom: '1px solid var(--rr-gray-200)',
        display: 'flex', alignItems: 'center', justifyContent: 'space-between',
        padding: '0 24px', height: 60, zIndex: 10,
      }}>
        <div style={{ display: 'flex', alignItems: 'center', gap: 10, fontWeight: 700 }}>
          <div style={{
            background: 'var(--rr-red)', color: '#fff', width: 32, height: 32,
            borderRadius: 6, display: 'flex', alignItems: 'center',
            justifyContent: 'center', fontWeight: 900, fontSize: 13,
          }}>RR</div>
          Repartos Rápidos
        </div>
        <div style={{ display: 'flex', alignItems: 'center', gap: 16, fontSize: 13 }}>
          <a href="tel:018000RAPIDO" style={{ color: 'var(--rr-gray-700)' }}>
            📞 01-8000-RAPIDO
          </a>
          <a href="/login" className="btn btn-outline btn-sm">Iniciar sesión</a>
        </div>
      </header>

      {/* Hero */}
      <div className="tracking-hero" style={{ marginTop: 60 }}>
        <div style={{ fontSize: 48, marginBottom: 12 }}>📦</div>
        <h1 className="tracking-hero__title">Rastrea tu paquete</h1>
        <p className="tracking-hero__sub">
          Ingresa tu número de guía y te decimos exactamente dónde está.
        </p>

        <form className="tracking-hero__form" onSubmit={handleSubmit}>
          <input
            value={query}
            onChange={(e) => setQuery(e.target.value)}
            placeholder="RPD-2026-045182"
            style={{ fontSize: 16, fontWeight: 500, letterSpacing: 1 }}
            required
          />
          <button type="submit" className="btn btn-primary">
            Rastrear →
          </button>
        </form>

        <p style={{ marginTop: 16, fontSize: 12, color: 'var(--rr-gray-500)' }}>
          ¿No tienes número? Búscalo en el correo de confirmación de tu compra.
        </p>
      </div>

      <footer style={{ marginTop: 60, fontSize: 12, color: 'var(--rr-gray-500)' }}>
        Términos · Privacidad · Contacto
      </footer>
    </div>
  );
}

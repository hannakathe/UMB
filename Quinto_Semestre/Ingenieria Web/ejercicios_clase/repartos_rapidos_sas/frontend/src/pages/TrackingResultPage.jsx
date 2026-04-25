import { useEffect, useState } from 'react';
import { useParams, Link } from 'react-router-dom';
import { enviosApi } from '../api/envios';
import StatusTimeline from '../components/StatusTimeline';

function Stars({ rating }) {
  const full = Math.round(rating);
  return (
    <span style={{ color: '#f59e0b', fontSize: 14 }}>
      {'★'.repeat(full)}{'☆'.repeat(5 - full)}
      <span style={{ color: 'var(--rr-gray-700)', marginLeft: 4, fontSize: 12 }}>{rating}/5</span>
    </span>
  );
}

export default function TrackingResultPage() {
  const { trackingNumber } = useParams();
  const [envio, setEnvio] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    setLoading(true);
    setError('');
    enviosApi.rastrear(trackingNumber)
      .then(({ data }) => setEnvio(data))
      .catch((err) => {
        if (err.response?.status === 404) setError('No encontramos ningún envío con ese número de guía.');
        else setError('Ocurrió un error. Intenta de nuevo en unos segundos.');
      })
      .finally(() => setLoading(false));
  }, [trackingNumber]);

  return (
    <div style={{ minHeight: '100vh', background: 'var(--rr-gray-50)' }}>
      {/* Header */}
      <header style={{
        background: '#fff', borderBottom: '1px solid var(--rr-gray-200)',
        display: 'flex', alignItems: 'center', justifyContent: 'space-between',
        padding: '0 24px', height: 60,
      }}>
        <Link to="/" style={{ display: 'flex', alignItems: 'center', gap: 10, fontWeight: 700 }}>
          <div style={{
            background: 'var(--rr-red)', color: '#fff', width: 32, height: 32,
            borderRadius: 6, display: 'flex', alignItems: 'center', justifyContent: 'center',
            fontWeight: 900, fontSize: 13,
          }}>RR</div>
          Repartos Rápidos
        </Link>
        <a href="/login" className="btn btn-outline btn-sm">Iniciar sesión</a>
      </header>

      <div style={{ maxWidth: 880, margin: '32px auto', padding: '0 16px' }}>
        {loading && <div className="spinner" />}

        {error && (
          <div style={{ textAlign: 'center', paddingTop: 60 }}>
            <div style={{ fontSize: 48, marginBottom: 16 }}>🔍</div>
            <div className="alert alert-error" style={{ maxWidth: 400, margin: '0 auto 24px' }}>{error}</div>
            <Link to="/" className="btn btn-primary">← Intentar de nuevo</Link>
          </div>
        )}

        {envio && (
          <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: 20 }}>
            {/* Columna izquierda */}
            <div style={{ display: 'flex', flexDirection: 'column', gap: 16 }}>
              {/* Encabezado guía */}
              <div className="card">
                <div style={{ fontSize: 11, color: 'var(--rr-gray-500)', textTransform: 'uppercase', marginBottom: 4 }}>
                  Número de guía
                </div>
                <div style={{ fontSize: 28, fontWeight: 900, fontStyle: 'italic', letterSpacing: 1, marginBottom: 8 }}>
                  {envio.tracking_number}
                </div>
                <div style={{ fontSize: 13, color: 'var(--rr-gray-700)' }}>
                  De <strong>{envio.sender_name}</strong>
                  {envio.sender_city && ` (${envio.sender_city})`}
                  {' → '}
                  De <strong>{envio.recipient_name}</strong>
                  {envio.recipient_city && ` (${envio.recipient_city})`}
                </div>
              </div>

              {/* Timeline */}
              <div className="card">
                <div style={{ fontWeight: 700, marginBottom: 16 }}>Estado del envío</div>
                <StatusTimeline
                  currentStatus={envio.status}
                  historial={envio.historial}
                />
              </div>

              {/* Repartidor */}
              {envio.repartidor_name && (
                <div className="card" style={{ display: 'flex', alignItems: 'center', gap: 16 }}>
                  <div style={{
                    width: 44, height: 44, borderRadius: '50%', background: 'var(--rr-gray-200)',
                    display: 'flex', alignItems: 'center', justifyContent: 'center', fontSize: 20,
                    flexShrink: 0,
                  }}>
                    {envio.repartidor_vehicle === 'moto' ? '🏍' : envio.repartidor_vehicle === 'bicicleta' ? '🚴' : '🚗'}
                  </div>
                  <div>
                    <div style={{ fontWeight: 700 }}>{envio.repartidor_name}</div>
                    {envio.repartidor_rating && <Stars rating={envio.repartidor_rating} />}
                    <div style={{ fontSize: 12, color: 'var(--rr-gray-500)', marginTop: 2 }}>
                      Tu repartidor · {envio.repartidor_vehicle} {envio.repartidor_plate}
                    </div>
                  </div>
                </div>
              )}
            </div>

            {/* Columna derecha — Mapa placeholder */}
            <div style={{ display: 'flex', flexDirection: 'column', gap: 16 }}>
              <div className="card" style={{ flex: 1 }}>
                <div style={{ fontWeight: 700, marginBottom: 12, display: 'flex', justifyContent: 'space-between' }}>
                  <span>Ubicación en vivo</span>
                  <span style={{ fontSize: 11, color: 'var(--rr-gray-500)' }}>↺ actualiza cada 30s</span>
                </div>
                <div className="map-placeholder" style={{ minHeight: 280 }}>
                  <span style={{ fontSize: 32 }}>🗺️</span>
                  <span>Mapa en tiempo real</span>
                  <span style={{ fontSize: 11 }}>(requiere integración con Google Maps API)</span>
                  <div style={{ marginTop: 8, fontSize: 11 }}>
                    🔴 repartidor · 🟢 entrega · — ruta
                  </div>
                </div>
              </div>
            </div>
          </div>
        )}

        {/* Footer de ayuda */}
        {envio && (
          <div style={{
            marginTop: 24, display: 'flex', justifyContent: 'space-between',
            flexWrap: 'wrap', gap: 8,
          }}>
            <div style={{ fontSize: 12, color: 'var(--rr-gray-500)' }}>
              <a href="#">¿Qué significa cada estado?</a>
              {' · '}
              <a href="#">¿Cuándo llega?</a>
              {' · '}
              <a href="#">Reportar problema</a>
            </div>
            <div style={{ fontSize: 12, color: 'var(--rr-gray-500)' }}>
              Términos · Privacidad · Contacto
            </div>
          </div>
        )}
      </div>
    </div>
  );
}

import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { enviosApi } from '../../api/envios';
import { repartidoresApi } from '../../api/repartidores';

const SERVICES = [
  { key: 'standard',    label: 'Standard',    price: '$8.000 – $8.000',  days: '2–5 días' },
  { key: 'prioritario', label: 'Prioritario', price: 'Desde $21.000',   days: '1 día' },
  { key: 'express',     label: 'Express',     price: 'Desde $38.000',   days: 'Hoy' },
];

const INIT = {
  sender_name: '', sender_phone: '', sender_address: '', sender_city: '',
  recipient_name: '', recipient_phone: '', recipient_address: '', recipient_city: '',
  description: '', weight: '', height: '', width: '', depth: '', insured_value: '0',
  repartidor: '', service_type: 'standard', is_draft: false,
};

export default function NewEnvioPage() {
  const navigate = useNavigate();
  const [form, setForm] = useState(INIT);
  const [repartidores, setRepartidores] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  useEffect(() => {
    repartidoresApi.list({ is_active: true })
      .then(({ data }) => setRepartidores(data.results ?? data))
      .catch(() => {});
  }, []);

  const set = (key, value) => setForm((p) => ({ ...p, [key]: value }));

  const handleSubmit = async (isDraft) => {
    setError('');
    setLoading(true);
    try {
      const payload = {
        ...form,
        is_draft: isDraft,
        repartidor: form.repartidor || null,
        weight: parseFloat(form.weight) || 0,
        height: parseFloat(form.height) || 0,
        width: parseFloat(form.width) || 0,
        depth: parseFloat(form.depth) || 0,
        insured_value: parseFloat(form.insured_value) || 0,
      };
      const { data } = await enviosApi.create(payload);
      navigate(`/admin/envios/${data.id}`);
    } catch (err) {
      const detail = err.response?.data;
      if (typeof detail === 'object') {
        const msgs = Object.entries(detail).map(([k, v]) => `${k}: ${Array.isArray(v) ? v.join(', ') : v}`);
        setError(msgs.join(' | '));
      } else {
        setError('Ocurrió un error al crear el envío.');
      }
    } finally {
      setLoading(false);
    }
  };

  return (
    <>
      <div className="page-header">
        <h1 className="page-header__title">① Formulario de Creación de Envíos</h1>
      </div>

      {error && <div className="alert alert-error">{error}</div>}

      {/* ── 1. Remitente ── */}
      <div className="form-section">
        <div className="form-section__title">
          <div className="form-section__num">1</div> Remitente
        </div>
        <div className="form-grid">
          <div className="form-group">
            <label>Nombre completo <span>*</span></label>
            <input value={form.sender_name} onChange={(e) => set('sender_name', e.target.value)} placeholder="Juan Pérez Lozano" required />
          </div>
          <div className="form-group">
            <label>Teléfono <span>*</span></label>
            <input value={form.sender_phone} onChange={(e) => set('sender_phone', e.target.value)} placeholder="+57 311 555 0143" required />
          </div>
          <div className="form-group">
            <label>Dirección <span>*</span></label>
            <input value={form.sender_address} onChange={(e) => set('sender_address', e.target.value)} placeholder="Cra 13 #93-45, Bogotá" required />
          </div>
          <div className="form-group">
            <label>Ciudad</label>
            <input value={form.sender_city} onChange={(e) => set('sender_city', e.target.value)} placeholder="Bogotá" />
          </div>
        </div>
      </div>

      {/* ── 2. Destinatario ── */}
      <div className="form-section">
        <div className="form-section__title">
          <div className="form-section__num">2</div> Destinatario
        </div>
        <div className="form-grid">
          <div className="form-group">
            <label>Nombre completo <span>*</span></label>
            <input value={form.recipient_name} onChange={(e) => set('recipient_name', e.target.value)} placeholder="Ana López" required />
          </div>
          <div className="form-group">
            <label>Teléfono <span>*</span></label>
            <input value={form.recipient_phone} onChange={(e) => set('recipient_phone', e.target.value)} placeholder="3005510099" required />
          </div>
          <div className="form-group" style={{ gridColumn: 'span 2' }}>
            <label>Dirección (búsqueda) <span>*</span></label>
            <input
              value={form.recipient_address}
              onChange={(e) => set('recipient_address', e.target.value)}
              placeholder="Cl 80 #11-22, Medellín"
              required
            />
            <span style={{ fontSize: 11, color: 'var(--rr-gray-500)', marginTop: 2 }}>
              🔍 Autocompleta con Google Places API
            </span>
          </div>
          <div className="form-group">
            <label>Ciudad</label>
            <input value={form.recipient_city} onChange={(e) => set('recipient_city', e.target.value)} placeholder="Medellín" />
          </div>
        </div>
      </div>

      {/* ── 3. Detalles del paquete ── */}
      <div className="form-section">
        <div className="form-section__title">
          <div className="form-section__num">3</div> Detalles del paquete
        </div>
        <div className="form-group" style={{ marginBottom: 12 }}>
          <label>Descripción</label>
          <textarea
            rows={2}
            value={form.description}
            onChange={(e) => set('description', e.target.value)}
            placeholder="Caja con accesorios electrónicos"
          />
        </div>
        <div className="form-grid">
          <div className="form-group">
            <label>Peso (kg) <span>*</span></label>
            <input type="number" min="0" step="0.1" value={form.weight} onChange={(e) => set('weight', e.target.value)} placeholder="2.4" required />
          </div>
          <div className="form-group">
            <label>Alto (cm) <span>*</span></label>
            <input type="number" min="0" value={form.height} onChange={(e) => set('height', e.target.value)} placeholder="20" required />
          </div>
          <div className="form-group">
            <label>Ancho (cm) <span>*</span></label>
            <input type="number" min="0" value={form.width} onChange={(e) => set('width', e.target.value)} placeholder="30" required />
          </div>
          <div className="form-group">
            <label>Profundo (cm) <span>*</span></label>
            <input type="number" min="0" value={form.depth} onChange={(e) => set('depth', e.target.value)} placeholder="15" required />
          </div>
          <div className="form-group">
            <label>Valor asegurado (COP)</label>
            <input type="number" min="0" value={form.insured_value} onChange={(e) => set('insured_value', e.target.value)} placeholder="0" />
          </div>
        </div>
      </div>

      {/* ── 4. Repartidor disponible ── */}
      <div className="form-section">
        <div className="form-section__title">
          <div className="form-section__num">4</div> Repartidor disponible
        </div>
        {repartidores.length === 0 ? (
          <p style={{ color: 'var(--rr-gray-500)', fontSize: 13 }}>No hay repartidores activos disponibles.</p>
        ) : (
          <div className="repartidor-cards">
            <div
              className={`repartidor-card ${!form.repartidor ? 'selected' : ''}`}
              onClick={() => set('repartidor', '')}
            >
              <div className="repartidor-card__name">Sin asignar</div>
              <div className="repartidor-card__rating">Asignar después</div>
            </div>
            {repartidores.map((r) => (
              <div
                key={r.id}
                className={`repartidor-card ${form.repartidor === r.id ? 'selected' : ''}`}
                onClick={() => set('repartidor', r.id)}
              >
                <div className="repartidor-card__name">{r.name}</div>
                <div className="repartidor-card__rating">★ {r.rating} · {r.plate}</div>
                <div className="repartidor-card__badge">{r.vehicle}</div>
              </div>
            ))}
          </div>
        )}
      </div>

      {/* ── 5. Tipo de servicio ── */}
      <div className="form-section">
        <div className="form-section__title">
          <div className="form-section__num">5</div> Tipo de servicio
        </div>
        <div className="service-cards">
          {SERVICES.map((s) => (
            <div
              key={s.key}
              className={`service-card ${form.service_type === s.key ? 'selected' : ''}`}
              onClick={() => set('service_type', s.key)}
            >
              <div className="service-card__name">{s.label}</div>
              <div style={{ fontSize: 11, color: 'var(--rr-gray-500)', marginBottom: 4 }}>{s.days}</div>
              <div className="service-card__price">{s.price}</div>
            </div>
          ))}
        </div>
      </div>

      {/* ── Acciones ── */}
      <div style={{ display: 'flex', gap: 12, justifyContent: 'flex-end', marginTop: 8 }}>
        <button
          className="btn btn-secondary"
          disabled={loading}
          onClick={() => handleSubmit(true)}
        >
          Guardar como borrador
        </button>
        <button
          className="btn btn-primary"
          disabled={loading}
          onClick={() => handleSubmit(false)}
        >
          {loading ? 'Creando…' : '✓ Crear Envío'}
        </button>
      </div>
    </>
  );
}

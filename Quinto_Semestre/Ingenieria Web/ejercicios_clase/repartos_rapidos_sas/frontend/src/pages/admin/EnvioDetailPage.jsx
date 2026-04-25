import { useEffect, useState } from 'react';
import { useParams, Link } from 'react-router-dom';
import { enviosApi } from '../../api/envios';
import { repartidoresApi } from '../../api/repartidores';
import StatusBadge from '../../components/StatusBadge';
import StatusTimeline from '../../components/StatusTimeline';

const STATUS_OPTIONS = [
  { value: 'en_bodega',  label: 'En bodega' },
  { value: 'en_ruta',    label: 'En ruta' },
  { value: 'en_entrega', label: 'En entrega' },
  { value: 'recibido',   label: 'Recibido' },
  { value: 'incidencia', label: 'Incidencia' },
];

function Field({ label, value }) {
  return (
    <div style={{ marginBottom: 12 }}>
      <div style={{ fontSize: 11, color: 'var(--rr-gray-500)', textTransform: 'uppercase', marginBottom: 2 }}>{label}</div>
      <div style={{ fontWeight: 500 }}>{value || '—'}</div>
    </div>
  );
}

export default function EnvioDetailPage() {
  const { id } = useParams();
  const [envio, setEnvio] = useState(null);
  const [repartidores, setRepartidores] = useState([]);
  const [loading, setLoading] = useState(true);
  const [newStatus, setNewStatus] = useState('');
  const [notes, setNotes] = useState('');
  const [saving, setSaving] = useState(false);
  const [success, setSuccess] = useState('');
  const [error, setError] = useState('');

  const load = () => {
    setLoading(true);
    Promise.all([
      enviosApi.get(id),
      repartidoresApi.list({ is_active: true }),
    ]).then(([envioRes, repRes]) => {
      setEnvio(envioRes.data);
      setRepartidores(repRes.data.results ?? repRes.data);
    }).finally(() => setLoading(false));
  };

  useEffect(load, [id]);

  const handleStatusChange = async () => {
    if (!newStatus) return;
    setSaving(true); setError(''); setSuccess('');
    try {
      const { data } = await enviosApi.cambiarEstado(id, newStatus, notes);
      setEnvio(data);
      setSuccess('Estado actualizado correctamente.');
      setNewStatus(''); setNotes('');
    } catch {
      setError('No se pudo actualizar el estado.');
    } finally {
      setSaving(false);
    }
  };

  const handleAssignRepartidor = async (repartidorId) => {
    setSaving(true); setError(''); setSuccess('');
    try {
      const { data } = await enviosApi.update(id, { repartidor: repartidorId || null });
      setEnvio((p) => ({ ...p, ...data }));
      setSuccess('Repartidor actualizado.');
    } catch {
      setError('No se pudo asignar el repartidor.');
    } finally {
      setSaving(false);
    }
  };

  if (loading) return <div className="spinner" />;
  if (!envio) return <div className="alert alert-error">Envío no encontrado.</div>;

  return (
    <>
      <div className="page-header">
        <div>
          <Link to="/admin/envios" style={{ fontSize: 13, color: 'var(--rr-gray-500)' }}>← Envíos</Link>
          <h1 className="page-header__title" style={{ fontFamily: 'monospace', marginTop: 4 }}>
            {envio.tracking_number}
          </h1>
        </div>
        <StatusBadge status={envio.status} />
      </div>

      {success && <div className="alert alert-success">{success}</div>}
      {error && <div className="alert alert-error">{error}</div>}

      <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: 16 }}>
        {/* Datos del envío */}
        <div style={{ display: 'flex', flexDirection: 'column', gap: 16 }}>
          <div className="card">
            <div style={{ fontWeight: 700, marginBottom: 14 }}>Remitente</div>
            <Field label="Nombre" value={envio.sender_name} />
            <Field label="Teléfono" value={envio.sender_phone} />
            <Field label="Dirección" value={`${envio.sender_address}${envio.sender_city ? ', ' + envio.sender_city : ''}`} />
          </div>
          <div className="card">
            <div style={{ fontWeight: 700, marginBottom: 14 }}>Destinatario</div>
            <Field label="Nombre" value={envio.recipient_name} />
            <Field label="Teléfono" value={envio.recipient_phone} />
            <Field label="Dirección" value={`${envio.recipient_address}${envio.recipient_city ? ', ' + envio.recipient_city : ''}`} />
          </div>
          <div className="card">
            <div style={{ fontWeight: 700, marginBottom: 14 }}>Paquete</div>
            <Field label="Descripción" value={envio.description} />
            <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr 1fr 1fr', gap: 12 }}>
              <Field label="Peso (kg)" value={envio.weight} />
              <Field label="Alto (cm)" value={envio.height} />
              <Field label="Ancho (cm)" value={envio.width} />
              <Field label="Profundo (cm)" value={envio.depth} />
            </div>
            <Field label="Valor asegurado" value={`$${Number(envio.insured_value).toLocaleString('es-CO')}`} />
            <Field label="Servicio" value={envio.service_type_display} />
          </div>
        </div>

        {/* Timeline + acciones */}
        <div style={{ display: 'flex', flexDirection: 'column', gap: 16 }}>
          <div className="card">
            <div style={{ fontWeight: 700, marginBottom: 14 }}>Historial de estados</div>
            <StatusTimeline currentStatus={envio.status} historial={envio.historial} />
          </div>

          {/* Cambiar estado */}
          <div className="card">
            <div style={{ fontWeight: 700, marginBottom: 14 }}>Cambiar estado</div>
            <div className="form-group" style={{ marginBottom: 10 }}>
              <label>Nuevo estado</label>
              <select value={newStatus} onChange={(e) => setNewStatus(e.target.value)}>
                <option value="">Selecciona…</option>
                {STATUS_OPTIONS.map((s) => (
                  <option key={s.value} value={s.value}>{s.label}</option>
                ))}
              </select>
            </div>
            <div className="form-group" style={{ marginBottom: 10 }}>
              <label>Notas (opcional)</label>
              <textarea rows={2} value={notes} onChange={(e) => setNotes(e.target.value)} placeholder="Ej: Paquete dañado en bodega" />
            </div>
            <button className="btn btn-primary" disabled={!newStatus || saving} onClick={handleStatusChange}>
              {saving ? 'Guardando…' : 'Aplicar cambio'}
            </button>
          </div>

          {/* Asignar repartidor */}
          <div className="card">
            <div style={{ fontWeight: 700, marginBottom: 14 }}>Repartidor asignado</div>
            {envio.repartidor_detail ? (
              <div style={{ marginBottom: 12 }}>
                <strong>{envio.repartidor_detail.name}</strong>
                <span style={{ color: 'var(--rr-gray-500)', marginLeft: 8 }}>
                  ★ {envio.repartidor_detail.rating} · {envio.repartidor_detail.plate}
                </span>
              </div>
            ) : (
              <p style={{ color: 'var(--rr-gray-500)', fontSize: 13, marginBottom: 12 }}>Sin repartidor asignado</p>
            )}
            <div className="form-group">
              <label>Reasignar</label>
              <select
                defaultValue={envio.repartidor ?? ''}
                onChange={(e) => handleAssignRepartidor(e.target.value ? Number(e.target.value) : null)}
              >
                <option value="">— Sin asignar —</option>
                {repartidores.map((r) => (
                  <option key={r.id} value={r.id}>{r.name} · {r.plate}</option>
                ))}
              </select>
            </div>
          </div>
        </div>
      </div>
    </>
  );
}

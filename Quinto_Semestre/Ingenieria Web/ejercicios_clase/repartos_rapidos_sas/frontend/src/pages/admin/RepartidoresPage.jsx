import { useEffect, useState, useCallback } from 'react';
import { repartidoresApi } from '../../api/repartidores';

const VEHICLE_ICONS = { moto: '🏍', bicicleta: '🚴', carro: '🚗' };
const EMPTY_FORM = { name: '', phone: '', vehicle: 'moto', plate: '', rating: '5.0', is_active: true };

function Modal({ title, onClose, children }) {
  return (
    <div style={{
      position: 'fixed', inset: 0, background: 'rgba(0,0,0,.4)',
      display: 'flex', alignItems: 'center', justifyContent: 'center', zIndex: 100,
    }}>
      <div className="card" style={{ width: 440, maxHeight: '90vh', overflow: 'auto' }}>
        <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: 16 }}>
          <strong>{title}</strong>
          <button className="btn btn-outline btn-sm" onClick={onClose}>✕</button>
        </div>
        {children}
      </div>
    </div>
  );
}

export default function RepartidoresPage() {
  const [repartidores, setRepartidores] = useState([]);
  const [loading, setLoading] = useState(true);
  const [modal, setModal] = useState(null); // null | 'create' | 'edit'
  const [selected, setSelected] = useState(null);
  const [form, setForm] = useState(EMPTY_FORM);
  const [saving, setSaving] = useState(false);
  const [error, setError] = useState('');
  const [search, setSearch] = useState('');

  const load = useCallback(async () => {
    setLoading(true);
    try {
      const { data } = await repartidoresApi.list({ search });
      setRepartidores(data.results ?? data);
    } finally {
      setLoading(false);
    }
  }, [search]);

  useEffect(() => { load(); }, [load]);

  const openCreate = () => { setForm(EMPTY_FORM); setSelected(null); setModal('create'); setError(''); };
  const openEdit = (r) => {
    setSelected(r);
    setForm({ name: r.name, phone: r.phone, vehicle: r.vehicle, plate: r.plate, rating: String(r.rating), is_active: r.is_active });
    setModal('edit');
    setError('');
  };

  const set = (k, v) => setForm((p) => ({ ...p, [k]: v }));

  const handleSave = async () => {
    setSaving(true); setError('');
    try {
      const payload = { ...form, rating: parseFloat(form.rating) };
      if (modal === 'create') await repartidoresApi.create(payload);
      else await repartidoresApi.update(selected.id, payload);
      setModal(null);
      load();
    } catch (err) {
      const d = err.response?.data;
      setError(typeof d === 'object' ? Object.values(d).flat().join(' | ') : 'Error al guardar.');
    } finally {
      setSaving(false);
    }
  };

  const handleDelete = async (id) => {
    if (!confirm('¿Eliminar este repartidor?')) return;
    await repartidoresApi.remove(id);
    load();
  };

  const handleToggleActive = async (r) => {
    await repartidoresApi.update(r.id, { is_active: !r.is_active });
    load();
  };

  return (
    <>
      <div className="page-header">
        <h1 className="page-header__title">Repartidores</h1>
        <button className="btn btn-primary" onClick={openCreate}>＋ Nuevo repartidor</button>
      </div>

      <div style={{ marginBottom: 16 }}>
        <input
          style={{ maxWidth: 280 }}
          placeholder="Buscar por nombre o placa…"
          value={search}
          onChange={(e) => setSearch(e.target.value)}
        />
      </div>

      <div className="card">
        {loading ? (
          <div className="spinner" />
        ) : repartidores.length === 0 ? (
          <div className="empty">
            <div className="empty__icon">🏍</div>
            <div className="empty__text">No hay repartidores registrados</div>
          </div>
        ) : (
          <div className="table-wrap">
            <table>
              <thead>
                <tr>
                  <th>Nombre</th>
                  <th>Teléfono</th>
                  <th>Vehículo</th>
                  <th>Placa</th>
                  <th>Calificación</th>
                  <th>Envíos activos</th>
                  <th>Estado</th>
                  <th>Acciones</th>
                </tr>
              </thead>
              <tbody>
                {repartidores.map((r) => (
                  <tr key={r.id}>
                    <td style={{ fontWeight: 600 }}>
                      {VEHICLE_ICONS[r.vehicle]} {r.name}
                    </td>
                    <td>{r.phone}</td>
                    <td style={{ textTransform: 'capitalize' }}>{r.vehicle}</td>
                    <td style={{ fontFamily: 'monospace', fontWeight: 600 }}>{r.plate}</td>
                    <td>
                      <span style={{ color: '#f59e0b' }}>★</span> {r.rating}
                    </td>
                    <td style={{ textAlign: 'center' }}>{r.envios_activos ?? '—'}</td>
                    <td>
                      <span className={`badge ${r.is_active ? 'badge-en_ruta' : 'badge-incidencia'}`}>
                        {r.is_active ? 'Activo' : 'Inactivo'}
                      </span>
                    </td>
                    <td>
                      <div style={{ display: 'flex', gap: 4 }}>
                        <button className="btn btn-outline btn-sm" onClick={() => openEdit(r)}>Editar</button>
                        <button className="btn btn-secondary btn-sm" onClick={() => handleToggleActive(r)}>
                          {r.is_active ? 'Desactivar' : 'Activar'}
                        </button>
                        <button className="btn btn-secondary btn-sm" onClick={() => handleDelete(r.id)}>🗑</button>
                      </div>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}
      </div>

      {/* Modal crear / editar */}
      {modal && (
        <Modal title={modal === 'create' ? 'Nuevo repartidor' : 'Editar repartidor'} onClose={() => setModal(null)}>
          {error && <div className="alert alert-error" style={{ marginBottom: 12 }}>{error}</div>}
          <div style={{ display: 'flex', flexDirection: 'column', gap: 12 }}>
            <div className="form-group">
              <label>Nombre completo <span style={{ color: 'var(--rr-red)' }}>*</span></label>
              <input value={form.name} onChange={(e) => set('name', e.target.value)} placeholder="Carlos M." />
            </div>
            <div className="form-group">
              <label>Teléfono <span style={{ color: 'var(--rr-red)' }}>*</span></label>
              <input value={form.phone} onChange={(e) => set('phone', e.target.value)} placeholder="311 000 0000" />
            </div>
            <div className="form-group">
              <label>Vehículo</label>
              <select value={form.vehicle} onChange={(e) => set('vehicle', e.target.value)}>
                <option value="moto">Moto</option>
                <option value="bicicleta">Bicicleta</option>
                <option value="carro">Carro</option>
              </select>
            </div>
            <div className="form-group">
              <label>Placa <span style={{ color: 'var(--rr-red)' }}>*</span></label>
              <input value={form.plate} onChange={(e) => set('plate', e.target.value.toUpperCase())} placeholder="ABC123" maxLength={10} />
            </div>
            <div className="form-group">
              <label>Calificación (0-5)</label>
              <input type="number" min="0" max="5" step="0.1" value={form.rating} onChange={(e) => set('rating', e.target.value)} />
            </div>
            <div style={{ display: 'flex', alignItems: 'center', gap: 8 }}>
              <input type="checkbox" id="is_active" checked={form.is_active} onChange={(e) => set('is_active', e.target.checked)} style={{ width: 'auto' }} />
              <label htmlFor="is_active" style={{ fontWeight: 500 }}>Activo (disponible para envíos)</label>
            </div>
            <div style={{ display: 'flex', gap: 10, justifyContent: 'flex-end', marginTop: 8 }}>
              <button className="btn btn-secondary" onClick={() => setModal(null)}>Cancelar</button>
              <button className="btn btn-primary" disabled={saving} onClick={handleSave}>
                {saving ? 'Guardando…' : 'Guardar'}
              </button>
            </div>
          </div>
        </Modal>
      )}
    </>
  );
}

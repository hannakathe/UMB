import { useEffect, useState, useCallback } from 'react';
import { Link, useSearchParams } from 'react-router-dom';
import { enviosApi } from '../../api/envios';
import StatusBadge from '../../components/StatusBadge';

const STATUS_OPTIONS = [
  { value: '', label: 'Todos los estados' },
  { value: 'en_bodega', label: 'En bodega' },
  { value: 'en_ruta', label: 'En ruta' },
  { value: 'en_entrega', label: 'En entrega' },
  { value: 'recibido', label: 'Recibido' },
  { value: 'incidencia', label: 'Incidencia' },
];

const SERVICE_OPTIONS = [
  { value: '', label: 'Todos los servicios' },
  { value: 'standard', label: 'Standard' },
  { value: 'prioritario', label: 'Prioritario' },
  { value: 'express', label: 'Express' },
];

export default function EnviosPage() {
  const [searchParams, setSearchParams] = useSearchParams();
  const [envios, setEnvios] = useState([]);
  const [total, setTotal] = useState(0);
  const [loading, setLoading] = useState(true);
  const [page, setPage] = useState(1);

  const [filters, setFilters] = useState({
    status: searchParams.get('status') ?? '',
    service_type: '',
    search: searchParams.get('search') ?? '',
    is_draft: '',
  });

  const load = useCallback(async () => {
    setLoading(true);
    try {
      const params = { page, ...filters };
      Object.keys(params).forEach((k) => { if (!params[k] && params[k] !== 0) delete params[k]; });
      const { data } = await enviosApi.list(params);
      setEnvios(data.results ?? data);
      setTotal(data.count ?? (data.results ?? data).length);
    } finally {
      setLoading(false);
    }
  }, [filters, page]);

  useEffect(() => { load(); }, [load]);

  const setFilter = (key, value) => {
    setFilters((p) => ({ ...p, [key]: value }));
    setPage(1);
  };

  const handleDelete = async (id) => {
    if (!confirm('¿Eliminar este envío?')) return;
    await enviosApi.remove(id);
    load();
  };

  return (
    <>
      <div className="page-header">
        <h1 className="page-header__title">Envíos <span style={{ color: 'var(--rr-gray-500)', fontWeight: 400 }}>({total})</span></h1>
        <Link to="/admin/envios/nuevo" className="btn btn-primary">＋ Nuevo Envío</Link>
      </div>

      {/* Filtros */}
      <div className="filters-bar">
        <select value={filters.status} onChange={(e) => setFilter('status', e.target.value)}>
          {STATUS_OPTIONS.map((o) => <option key={o.value} value={o.value}>{o.label}</option>)}
        </select>
        <select value={filters.service_type} onChange={(e) => setFilter('service_type', e.target.value)}>
          {SERVICE_OPTIONS.map((o) => <option key={o.value} value={o.value}>{o.label}</option>)}
        </select>
        <select value={filters.is_draft} onChange={(e) => setFilter('is_draft', e.target.value)}>
          <option value="">Todos</option>
          <option value="false">Activos</option>
          <option value="true">Borradores</option>
        </select>
        <input
          style={{ width: 220 }}
          placeholder="Buscar guía, nombre…"
          value={filters.search}
          onChange={(e) => setFilter('search', e.target.value)}
        />
      </div>

      <div className="card">
        {loading ? (
          <div className="spinner" />
        ) : envios.length === 0 ? (
          <div className="empty">
            <div className="empty__icon">📭</div>
            <div className="empty__text">No hay envíos con estos filtros</div>
          </div>
        ) : (
          <div className="table-wrap">
            <table>
              <thead>
                <tr>
                  <th>Guía</th>
                  <th>Remitente</th>
                  <th>Destinatario</th>
                  <th>Destino</th>
                  <th>Repartidor</th>
                  <th>Estado</th>
                  <th>Servicio</th>
                  <th>Fecha</th>
                  <th>Acciones</th>
                </tr>
              </thead>
              <tbody>
                {envios.map((e) => (
                  <tr key={e.id}>
                    <td>
                      <Link to={`/admin/envios/${e.id}`} style={{ color: 'var(--rr-red)', fontWeight: 600, fontFamily: 'monospace', fontSize: 12 }}>
                        {e.tracking_number}
                      </Link>
                      {e.is_draft && <span style={{ marginLeft: 4, fontSize: 10, background: 'var(--rr-gray-200)', padding: '1px 5px', borderRadius: 4 }}>borrador</span>}
                    </td>
                    <td>{e.sender_name}</td>
                    <td>{e.recipient_name}</td>
                    <td>{e.recipient_city || '—'}</td>
                    <td>{e.repartidor_name ?? <span style={{ color: 'var(--rr-gray-500)' }}>—</span>}</td>
                    <td><StatusBadge status={e.status} /></td>
                    <td style={{ textTransform: 'capitalize' }}>{e.service_type}</td>
                    <td style={{ whiteSpace: 'nowrap', fontSize: 12 }}>
                      {new Date(e.created_at).toLocaleDateString('es-CO')}
                    </td>
                    <td>
                      <div style={{ display: 'flex', gap: 4 }}>
                        <Link to={`/admin/envios/${e.id}`} className="btn btn-outline btn-sm">Ver</Link>
                        <button className="btn btn-secondary btn-sm" onClick={() => handleDelete(e.id)}>🗑</button>
                      </div>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}

        {/* Paginación */}
        {total > 20 && (
          <div style={{ display: 'flex', justifyContent: 'center', alignItems: 'center', gap: 10, marginTop: 16 }}>
            <button className="btn btn-outline btn-sm" disabled={page === 1} onClick={() => setPage((p) => p - 1)}>← Anterior</button>
            <span style={{ fontSize: 13 }}>Página {page} · {total} registros</span>
            <button className="btn btn-outline btn-sm" disabled={envios.length < 20} onClick={() => setPage((p) => p + 1)}>Siguiente →</button>
          </div>
        )}
      </div>
    </>
  );
}

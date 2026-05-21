import { useEffect, useState, useCallback } from 'react';
import { Link } from 'react-router-dom';
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

export default function MisEnviosPage() {
  const [envios, setEnvios] = useState([]);
  const [filtered, setFiltered] = useState([]);
  const [loading, setLoading] = useState(true);
  const [filterStatus, setFilterStatus] = useState('');
  const [search, setSearch] = useState('');

  const load = useCallback(async () => {
    setLoading(true);
    try {
      const { data } = await enviosApi.misEnvios();
      setEnvios(data);
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => { load(); }, [load]);

  // Filtrado local (la respuesta ya es el historial completo del operador)
  useEffect(() => {
    let result = envios;
    if (filterStatus) result = result.filter((e) => e.status === filterStatus);
    if (search) {
      const q = search.toLowerCase();
      result = result.filter(
        (e) =>
          e.tracking_number.toLowerCase().includes(q) ||
          e.recipient_name.toLowerCase().includes(q) ||
          (e.recipient_city ?? '').toLowerCase().includes(q) ||
          (e.repartidor_name ?? '').toLowerCase().includes(q)
      );
    }
    setFiltered(result);
  }, [envios, filterStatus, search]);

  return (
    <>
      <div className="page-header">
        <h1 className="page-header__title">
          Mis Envíos{' '}
          <span style={{ color: 'var(--rr-gray-500)', fontWeight: 400 }}>({filtered.length})</span>
        </h1>
        <Link to="/admin/envios/nuevo" className="btn btn-primary">＋ Nuevo Envío</Link>
      </div>

      <div className="filters-bar">
        <select value={filterStatus} onChange={(e) => setFilterStatus(e.target.value)}>
          {STATUS_OPTIONS.map((o) => (
            <option key={o.value} value={o.value}>{o.label}</option>
          ))}
        </select>
        <input
          style={{ width: 230 }}
          placeholder="Buscar guía, destinatario, ciudad…"
          value={search}
          onChange={(e) => setSearch(e.target.value)}
        />
      </div>

      <div className="card">
        {loading ? (
          <div className="spinner" />
        ) : filtered.length === 0 ? (
          <div className="empty">
            <div className="empty__icon">📭</div>
            <div className="empty__text">
              {envios.length === 0
                ? 'Aún no has creado ningún envío'
                : 'Sin resultados para los filtros aplicados'}
            </div>
            {envios.length === 0 && (
              <Link to="/admin/envios/nuevo" className="btn btn-primary" style={{ marginTop: 12 }}>
                Crear primer envío
              </Link>
            )}
          </div>
        ) : (
          <div className="table-wrap">
            <table>
              <thead>
                <tr>
                  <th>Guía</th>
                  <th>Destinatario</th>
                  <th>Ciudad destino</th>
                  <th>Repartidor</th>
                  <th>Estado</th>
                  <th>Servicio</th>
                  <th>Fecha</th>
                  <th></th>
                </tr>
              </thead>
              <tbody>
                {filtered.map((e) => (
                  <tr key={e.id}>
                    <td style={{ fontFamily: 'monospace', fontSize: 12, fontWeight: 600, color: 'var(--rr-red)' }}>
                      {e.tracking_number}
                    </td>
                    <td>{e.recipient_name}</td>
                    <td>{e.recipient_city || '—'}</td>
                    <td>
                      {e.repartidor_name ?? (
                        <span style={{ color: 'var(--rr-gray-500)' }}>Sin asignar</span>
                      )}
                    </td>
                    <td><StatusBadge status={e.status} /></td>
                    <td style={{ textTransform: 'capitalize', fontSize: 13 }}>{e.service_type}</td>
                    <td style={{ fontSize: 12, color: 'var(--rr-gray-500)', whiteSpace: 'nowrap' }}>
                      {new Date(e.created_at).toLocaleDateString('es-CO')}
                    </td>
                    <td>
                      <Link to={`/admin/envios/${e.id}`} className="btn btn-outline btn-sm">Ver</Link>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}
      </div>
    </>
  );
}

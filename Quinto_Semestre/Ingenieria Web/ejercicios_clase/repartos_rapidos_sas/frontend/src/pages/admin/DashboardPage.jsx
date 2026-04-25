import { useEffect, useState, useCallback } from 'react';
import { Link } from 'react-router-dom';
import { dashboardApi } from '../../api/dashboard';
import { enviosApi } from '../../api/envios';
import KPICard from '../../components/KPICard';
import StatusBadge from '../../components/StatusBadge';

const STATUS_OPTIONS = [
  { value: '', label: 'Todos los estados' },
  { value: 'en_bodega', label: 'En bodega' },
  { value: 'en_ruta', label: 'En ruta' },
  { value: 'en_entrega', label: 'En entrega' },
  { value: 'recibido', label: 'Recibido' },
  { value: 'incidencia', label: 'Incidencia' },
];

function downloadCSV(rows) {
  const header = 'Guía,Remitente,Destino,Repartidor,Estado,Servicio,Fecha\n';
  const body = rows.map((e) =>
    [
      e.tracking_number,
      e.sender_name,
      e.recipient_name,
      e.repartidor_name ?? '—',
      e.status_display,
      e.service_type_display,
      new Date(e.created_at).toLocaleDateString('es-CO'),
    ].join(',')
  ).join('\n');
  const blob = new Blob([header + body], { type: 'text/csv;charset=utf-8;' });
  const url = URL.createObjectURL(blob);
  const a = document.createElement('a');
  a.href = url; a.download = 'envios.csv'; a.click();
  URL.revokeObjectURL(url);
}

export default function DashboardPage() {
  const [stats, setStats] = useState(null);
  const [envios, setEnvios] = useState([]);
  const [total, setTotal] = useState(0);
  const [loading, setLoading] = useState(true);
  const [filters, setFilters] = useState({ status: '', search: '' });
  const [page, setPage] = useState(1);

  const loadData = useCallback(async () => {
    setLoading(true);
    try {
      const [statsRes, enviosRes] = await Promise.all([
        dashboardApi.stats(),
        enviosApi.list({ ...filters, is_draft: false, page }),
      ]);
      setStats(statsRes.data);
      setEnvios(enviosRes.data.results ?? enviosRes.data);
      setTotal(enviosRes.data.count ?? (enviosRes.data.results ?? enviosRes.data).length);
    } catch (e) {
      console.error(e);
    } finally {
      setLoading(false);
    }
  }, [filters, page]);

  useEffect(() => { loadData(); }, [loadData]);

  const handleFilter = (key, value) => {
    setFilters((p) => ({ ...p, [key]: value }));
    setPage(1);
  };

  const handleCambiarEstado = async (id, newStatus) => {
    await enviosApi.cambiarEstado(id, newStatus);
    loadData();
  };

  return (
    <>
      <div className="page-header">
        <h1 className="page-header__title">Dashboard</h1>
        <Link to="/admin/envios/nuevo" className="btn btn-primary">＋ Nuevo Envío</Link>
      </div>

      {/* KPIs */}
      <div className="kpi-grid">
        {stats ? (
          <>
            <KPICard
              label="Envíos hoy"
              value={stats.envios_hoy.total}
              deltaPct={stats.envios_hoy.delta_pct}
            />
            <KPICard
              label="En ruta"
              value={stats.en_ruta}
              sub="paquetes activos"
            />
            <KPICard
              label="Tasa de entrega"
              value={`${stats.tasa_entrega}%`}
              sub="meta diaria: 90%"
            />
            <KPICard
              label="Incidencias"
              value={stats.incidencias}
              sub={stats.incidencias > 0 ? '⚠ atención urgente' : 'sin alertas'}
              icon={stats.incidencias > 0 ? '🚨' : '✅'}
            />
          </>
        ) : (
          Array.from({ length: 4 }).map((_, i) => (
            <div key={i} className="card" style={{ height: 100, background: 'var(--rr-gray-100)' }} />
          ))
        )}
      </div>

      {/* Envíos activos */}
      <div className="card">
        <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', marginBottom: 16, flexWrap: 'wrap', gap: 10 }}>
          <span style={{ fontWeight: 700 }}>
            Envíos activos <span style={{ color: 'var(--rr-gray-500)', fontWeight: 400 }}>({total})</span>
          </span>
          <button className="btn btn-outline btn-sm" onClick={() => downloadCSV(envios)}>
            ⬇ Descargar CSV
          </button>
        </div>

        {/* Filtros */}
        <div className="filters-bar">
          <select value={filters.status} onChange={(e) => handleFilter('status', e.target.value)}>
            {STATUS_OPTIONS.map((o) => <option key={o.value} value={o.value}>{o.label}</option>)}
          </select>
          <input
            style={{ width: 200 }}
            placeholder="Buscar…"
            value={filters.search}
            onChange={(e) => handleFilter('search', e.target.value)}
          />
        </div>

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
                  <th>ID Guía</th>
                  <th>Remitente</th>
                  <th>Destino</th>
                  <th>Repartidor</th>
                  <th>Estado</th>
                  <th>Acciones</th>
                </tr>
              </thead>
              <tbody>
                {envios.map((e) => (
                  <tr key={e.id}>
                    <td>
                      <Link to={`/admin/envios/${e.id}`} style={{ color: 'var(--rr-red)', fontWeight: 600, fontFamily: 'monospace' }}>
                        {e.tracking_number}
                      </Link>
                    </td>
                    <td>{e.sender_name}</td>
                    <td>{e.recipient_name}{e.recipient_city ? ` · ${e.recipient_city}` : ''}</td>
                    <td>{e.repartidor_name ?? <span style={{ color: 'var(--rr-gray-500)' }}>—</span>}</td>
                    <td><StatusBadge status={e.status} /></td>
                    <td>
                      <div style={{ display: 'flex', gap: 4 }}>
                        <Link to={`/admin/envios/${e.id}`} className="btn btn-outline btn-sm">Ver</Link>
                        {e.status !== 'recibido' && e.status !== 'incidencia' && (
                          <select
                            className="btn btn-secondary btn-sm"
                            style={{ padding: '4px 6px', fontSize: 11 }}
                            value=""
                            onChange={(ev) => {
                              if (ev.target.value) handleCambiarEstado(e.id, ev.target.value);
                            }}
                          >
                            <option value="">Estado ▾</option>
                            <option value="en_bodega">En bodega</option>
                            <option value="en_ruta">En ruta</option>
                            <option value="en_entrega">En entrega</option>
                            <option value="recibido">Recibido</option>
                            <option value="incidencia">Incidencia</option>
                          </select>
                        )}
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
          <div style={{ display: 'flex', justifyContent: 'center', gap: 8, marginTop: 16 }}>
            <button className="btn btn-outline btn-sm" disabled={page === 1} onClick={() => setPage((p) => p - 1)}>← Anterior</button>
            <span style={{ lineHeight: '30px', fontSize: 13 }}>Página {page}</span>
            <button className="btn btn-outline btn-sm" disabled={envios.length < 20} onClick={() => setPage((p) => p + 1)}>Siguiente →</button>
          </div>
        )}
      </div>

      {/* Mapa placeholder */}
      <div className="card" style={{ marginTop: 20 }}>
        <div style={{ fontWeight: 700, marginBottom: 12 }}>🗺️ Mapa en vivo</div>
        <div className="map-placeholder" style={{ minHeight: 220 }}>
          <span style={{ fontSize: 32 }}>🗺️</span>
          <span>Mapa de entregas en tiempo real</span>
          <span style={{ fontSize: 11 }}>(integra Google Maps API con lat/lng de repartidores)</span>
        </div>
      </div>
    </>
  );
}

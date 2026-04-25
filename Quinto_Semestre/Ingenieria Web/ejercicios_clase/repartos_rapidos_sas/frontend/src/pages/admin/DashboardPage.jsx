import { useEffect, useState, useCallback } from 'react';
import { Link } from 'react-router-dom';
import { dashboardApi } from '../../api/dashboard';
import { enviosApi } from '../../api/envios';
import { useAuth } from '../../context/AuthContext';
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

// ── Dashboard del ADMINISTRADOR ───────────────────────────────────────────────
function AdminDashboard() {
  const [stats, setStats] = useState(null);
  const [envios, setEnvios] = useState([]);
  const [total, setTotal] = useState(0);
  const [loading, setLoading] = useState(true);
  const [filterStatus, setFilterStatus] = useState('');
  const [search, setSearch] = useState('');
  const [page, setPage] = useState(1);

  const load = useCallback(async () => {
    setLoading(true);
    try {
      const params = { is_draft: false, page };
      if (filterStatus) params.status = filterStatus;
      if (search) params.search = search;

      const [statsRes, enviosRes] = await Promise.all([
        dashboardApi.stats(),
        enviosApi.list(params),
      ]);
      setStats(statsRes.data);
      setEnvios(enviosRes.data.results ?? enviosRes.data);
      setTotal(enviosRes.data.count ?? (enviosRes.data.results ?? enviosRes.data).length);
    } finally {
      setLoading(false);
    }
  }, [filterStatus, search, page]);

  useEffect(() => { load(); }, [load]);

  const handleCambiarEstado = async (id, newStatus) => {
    await enviosApi.cambiarEstado(id, newStatus);
    load();
  };

  return (
    <>
      <div className="page-header">
        <h1 className="page-header__title">Dashboard — Administrador</h1>
        <Link to="/admin/envios/nuevo" className="btn btn-primary">＋ Nuevo Envío</Link>
      </div>

      {/* KPIs */}
      <div className="kpi-grid">
        {stats ? (
          <>
            <KPICard label="Envíos hoy (global)" value={stats.envios_hoy.total} deltaPct={stats.envios_hoy.delta_pct} />
            <KPICard label="En ruta" value={stats.en_ruta} sub="paquetes activos" />
            <KPICard label="Tasa de entrega" value={`${stats.tasa_entrega}%`} sub={`${stats.total_envios} envíos totales`} />
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

      {/* Tabla de todos los envíos */}
      <div className="card">
        <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', marginBottom: 14, flexWrap: 'wrap', gap: 10 }}>
          <span style={{ fontWeight: 700 }}>
            Todos los envíos <span style={{ color: 'var(--rr-gray-500)', fontWeight: 400 }}>({total})</span>
          </span>
          <div style={{ display: 'flex', gap: 8 }}>
            <Link to="/admin/envios" className="btn btn-outline btn-sm">Ver completo</Link>
            <button className="btn btn-outline btn-sm" onClick={() => downloadCSV(envios)}>⬇ CSV</button>
          </div>
        </div>

        <div className="filters-bar">
          <select value={filterStatus} onChange={(e) => { setFilterStatus(e.target.value); setPage(1); }}>
            {STATUS_OPTIONS.map((o) => <option key={o.value} value={o.value}>{o.label}</option>)}
          </select>
          <input
            style={{ width: 200 }}
            placeholder="Buscar…"
            value={search}
            onChange={(e) => { setSearch(e.target.value); setPage(1); }}
          />
        </div>

        {loading ? <div className="spinner" /> : envios.length === 0 ? (
          <div className="empty"><div className="empty__icon">📭</div><div className="empty__text">Sin resultados</div></div>
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
                            onChange={(ev) => { if (ev.target.value) handleCambiarEstado(e.id, ev.target.value); }}
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

        {total > 20 && (
          <div style={{ display: 'flex', justifyContent: 'center', gap: 8, marginTop: 16 }}>
            <button className="btn btn-outline btn-sm" disabled={page === 1} onClick={() => setPage(p => p - 1)}>← Anterior</button>
            <span style={{ lineHeight: '30px', fontSize: 13 }}>Página {page}</span>
            <button className="btn btn-outline btn-sm" disabled={envios.length < 20} onClick={() => setPage(p => p + 1)}>Siguiente →</button>
          </div>
        )}
      </div>
    </>
  );
}

// ── Dashboard del OPERADOR ────────────────────────────────────────────────────
function OperatorDashboard({ user }) {
  const [stats, setStats] = useState(null);
  const [recientes, setRecientes] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    Promise.all([dashboardApi.stats(), enviosApi.list({ is_draft: false, page: 1 })])
      .then(([statsRes, enviosRes]) => {
        setStats(statsRes.data);
        setRecientes((enviosRes.data.results ?? enviosRes.data).slice(0, 8));
      })
      .finally(() => setLoading(false));
  }, []);

  const nombre = user?.full_name || user?.username;

  return (
    <>
      <div className="page-header">
        <div>
          <div style={{ fontSize: 13, color: 'var(--rr-gray-500)', marginBottom: 4 }}>
            Bienvenido de nuevo 👋
          </div>
          <h1 className="page-header__title">{nombre}</h1>
        </div>
        <Link to="/admin/envios/nuevo" className="btn btn-primary">＋ Nuevo Envío</Link>
      </div>

      {/* KPIs personales */}
      <div className="kpi-grid">
        {stats ? (
          <>
            <KPICard label="Mis envíos hoy" value={stats.envios_hoy.total} deltaPct={stats.envios_hoy.delta_pct} />
            <KPICard label="En ruta ahora" value={stats.en_ruta} sub="de mis envíos" />
            <KPICard label="Tasa de entrega" value={`${stats.tasa_entrega}%`} sub={`${stats.total_envios} envíos totales`} />
            <KPICard
              label="Incidencias"
              value={stats.incidencias}
              sub={stats.incidencias > 0 ? '⚠ revisar' : 'sin alertas'}
              icon={stats.incidencias > 0 ? '🚨' : '✅'}
            />
          </>
        ) : (
          Array.from({ length: 4 }).map((_, i) => (
            <div key={i} className="card" style={{ height: 100, background: 'var(--rr-gray-100)' }} />
          ))
        )}
      </div>

      {/* Accesos rápidos */}
      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(3, 1fr)', gap: 12, marginBottom: 24 }}>
        {[
          { to: '/admin/envios/nuevo', icon: '📦', label: 'Crear envío', desc: 'Registrar un nuevo paquete' },
          { to: '/admin/mis-envios',   icon: '📋', label: 'Mis envíos',  desc: 'Ver historial completo' },
          { to: '/admin/borradores',   icon: '📝', label: 'Borradores',  desc: 'Envíos sin confirmar' },
        ].map(({ to, icon, label, desc }) => (
          <Link key={to} to={to} style={{ textDecoration: 'none' }}>
            <div className="card" style={{ textAlign: 'center', cursor: 'pointer', transition: 'box-shadow .15s' }}
              onMouseEnter={e => e.currentTarget.style.boxShadow = 'var(--shadow-md)'}
              onMouseLeave={e => e.currentTarget.style.boxShadow = 'var(--shadow)'}
            >
              <div style={{ fontSize: 28, marginBottom: 8 }}>{icon}</div>
              <div style={{ fontWeight: 700, marginBottom: 4 }}>{label}</div>
              <div style={{ fontSize: 12, color: 'var(--rr-gray-500)' }}>{desc}</div>
            </div>
          </Link>
        ))}
      </div>

      {/* Envíos recientes del operador */}
      <div className="card">
        <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', marginBottom: 14 }}>
          <span style={{ fontWeight: 700 }}>Mis envíos recientes</span>
          <Link to="/admin/mis-envios" className="btn btn-outline btn-sm">Ver todos</Link>
        </div>

        {loading ? <div className="spinner" /> : recientes.length === 0 ? (
          <div className="empty">
            <div className="empty__icon">📭</div>
            <div className="empty__text">Aún no has creado ningún envío</div>
            <Link to="/admin/envios/nuevo" className="btn btn-primary" style={{ marginTop: 12 }}>
              Crear primer envío
            </Link>
          </div>
        ) : (
          <div className="table-wrap">
            <table>
              <thead>
                <tr>
                  <th>Guía</th>
                  <th>Destinatario</th>
                  <th>Ciudad</th>
                  <th>Repartidor</th>
                  <th>Estado</th>
                  <th>Fecha</th>
                  <th></th>
                </tr>
              </thead>
              <tbody>
                {recientes.map((e) => (
                  <tr key={e.id}>
                    <td style={{ fontFamily: 'monospace', fontSize: 12, fontWeight: 600, color: 'var(--rr-red)' }}>
                      {e.tracking_number}
                    </td>
                    <td>{e.recipient_name}</td>
                    <td>{e.recipient_city || '—'}</td>
                    <td>{e.repartidor_name ?? <span style={{ color: 'var(--rr-gray-500)' }}>Sin asignar</span>}</td>
                    <td><StatusBadge status={e.status} /></td>
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

// ── Componente raíz: elige qué dashboard mostrar ──────────────────────────────
export default function DashboardPage() {
  const { user } = useAuth();
  return user?.role === 'admin'
    ? <AdminDashboard />
    : <OperatorDashboard user={user} />;
}

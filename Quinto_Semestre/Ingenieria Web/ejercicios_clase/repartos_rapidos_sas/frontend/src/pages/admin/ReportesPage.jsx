import { useEffect, useState } from 'react';
import { dashboardApi } from '../../api/dashboard';

const STATUS_LABELS = {
  en_bodega: 'En bodega', en_ruta: 'En ruta', en_entrega: 'En entrega',
  recibido: 'Recibido', incidencia: 'Incidencia',
};
const STATUS_COLORS = {
  en_bodega: 'var(--rr-blue)', en_ruta: '#f57f17', en_entrega: 'var(--rr-green)',
  recibido: '#6a1b9a', incidencia: 'var(--rr-red)',
};
const SERVICE_COLORS = { standard: 'var(--rr-blue)', prioritario: '#f57f17', express: 'var(--rr-red)' };

function BarChart({ data, colorMap, labelMap }) {
  if (!data?.length) return <p style={{ color: 'var(--rr-gray-500)', fontSize: 13 }}>Sin datos</p>;
  const max = Math.max(...data.map((d) => d.total), 1);
  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 10 }}>
      {data.map((d) => {
        const key = d.status ?? d.service_type;
        const pct = Math.round((d.total / max) * 100);
        return (
          <div key={key}>
            <div style={{ display: 'flex', justifyContent: 'space-between', fontSize: 12, marginBottom: 4 }}>
              <span>{labelMap?.[key] ?? key}</span>
              <strong>{d.total}</strong>
            </div>
            <div style={{ height: 10, background: 'var(--rr-gray-100)', borderRadius: 5, overflow: 'hidden' }}>
              <div style={{ height: '100%', width: `${pct}%`, background: colorMap?.[key] ?? 'var(--rr-red)', borderRadius: 5, transition: 'width .4s' }} />
            </div>
          </div>
        );
      })}
    </div>
  );
}

export default function ReportesPage() {
  const [data, setData] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    dashboardApi.reportes()
      .then(({ data }) => setData(data))
      .finally(() => setLoading(false));
  }, []);

  if (loading) return <div className="spinner" />;

  return (
    <>
      <div className="page-header">
        <h1 className="page-header__title">Reportes</h1>
      </div>

      <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: 16 }}>
        <div className="card">
          <div style={{ fontWeight: 700, marginBottom: 16 }}>Envíos por estado</div>
          <BarChart data={data?.por_estado} colorMap={STATUS_COLORS} labelMap={STATUS_LABELS} />
        </div>

        <div className="card">
          <div style={{ fontWeight: 700, marginBottom: 16 }}>Envíos por servicio</div>
          <BarChart data={data?.por_servicio} colorMap={SERVICE_COLORS} />
        </div>

        <div className="card" style={{ gridColumn: 'span 2' }}>
          <div style={{ fontWeight: 700, marginBottom: 16 }}>Top 10 repartidores</div>
          {data?.top_repartidores?.length ? (
            <div className="table-wrap">
              <table>
                <thead>
                  <tr>
                    <th>#</th>
                    <th>Repartidor</th>
                    <th>Total envíos</th>
                    <th>Entregados</th>
                    <th>Tasa</th>
                  </tr>
                </thead>
                <tbody>
                  {data.top_repartidores.map((r, i) => {
                    const tasa = r.total ? Math.round(r.entregados / r.total * 100) : 0;
                    return (
                      <tr key={r.repartidor__name}>
                        <td style={{ color: 'var(--rr-gray-500)' }}>{i + 1}</td>
                        <td style={{ fontWeight: 600 }}>{r.repartidor__name}</td>
                        <td>{r.total}</td>
                        <td>{r.entregados}</td>
                        <td>
                          <div style={{ display: 'flex', alignItems: 'center', gap: 8 }}>
                            <div style={{ flex: 1, height: 6, background: 'var(--rr-gray-100)', borderRadius: 3, overflow: 'hidden' }}>
                              <div style={{ height: '100%', width: `${tasa}%`, background: 'var(--rr-green)', borderRadius: 3 }} />
                            </div>
                            <span style={{ fontSize: 12, minWidth: 36 }}>{tasa}%</span>
                          </div>
                        </td>
                      </tr>
                    );
                  })}
                </tbody>
              </table>
            </div>
          ) : (
            <div className="empty">
              <div className="empty__icon">📊</div>
              <div className="empty__text">Aún no hay datos suficientes</div>
            </div>
          )}
        </div>
      </div>
    </>
  );
}

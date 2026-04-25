import { useEffect, useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { enviosApi } from '../../api/envios';
import StatusBadge from '../../components/StatusBadge';

export default function BorradoresPage() {
  const [borradores, setBorradores] = useState([]);
  const [loading, setLoading] = useState(true);
  const [confirming, setConfirming] = useState(null); // id del envío que se está confirmando
  const navigate = useNavigate();

  const load = async () => {
    setLoading(true);
    try {
      const { data } = await enviosApi.borradores();
      setBorradores(data);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => { load(); }, []);

  const handleConfirmar = async (id) => {
    setConfirming(id);
    try {
      await enviosApi.update(id, { is_draft: false });
      load(); // refrescar lista
    } finally {
      setConfirming(null);
    }
  };

  const handleEliminar = async (id) => {
    if (!confirm('¿Eliminar este borrador? Esta acción no se puede deshacer.')) return;
    await enviosApi.remove(id);
    load();
  };

  return (
    <>
      <div className="page-header">
        <h1 className="page-header__title">
          Borradores{' '}
          <span style={{ color: 'var(--rr-gray-500)', fontWeight: 400 }}>({borradores.length})</span>
        </h1>
        <Link to="/admin/envios/nuevo" className="btn btn-primary">＋ Nuevo Envío</Link>
      </div>

      {/* Info */}
      <div style={{
        background: '#fffbeb',
        border: '1px solid #fde68a',
        borderRadius: 8,
        padding: '10px 16px',
        fontSize: 13,
        color: '#92400e',
        marginBottom: 16,
      }}>
        📝 Los borradores son envíos guardados pero aún no confirmados. Puedes editarlos, confirmarlos o eliminarlos.
      </div>

      <div className="card">
        {loading ? (
          <div className="spinner" />
        ) : borradores.length === 0 ? (
          <div className="empty">
            <div className="empty__icon">📝</div>
            <div className="empty__text">No tienes borradores pendientes</div>
            <Link to="/admin/envios/nuevo" className="btn btn-primary" style={{ marginTop: 12 }}>
              Crear envío
            </Link>
          </div>
        ) : (
          <div className="table-wrap">
            <table>
              <thead>
                <tr>
                  <th>Guía (borrador)</th>
                  <th>Remitente</th>
                  <th>Destinatario</th>
                  <th>Ciudad destino</th>
                  <th>Servicio</th>
                  <th>Creado</th>
                  <th>Acciones</th>
                </tr>
              </thead>
              <tbody>
                {borradores.map((e) => (
                  <tr key={e.id}>
                    <td style={{ fontFamily: 'monospace', fontSize: 12, fontWeight: 600, color: 'var(--rr-gray-500)' }}>
                      {e.tracking_number}
                      <span style={{
                        marginLeft: 6,
                        fontSize: 10,
                        background: 'var(--rr-gray-200)',
                        padding: '1px 5px',
                        borderRadius: 4,
                        fontFamily: 'sans-serif',
                      }}>
                        borrador
                      </span>
                    </td>
                    <td>{e.sender_name}</td>
                    <td>{e.recipient_name}</td>
                    <td>{e.recipient_city || '—'}</td>
                    <td style={{ textTransform: 'capitalize', fontSize: 13 }}>{e.service_type}</td>
                    <td style={{ fontSize: 12, color: 'var(--rr-gray-500)', whiteSpace: 'nowrap' }}>
                      {new Date(e.created_at).toLocaleDateString('es-CO')}
                    </td>
                    <td>
                      <div style={{ display: 'flex', gap: 4 }}>
                        <Link to={`/admin/envios/${e.id}`} className="btn btn-outline btn-sm">Editar</Link>
                        <button
                          className="btn btn-primary btn-sm"
                          disabled={confirming === e.id}
                          onClick={() => handleConfirmar(e.id)}
                        >
                          {confirming === e.id ? '…' : '✔ Confirmar'}
                        </button>
                        <button
                          className="btn btn-secondary btn-sm"
                          onClick={() => handleEliminar(e.id)}
                        >
                          🗑
                        </button>
                      </div>
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

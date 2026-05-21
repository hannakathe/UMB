const LABELS = {
  en_bodega: 'En bodega',
  en_ruta: 'En ruta',
  en_entrega: 'En entrega',
  recibido: 'Recibido',
  incidencia: 'Incidencia',
};

export default function StatusBadge({ status }) {
  return (
    <span className={`badge badge-${status}`}>
      {LABELS[status] ?? status}
    </span>
  );
}

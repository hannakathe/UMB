const STEPS = [
  { key: 'en_bodega',  label: 'En bodega',   icon: '🏬' },
  { key: 'en_ruta',    label: 'En ruta',      icon: '🚴' },
  { key: 'en_entrega', label: 'En entrega',   icon: '📍' },
  { key: 'recibido',   label: 'Recibido',     icon: '✅' },
];

const ORDER = ['en_bodega', 'en_ruta', 'en_entrega', 'recibido'];

function stepState(stepKey, currentStatus) {
  if (currentStatus === 'incidencia') {
    // show all as pending except en_bodega which is done
    if (stepKey === 'en_bodega') return 'done';
    return 'pending';
  }
  const cur = ORDER.indexOf(currentStatus);
  const idx = ORDER.indexOf(stepKey);
  if (idx < cur) return 'done';
  if (idx === cur) return 'current';
  return 'pending';
}

function fmtTimestamp(ts) {
  if (!ts) return null;
  const d = new Date(ts);
  return d.toLocaleDateString('es-CO', { day: '2-digit', month: 'short' }) +
    ' · ' + d.toLocaleTimeString('es-CO', { hour: '2-digit', minute: '2-digit' });
}

export default function StatusTimeline({ currentStatus, historial = [] }) {
  // Build a map of status → timestamp from historial
  const tsMap = {};
  historial.forEach((h) => { tsMap[h.status] = h.timestamp; });

  return (
    <div className="timeline">
      {STEPS.map((step) => {
        const state = stepState(step.key, currentStatus);
        return (
          <div key={step.key} className={`timeline-step ${state}`}>
            <div className="timeline-step__icon">
              {state === 'done' ? '✓' : state === 'current' ? step.icon : '○'}
            </div>
            <div className="timeline-step__body">
              <div className="timeline-step__label">{step.label}</div>
              {tsMap[step.key] && (
                <div className="timeline-step__time">
                  {fmtTimestamp(tsMap[step.key])}
                  {state === 'current' && (
                    <span style={{ marginLeft: 8, color: 'var(--rr-red)', fontWeight: 600 }}>HOY</span>
                  )}
                </div>
              )}
              {state === 'pending' && !tsMap[step.key] && (
                <div className="timeline-step__time">Próximo</div>
              )}
            </div>
          </div>
        );
      })}

      {currentStatus === 'incidencia' && (
        <div className="timeline-step current" style={{ marginTop: 8 }}>
          <div className="timeline-step__icon">⚠</div>
          <div className="timeline-step__body">
            <div className="timeline-step__label" style={{ color: 'var(--rr-red)' }}>Incidencia reportada</div>
            {tsMap['incidencia'] && (
              <div className="timeline-step__time">{fmtTimestamp(tsMap['incidencia'])}</div>
            )}
          </div>
        </div>
      )}
    </div>
  );
}

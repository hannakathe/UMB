export default function KPICard({ label, value, sub, delta, deltaPct, icon }) {
  const positive = deltaPct > 0;
  const negative = deltaPct < 0;

  return (
    <div className="card kpi-card">
      <div className="kpi-card__label">{label}</div>
      <div className="kpi-card__value" style={icon ? { display: 'flex', alignItems: 'center', gap: 8 } : {}}>
        {value}
        {icon && <span style={{ fontSize: 24 }}>{icon}</span>}
      </div>
      {sub && <div className="kpi-card__sub">{sub}</div>}
      {deltaPct !== undefined && (
        <div className={`kpi-card__delta ${positive ? 'positive' : negative ? 'negative' : ''}`}>
          {positive ? '▲' : negative ? '▼' : '→'} {Math.abs(deltaPct)}% vs ayer
        </div>
      )}
    </div>
  );
}

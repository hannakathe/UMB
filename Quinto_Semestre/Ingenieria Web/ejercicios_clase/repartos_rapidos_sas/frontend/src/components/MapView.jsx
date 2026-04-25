/**
 * MapView — Leaflet + OpenStreetMap + Nominatim geocoding (todo gratuito, sin API key)
 *
 * Props:
 *   origin      { name, city, address }
 *   destination { name, city, address }
 *   height      number  (default 320)
 */

import { useEffect, useRef, useState } from 'react';
import L from 'leaflet';
import 'leaflet/dist/leaflet.css';

// ── Geocoding via Nominatim (OpenStreetMap) ──────────────────────────────────
// Diccionario local como caché/fallback offline
const COORDS_CACHE = {};

async function geocodeCity(city) {
  if (!city) return null;
  const key = city.trim();
  if (COORDS_CACHE[key]) return COORDS_CACHE[key];

  try {
    const q = encodeURIComponent(`${key}, Colombia`);
    const res = await fetch(
      `https://nominatim.openstreetmap.org/search?q=${q}&format=json&limit=1&countrycodes=co&accept-language=es`,
    );
    const json = await res.json();
    if (json.length > 0) {
      const coords = [parseFloat(json[0].lat), parseFloat(json[0].lon)];
      COORDS_CACHE[key] = coords;
      return coords;
    }
  } catch {
    // sin conexión → fallback estático
  }

  // Fallback estático para las ciudades más comunes
  const STATIC = {
    'bogotá':       [4.7110,  -74.0721],
    'bogota':       [4.7110,  -74.0721],
    'medellín':     [6.2442,  -75.5812],
    'medellin':     [6.2442,  -75.5812],
    'cali':         [3.4516,  -76.5320],
    'barranquilla': [10.9685, -74.7813],
    'cartagena':    [10.3997, -75.5144],
    'bucaramanga':  [7.1254,  -73.1198],
    'pereira':      [4.8133,  -75.6961],
    'manizales':    [5.0703,  -75.5138],
    'cúcuta':       [7.8939,  -72.5078],
    'cucuta':       [7.8939,  -72.5078],
    'ibagué':       [4.4389,  -75.2322],
    'ibague':       [4.4389,  -75.2322],
    'santa marta':  [11.2408, -74.2110],
    'villavicencio':[4.1420,  -73.6266],
    'pasto':        [1.2136,  -77.2811],
    'armenia':      [4.5339,  -75.6811],
    'montería':     [8.7575,  -75.8899],
    'monteria':     [8.7575,  -75.8899],
    'neiva':        [2.9273,  -75.2819],
    'popayán':      [2.4448,  -76.6147],
    'popayan':      [2.4448,  -76.6147],
    'sincelejo':    [9.3047,  -75.3978],
    'valledupar':   [10.4781, -73.2537],
    'tunja':        [5.5353,  -73.3678],
  };
  return STATIC[key.toLowerCase()] ?? null;
}

// ── Íconos SVG inline (sin imágenes PNG de Leaflet → evita bugs de Vite) ────
function makeIcon(color) {
  const svg = `<svg xmlns="http://www.w3.org/2000/svg" width="26" height="34" viewBox="0 0 26 34">
    <path d="M13 0C5.82 0 0 5.82 0 13c0 8.667 13 21 13 21S26 21.667 26 13C26 5.82 20.18 0 13 0z"
          fill="${color}" stroke="#fff" stroke-width="1.5"/>
    <circle cx="13" cy="13" r="5" fill="#fff"/>
  </svg>`;
  return L.divIcon({
    html: svg,
    className: '',
    iconSize: [26, 34],
    iconAnchor: [13, 34],
    popupAnchor: [0, -34],
  });
}

const ICON_ORIGIN      = makeIcon('#22c55e'); // verde
const ICON_DESTINATION = makeIcon('#e53935'); // rojo marca

// ── Componente ────────────────────────────────────────────────────────────────
export default function MapView({ origin, destination, height = 320 }) {
  const containerRef = useRef(null);
  const mapRef       = useRef(null);
  const [loading, setLoading]   = useState(true);
  const [noCoords, setNoCoords] = useState(false);

  useEffect(() => {
    if (!containerRef.current) return;

    let cancelled = false;
    setLoading(true);
    setNoCoords(false);

    // Destruir mapa anterior si existe
    if (mapRef.current) {
      mapRef.current.remove();
      mapRef.current = null;
    }

    (async () => {
      // Geocodificar ambas ciudades en paralelo
      const [oc, dc] = await Promise.all([
        geocodeCity(origin?.city),
        geocodeCity(destination?.city),
      ]);

      if (cancelled || !containerRef.current) return;

      if (!oc && !dc) {
        setLoading(false);
        setNoCoords(true);
        return;
      }

      setLoading(false);

      const center = oc ?? dc;
      const map = L.map(containerRef.current).setView(center, 7);
      mapRef.current = map;

      // Capa base OpenStreetMap
      L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
        attribution: '© <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a>',
        maxZoom: 18,
      }).addTo(map);

      // Marcador origen
      if (oc) {
        L.marker(oc, { icon: ICON_ORIGIN })
          .addTo(map)
          .bindPopup(
            `<b>📦 Origen</b><br/>
             ${origin?.name ?? 'Remitente'}<br/>
             <span style="color:#666;font-size:12px">${[origin?.address, origin?.city].filter(Boolean).join(' · ')}</span>`
          );
      }

      // Marcador destino
      if (dc) {
        L.marker(dc, { icon: ICON_DESTINATION })
          .addTo(map)
          .bindPopup(
            `<b>📍 Destino</b><br/>
             ${destination?.name ?? 'Destinatario'}<br/>
             <span style="color:#666;font-size:12px">${[destination?.address, destination?.city].filter(Boolean).join(' · ')}</span>`
          );
      }

      // Línea de ruta punteada
      if (oc && dc) {
        L.polyline([oc, dc], {
          color: '#6366f1',
          weight: 3,
          opacity: 0.75,
          dashArray: '8 6',
        }).addTo(map);

        map.fitBounds(L.latLngBounds([oc, dc]), { padding: [48, 48] });
      }
    })().catch(console.error);

    return () => {
      cancelled = true;
      if (mapRef.current) {
        mapRef.current.remove();
        mapRef.current = null;
      }
    };
  }, [origin?.city, destination?.city]);

  // Datos para el selector
  const puntos = [
    origin?.city      && { label: `📦 Origen — ${origin.city}`,           key: 'o' },
    destination?.city && { label: `📍 Destino — ${destination.city}`,     key: 'd' },
  ].filter(Boolean);

  const flyToCity = async (e) => {
    const key = e.target.value;
    if (!mapRef.current || !key) return;
    const city = key === 'o' ? origin?.city : destination?.city;
    const coords = await geocodeCity(city);
    if (coords) mapRef.current.setView(coords, 13);
    e.target.value = '';
  };

  return (
    <div>
      {/* Selector — igual al ejemplo de clase */}
      {puntos.length > 0 && (
        <div style={{ marginBottom: 8 }}>
          <select
            defaultValue=""
            onChange={flyToCity}
            style={{
              width: '100%', padding: '6px 10px',
              borderRadius: 6, border: '1px solid var(--rr-gray-300)', fontSize: 13,
            }}
          >
            <option value="" disabled>Ir a…</option>
            {puntos.map((p) => (
              <option key={p.key} value={p.key}>{p.label}</option>
            ))}
          </select>
        </div>
      )}

      {/* Leyenda */}
      <div style={{ display: 'flex', gap: 16, marginBottom: 8, fontSize: 12, color: 'var(--rr-gray-600, #555)' }}>
        {[
          { color: '#22c55e', label: 'Origen' },
          { color: '#e53935', label: 'Destino' },
          { color: '#6366f1', label: 'Ruta', line: true },
        ].map(({ color, label, line }) => (
          <span key={label} style={{ display: 'flex', alignItems: 'center', gap: 4 }}>
            {line
              ? <span style={{ display:'inline-block', width:18, height:2, background:color, verticalAlign:'middle' }} />
              : <span style={{ display:'inline-block', width:10, height:10, borderRadius:'50%', background:color }} />
            }
            {label}
          </span>
        ))}
      </div>

      {/* Contenedor del mapa */}
      <div style={{ position: 'relative', height, borderRadius: 8, overflow: 'hidden', border: '1px solid var(--rr-gray-200)' }}>
        {/* Overlay de carga */}
        {loading && (
          <div style={{
            position: 'absolute', inset: 0, zIndex: 1000,
            background: 'rgba(255,255,255,.85)',
            display: 'flex', flexDirection: 'column', alignItems: 'center', justifyContent: 'center', gap: 10,
          }}>
            <div className="spinner" style={{ margin: 0 }} />
            <span style={{ fontSize: 12, color: 'var(--rr-gray-500)' }}>Cargando mapa…</span>
          </div>
        )}
        {noCoords && (
          <div style={{
            position: 'absolute', inset: 0, zIndex: 1000,
            display: 'flex', flexDirection: 'column', alignItems: 'center', justifyContent: 'center', gap: 8,
            background: 'var(--rr-gray-100)',
          }}>
            <span style={{ fontSize: 28 }}>🗺️</span>
            <span style={{ fontSize: 13, color: 'var(--rr-gray-500)' }}>No se encontraron coordenadas para las ciudades del envío</span>
          </div>
        )}
        <div ref={containerRef} style={{ height: '100%', width: '100%' }} />
      </div>
    </div>
  );
}

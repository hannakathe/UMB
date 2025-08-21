// Módulo de utilidades para consumir la API de Dragon Ball
// Documentación: https://web.dragonball-api.com/documentation
// Endpoints usados: GET /planets  y  GET /planets/:id

const API_BASE = "https://dragonball-api.com/api"; // base pública actual

/**
 * Envuelve fetch con manejo de errores y mensaje claro.
 * @param {string} url
 * @returns {Promise<any>}
 */
async function safeFetch(url) {
  const res = await fetch(url);
  if (!res.ok) {
    const text = await res.text().catch(() => "");
    throw new Error(`Error ${res.status} al consultar API: ${text || res.statusText}`);
  }
  return res.json();
}

/**
 * Obtiene planetas con soporte de paginación y filtro por nombre.
 * @param {number} page
 * @param {string} [name]
 */
export async function getPlanets(page = 1, name = "") {
  const params = new URLSearchParams();
  params.set("page", String(page));
  if (name?.trim()) params.set("name", name.trim());
  const url = `${API_BASE}/planets?${params.toString()}`;
  return safeFetch(url);
}

/**
 * Obtiene el detalle de un planeta por id.
 * @param {string|number} id
 */
export async function getPlanetById(id) {
  const url = `${API_BASE}/planets/${id}`;
  return safeFetch(url);
}

/**
 * Devuelve un texto legible en caso de valor nulo/indefinido.
 */
export const fmt = (v, fallback = "—") =>
  v === null || v === undefined || v === "" ? fallback : String(v);

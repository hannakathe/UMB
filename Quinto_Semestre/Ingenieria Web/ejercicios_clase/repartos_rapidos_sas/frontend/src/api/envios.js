import client from './client';
import axios from 'axios';

const BASE_URL = import.meta.env.VITE_API_URL ?? '/api';

export const enviosApi = {
  // ── Listado con filtros opcionales ──────────────────────────────────────
  list: (params = {}) => client.get('/envios/', { params }),

  // ── Detalle por id ───────────────────────────────────────────────────────
  get: (id) => client.get(`/envios/${id}/`),

  // ── Crear envío (borrador o definitivo) ──────────────────────────────────
  create: (data) => client.post('/envios/', data),

  // ── Actualizar campos del envío ──────────────────────────────────────────
  update: (id, data) => client.patch(`/envios/${id}/`, data),

  // ── Eliminar ─────────────────────────────────────────────────────────────
  remove: (id) => client.delete(`/envios/${id}/`),

  // ── Cambiar estado ────────────────────────────────────────────────────────
  cambiarEstado: (id, status, notes = '') =>
    client.post(`/envios/${id}/cambiar-estado/`, { status, notes }),

  // ── Mis envíos (historial personal del operador) ─────────────────────────
  misEnvios: () => client.get('/envios/mis-envios/'),

  // ── Borradores ────────────────────────────────────────────────────────────
  borradores: () => client.get('/envios/borradores/'),

  // ── Rastreo público (sin auth) ────────────────────────────────────────────
  rastrear: (trackingNumber) =>
    axios.get(`${BASE_URL}/envios/rastrear/${trackingNumber}/`),
};

import axios from 'axios';

const BASE_URL = import.meta.env.VITE_API_URL ?? '/api';

const client = axios.create({ baseURL: BASE_URL });

// Adjunta el access token a cada petición autenticada
client.interceptors.request.use((config) => {
  const token = localStorage.getItem('access');
  if (token) config.headers.Authorization = `Bearer ${token}`;
  return config;
});

// Si el servidor devuelve 401, intenta renovar el token
client.interceptors.response.use(
  (res) => res,
  async (err) => {
    const original = err.config;
    if (err.response?.status === 401 && !original._retry) {
      original._retry = true;
      try {
        const refresh = localStorage.getItem('refresh');
        if (!refresh) throw new Error('no refresh token');
        const { data } = await axios.post(`${BASE_URL}/auth/refresh/`, { refresh });
        localStorage.setItem('access', data.access);
        original.headers.Authorization = `Bearer ${data.access}`;
        return client(original);
      } catch {
        localStorage.removeItem('access');
        localStorage.removeItem('refresh');
        window.location.href = '/login';
      }
    }
    return Promise.reject(err);
  },
);

export default client;

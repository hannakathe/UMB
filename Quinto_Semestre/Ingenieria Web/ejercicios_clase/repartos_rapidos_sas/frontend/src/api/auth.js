import axios from 'axios';
import client from './client';

const BASE_URL = import.meta.env.VITE_API_URL ?? '/api';

export const authApi = {
  login: (username, password) =>
    axios.post(`${BASE_URL}/auth/login/`, { username, password }),

  logout: (refresh) =>
    client.post('/auth/logout/', { refresh }),

  me: () => client.get('/auth/me/'),

  listUsers: () => client.get('/auth/users/'),

  createUser: (data) => client.post('/auth/users/', data),
};

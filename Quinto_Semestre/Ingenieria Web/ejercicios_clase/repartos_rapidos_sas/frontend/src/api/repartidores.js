import client from './client';

export const repartidoresApi = {
  list: (params = {}) => client.get('/repartidores/', { params }),
  get: (id) => client.get(`/repartidores/${id}/`),
  create: (data) => client.post('/repartidores/', data),
  update: (id, data) => client.patch(`/repartidores/${id}/`, data),
  remove: (id) => client.delete(`/repartidores/${id}/`),
};

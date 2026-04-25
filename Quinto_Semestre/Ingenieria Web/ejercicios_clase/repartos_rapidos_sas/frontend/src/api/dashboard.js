import client from './client';

export const dashboardApi = {
  stats: () => client.get('/dashboard/stats/'),
  reportes: () => client.get('/dashboard/reportes/'),
};

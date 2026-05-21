import { createContext, useContext, useState, useEffect, useCallback } from 'react';
import { authApi } from '../api/auth';

const AuthContext = createContext(null);

export function AuthProvider({ children }) {
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true); // true mientras se verifica la sesión

  // Al montar, si hay tokens guardados, recupera el perfil
  useEffect(() => {
    const access = localStorage.getItem('access');
    if (!access) { setLoading(false); return; }

    authApi.me()
      .then(({ data }) => setUser(data))
      .catch(() => {
        localStorage.removeItem('access');
        localStorage.removeItem('refresh');
      })
      .finally(() => setLoading(false));
  }, []);

  const login = useCallback(async (username, password) => {
    const { data } = await authApi.login(username, password);
    localStorage.setItem('access', data.access);
    localStorage.setItem('refresh', data.refresh);
    setUser(data.user);
    return data.user;
  }, []);

  const logout = useCallback(async () => {
    const refresh = localStorage.getItem('refresh');
    try { await authApi.logout(refresh); } catch { /* ignore */ }
    localStorage.removeItem('access');
    localStorage.removeItem('refresh');
    setUser(null);
  }, []);

  return (
    <AuthContext.Provider value={{ user, loading, login, logout }}>
      {children}
    </AuthContext.Provider>
  );
}

export function useAuth() {
  const ctx = useContext(AuthContext);
  if (!ctx) throw new Error('useAuth debe usarse dentro de <AuthProvider>');
  return ctx;
}

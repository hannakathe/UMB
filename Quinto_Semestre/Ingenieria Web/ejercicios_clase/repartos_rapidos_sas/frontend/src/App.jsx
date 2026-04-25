import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import { AuthProvider } from './context/AuthContext';
import ProtectedRoute from './components/ProtectedRoute';
import AdminLayout from './components/AdminLayout';

// Páginas públicas
import TrackingPage from './pages/TrackingPage';
import TrackingResultPage from './pages/TrackingResultPage';
import LoginPage from './pages/LoginPage';

// Páginas admin
import DashboardPage from './pages/admin/DashboardPage';
import EnviosPage from './pages/admin/EnviosPage';
import NewEnvioPage from './pages/admin/NewEnvioPage';
import EnvioDetailPage from './pages/admin/EnvioDetailPage';
import RepartidoresPage from './pages/admin/RepartidoresPage';
import ReportesPage from './pages/admin/ReportesPage';

export default function App() {
  return (
    <AuthProvider>
      <BrowserRouter>
        <Routes>
          {/* ── Públicas ─────────────────────────────────────────── */}
          <Route path="/" element={<TrackingPage />} />
          <Route path="/rastrear/:trackingNumber" element={<TrackingResultPage />} />
          <Route path="/login" element={<LoginPage />} />

          {/* ── Admin (requieren autenticación) ──────────────────── */}
          <Route element={<ProtectedRoute />}>
            <Route element={<AdminLayout />}>
              <Route path="/admin/dashboard" element={<DashboardPage />} />
              <Route path="/admin/envios" element={<EnviosPage />} />
              <Route path="/admin/envios/nuevo" element={<NewEnvioPage />} />
              <Route path="/admin/envios/:id" element={<EnvioDetailPage />} />
              <Route path="/admin/repartidores" element={<RepartidoresPage />} />
              <Route path="/admin/reportes" element={<ReportesPage />} />
            </Route>
          </Route>

          {/* Redirect genérico */}
          <Route path="/admin" element={<Navigate to="/admin/dashboard" replace />} />
          <Route path="*" element={<Navigate to="/" replace />} />
        </Routes>
      </BrowserRouter>
    </AuthProvider>
  );
}

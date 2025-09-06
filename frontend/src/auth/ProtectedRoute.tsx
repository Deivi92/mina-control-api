import React from 'react';
import { Navigate, Outlet } from 'react-router-dom';
import { useAuth } from './useAuth';
import { Box, CircularProgress } from '@mui/material';

/**
 * Componente de orden superior para proteger rutas.
 * Verifica si el usuario está autenticado o si el modo de desarrollo permite un bypass.
 */
export const ProtectedRoute: React.FC = () => {
  const { isAuthenticated, isLoading } = useAuth();

  // La variable de entorno VITE_BYPASS_AUTH es leída aquí.
  // Vite reemplaza `import.meta.env.VITE_BYPASS_AUTH` con el valor real durante el build.
  const bypassAuth = import.meta.env.VITE_BYPASS_AUTH === 'true';

  // 1. Si estamos verificando el estado de autenticación, mostramos un spinner.
  if (isLoading) {
    return (
      <Box sx={{ display: 'flex', justifyContent: 'center', alignItems: 'center', height: '100vh' }}>
        <CircularProgress />
      </Box>
    );
  }

  // 2. Si el usuario está autenticado O si el bypass está activado, permitimos el acceso.
  if (isAuthenticated || bypassAuth) {
    return <Outlet />; // Outlet renderiza el componente hijo de la ruta (ej. <DashboardPage />)
  }

  // 3. Si no se cumple ninguna de las condiciones anteriores, redirigimos al login.
  return <Navigate to="/login" replace />;
};

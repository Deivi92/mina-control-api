import { Outlet, Link } from 'react-router-dom';
import { AuthProvider, useAuth } from '../auth/hooks/useAuth';
import { Box, Button } from '@mui/material';

// Creamos un componente interno para poder acceder al contexto de autenticación
const AppLayout = () => {
  const { isAuthenticated, logout } = useAuth(); // Obtenemos la función de logout

  return (
    <>
      {/* La navegación solo se muestra si el usuario está autenticado */}
      {isAuthenticated && (
        <Box component="nav" sx={{ p: 1, borderBottom: '1px solid #ddd', display: 'flex', gap: 1 }}>
          <Button component={Link} to="/dashboard">
            Dashboard
          </Button>
          <Button component={Link} to="/empleados">
            Empleados
          </Button>
          <Button onClick={logout} sx={{ ml: 'auto' }}> {/* Botón de Logout a la derecha */}
            Cerrar Sesión
          </Button>
        </Box>
      )}
      <main>
        <Outlet />
      </main>
    </>
  );
};

function App() {
  return (
    // AuthProvider nos da acceso global al estado de autenticación
    <AuthProvider>
      <AppLayout />
    </AuthProvider>
  );
}

export default App;


import { Container, Typography, Button } from '@mui/material';
import { useAuth } from '../auth/hooks/useAuth';

export const DashboardPage = () => {
  const { user, logout } = useAuth();

  return (
    <Container>
      <Typography variant="h4" component="h1" gutterBottom>
        Dashboard
      </Typography>
      <Typography variant="h6" component="h2" gutterBottom>
        Bienvenido, {user?.email}!
      </Typography>
      <Button variant="contained" color="primary" onClick={logout}>
        Cerrar Sesión
      </Button>
    </Container>
  );
};
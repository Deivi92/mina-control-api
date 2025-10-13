import { Container, Typography, Button, Box } from '@mui/material';
import { useAuth } from '../../auth/hooks/useAuth';

export const DashboardPage = () => {
  const { user, logout } = useAuth();

  return (
    <Container>
      <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
        <Typography variant="h4" component="h1" gutterBottom>
          Dashboard
        </Typography>
        <Button variant="contained" color="secondary" onClick={logout}>
          Cerrar Sesión
        </Button>
      </Box>
      <Typography variant="h6" component="h2" gutterBottom>
        Bienvenido, {user?.email}!
      </Typography>
      
    </Container>
  );
};
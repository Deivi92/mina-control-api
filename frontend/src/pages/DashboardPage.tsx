import { Container, Typography } from '@mui/material';
import { useAuth } from '../auth/hooks/useAuth';

export const DashboardPage = () => {
  const { user } = useAuth();

  return (
    <Container>
      <Typography variant="h4" component="h1" gutterBottom>
        Dashboard
      </Typography>
      <Typography variant="h6" component="h2" gutterBottom>
        Bienvenido, {user?.email}!
      </Typography>
      
    </Container>
  );
};
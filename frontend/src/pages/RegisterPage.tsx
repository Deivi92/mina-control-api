import React, { useState } from 'react';
import { Link as RouterLink } from 'react-router-dom';
import { useRegistration } from '../auth/hooks/useRegistration';
import { Container, Card, CardContent, Typography, TextField, Button, CircularProgress, Alert, Box, Link } from '@mui/material';
import { theme } from '../app/styles/theme';

export const RegisterPage = () => {
  const { registerUser, isLoading, isSuccess, error } = useRegistration();
  // Estado simplificado para coincidir con la API
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');

  console.log('REGISTRATION_DEBUG: Estado del componente:', { isLoading, isSuccess, error });

  const handleSubmit = (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    // Enviamos solo los datos que la API espera
    registerUser({ email, password });
  };

  return (
    <Container component="main" maxWidth="xs" sx={{ display: 'flex', alignItems: 'center', height: '100vh', py: 4 }}>
      <Card sx={{ width: '100%', padding: 2 }}>
        <CardContent>
          <Box sx={{ textAlign: 'center', mb: 2 }}>
            <Typography component="h1" variant="h1" sx={{ fontSize: '2rem' }}>
              Crear Cuenta
            </Typography>
          </Box>

          <form onSubmit={handleSubmit} noValidate>
            {error && (
              <Alert severity="error" sx={{ mb: 2 }}>
                {error.message}
              </Alert>
            )}
            <TextField
              name="email"
              type="email"
              label="Correo Electrónico"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              fullWidth
              required
              autoFocus
              sx={{ mb: 2 }}
            />
            <TextField
              name="password"
              type="password"
              label="Contraseña"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              fullWidth
              required
            />
            <Box sx={{ position: 'relative', mt: 3 }}>
              <Button type="submit" fullWidth variant="contained" color="primary" disabled={isLoading || !email || !password}>
                Registrarse
              </Button>
              {isLoading && (
                <CircularProgress
                  size={24}
                  sx={{
                    color: theme.palette.secondary.main,
                    position: 'absolute',
                    top: '50%',
                    left: '50%',
                    marginTop: '-12px',
                    marginLeft: '-12px',
                  }}
                />
              )}
            </Box>
          </form>

          <Box sx={{ textAlign: 'center', mt: 2 }}>
            <Link component={RouterLink} to="/login" variant="body2">
              ¿Ya tienes una cuenta? Inicia sesión
            </Link>
          </Box>
        </CardContent>
      </Card>
    </Container>
  );
};
import React, { useState } from 'react';
import { useAuth } from '../auth/hooks/useAuth';
import { Container, Card, CardContent, Typography, TextField, Button, CircularProgress, Alert, Box } from '@mui/material';
import { theme } from '../app/styles/theme'; // Importamos nuestro tema para usar los colores directamente si es necesario

export const LoginPage = () => {
  const { login, isLoading, error } = useAuth();
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');

  const handleSubmit = (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    login({ email, password });
  };

  const isButtonDisabled = !email || !password || isLoading;

  return (
    <Container component="main" maxWidth="xs" sx={{ display: 'flex', alignItems: 'center', height: '100vh' }}>
      <Card sx={{ width: '100%', padding: 2 }}>
        <CardContent>
          <Box sx={{ textAlign: 'center', mb: 2 }}>
            {/* Aquí podríamos poner nuestro logo SVG */}
            {/* Usamos variant="h1" para aplicar el gradiente del tema, pero ajustamos el tamaño */}
            <Typography component="h1" variant="h1" sx={{ fontSize: '2rem' }}>
              Iniciar Sesión
            </Typography>
          </Box>
          
          <form onSubmit={handleSubmit} noValidate>
            {error && (
              <Alert severity="error" sx={{ mb: 2 }}>
                {error}
              </Alert>
            )}
            <TextField
              variant="outlined"
              margin="normal"
              required
              fullWidth
              id="email"
              label="Correo Electrónico"
              name="email"
              autoComplete="email"
              autoFocus
              value={email}
              onChange={(e) => setEmail(e.target.value)}
            />
            <TextField
              variant="outlined"
              margin="normal"
              required
              fullWidth
              name="password"
              label="Contraseña"
              type="password"
              id="password"
              autoComplete="current-password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
            />
            <Box sx={{ position: 'relative', mt: 2 }}>
              <Button
                type="submit"
                fullWidth
                variant="contained"
                color="primary"
                disabled={isButtonDisabled}
              >
                Ingresar
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
        </CardContent>
      </Card>
    </Container>
  );
};

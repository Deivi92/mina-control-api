import React, { useState } from 'react';
import { Link as RouterLink } from 'react-router-dom';
import { useForgotPassword } from '../auth/hooks/useForgotPassword';
import { Container, Card, CardContent, Typography, TextField, Button, CircularProgress, Alert, Box, Link } from '@mui/material';

export const ForgotPasswordPage = () => {
  const { forgotPassword, isLoading, isSuccess, error } = useForgotPassword();
  const [email, setEmail] = useState('');

  const handleSubmit = (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    forgotPassword({ email });
  };

  return (
    <Container component="main" maxWidth="xs" sx={{ display: 'flex', alignItems: 'center', height: '100vh' }}>
      <Card sx={{ width: '100%', padding: 2 }}>
        <CardContent>
          <Box sx={{ textAlign: 'center', mb: 2 }}>
            <Typography component="h1" variant="h1">
              Recuperar Contraseña
            </Typography>
          </Box>

          {isSuccess ? (
            <Alert severity="success">
              Si existe una cuenta con ese correo, recibirás un enlace para restablecer tu contraseña en breve.
            </Alert>
          ) : (
            <form onSubmit={handleSubmit} noValidate>
              <Typography variant="body2" color="text.secondary" align="center" sx={{ mb: 2 }}>
                Ingresa tu correo electrónico y te enviaremos un enlace para recuperar tu cuenta.
              </Typography>
              {error && (
                <Alert severity="error" sx={{ mb: 2 }}>
                  {error.message}
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
              <Box sx={{ position: 'relative', mt: 2 }}>
                <Button type="submit" fullWidth variant="contained" color="primary" disabled={isLoading || !email}>
                  Enviar Enlace
                </Button>
                {isLoading && <CircularProgress size={24} sx={{ position: 'absolute', top: '50%', left: '50%', mt: '-12px', ml: '-12px' }} />}
              </Box>
            </form>
          )}

          <Box sx={{ textAlign: 'center', mt: 2 }}>
            <Link component={RouterLink} to="/login" variant="body2">
              Volver a Iniciar Sesión
            </Link>
          </Box>
        </CardContent>
      </Card>
    </Container>
  );
};

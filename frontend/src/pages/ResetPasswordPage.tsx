import React, { useState, useEffect } from 'react';
import { useParams, Link as RouterLink } from 'react-router-dom';
import { useResetPassword } from '../auth/hooks/useResetPassword';
import { Container, Card, CardContent, Typography, TextField, Button, CircularProgress, Alert, Box, Link } from '@mui/material';

export const ResetPasswordPage = () => {
  const { token } = useParams<{ token: string }>();
  const { resetPassword, isLoading, isSuccess, error } = useResetPassword();
  const [password, setPassword] = useState('');
  const [confirmPassword, setConfirmPassword] = useState('');
  const [passwordsMatch, setPasswordsMatch] = useState(true);

  useEffect(() => {
    setPasswordsMatch(password === confirmPassword);
  }, [password, confirmPassword]);

  const handleSubmit = (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    if (!token || !passwordsMatch) return;
    resetPassword({ token, newPassword: password });
  };

  return (
    <Container component="main" maxWidth="xs" sx={{ display: 'flex', alignItems: 'center', height: '100vh' }}>
      <Card sx={{ width: '100%', padding: 2 }}>
        <CardContent>
          <Box sx={{ textAlign: 'center', mb: 2 }}>
            <Typography component="h1" variant="h5">
              Establecer Nueva Contraseña
            </Typography>
          </Box>

          {isSuccess ? (
            <Alert severity="success">
              ¡Contraseña actualizada con éxito! Ya puedes iniciar sesión.
            </Alert>
          ) : (
            <form onSubmit={handleSubmit} noValidate>
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
                name="password"
                label="Nueva Contraseña"
                type="password"
                id="password"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
              />
              <TextField
                variant="outlined"
                margin="normal"
                required
                fullWidth
                name="confirmPassword"
                label="Confirmar Nueva Contraseña"
                type="password"
                id="confirmPassword"
                value={confirmPassword}
                onChange={(e) => setConfirmPassword(e.target.value)}
                error={!passwordsMatch}
                helperText={!passwordsMatch ? 'Las contraseñas no coinciden' : ''}
              />
              <Box sx={{ position: 'relative', mt: 2 }}>
                <Button type="submit" fullWidth variant="contained" color="primary" disabled={isLoading || !password || !passwordsMatch}>
                  Guardar Contraseña
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

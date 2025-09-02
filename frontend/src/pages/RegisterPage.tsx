import React, { useState } from 'react';
import { Link as RouterLink } from 'react-router-dom';
import { useRegistration } from '../auth/hooks/useRegistration';
import { Container, Card, CardContent, Typography, TextField, Button, CircularProgress, Alert, Box, Link } from '@mui/material';
import { theme } from '../app/styles/theme';
import type { RegistroUsuarioRequest } from '../auth/types';

export const RegisterPage = () => {
  const { registerUser, isLoading, error, isSuccess } = useRegistration();
  const [formData, setFormData] = useState<RegistroUsuarioRequest>({
    nombre: '',
    apellido: '',
    email: '',
    password: '',
    cedula: '',
    telefono: '',
    cargo: '',
  });

  const handleChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    setFormData({
      ...formData,
      [event.target.name]: event.target.value,
    });
  };

  const handleSubmit = (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    registerUser(formData);
  };

  return (
    <Container component="main" maxWidth="sm" sx={{ display: 'flex', alignItems: 'center', py: 4 }}>
      <Card sx={{ width: '100%', padding: 2 }}>
        <CardContent>
          <Box sx={{ textAlign: 'center', mb: 2 }}>
            <Typography component="h1" variant="h1" sx={{ fontSize: '2rem' }}>
              Crear Cuenta
            </Typography>
          </Box>

          {isSuccess ? (
            <Alert severity="success">
              ¡Registro exitoso! Ahora puedes <Link component={RouterLink} to="/login">iniciar sesión</Link>.
            </Alert>
          ) : (
            <form onSubmit={handleSubmit} noValidate>
              {error && (
                <Alert severity="error" sx={{ mb: 2 }}>
                  {error.message}
                </Alert>
              )}
              <Box sx={{ display: 'grid', gap: 2, gridTemplateColumns: { xs: '1fr', sm: 'repeat(2, 1fr)' } }}>
                <TextField name="nombre" label="Nombre" value={formData.nombre} onChange={handleChange} fullWidth required />
                <TextField name="apellido" label="Apellido" value={formData.apellido} onChange={handleChange} fullWidth required />
                <TextField name="email" type="email" label="Correo Electrónico" value={formData.email} onChange={handleChange} fullWidth required sx={{ gridColumn: '1 / -1' }} />
                <TextField name="password" type="password" label="Contraseña" value={formData.password} onChange={handleChange} fullWidth required sx={{ gridColumn: '1 / -1' }} />
                <TextField name="cedula" label="Cédula" value={formData.cedula} onChange={handleChange} fullWidth required />
                <TextField name="telefono" label="Teléfono" value={formData.telefono} onChange={handleChange} fullWidth required />
                <TextField name="cargo" label="Cargo" value={formData.cargo} onChange={handleChange} fullWidth required sx={{ gridColumn: '1 / -1' }} />
              </Box>
              <Box sx={{ position: 'relative', mt: 3 }}>
                <Button type="submit" fullWidth variant="contained" color="primary" disabled={isLoading}>
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
          )}

          {!isSuccess && (
            <Box sx={{ textAlign: 'center', mt: 2 }}>
              <Link component={RouterLink} to="/login" variant="body2">
                ¿Ya tienes una cuenta? Inicia sesión
              </Link>
            </Box>
          )}
        </CardContent>
      </Card>
    </Container>
  );
};

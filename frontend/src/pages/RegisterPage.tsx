import { Link as RouterLink } from 'react-router-dom';
import { useRegistration } from '../auth/hooks/useRegistration';
import { Container, Card, CardContent, Typography, Button, CircularProgress, Alert, Box, Link } from '@mui/material';
import { useForm, Controller } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { TextField } from '@mui/material';
import { registerSchema, type RegisterFormData } from '../auth/validation/register.schema';
import { theme } from '../app/styles/theme';

export const RegisterPage = () => {
  const { registerUser, isLoading, isSuccess, error } = useRegistration();
  
  const { control, handleSubmit, formState: { errors, isValid } } = useForm<RegisterFormData>({
    resolver: zodResolver(registerSchema),
    mode: 'onChange',
    defaultValues: {
      email: '',
      password: '',
    }
  });

  console.log('REGISTRATION_DEBUG: Estado del componente:', { isLoading, isSuccess, error });

  const onSubmit = (data: RegisterFormData) => {
    // Enviamos solo los datos que la API espera
    registerUser(data);
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

          <form onSubmit={handleSubmit(onSubmit)} noValidate>
            {error && (
              <Alert severity="error" sx={{ mb: 2 }}>
                {error.message}
              </Alert>
            )}
            <Controller
              name="email"
              control={control}
              render={({ field }) => (
                <TextField
                  {...field}
                  name="email"
                  type="email"
                  label="Correo Electrónico"
                  fullWidth
                  required
                  autoFocus
                  sx={{ mb: 2 }}
                  error={!!errors.email}
                  helperText={errors.email?.message}
                />
              )}
            />
            <Controller
              name="password"
              control={control}
              render={({ field }) => (
                <TextField
                  {...field}
                  name="password"
                  type="password"
                  label="Contraseña"
                  fullWidth
                  required
                  error={!!errors.password}
                  helperText={errors.password?.message}
                />
              )}
            />
            <Box sx={{ position: 'relative', mt: 3 }}>
              <Button type="submit" fullWidth variant="contained" color="primary" disabled={isLoading || !isValid}>
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
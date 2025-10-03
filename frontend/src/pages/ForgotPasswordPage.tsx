import { Link as RouterLink } from 'react-router-dom';
import { useForgotPassword } from '../auth/hooks/useForgotPassword';
import { Container, Card, CardContent, Typography, Button, CircularProgress, Alert, Box, Link } from '@mui/material';
import { useForm, Controller } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { TextField } from '@mui/material';
import { forgotPasswordSchema, type ForgotPasswordFormData } from '../auth/validation/forgotPassword.schema';

export const ForgotPasswordPage = () => {
  const { forgotPassword, isLoading, isSuccess, error } = useForgotPassword();
  
  const { control, handleSubmit, formState: { errors, isValid } } = useForm<ForgotPasswordFormData>({
    resolver: zodResolver(forgotPasswordSchema),
    mode: 'onChange',
    defaultValues: {
      email: '',
    }
  });

  const onSubmit = (data: ForgotPasswordFormData) => {
    forgotPassword(data);
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
            <form onSubmit={handleSubmit(onSubmit)} noValidate>
              <Typography variant="body2" color="text.secondary" align="center" sx={{ mb: 2 }}>
                Ingresa tu correo electrónico y te enviaremos un enlace para recuperar tu cuenta.
              </Typography>
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
                    variant="outlined"
                    margin="normal"
                    required
                    fullWidth
                    id="email"
                    label="Correo Electrónico"
                    name="email"
                    autoComplete="email"
                    autoFocus
                    error={!!errors.email}
                    helperText={errors.email?.message}
                  />
                )}
              />
              <Box sx={{ position: 'relative', mt: 2 }}>
                <Button type="submit" fullWidth variant="contained" color="primary" disabled={isLoading || !isValid}>
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

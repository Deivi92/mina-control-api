import { useParams, Link as RouterLink } from 'react-router-dom';
import { useResetPassword } from '../auth/hooks/useResetPassword';
import { Container, Card, CardContent, Typography, Button, CircularProgress, Alert, Box, Link } from '@mui/material';
import { useForm, Controller } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { TextField } from '@mui/material';
import { resetPasswordSchema, type ResetPasswordFormData } from '../auth/validation/resetPassword.schema';

export const ResetPasswordPage = () => {
  const { token } = useParams<{ token: string }>();
  const { resetPassword, isLoading, isSuccess, error } = useResetPassword();
  
  const { control, handleSubmit, formState: { errors, isValid }, setError } = useForm<ResetPasswordFormData>({
    resolver: zodResolver(resetPasswordSchema),
    mode: 'onSubmit',
    defaultValues: {
      newPassword: '',
      confirmPassword: '',
    }
  });

  const onSubmit = (data: ResetPasswordFormData) => {
    if (data.newPassword !== data.confirmPassword) {
      setError('confirmPassword', {
        type: 'manual',
        message: 'Las contraseñas no coinciden',
      });
      return;
    }
    
    if (token) {
      resetPassword({ token, newPassword: data.newPassword });
    }
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
            <form onSubmit={handleSubmit(onSubmit)} noValidate>
              {error && (
                <Alert severity="error" sx={{ mb: 2 }}>
                  {error.message}
                </Alert>
              )}
              <Controller
                name="newPassword"
                control={control}
                render={({ field }) => (
                  <TextField
                    {...field}
                    variant="outlined"
                    margin="normal"
                    required
                    fullWidth
                    name="password"
                    label="Nueva Contraseña"
                    type="password"
                    id="newPassword"
                    error={!!errors.newPassword}
                    helperText={errors.newPassword?.message}
                  />
                )}
              />
              <Controller
                name="confirmPassword"
                control={control}
                render={({ field }) => (
                  <TextField
                    {...field}
                    variant="outlined"
                    margin="normal"
                    required
                    fullWidth
                    name="confirmPassword"
                    label="Confirmar Nueva Contraseña"
                    type="password"
                    id="confirmPassword"
                    error={!!errors.confirmPassword}
                    helperText={errors.confirmPassword?.message}
                  />
                )}
              />
              <Box sx={{ position: 'relative', mt: 2 }}>
                <Button 
                  type="submit" 
                  fullWidth 
                  variant="contained" 
                  color="primary" 
                  disabled={isLoading || !isValid}
                >
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

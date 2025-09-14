import { useRouteError } from 'react-router-dom';
import { Box, Container, Paper, Typography } from '@mui/material';

/**
 * Componente reutilizable para mostrar errores capturados por el enrutador.
 * Reemplaza el componente de error por defecto de React Router que está causando el crash.
 */
export const ErrorBoundary: React.FC = () => {
  // useRouteError nos da el error que fue lanzado
  const error = useRouteError();

  // --- Enhanced Error Logging for Debugging ---
  console.log("--- ErrorBoundary Caught ---");
  console.log("Raw error object:", error);
  if (error instanceof Error) {
    console.log("Error name:", error.name);
    console.log("Error message:", error.message);
    console.log("Error stack:", error.stack);
  } else if (typeof error === 'object' && error !== null) {
    // Fallback for non-Error objects
    console.log("Error object keys:", Object.keys(error));
    // Try to log potentially useful properties
    for (const key in error) {
      if (Object.prototype.hasOwnProperty.call(error, key)) {
        console.log(`error[${key}] =`, (error as any)[key]);
      }
    }
  }
  // --- End of Enhanced Logging ---

  console.error("Original error object for inspection:", error); // Logueamos el error para depuración

  // Función para obtener un mensaje de error legible
  const getErrorMessage = (err: unknown): string => {
    if (typeof err === 'object' && err !== null) {
      if ('statusText' in err && typeof err.statusText === 'string') {
        return err.statusText;
      }
      if ('message' in err && typeof err.message === 'string') {
        return err.message;
      }
    }
    return 'Ha ocurrido un error inesperado.';
  };

  // En desarrollo, los errores son manejados por el overlay de Vite.
  // Devolver null aquí previene el conflicto de 'removeChild'.
  if (import.meta.env.DEV) {
    return null;
  }

  // En producción, mostramos nuestro componente de error personalizado.
  return (
    <Container component="main" maxWidth="sm" sx={{ mt: 8 }}>
      <Paper elevation={3} sx={{ p: 4, textAlign: 'center' }}>
        <Box>
          <Typography variant="h4" component="h1" gutterBottom color="error">
            Oops! Algo salió mal.
          </Typography>
          <Typography variant="body1">
            {getErrorMessage(error)}
          </Typography>
        </Box>
      </Paper>
    </Container>
  );
};

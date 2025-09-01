import React from 'react';
import ReactDOM from 'react-dom/client';
import { RouterProvider } from 'react-router-dom';
import { router } from './router';
import { CssBaseline, ThemeProvider } from '@mui/material';
import { theme } from './app/styles/theme';
import { AuthProvider } from './auth/hooks/useAuth'; // Importamos el nuevo AuthProvider

ReactDOM.createRoot(document.getElementById('root')!).render(
  <React.StrictMode>
    <ThemeProvider theme={theme}>
      <CssBaseline />
      <AuthProvider> {/* Envolver la app con AuthProvider */}
        <RouterProvider router={router} />
      </AuthProvider>
    </ThemeProvider>
  </React.StrictMode>
);

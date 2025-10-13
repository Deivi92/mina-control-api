import { createBrowserRouter } from 'react-router-dom';
import App from '../app/App';
import { LoginPage } from '../auth/pages/LoginPage';
import { DashboardPage } from '../dashboard/pages/DashboardPage';
import { RegisterPage } from '../auth/pages/RegisterPage';
import { ForgotPasswordPage } from '../auth/pages/ForgotPasswordPage';
import { ResetPasswordPage } from '../auth/pages/ResetPasswordPage';
import { EmpleadosPage } from '../domains/empleado/pages/EmpleadosPage';
import { ProtectedRoute } from '../auth/ProtectedRoute';
import { ErrorBoundary } from '../shared/components/ErrorBoundary';

export const router = createBrowserRouter([
  {
    path: '/',
    element: <App />,
    errorElement: <ErrorBoundary />,
    children: [
      // --- Rutas Públicas ---
      {
        index: true,
        element: <LoginPage />,
      },
      {
        path: 'login',
        element: <LoginPage />,
      },
      {
        path: 'register',
        element: <RegisterPage />,
      },
      {
        path: 'forgot-password',
        element: <ForgotPasswordPage />,
      },
      {
        path: 'reset-password/:token',
        element: <ResetPasswordPage />,
      },

      // --- Rutas Protegidas ---
      {
        element: <ProtectedRoute />,
        children: [
          {
            path: 'dashboard',
            element: <DashboardPage />,
          },
          {
            path: 'empleados',
            element: <EmpleadosPage />,
          },
          // ...aquí irán las futuras rutas protegidas
        ],
      },
    ],
  },
]);

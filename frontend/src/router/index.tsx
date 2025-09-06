import { createBrowserRouter } from 'react-router-dom';
import App from '../app/App';
import { LoginPage } from '../pages/LoginPage';
import { DashboardPage } from '../pages/DashboardPage';
import { RegisterPage } from '../pages/RegisterPage';
import { ForgotPasswordPage } from '../pages/ForgotPasswordPage';
import { ResetPasswordPage } from '../pages/ResetPasswordPage';
import { EmpleadosPage } from '../pages/EmpleadosPage';

export const router = createBrowserRouter([
  {
    path: '/',
    element: <App />,
    children: [
      {
        index: true,
        element: <LoginPage />,
      },
      {
        path: 'login', // Ruta explícita para /login
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
      {
        path: 'dashboard',
        element: <DashboardPage />,
      },
      {
        path: 'empleados',
        element: <EmpleadosPage />,
      },
    ],
  },
]);

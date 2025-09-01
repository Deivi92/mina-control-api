import { createBrowserRouter } from 'react-router-dom';
import App from '../app/App';
import { LoginPage } from '../pages/LoginPage';

export const router = createBrowserRouter([
  {
    path: '/',
    element: <App />,
    children: [
      {
        index: true, // Esto hace que LoginPage sea la ruta por defecto para '/'
        element: <LoginPage />,
      },
      // Futuras rutas como /dashboard irían aquí como hijos de App
    ],
  },
]);

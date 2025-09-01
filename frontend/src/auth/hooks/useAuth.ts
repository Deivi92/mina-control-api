import { useState } from 'react';
import * as authService from '../services/auth.service';
import type { LoginRequest, Usuario } from '../types';

// Asumimos que la respuesta del login puede incluir al usuario
import type { LoginResponse } from '../types';
type LoginResponseWithUser = LoginResponse & { user?: Usuario };

export const useAuth = () => {
  const [user, setUser] = useState<Usuario | null>(null);
  const [error, setError] = useState<string | null>(null);
  const [isLoading, setIsLoading] = useState(false);

  const login = async (credentials: LoginRequest) => {
    setIsLoading(true);
    setError(null);
    try {
      const response = await authService.login(credentials) as LoginResponseWithUser;
      
      // En una app real, aquí se guardarían los tokens (ej. localStorage)
      // y se podría decodificar el JWT para obtener info del usuario.
      // Para nuestro hook, confiamos en que el servicio nos devuelva el usuario.
      if (response.user) {
        setUser(response.user);
      } else {
        // Si el backend no devuelve el usuario, podríamos necesitar otra llamada
        // o decodificar el token. Por ahora, lo dejamos como un posible caso.
        console.warn('Login exitoso pero no se recibió información del usuario.');
      }
      
    } catch (err) {
      const errorMessage = err instanceof Error ? err.message : 'Error desconocido';
      setError(errorMessage);
    } finally {
      setIsLoading(false);
    }
  };

  const logout = () => {
    // Lógica de logout irá aquí
    setUser(null);
  };

  return {
    user,
    isAuthenticated: !!user,
    error,
    isLoading,
    login,
    logout,
  };
};

import { createContext, useState, useContext } from 'react';
import type { ReactNode } from 'react';
import * as authService from '../services/auth.service';
import type { LoginRequest, Usuario } from '../types';

// 1. Definimos la forma del contexto
interface AuthContextType {
  user: Usuario | null;
  isAuthenticated: boolean;
  isLoading: boolean;
  error: string | null;
  login: (credentials: LoginRequest) => Promise<void>;
  logout: () => void;
}

// 2. Creamos el Context con un valor por defecto
const AuthContext = createContext<AuthContextType | undefined>(undefined);

// 3. Creamos el componente Proveedor
export const AuthProvider = ({ children }: { children: ReactNode }) => {
  const [user, setUser] = useState<Usuario | null>(null);
  const [error, setError] = useState<string | null>(null);
  const [isLoading, setIsLoading] = useState(false);

  const login = async (credentials: LoginRequest) => {
    setIsLoading(true);
    setError(null);
    try {
      // En una app real, aquí se guardarían los tokens (ej. localStorage)
      // y se podría decodificar el JWT para obtener info del usuario.
      await authService.login(credentials);
      setUser({ id: 1, email: credentials.email }); // Simulamos un usuario
    } catch (err) {
      const errorMessage = err instanceof Error ? err.message : 'Error desconocido';
      setError(errorMessage);
      throw err; // Re-lanzamos el error para que el componente pueda reaccionar
    } finally {
      setIsLoading(false);
    }
  };

  const logout = () => {
    setUser(null);
    // Aquí se limpiarían los tokens
  };

  const value = {
    user,
    isAuthenticated: !!user,
    isLoading,
    error,
    login,
    logout,
  };

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
};

// 4. Creamos el hook que consume el contexto
export const useAuth = () => {
  const context = useContext(AuthContext);
  if (context === undefined) {
    throw new Error('useAuth debe ser usado dentro de un AuthProvider');
  }
  return context;
};
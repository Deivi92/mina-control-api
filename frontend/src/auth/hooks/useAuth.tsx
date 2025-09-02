import { createContext, useState, useContext } from 'react';
import type { ReactNode } from 'react';
import * as authService from '../services/auth.service';
import type { LoginRequest, Usuario } from '../types';
import { useNavigate } from 'react-router-dom';
import { jwtDecode } from 'jwt-decode';

// 1. Definimos la forma del contexto
interface AuthContextType {
  user: Usuario | null;
  isAuthenticated: boolean;
  isLoading: boolean;
  error: string | null;
  login: (credentials: LoginRequest) => Promise<void>;
  logout: () => Promise<void>;
}

// 2. Creamos el Context
const AuthContext = createContext<AuthContextType | undefined>(undefined);

// 3. Creamos el Proveedor
export const AuthProvider = ({ children }: { children: ReactNode }) => {
  const [user, setUser] = useState<Usuario | null>(null);
  // const [accessToken, setAccessToken] = useState<string | null>(null); // Corrección: accessToken no se usa
  const [refreshToken, setRefreshToken] = useState<string | null>(null);
  const [error, setError] = useState<string | null>(null);
  const [isLoading, setIsLoading] = useState(false);
  const navigate = useNavigate();

  const login = async (credentials: LoginRequest) => {
    setIsLoading(true);
    setError(null);
    try {
      const { accessToken: _, refreshToken } = await authService.login(credentials);
      
      // Guardamos los tokens
      // accessToken no se usa directamente, solo refreshToken
      setRefreshToken(refreshToken);

      // Decodificamos el token para obtener la info del usuario
      // En una app real, la estructura del token puede variar
      const decodedToken: { sub: string, [key: string]: any } = jwtDecode(_);
      setUser({ email: decodedToken.sub } as Usuario); // Simulación parcial

      navigate('/dashboard');
    } catch (err) {
      const errorMessage = err instanceof Error ? err.message : 'Error desconocido';
      setError(errorMessage);
      throw err;
    } finally {
      setIsLoading(false);
    }
  };

  const logout = async () => {
    setIsLoading(true);
    try {
      if (refreshToken) {
        await authService.logout({ refreshToken });
      }
    } catch (err) {
      console.error("Error during server logout, proceeding with client logout", err);
      // No bloqueamos al usuario si el logout del servidor falla
    } finally {
      // Limpiamos todo el estado local
      setUser(null);
      // setAccessToken(null); // Corrección: accessToken ya no se usa
      setRefreshToken(null);
      setError(null);
      setIsLoading(false);
      navigate('/');
    }
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

// 4. Creamos el hook de consumo
export const useAuth = () => {
  const context = useContext(AuthContext);
  if (context === undefined) {
    throw new Error('useAuth debe ser usado dentro de un AuthProvider');
  }
  return context;
};

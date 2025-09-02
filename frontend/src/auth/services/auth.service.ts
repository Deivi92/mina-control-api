import axios from 'axios';
import type { LoginRequest, LoginResponse, LogoutRequest, RegistroUsuarioRequest } from '../types';

const API_URL = '/api/auth'; // Base URL para los endpoints de autenticación

export const login = async (credentials: LoginRequest): Promise<LoginResponse> => {
  try {
    const response = await axios.post<LoginResponse>(`${API_URL}/login`, credentials);
    return response.data;
  } catch (error) {
    // Manejar errores de manera más amigable
    if (axios.isAxiosError(error)) {
      if (error.response?.status === 401) {
        throw new Error('Usuario o contraseña incorrectos');
      } else if (error.response?.status === 400) {
        throw new Error('Solicitud incorrecta. Por favor, verifica tus credenciales.');
      } else if (error.response?.status === 500) {
        throw new Error('Error del servidor. Por favor, inténtalo más tarde.');
      } else {
        throw new Error(`Error de autenticación: ${error.response?.statusText || 'Error desconocido'}`);
      }
    } else {
      throw new Error('Error de red. Por favor, verifica tu conexión.');
    }
  }
};

export const register = async (userData: RegistroUsuarioRequest): Promise<void> => {
  try {
    await axios.post(`${API_URL}/register`, userData);
  } catch (error) {
    if (axios.isAxiosError(error)) {
      throw new Error(`Error en el registro: ${error.response?.statusText || 'Error desconocido'}`);
    } else {
      throw new Error('Error de red. Por favor, verifica tu conexión.');
    }
  }
};

export const logout = async (payload: LogoutRequest): Promise<void> => {
  try {
    await axios.post(`${API_URL}/logout`, payload);
  } catch (error) {
    if (axios.isAxiosError(error)) {
      throw new Error(`Error al cerrar sesión: ${error.response?.statusText || 'Error desconocido'}`);
    } else {
      throw new Error('Error de red. Por favor, verifica tu conexión.');
    }
  }
};

// Dejamos esta función sin implementar por ahora ya que no tenemos pruebas para ella
export const refreshToken = async (): Promise<void> => {
  throw new Error('Función no implementada');
};

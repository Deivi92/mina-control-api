import axios from 'axios';
import type { LoginRequest, LoginResponse, LogoutRequest, RegistroUsuarioRequest, Usuario, RecuperarContrasenaRequest, CambiarContrasenaRequest } from '../types';

const API_URL = '/api/auth'; // Base URL para los endpoints de autenticación

export const login = async (credentials: LoginRequest): Promise<LoginResponse> => {
  try {
    const response = await axios.post<LoginResponse>(`${API_URL}/login`, credentials);
    return response.data;
  } catch (error) {
    if (axios.isAxiosError(error)) {
      if (error.response?.status === 401) {
        throw new Error('Usuario o contraseña incorrectos');
      }
      throw new Error(error.response?.data?.message || 'Error de autenticación');
    } 
    throw new Error('Error de red. Por favor, verifica tu conexión.');
  }
};

export const register = async (userData: RegistroUsuarioRequest): Promise<Usuario> => {
  try {
    const response = await axios.post<Usuario>(`${API_URL}/register`, userData);
    return response.data;
  } catch (error) {
    if (axios.isAxiosError(error)) {
      if (error.response?.status === 409) { // Conflict
        throw new Error('El correo electrónico o la cédula ya están registrados.');
      }
      throw new Error(error.response?.data?.message || 'Error en el registro');
    }
    throw new Error('Error de red. Por favor, verifica tu conexión.');
  }
};

export const logout = async (payload: LogoutRequest): Promise<void> => {
  try {
    await axios.post(`${API_URL}/logout`, payload);
  } catch (error) {
    if (axios.isAxiosError(error)) {
      throw new Error(error.response?.data?.message || 'Error al cerrar sesión');
    }
    throw new Error('Error de red. Por favor, verifica tu conexión.');
  }
};

export const forgotPassword = async (payload: RecuperarContrasenaRequest): Promise<void> => {
  try {
    await axios.post(`${API_URL}/forgot-password`, payload);
  } catch (error) {
    // No lanzamos un error específico para no revelar si un email existe o no
    // La UI mostrará un mensaje genérico. El error real se puede loguear.
    console.error('Forgot Password Error:', error);
    throw new Error('No se pudo procesar la solicitud.');
  }
};

export const resetPassword = async (payload: CambiarContrasenaRequest): Promise<void> => {
  try {
    await axios.post(`${API_URL}/reset-password`, payload);
  } catch (error) {
    if (axios.isAxiosError(error) && error.response?.status === 400) {
      throw new Error('El enlace de recuperación es inválido o ha expirado.');
    }
    throw new Error('No se pudo cambiar la contraseña.');
  }
};


// Dejamos esta función sin implementar por ahora ya que no tenemos pruebas para ella
export const refreshToken = async (): Promise<void> => {
  throw new Error('Función no implementada');
};

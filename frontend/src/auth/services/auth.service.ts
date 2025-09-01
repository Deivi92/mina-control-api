import axios from 'axios';
import type { LoginRequest, LoginResponse, LogoutRequest, RegistroUsuarioRequest } from '../types';

const API_URL = '/api/auth'; // Base URL para los endpoints de autenticación

export const login = async (credentials: LoginRequest): Promise<LoginResponse> => {
  const response = await axios.post<LoginResponse>(`${API_URL}/login`, credentials);
  return response.data;
};

export const register = async (userData: RegistroUsuarioRequest): Promise<void> => {
  await axios.post(`${API_URL}/register`, userData);
};

export const logout = async (payload: LogoutRequest): Promise<void> => {
  await axios.post(`${API_URL}/logout`, payload);
};

// Dejamos esta función sin implementar por ahora ya que no tenemos pruebas para ella
export const refreshToken = async (): Promise<void> => {
  throw new Error('Función no implementada');
};

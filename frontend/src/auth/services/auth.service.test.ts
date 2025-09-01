import axios from 'axios';
import { login, register, logout } from './auth.service';
import type { LoginRequest, RegistroUsuarioRequest, LogoutRequest } from '../types';
import { vi } from 'vitest';

vi.mock('axios');
const mockedAxios = axios as vi.Mocked<typeof axios>;

describe('AuthService', () => {
  afterEach(() => {
    vi.clearAllMocks();
  });

  // Pruebas para la función de LOGIN
  describe('login', () => {
    it('debería llamar a axios.post con la URL y los datos correctos', async () => {
      const credentials: LoginRequest = { email: 'test@example.com', password: 'password123' };
      mockedAxios.post.mockResolvedValue({ data: {} });
      await login(credentials);
      expect(mockedAxios.post).toHaveBeenCalledWith('/api/auth/login', credentials);
    });

    it('debería devolver los tokens en caso de éxito', async () => {
      const credentials: LoginRequest = { email: 'test@example.com', password: 'password123' };
      const mockResponse = { data: { accessToken: 'fake-access-token', refreshToken: 'fake-refresh-token' } };
      mockedAxios.post.mockResolvedValue(mockResponse);
      const result = await login(credentials);
      expect(result).toEqual(mockResponse.data);
    });

    it('debería lanzar un error si la llamada a la API falla', async () => {
      const credentials: LoginRequest = { email: 'test@example.com', password: 'password123' };
      const errorMessage = 'Request failed with status code 401';
      mockedAxios.post.mockRejectedValue(new Error(errorMessage));
      await expect(login(credentials)).rejects.toThrow(errorMessage);
    });
  });

  // Pruebas para la función de REGISTRO
  describe('register', () => {
    it('debería llamar a axios.post con la URL y los datos de registro correctos', async () => {
      const userData: RegistroUsuarioRequest = { email: 'new@example.com', password: 'new-password' };
      mockedAxios.post.mockResolvedValue({ data: {} });
      await register(userData);
      expect(mockedAxios.post).toHaveBeenCalledWith('/api/auth/register', userData);
    });
  });

  // Pruebas para la función de LOGOUT
  describe('logout', () => {
    it('debería llamar a axios.post para hacer logout', async () => {
      const logoutPayload: LogoutRequest = { refreshToken: 'some-refresh-token' };
      mockedAxios.post.mockResolvedValue({});
      await logout(logoutPayload);
      expect(mockedAxios.post).toHaveBeenCalledWith('/api/auth/logout', logoutPayload);
    });
  });
});

import axios from 'axios';
import { login, register, forgotPassword, resetPassword } from './auth.service';
import type { LoginRequest, RegistroUsuarioRequest, RecuperarContrasenaRequest, CambiarContrasenaRequest, Usuario, LoginResponse } from '../types';
import { vi } from 'vitest';

vi.mock('axios');

describe('AuthService', () => {
  afterEach(() => {
    vi.clearAllMocks();
  });

  describe('login', () => {
    it('debería llamar a axios.post con la URL y los datos correctos y devolver tokens', async () => {
      const credentials: LoginRequest = { email: 'test@example.com', password: 'password123' };
      const mockTokens: LoginResponse = { accessToken: 'fake-access-token', refreshToken: 'fake-refresh-token' };
      (axios.post as vi.Mock).mockResolvedValue({ data: mockTokens });

      const result = await login(credentials);

      expect(axios.post).toHaveBeenCalledWith('/api/auth/login', credentials);
      expect(result).toEqual(mockTokens);
    });
  });

  describe('register', () => {
    it('debería llamar a la API de registro y devolver los datos del usuario creado', async () => {
      const newUser: RegistroUsuarioRequest = { nombre: 'Test', apellido: 'User', email: 'test@test.com', password: 'password', cedula: '1', telefono: '1', cargo: 'Test' };
      const createdUser: Usuario = { id: 1, ...newUser };
      (axios.post as vi.Mock).mockResolvedValue({ data: createdUser });

      const result = await register(newUser);

      expect(axios.post).toHaveBeenCalledWith('/api/auth/register', newUser);
      expect(result).toEqual(createdUser);
    });
  });

  describe('forgotPassword', () => {
    it('debería llamar a la API de forgot-password con el email correcto', async () => {
      // Arrange
      const request: RecuperarContrasenaRequest = { email: 'test@example.com' };
      (axios.post as vi.Mock).mockResolvedValue({});

      // Act
      await forgotPassword(request);

      // Assert
      expect(axios.post).toHaveBeenCalledWith('/api/auth/forgot-password', request);
    });
  });

  describe('resetPassword', () => {
    it('debería llamar a la API de reset-password con los datos correctos', async () => {
      // Arrange
      const request: CambiarContrasenaRequest = { token: 'valid-token', newPassword: 'new-password-123' };
       (axios.post as vi.Mock).mockResolvedValue({});

      // Act
      await resetPassword(request);

      // Assert
      expect(axios.post).toHaveBeenCalledWith('/api/auth/reset-password', request);
    });
  });
});
import { renderHook, act } from '@testing-library/react';
import { useAuth } from './useAuth';
import * as authService from '../services/auth.service';

// Mockeamos el módulo completo de auth.service
jest.mock('../services/auth.service');
const mockedAuthService = authService as jest.Mocked<typeof authService>;

describe('useAuth Hook', () => {
  
  afterEach(() => {
    jest.clearAllMocks();
  });

  it('debería tener un estado inicial correcto (no autenticado, sin usuario)', () => {
    // TODO: Implementar
  });

  describe('Función de Login', () => {
    it('debería poner isLoading a true y luego a false durante el login', async () => {
      // TODO: Implementar
    });

    it('debería llamar a authService.login con las credenciales correctas', async () => {
      // TODO: Implementar
    });

    it('debería actualizar el estado (usuario, isAuthenticated) si el login es exitoso', async () => {
      // TODO: Implementar
    });

    it('debería rellenar el estado de error si el login falla', async () => {
      // TODO: Implementar
    });
  });

  describe('Función de Logout', () => {
    it('debería llamar a authService.logout', async () => {
      // TODO: Implementar
    });

    it('debería limpiar el estado del usuario y poner isAuthenticated a false', async () => {
      // TODO: Implementar
    });
  });
});

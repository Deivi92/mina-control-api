import { renderHook, act } from '@testing-library/react';
import { useAuth, AuthProvider } from './useAuth.tsx'; // Importamos el provider
import * as authService from '../services/auth.service';
import type { LoginRequest } from '../types';
import { vi } from 'vitest';
import React from 'react';

// Mock del servicio que es el límite de nuestro hook
vi.mock('../services/auth.service');
const mockedAuthService = authService as vi.Mocked<typeof authService>;

// Creamos un wrapper reutilizable
const wrapper = ({ children }: { children: React.ReactNode }) => (
  <AuthProvider>{children}</AuthProvider>
);

describe('useAuth Hook with Provider', () => {
  afterEach(() => {
    vi.clearAllMocks();
  });

  it('debería tener un estado inicial correcto', () => {
    const { result } = renderHook(() => useAuth(), { wrapper });
    expect(result.current.user).toBeNull();
    expect(result.current.isAuthenticated).toBe(false);
    expect(result.current.isLoading).toBe(false);
    expect(result.current.error).toBeNull();
  });

  it('debería manejar el flujo de login exitoso correctamente', async () => {
    const { result } = renderHook(() => useAuth(), { wrapper });
    const credentials: LoginRequest = { email: 'test@test.com', password: 'password' };
    
    mockedAuthService.login.mockResolvedValue({ accessToken: 'fake-token', refreshToken: 'fake-refresh-token' });

    await act(async () => {
      await result.current.login(credentials);
    });

    expect(result.current.isLoading).toBe(false);
    // Verificamos que el usuario simulado se establece correctamente
    expect(result.current.user).toEqual({ id: '1', nombre: credentials.email, rol: 'USER' });
    expect(result.current.isAuthenticated).toBe(true);
    expect(result.current.error).toBeNull();
    expect(authService.login).toHaveBeenCalledWith(credentials);
  });

  it('debería manejar el flujo de login fallido correctamente', async () => {
    const { result } = renderHook(() => useAuth(), { wrapper });
    const credentials: LoginRequest = { email: 'test@test.com', password: 'password' };
    const errorMessage = 'Credenciales inválidas';
    
    mockedAuthService.login.mockRejectedValue(new Error(errorMessage));

    // Usamos un try-catch porque el error se re-lanza
    await act(async () => {
      try {
        await result.current.login(credentials);
      } catch (e) {
        // Error esperado, no hacer nada
      }
    });

    expect(result.current.isLoading).toBe(false);
    expect(result.current.user).toBeNull();
    expect(result.current.isAuthenticated).toBe(false);
    expect(result.current.error).toBe(errorMessage);
  });
});
import { renderHook, act } from '@testing-library/react';
import { useAuth, AuthProvider } from './useAuth.tsx';
import * as authService from '../services/auth.service';
import type { LoginRequest, Usuario } from '../types';
import { vi } from 'vitest';
import React from 'react';
import { jwtDecode } from 'jwt-decode';
import { MemoryRouter } from 'react-router-dom';

// -- Mocks --
vi.mock('../services/auth.service');
// Corrección: Mockear jwt_decode para que tenga una exportación nombrada jwtDecode
vi.mock('jwt-decode', () => ({
  jwtDecode: vi.fn(),
}));

const mockNavigate = vi.fn();
vi.mock('react-router-dom', async () => {
  const actual = await vi.importActual('react-router-dom');
  return {
    ...actual,
    useNavigate: () => mockNavigate,
  };
});
// -- Fin Mocks --

const mockedAuthService = authService as vi.Mocked<typeof authService>;
// Tipar explícitamente el mock de jwtDecode para un acceso más claro
const mockedJwtDecode = jwtDecode as vi.Mock;

// Corrección: El wrapper debe proporcionar un Router para que useNavigate funcione
const wrapper = ({ children }: { children: React.ReactNode }) => (
  <MemoryRouter>
    <AuthProvider>{children}</AuthProvider>
  </MemoryRouter>
);

describe('useAuth Hook with Provider', () => {
  beforeEach(() => {
    vi.clearAllMocks();
  });

  it('debería tener un estado inicial correcto', () => {
    const { result } = renderHook(() => useAuth(), { wrapper });
    expect(result.current.user).toBeNull();
    expect(result.current.isAuthenticated).toBe(false);
  });

  it('debería manejar el flujo de login exitoso', async () => {
    // Arrange
    const { result } = renderHook(() => useAuth(), { wrapper });
    const credentials = { email: 'test@test.com', password: 'password' };
    mockedAuthService.login.mockResolvedValue({ accessToken: 'fake-token', refreshToken: 'fake-refresh-token' });
    // Corrección: Usar el mock tipado
    mockedJwtDecode.mockReturnValue({ sub: credentials.email });

    // Act
    await act(async () => { await result.current.login(credentials); });

    // Assert
    expect(result.current.isAuthenticated).toBe(true);
    expect(result.current.user?.email).toBe(credentials.email);
    expect(mockNavigate).toHaveBeenCalledWith('/dashboard');
  });

  it('debería manejar el flujo de login fallido', async () => {
    const { result } = renderHook(() => useAuth(), { wrapper });
    const credentials = { email: 'test@test.com', password: 'password' };
    mockedAuthService.login.mockRejectedValue(new Error('Credenciales inválidas'));

    await act(async () => { 
      try { await result.current.login(credentials); } catch (e) { /* Ignorado */ } 
    });

    expect(result.current.isAuthenticated).toBe(false);
    expect(result.current.error).toBe('Credenciales inválidas');
  });

  it('debería llamar al servicio de logout y limpiar el estado del usuario', async () => {
    // Arrange
    const { result } = renderHook(() => useAuth(), { wrapper });
    const loginCredentials = { email: 'test@test.com', password: 'password' };
    const loginResponse = { accessToken: 'fake-access-token', refreshToken: 'fake-refresh-token' };
    mockedAuthService.login.mockResolvedValue(loginResponse);
    // Corrección: Usar el mock tipado
    mockedJwtDecode.mockReturnValue({ sub: loginCredentials.email });
    await act(async () => { await result.current.login(loginCredentials); });
    expect(result.current.isAuthenticated).toBe(true);

    // Act
    await act(async () => { await result.current.logout(); });

    // Assert
    expect(mockedAuthService.logout).toHaveBeenCalledWith({ refreshToken: loginResponse.refreshToken });
    expect(result.current.user).toBeNull();
    expect(result.current.isAuthenticated).toBe(false);
    expect(mockNavigate).toHaveBeenCalledWith('/');
  });
});

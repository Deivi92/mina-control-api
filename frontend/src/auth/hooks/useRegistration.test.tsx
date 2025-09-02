import { renderHook, waitFor } from '@testing-library/react';
import { useRegistration } from './useRegistration';
import * as authService from '../services/auth.service';
import { RegistroUsuarioRequest, Usuario } from '../types';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import React from 'react';

// Mock del servicio de autenticación
vi.mock('../services/auth.service');
const mockedAuthService = authService as vi.Mocked<typeof authService>;

// React Query necesita un QueryClientProvider para funcionar en los tests
const createWrapper = () => {
  const queryClient = new QueryClient({
    defaultOptions: { queries: { retry: false } },
  });
  return ({ children }: { children: React.ReactNode }) => (
    <QueryClientProvider client={queryClient}>{children}</QueryClientProvider>
  );
};

describe('useRegistration', () => {
  beforeEach(() => {
    vi.clearAllMocks();
  });

  it('debería manejar el estado de carga y éxito correctamente', async () => {
    // Arrange
    const createdUser: Usuario = { id: 1, nombre: 'Test', apellido: 'User', email: 'test@test.com', cargo: 'Miner', cedula: '1', telefono: '1' };
    mockedAuthService.register.mockResolvedValue(createdUser);
    const wrapper = createWrapper();
    const { result } = renderHook(() => useRegistration(), { wrapper });

    // Act
    result.current.registerUser({} as RegistroUsuarioRequest);

    // Assert
    await waitFor(() => {
      expect(result.current.isSuccess).toBe(true);
    });

    expect(result.current.isLoading).toBe(false);
    expect(result.current.error).toBe(null);
    expect(mockedAuthService.register).toHaveBeenCalledTimes(1);
  });

  it('debería manejar los errores correctamente', async () => {
    // Arrange
    const errorMessage = 'Registration failed';
    mockedAuthService.register.mockRejectedValue(new Error(errorMessage));
    const wrapper = createWrapper();
    const { result } = renderHook(() => useRegistration(), { wrapper });

    // Act
    result.current.registerUser({} as RegistroUsuarioRequest);

    // Assert
    await waitFor(() => {
      expect(result.current.error).not.toBe(null);
    });

    expect(result.current.isLoading).toBe(false);
    expect(result.current.isSuccess).toBe(false);
    expect(result.current.error?.message).toBe(errorMessage);
    expect(mockedAuthService.register).toHaveBeenCalledTimes(1);
  });
});

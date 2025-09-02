import { renderHook, waitFor } from '@testing-library/react';
import { useForgotPassword } from './useForgotPassword';
import { forgotPassword as forgotPasswordService } from '../services/auth.service';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import React from 'react';

// Mock del servicio
vi.mock('../services/auth.service');

// Wrapper con el provider de React Query
const createWrapper = () => {
  const queryClient = new QueryClient({ defaultOptions: { queries: { retry: false } } });
  return ({ children }: { children: React.ReactNode }) => (
    <QueryClientProvider client={queryClient}>{children}</QueryClientProvider>
  );
};

describe('useForgotPassword', () => {
  const mockForgotPassword = vi.mocked(forgotPasswordService);

  beforeEach(() => {
    vi.clearAllMocks();
  });

  it('debería manejar el estado de éxito correctamente', async () => {
    // Arrange
    mockForgotPassword.mockResolvedValue();
    const wrapper = createWrapper();
    const { result } = renderHook(() => useForgotPassword(), { wrapper });

    // Act
    result.current.forgotPassword({ email: 'test@example.com' });

    // Assert
    await waitFor(() => {
      expect(result.current.isSuccess).toBe(true);
    });

    expect(result.current.isLoading).toBe(false);
    expect(result.current.error).toBe(null);
    expect(mockForgotPassword).toHaveBeenCalledTimes(1);
  });

  it('debería manejar el estado de error correctamente', async () => {
    // Arrange
    const errorMessage = 'Network Error';
    mockForgotPassword.mockRejectedValue(new Error(errorMessage));
    const wrapper = createWrapper();
    const { result } = renderHook(() => useForgotPassword(), { wrapper });

    // Act
    result.current.forgotPassword({ email: 'test@example.com' });

    // Assert
    await waitFor(() => {
      expect(result.current.error).not.toBe(null);
    });

    expect(result.current.isLoading).toBe(false);
    expect(result.current.isSuccess).toBe(false);
    expect(result.current.error?.message).toBe(errorMessage);
    expect(mockForgotPassword).toHaveBeenCalledTimes(1);
  });
});
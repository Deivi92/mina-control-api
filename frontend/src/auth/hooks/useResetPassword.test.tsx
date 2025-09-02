import { renderHook, waitFor } from '@testing-library/react';
import { useResetPassword } from './useResetPassword';
import { resetPassword as resetPasswordService } from '../services/auth.service';
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

describe('useResetPassword', () => {
  const mockResetPassword = vi.mocked(resetPasswordService);

  beforeEach(() => {
    vi.clearAllMocks();
  });

  it('debería manejar el estado de éxito correctamente', async () => {
    // Arrange
    mockResetPassword.mockResolvedValue();
    const wrapper = createWrapper();
    const { result } = renderHook(() => useResetPassword(), { wrapper });

    // Act
    result.current.resetPassword({ token: 'valid-token', newPassword: 'new-password' });

    // Assert
    await waitFor(() => {
      expect(result.current.isSuccess).toBe(true);
    });

    expect(result.current.isLoading).toBe(false);
    expect(result.current.error).toBe(null);
    expect(mockResetPassword).toHaveBeenCalledTimes(1);
  });

  it('debería manejar el estado de error correctamente', async () => {
    // Arrange
    const errorMessage = 'Invalid token';
    mockResetPassword.mockRejectedValue(new Error(errorMessage));
    const wrapper = createWrapper();
    const { result } = renderHook(() => useResetPassword(), { wrapper });

    // Act
    result.current.resetPassword({ token: 'invalid-token', newPassword: 'new-password' });

    // Assert
    await waitFor(() => {
      expect(result.current.error).not.toBe(null);
    });

    expect(result.current.isLoading).toBe(false);
    expect(result.current.isSuccess).toBe(false);
    expect(result.current.error?.message).toBe(errorMessage);
    expect(mockResetPassword).toHaveBeenCalledTimes(1);
  });
});
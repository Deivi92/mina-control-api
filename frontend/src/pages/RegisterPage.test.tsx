import React from 'react';
import { render, screen, waitFor } from '@testing-library/react';
import { RegisterPage } from './RegisterPage';
import { MemoryRouter } from 'react-router-dom';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import userEvent from '@testing-library/user-event';

// Mock del hook useRegistration
const mockRegisterUser = vi.fn();
const mockUseRegistration = vi.fn();

vi.mock('../auth/hooks/useRegistration', () => ({
  useRegistration: () => mockUseRegistration(),
}));

const renderWithRouter = (component: React.ReactElement) => {
  const queryClient = new QueryClient({
    defaultOptions: {
      queries: {
        retry: false
      }
    }
  });
  
  return render(
    <QueryClientProvider client={queryClient}>
      <MemoryRouter>
        {component}
      </MemoryRouter>
    </QueryClientProvider>
  );
};

describe('RegisterPage', () => {
  beforeEach(() => {
    vi.clearAllMocks();
    
    // Configurar el mock para que retorne el valor por defecto
    mockUseRegistration.mockReturnValue({
      registerUser: mockRegisterUser,
      isLoading: false,
      isSuccess: false,
      error: null,
    });
  });

  it('debería renderizar el formulario de registro con los campos correctos', () => {
    renderWithRouter(<RegisterPage />);
    
    expect(screen.getByLabelText(/Correo Electrónico/i)).toBeInTheDocument();
    expect(screen.getByLabelText(/Contraseña/i)).toBeInTheDocument();
    expect(screen.getByRole('button', { name: /Registrarse/i })).toBeInTheDocument();
  }, 10000);

  it('debería llamar a la función de registro con los datos del formulario al hacer submit', async () => {
    // Arrange
    const user = userEvent.setup();
    renderWithRouter(<RegisterPage />);
    
    // Act
    await user.type(screen.getByLabelText(/Correo Electrónico/i), 'test@example.com');
    await user.type(screen.getByLabelText(/Contraseña/i), 'password123');
    
    await user.click(screen.getByRole('button', { name: /Registrarse/i }));

    // Assert
    await waitFor(() => {
      expect(mockRegisterUser).toHaveBeenCalledWith({
        email: 'test@example.com',
        password: 'password123',
      });
    }, { timeout: 10000 });
  }, 15000);
});

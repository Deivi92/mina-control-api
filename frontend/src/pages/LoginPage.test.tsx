import { render, screen, waitFor } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { describe, it, expect, vi, beforeEach } from 'vitest';
import { LoginPage } from './LoginPage';
import { BrowserRouter } from 'react-router-dom';
import { useAuth } from '../auth/hooks/useAuth';

// Mock del hook de autenticación para aislar completamente el componente LoginPage
vi.mock('../auth/hooks/useAuth');

const renderWithRouter = (ui: React.ReactElement) => {
  return render(<BrowserRouter>{ui}</BrowserRouter>);
};

describe('LoginPage (Component Test)', () => {
  const mockLogin = vi.fn();

  beforeEach(() => {
    // Limpiamos los mocks antes de cada prueba
    vi.clearAllMocks();
    // Configuramos el estado por defecto del mock de useAuth
    (useAuth as vi.Mock).mockReturnValue({
      login: mockLogin,
      isLoading: false,
      error: null,
    });
  });

  it('debería renderizar el formulario correctamente', () => {
    renderWithRouter(<LoginPage />);
    expect(screen.getByLabelText(/Correo Electrónico/i)).toBeInTheDocument();
    expect(screen.getByLabelText(/Contraseña/i)).toBeInTheDocument();
    expect(screen.getByRole('button', { name: /Ingresar/i })).toBeInTheDocument();
  });

  it('debería llamar a la función login con las credenciales correctas al enviar el formulario', async () => {
    const user = userEvent.setup();
    renderWithRouter(<LoginPage />);

    await user.type(screen.getByLabelText(/Correo Electrónico/i), 'test@example.com');
    await user.type(screen.getByLabelText(/Contraseña/i), 'password123');
    await user.click(screen.getByRole('button', { name: /Ingresar/i }));

    await waitFor(() => {
      expect(mockLogin).toHaveBeenCalledWith({ 
        email: 'test@example.com', 
        password: 'password123' 
      });
    });
  }, 10000);

  it('debería deshabilitar el botón y mostrar un spinner mientras isLoading es true', () => {
    (useAuth as vi.Mock).mockReturnValue({
      login: mockLogin,
      isLoading: true,
      error: null,
    });

    renderWithRouter(<LoginPage />);

    expect(screen.getByRole('button', { name: /Ingresar/i })).toBeDisabled();
    expect(screen.getByRole('progressbar')).toBeInTheDocument();
  });

  it('debería mostrar un mensaje de error cuando el hook devuelve un error', async () => {
    (useAuth as vi.Mock).mockReturnValue({
      login: mockLogin,
      isLoading: false,
      error: 'Credenciales incorrectas',
    });

    renderWithRouter(<LoginPage />);

    expect(await screen.findByText('Credenciales incorrectas')).toBeInTheDocument();
  });

  it('debería tener el botón de login deshabilitado si los campos están vacíos', () => {
    renderWithRouter(<LoginPage />);
    expect(screen.getByRole('button', { name: /Ingresar/i })).toBeDisabled();
  });
});

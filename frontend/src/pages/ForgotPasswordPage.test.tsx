import { render, screen } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { ForgotPasswordPage } from './ForgotPasswordPage';
import { BrowserRouter } from 'react-router-dom';
import { useForgotPassword } from '../auth/hooks/useForgotPassword';

// Mock del hook
vi.mock('../auth/hooks/useForgotPassword', () => ({
  useForgotPassword: vi.fn(),
}));

const mockForgotPassword = vi.fn();

describe('ForgotPasswordPage', () => {
  beforeEach(() => {
    vi.clearAllMocks();
    // Configuración por defecto del mock
    (useForgotPassword as vi.Mock).mockReturnValue({
      forgotPassword: mockForgotPassword,
      isLoading: false,
      isSuccess: false,
      error: null,
    });
  });

  const renderComponent = () => {
    render(
      <BrowserRouter>
        <ForgotPasswordPage />
      </BrowserRouter>
    );
  };

  it('debería renderizar el formulario de solicitud de recuperación', () => {
    renderComponent();
    expect(screen.getByRole('heading', { name: /recuperar contraseña/i })).toBeInTheDocument();
    expect(screen.getByLabelText(/correo electrónico/i)).toBeInTheDocument();
    expect(screen.getByRole('button', { name: /enviar enlace/i })).toBeInTheDocument();
  }, 10000);

  it('debería llamar a la función forgotPassword al enviar el formulario', async () => {
    const user = userEvent.setup();
    renderComponent();

    const emailInput = screen.getByLabelText(/correo electrónico/i);
    const submitButton = screen.getByRole('button', { name: /enviar enlace/i });

    await user.type(emailInput, 'test@example.com');
    await user.click(submitButton);

    expect(mockForgotPassword).toHaveBeenCalledWith({ email: 'test@example.com' });
  }, 10000);

  it('debería mostrar el mensaje de éxito si isSuccess es true', () => {
    (useForgotPassword as vi.Mock).mockReturnValue({
      isSuccess: true,
    });
    renderComponent();

    expect(screen.getByText(/recibirás un enlace/i)).toBeInTheDocument();
    expect(screen.queryByLabelText(/correo electrónico/i)).not.toBeInTheDocument();
  });

  it('debería mostrar un mensaje de error si hay un error', () => {
    (useForgotPassword as vi.Mock).mockReturnValue({
      error: new Error('Hubo un problema'),
    });
    renderComponent();

    expect(screen.getByText(/hubo un problema/i)).toBeInTheDocument();
  });
});
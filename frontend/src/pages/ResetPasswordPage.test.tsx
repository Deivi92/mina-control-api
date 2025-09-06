import { render, screen } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { ResetPasswordPage } from './ResetPasswordPage';
import { MemoryRouter, Route, Routes } from 'react-router-dom';
import { useResetPassword } from '../auth/hooks/useResetPassword';

// Mock del hook y de react-router-dom
vi.mock('../auth/hooks/useResetPassword', () => ({
  useResetPassword: vi.fn(),
}));

const mockResetPassword = vi.fn();

describe('ResetPasswordPage', () => {
  beforeEach(() => {
    vi.clearAllMocks();
    (useResetPassword as vi.Mock).mockReturnValue({
      resetPassword: mockResetPassword,
      isLoading: false,
      isSuccess: false,
      error: null,
    });
  });

  // Usamos MemoryRouter para poder simular la URL con el token
  // Capturamos el container para poder hacer queries directos al DOM
  const renderComponent = (token: string) => {
    const utils = render(
      <MemoryRouter initialEntries={[`/reset-password/${token}`]}>
        <Routes>
          <Route path="/reset-password/:token" element={<ResetPasswordPage />} />
        </Routes>
      </MemoryRouter>
    );
    // Devolvemos las utilidades de render + el container para queries directos
    return { ...utils, container: utils.container };
  };

  it('debería renderizar el formulario correctamente', () => {
    const { container } = renderComponent('test-token');
    expect(screen.getByRole('heading', { name: /establecer nueva contraseña/i })).toBeInTheDocument();
    // Seleccionar directamente el input por su id usando el container
    expect(container.querySelector('#password')).toBeInTheDocument();
    expect(container.querySelector('#confirmPassword')).toBeInTheDocument();
  }, 10000);

  it('debería mostrar un error si las contraseñas no coinciden', async () => {
    const user = userEvent.setup();
    const { container } = renderComponent('test-token');

    // Seleccionar directamente el input por su id usando el container
    const passwordInput = container.querySelector('#password');
    const confirmPasswordInput = container.querySelector('#confirmPassword');
    
    if (!passwordInput || !confirmPasswordInput) {
      throw new Error('No se encontraron los elementos de input');
    }

    await user.type(passwordInput, 'password123');
    await user.type(confirmPasswordInput, 'password456');

    expect(screen.getByText(/las contraseñas no coinciden/i)).toBeInTheDocument();
    expect(screen.getByRole('button', { name: /guardar contraseña/i })).toBeDisabled();
  }, 10000);

  it('debería llamar a resetPassword con el token y la nueva contraseña', async () => {
    const user = userEvent.setup();
    const { container } = renderComponent('test-token');

    // Seleccionar directamente el input por su id usando el container
    const passwordInput = container.querySelector('#password');
    const confirmPasswordInput = container.querySelector('#confirmPassword');
    const submitButton = screen.getByRole('button', { name: /guardar contraseña/i });
    
    if (!passwordInput || !confirmPasswordInput) {
      throw new Error('No se encontraron los elementos de input');
    }

    await user.type(passwordInput, 'new-password');
    await user.type(confirmPasswordInput, 'new-password');
    await user.click(submitButton);

    expect(mockResetPassword).toHaveBeenCalledWith({ token: 'test-token', newPassword: 'new-password' });
  }, 10000);

  it('debería mostrar el mensaje de éxito si isSuccess es true', () => {
    (useResetPassword as vi.Mock).mockReturnValue({ isSuccess: true });
    const { container } = renderComponent('test-token');

    expect(screen.getByText(/contraseña actualizada con éxito/i)).toBeInTheDocument();
    // Ahora, al ser exitoso, los campos no deberían estar presentes
    expect(container.querySelector('#password')).not.toBeInTheDocument();
    expect(container.querySelector('#confirmPassword')).not.toBeInTheDocument();
  }, 10000);
});
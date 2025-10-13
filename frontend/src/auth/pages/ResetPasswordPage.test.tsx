import { render, screen } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { ResetPasswordPage } from './ResetPasswordPage';
import { MemoryRouter, Route, Routes } from 'react-router-dom';
import { useResetPassword } from '../hooks/useResetPassword';

// Mock del hook y de react-router-dom
vi.mock('../hooks/useResetPassword', () => ({
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
    // Usar querySelector con el id del input para seleccionar directamente
    expect(container.querySelector('#newPassword')).toBeInTheDocument();
    expect(container.querySelector('#confirmPassword')).toBeInTheDocument();
  }, 10000);

  it('debería mostrar un error si las contraseñas no coinciden', async () => {
    const user = userEvent.setup();
    const { container } = renderComponent('test-token');

    // Usar querySelector con el id del input para seleccionar directamente
    const passwordInput = container.querySelector('#newPassword');
    const confirmPasswordInput = container.querySelector('#confirmPassword');
    const submitButton = screen.getByRole('button', { name: /guardar contraseña/i });
    
    if (!passwordInput || !confirmPasswordInput) {
      throw new Error('No se encontraron los elementos de input');
    }

    await user.type(passwordInput, 'password123');
    await user.type(confirmPasswordInput, 'password456');

    // Asegurarse de que el botón esté habilitado antes de hacer click
    // La validación debe ocurrir al enviar, no durante la escritura
    expect(submitButton).not.toBeDisabled();

    // Esperar a que se active la validación al hacer click en submit
    await user.click(submitButton);

    // Verificar que se muestra el error de validación
    expect(screen.getByText(/las contraseñas no coinciden/i)).toBeInTheDocument();
  }, 10000);

  it('debería llamar a resetPassword con el token y la nueva contraseña', async () => {
    const user = userEvent.setup();
    const { container } = renderComponent('test-token');

    // Usar querySelector con el id del input para seleccionar directamente
    const passwordInput = container.querySelector('#newPassword');
    const confirmPasswordInput = container.querySelector('#confirmPassword');
    const submitButton = screen.getByRole('button', { name: /guardar contraseña/i });
    
    if (!passwordInput || !confirmPasswordInput) {
      throw new Error('No se encontraron los elementos de input');
    }

    await user.type(passwordInput, 'new-password');
    await user.type(confirmPasswordInput, 'new-password');
    
    // Asegurarse de que el botón no esté deshabilitado antes de hacer click
    expect(submitButton).not.toBeDisabled();
    
    await user.click(submitButton);

    expect(mockResetPassword).toHaveBeenCalledWith({ token: 'test-token', newPassword: 'new-password' });
  }, 10000);

  it('debería mostrar el mensaje de éxito si isSuccess es true', () => {
    (useResetPassword as vi.Mock).mockReturnValue({ isSuccess: true });
    renderComponent('test-token');

    expect(screen.getByText(/contraseña actualizada con éxito/i)).toBeInTheDocument();
    // Ahora, al ser exitoso, el formulario no debería estar presente
    expect(screen.queryByLabelText(/nueva contraseña/i)).not.toBeInTheDocument();
    expect(screen.queryByLabelText(/confirmar nueva contraseña/i)).not.toBeInTheDocument();
  }, 10000);
});
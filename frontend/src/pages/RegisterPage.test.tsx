import { render, screen } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { RegisterPage } from './RegisterPage';
import { BrowserRouter } from 'react-router-dom';
import { useRegistration } from '../auth/hooks/useRegistration';

// Mock del hook de registro para tener control sobre él en los tests
vi.mock('../auth/hooks/useRegistration', () => ({
  useRegistration: vi.fn(),
}));

const mockRegisterUser = vi.fn();

describe('RegisterPage', () => {

  beforeEach(() => {
    // Reseteamos los mocks y configuramos el valor devuelto por defecto para cada test
    vi.clearAllMocks();
    // Mockeamos registerUser para que devuelva una promesa resuelta, simulando un registro exitoso
    mockRegisterUser.mockResolvedValue({});
    (useRegistration as vi.Mock).mockReturnValue({
      registerUser: mockRegisterUser,
      isLoading: false,
      error: null,
      isSuccess: false,
    });
  });

  it('debería renderizar el formulario de registro con todos los campos', () => {
    // Arrange
    render(
      <BrowserRouter>
        <RegisterPage />
      </BrowserRouter>
    );

    // Assert
    expect(screen.getByLabelText(/nombre/i)).toBeInTheDocument();
    expect(screen.getByLabelText(/apellido/i)).toBeInTheDocument();
    expect(screen.getByLabelText(/correo electrónico/i)).toBeInTheDocument();
    expect(screen.getByLabelText(/contraseña/i)).toBeInTheDocument();
    expect(screen.getByLabelText(/cédula/i)).toBeInTheDocument();
    expect(screen.getByLabelText(/teléfono/i)).toBeInTheDocument();
    expect(screen.getByLabelText(/cargo/i)).toBeInTheDocument();
    expect(screen.getByRole('button', { name: /registrarse/i })).toBeInTheDocument();
  });

  it('debería llamar a la función de registro con los datos del formulario al hacer submit', async () => {
    // Arrange
    const user = userEvent.setup();
    render(
      <BrowserRouter>
        <RegisterPage />
      </BrowserRouter>
    );

    // Localizamos todos los campos y el botón
    const nombreInput = screen.getByLabelText(/nombre/i);
    const apellidoInput = screen.getByLabelText(/apellido/i);
    const emailInput = screen.getByLabelText(/correo electrónico/i);
    const passwordInput = screen.getByLabelText(/contraseña/i);
    const cedulaInput = screen.getByLabelText(/cédula/i);
    const telefonoInput = screen.getByLabelText(/teléfono/i);
    const cargoInput = screen.getByLabelText(/cargo/i);
    const submitButton = screen.getByRole('button', { name: /registrarse/i });

    const testData = {
      nombre: 'Test',
      apellido: 'User',
      email: 'test@example.com',
      password: 'password123',
      cedula: '12345678',
      telefono: '555-5555',
      cargo: 'Tester',
    };

    // Act: Simulamos la acción del usuario llenando el formulario y haciendo clic
    // Se ha reportado que este test puede ser lento, aumentamos el timeout explícitamente
    await user.type(nombreInput, testData.nombre);
    await user.type(apellidoInput, testData.apellido);
    await user.type(emailInput, testData.email);
    await user.type(passwordInput, testData.password);
    await user.type(cedulaInput, testData.cedula);
    await user.type(telefonoInput, testData.telefono);
    await user.type(cargoInput, testData.cargo);
    
    await user.click(submitButton);

    // Assert: Verificamos que nuestro mock fue llamado correctamente
    expect(mockRegisterUser).toHaveBeenCalledTimes(1);
    expect(mockRegisterUser).toHaveBeenCalledWith(testData);
  }, 15000); // Aumentamos el timeout a 15 segundos para este test específico si es necesario
});
import { render, screen, fireEvent } from '@testing-library/react';
import { LoginPage } from './LoginPage';
import { useAuth } from '../auth/hooks/useAuth';

// Mockeamos nuestro hook useAuth
jest.mock('../auth/hooks/useAuth');
const mockedUseAuth = useAuth as jest.Mock;

describe('LoginPage', () => {

  beforeEach(() => {
    // Reseteamos el mock antes de cada prueba con valores por defecto
    mockedUseAuth.mockReturnValue({
      login: jest.fn(), // una función mock para espiar si es llamada
      isLoading: false,
      error: null,
    });
  });

  it('debería renderizar el título, campos de email/contraseña y botón', () => {
    // TODO: Implementar
  });

  it('debería permitir al usuario escribir en los campos', () => {
    // TODO: Implementar
  });

  it('debería llamar a la función login del hook con las credenciales al hacer submit', () => {
    // TODO: Implementar
  });

  it('debería mostrar un indicador de carga y deshabilitar el botón si isLoading es true', () => {
    // TODO: Implementar
  });

  it('debería mostrar un mensaje de error si el hook tiene un error', () => {
    // TODO: Implementar
  });

});

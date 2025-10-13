import { render, screen } from '@testing-library/react';
import { describe, it, expect, vi } from 'vitest';
import { DashboardPage } from './DashboardPage';
import { BrowserRouter } from 'react-router-dom';
import { useAuth } from '../../auth/hooks/useAuth';

// Mock del hook de autenticación
vi.mock('../../auth/hooks/useAuth');

const renderWithRouter = (ui: React.ReactElement) => {
  return render(<BrowserRouter>{ui}</BrowserRouter>);
};

describe('DashboardPage (Component Test)', () => {
  beforeEach(() => {
    // Limpiamos los mocks antes de cada prueba
    vi.clearAllMocks();
    // Configuramos el estado por defecto del mock de useAuth
    (useAuth as vi.Mock).mockReturnValue({
      user: { id: 1, email: 'test@example.com' },
      logout: vi.fn(),
    });
  });

  it('debería renderizar el dashboard correctamente', () => {
    renderWithRouter(<DashboardPage />);
    expect(screen.getByText('Dashboard')).toBeInTheDocument();
    expect(screen.getByText('Bienvenido, test@example.com!')).toBeInTheDocument();
    expect(screen.getByRole('button', { name: /Cerrar Sesión/i })).toBeInTheDocument();
  });

  it('debería llamar a la función logout al hacer clic en el botón', () => {
    const mockLogout = vi.fn();
    (useAuth as vi.Mock).mockReturnValue({
      user: { id: 1, email: 'test@example.com' },
      logout: mockLogout,
    });

    renderWithRouter(<DashboardPage />);
    const logoutButton = screen.getByRole('button', { name: /Cerrar Sesión/i });
    logoutButton.click();

    expect(mockLogout).toHaveBeenCalled();
  });
});
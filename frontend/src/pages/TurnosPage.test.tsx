
import { render, screen, waitFor, fireEvent } from '@testing-library/react';
import { MemoryRouter } from 'react-router-dom';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import axios from 'axios';
import { TurnosPage } from './TurnosPage';
import { TipoTurno } from '../domains/turnos/types';
import { Empleado } from '../domains/empleado/types';

// Mockear axios, nuestro límite del sistema
vi.mock('axios');
const mockedAxios = vi.mocked(axios, true);

const queryClient = new QueryClient({
  defaultOptions: {
    queries: { retry: false },
  },
});

const renderComponent = () => {
  return render(
    <QueryClientProvider client={queryClient}>
      <MemoryRouter>
        <TurnosPage />
      </MemoryRouter>
    </QueryClientProvider>
  );
};

describe('TurnosPage - Integration Test', () => {
  beforeEach(() => {
    vi.resetAllMocks();
    queryClient.clear();

    // Mock de endpoints que se llaman al cargar la página
    const mockEmpleados: Empleado[] = [{ id: 1, nombre: 'Test', apellido: 'User' }];
    mockedAxios.get.mockImplementation((url) => {
        if (url.includes('/api/v1/empleados')) {
            return Promise.resolve({ data: mockEmpleados });
        }
        if (url.includes('/api/v1/turnos')) {
            return Promise.resolve({ data: [] });
        }
        if (url.includes('/api/v1/asistencia/consultar')) {
            return Promise.resolve({ data: [] });
        }
        return Promise.reject(new Error(`Unhandled GET request to ${url}`));
    });

  });

  it('debería renderizar el título y las pestañas correctamente', () => {
    renderComponent();
    expect(screen.getByText('Gestión de Turnos')).toBeInTheDocument();
    expect(screen.getByRole('tab', { name: /administrar tipos de turno/i })).toBeInTheDocument();
    expect(screen.getByRole('tab', { name: /control de asistencia/i })).toBeInTheDocument();
  });

  it('debería obtener y renderizar la lista de tipos de turno en la primera pestaña', async () => {
    const mockTurnos: TipoTurno[] = [
      { id: 1, nombre: 'Turno de Integración', horaInicio: '08:00:00', horaFin: '16:00:00', color: '#aabbcc' },
    ];
    mockedAxios.get.mockResolvedValueOnce({ data: mockTurnos });

    renderComponent();

    await waitFor(() => {
      expect(screen.getByText('Turno de Integración')).toBeInTheDocument();
    });
    expect(mockedAxios.get).toHaveBeenCalledWith('/api/v1/turnos');
  });

  it('debería cambiar a la pestaña de asistencia y realizar la llamada a la API correspondiente', async () => {
    renderComponent();

    // La primera llamada es para los tipos de turno
    await waitFor(() => expect(mockedAxios.get).toHaveBeenCalledWith('/api/v1/turnos'));

    const asistenciaTab = screen.getByRole('tab', { name: /control de asistencia/i });
    fireEvent.click(asistenciaTab);

    // Al hacer clic, se debería disparar la consulta de asistencia
    await waitFor(() => {
        expect(mockedAxios.get).toHaveBeenCalledWith('/api/v1/asistencia/consultar', expect.any(Object));
    });
    expect(screen.getByText(/registro rápido de asistencia/i)).toBeInTheDocument();
  });

});


import React from 'react';
import { render, screen, waitFor } from '@testing-library/react';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import axios from 'axios';
import { ProduccionPage } from './ProduccionPage';
import { RegistroProduccionDTO } from '../domains/produccion/types';

// Mockear axios, nuestro límite del sistema
vi.mock('axios');
const mockedAxios = axios as jest.Mocked<typeof axios>;

// Wrapper con el QueryClient para los tests
const createWrapper = () => {
  const queryClient = new QueryClient({
    defaultOptions: { queries: { retry: false, cacheTime: 0 } },
  });
  return ({ children }: { children: React.ReactNode }) => (
    <QueryClientProvider client={queryClient}>{children}</QueryClientProvider>
  );
};

describe('ProduccionPage - Pruebas de Integración Pura', () => {

  beforeEach(() => {
    vi.clearAllMocks();
    // Mock para las dependencias secundarias que necesita la página (filtros)
    mockedAxios.get.mockImplementation((url) => {
      if (url.includes('/api/v1/empleados')) {
        return Promise.resolve({ data: [] });
      }
      if (url.includes('/api/v1/turnos')) {
        return Promise.resolve({ data: [] });
      }
      return Promise.reject(new Error(`Unhandled GET request to ${url}`));
    });
  });

  it('debería mostrar la lista de registros si la API responde con éxito', async () => {
    const mockData: RegistroProduccionDTO[] = [
      { id: 1, nombreEmpleado: 'Integration User', nombreTurno: 'Test Turno', fechaRegistro: '2024-05-20', cantidadExtraidaToneladas: 100, validado: false, ubicacionExtraccion: 'Mina 1', observaciones: '' },
    ];
    mockedAxios.get.mockResolvedValue({ data: mockData });

    render(<ProduccionPage />, { wrapper: createWrapper() });

    // Esperamos que el nombre del empleado aparezca en la tabla
    expect(await screen.findByText('Integration User')).toBeInTheDocument();
    expect(screen.getByText('Test Turno')).toBeInTheDocument();
  });

  it('debería mostrar un estado de carga mientras se obtienen los datos', async () => {
    // Hacemos que la promesa nunca se resuelva para mantener el estado de carga
    mockedAxios.get.mockReturnValue(new Promise(() => {}));

    render(<ProduccionPage />, { wrapper: createWrapper() });

    // Esperamos que el spinner de carga esté presente
    expect(await screen.findByRole('progressbar')).toBeInTheDocument();
  });

  it('debería mostrar un mensaje de error si la API falla', async () => {
    // Simulamos un error de red
    mockedAxios.get.mockRejectedValue(new Error('Error de Red 500'));

    render(<ProduccionPage />, { wrapper: createWrapper() });

    // Esperamos que aparezca el mensaje de error en la página
    expect(await screen.findByText(/Error al cargar los registros/i)).toBeInTheDocument();
  });

});

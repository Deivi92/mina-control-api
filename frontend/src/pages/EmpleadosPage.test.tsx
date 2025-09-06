import React from 'react';
import { render, screen, waitFor } from '@testing-library/react';
import { EmpleadosPage } from './EmpleadosPage';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import axios from 'axios';
import type { Empleado } from '../domains/empleado/types';
import userEvent from '@testing-library/user-event';

// Mockear axios, que es el límite de nuestro sistema en esta prueba
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

// Datos de prueba
const mockEmpleados: Empleado[] = [
  { id: 1, nombre: 'Juan', apellido: 'Pérez', email: 'juan@test.com', puesto: 'Minero', salario: 50000, estado: 'ACTIVO', fechaContratacion: '2023-01-15', fechaNacimiento: '1990-05-20', numeroIdentificacion: '111' },
];

describe('EmpleadosPage - Pruebas de Integración', () => {

  beforeEach(() => {
    vi.clearAllMocks();
  });

  it('debería mostrar la lista de empleados al cargar la página exitosamente', async () => {
    mockedAxios.get.mockResolvedValue({ data: mockEmpleados });
    
    render(<EmpleadosPage />, { wrapper: createWrapper() });

    // Buscamos directamente el elemento td que contiene el nombre del empleado
    expect(await screen.findByText('Juan Pérez')).toBeInTheDocument();
  }, 10000);

  it('debería mostrar un estado de error si la API falla', async () => {
    mockedAxios.get.mockRejectedValue(new Error('Error de red'));
    render(<EmpleadosPage />, { wrapper: createWrapper() });

    expect(await screen.findByText(/Error al cargar los empleados/i)).toBeInTheDocument();
  }, 10000);

  it('debería permitir crear un empleado y mostrarlo en la tabla', async () => {
    const user = userEvent.setup();
    mockedAxios.get.mockResolvedValueOnce({ data: [] }); // Carga inicial vacía
    render(<EmpleadosPage />, { wrapper: createWrapper() });
    expect(await screen.findByText(/No se encontraron empleados/i)).toBeInTheDocument();

    const nuevoEmpleado = { id: 2, nombre: 'Ana', apellido: 'García', email: 'ana@test.com', puesto: 'Geóloga', salario: 60000, estado: 'ACTIVO', fechaContratacion: '2024-01-01', fechaNacimiento: '1992-01-01', numeroIdentificacion: '222' };
    mockedAxios.post.mockResolvedValue({ data: nuevoEmpleado });
    mockedAxios.get.mockResolvedValueOnce({ data: [nuevoEmpleado] }); // Recarga con el nuevo dato

    await user.click(screen.getByRole('button', { name: /Crear Nuevo Empleado/i }));
    await user.type(screen.getByLabelText(/Nombre/i), 'Ana');
    await user.type(screen.getByLabelText(/Apellido/i), 'García');
    await user.type(screen.getByLabelText(/Email/i), 'ana@test.com');
    await user.type(screen.getByLabelText(/Puesto/i), 'Geóloga');
    await user.type(screen.getByLabelText(/Salario/i), '60000');
    await user.type(screen.getByLabelText(/Número de Identificación/i), '222');
    await user.type(screen.getByLabelText(/Fecha de Nacimiento/i), '1992-01-01');
    
    await user.click(screen.getByRole('button', { name: /Guardar/i }));

    // Buscamos directamente el elemento td que contiene el nombre del empleado
    expect(await screen.findByText('Ana García')).toBeInTheDocument();
    expect(mockedAxios.post).toHaveBeenCalledTimes(1);
  }, 20000);

  it('debería permitir eliminar un empleado', async () => {
    const user = userEvent.setup();
    mockedAxios.get.mockResolvedValueOnce({ data: mockEmpleados });
    render(<EmpleadosPage />, { wrapper: createWrapper() });
    
    // Buscamos directamente el elemento td que contiene el nombre del empleado
    expect(await screen.findByText('Juan Pérez')).toBeInTheDocument();

    mockedAxios.delete.mockResolvedValue({});
    mockedAxios.get.mockResolvedValueOnce({ data: [] }); // Recarga vacía

    // Buscamos el botón de eliminar por su aria-label
    await user.click(screen.getByRole('button', { name: /eliminar/i }));
    await user.click(await screen.findByRole('button', { name: /Eliminar/i }));

    expect(await screen.findByText(/No se encontraron empleados/i)).toBeInTheDocument();
    expect(mockedAxios.delete).toHaveBeenCalledWith('/api/empleados/1');
  }, 15000);
});
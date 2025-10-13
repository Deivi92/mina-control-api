import React from 'react';
import { render, screen, waitFor, act } from '@testing-library/react';
import { EmpleadosPage } from './EmpleadosPage';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import axios from 'axios';
import type { Empleado } from '../domains/empleado/types';
import userEvent from '@testing-library/user-event';

// Mockear axios
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

// Datos de prueba con la estructura actual
const mockEmpleados = [
  { 
    id: 1, 
    nombres: 'Ana', 
    apellidos: 'García', 
    email: 'ana@test.com', 
    cargo: 'Geóloga', 
    salarioBase: 60000, 
    estado: 'ACTIVO', 
    fechaContratacion: '2024-01-01', 
    rolSistema: 'EMPLEADO' as const,
    numeroIdentificacion: '222' 
  },
];

describe('EmpleadosPage - Pruebas de Integración', () => {

  beforeEach(() => {
    vi.clearAllMocks();
  });

  it('debería mostrar la lista de empleados al cargar la página exitosamente', async () => {
    mockedAxios.get.mockResolvedValue({ data: mockEmpleados });
    
    render(<EmpleadosPage />, { wrapper: createWrapper() });

    // Buscamos directamente el elemento td que contiene el nombre del empleado
    expect(await screen.findByText('Ana García')).toBeInTheDocument();
  }, 10000);

  it('debería mostrar un estado de error si la API falla', async () => {
    mockedAxios.get.mockRejectedValue(new Error('Error de red'));
    render(<EmpleadosPage />, { wrapper: createWrapper() });

    expect(await screen.findByText(/Error al cargar los empleados/i)).toBeInTheDocument();
  }, 10000);

  it('debería permitir crear un empleado y mostrarlo en la tabla', async () => {
    const user = userEvent.setup();
    
    // Simular la respuesta inicial vacía
    mockedAxios.get.mockResolvedValueOnce({ data: [] });
    render(<EmpleadosPage />, { wrapper: createWrapper() });
    
    // Esperar a que se muestre el mensaje de "no empleados"
    expect(await screen.findByText(/No se encontraron empleados/i)).toBeInTheDocument();

    const nuevoEmpleado = { 
      id: 2, 
      nombres: 'Ana', 
      apellidos: 'García', 
      email: 'ana@test.com', 
      cargo: 'Geóloga', 
      salarioBase: 60000, 
      estado: 'ACTIVO', 
      fechaContratacion: '2024-01-01', 
      rolSistema: 'EMPLEADO' as const,
      numeroIdentificacion: '222',
      telefono: '123456789'
    };
    
    // Asegurar que todas las futuras solicitudes get devuelven la lista actualizada
    mockedAxios.get.mockResolvedValue({ data: [nuevoEmpleado] });
    mockedAxios.post.mockResolvedValue({ data: nuevoEmpleado });

    await user.click(screen.getByRole('button', { name: /Crear Nuevo Empleado/i }));

    // Rellenamos el formulario con los campos correctos
    await user.type(await screen.findByLabelText(/Nombres/i), 'Ana');
    await user.type(screen.getByLabelText(/Apellidos/i), 'García');
    await user.type(screen.getByLabelText(/Correo Electrónico/i), 'ana@test.com');
    await user.type(screen.getByLabelText(/Cargo/i), 'Geóloga');
    await user.type(screen.getByLabelText(/Salario Base/i), '60000');
    await user.type(screen.getByLabelText(/Número de Identificación/i), '222');
    await user.type(screen.getByLabelText(/Fecha de Contratación/i), '2024-01-01');
    await user.type(screen.getByLabelText(/Teléfono/i), '123456789');
    
    await user.click(screen.getByRole('button', { name: /Guardar/i }));

    // Esperar a que se actualice la tabla y aparezca el nuevo empleado
    expect(await screen.findByText('Ana García')).toBeInTheDocument();
    expect(mockedAxios.post).toHaveBeenCalledTimes(1);
  }, 40000);

  it('debería permitir eliminar un empleado', async () => {
    const user = userEvent.setup();
    mockedAxios.get.mockResolvedValueOnce({ data: mockEmpleados });
    // Simular que las llamadas posteriores a la API también funcionan correctamente
    mockedAxios.get.mockResolvedValue({ data: mockEmpleados });
    render(<EmpleadosPage />, { wrapper: createWrapper() });
    
    // Buscamos directamente el elemento que contiene el nombre completo del empleado en la tabla
    // El empleado que se espera eliminar es 'Ana García' basado en los datos mockeados
    expect(await screen.findByText('Ana García')).toBeInTheDocument();

    mockedAxios.delete.mockResolvedValue({});
    // Después de eliminar, la API debería devolver una lista vacía
    await act(async () => {
      mockedAxios.get.mockResolvedValue({ data: [] });
    });

    // Buscamos el botón de eliminar por su aria-label
    await user.click(screen.getByRole('button', { name: /eliminar/i }));
    await user.click(await screen.findByRole('button', { name: /Eliminar/i }));

    expect(await screen.findByText(/No se encontraron empleados/i)).toBeInTheDocument();
    expect(mockedAxios.delete).toHaveBeenCalledWith('/api/v1/empleados/1');
  }, 15000);
});
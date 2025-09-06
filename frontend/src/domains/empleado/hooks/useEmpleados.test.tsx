import { renderHook, waitFor } from '@testing-library/react';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import React from 'react';
import { useEmpleados } from './useEmpleados';
import { empleadoService } from '../services/empleado.service';
import { Empleado, EmpleadoRequest } from '../types';

// Mockear el servicio para aislar el hook de la capa de datos
vi.mock('../services/empleado.service');
const mockedEmpleadoService = empleadoService as vi.Mocked<typeof empleadoService>;

// Wrapper para proveer el QueryClient que React Query necesita en los tests
const createWrapper = () => {
  const queryClient = new QueryClient({
    defaultOptions: {
      queries: {
        retry: false, // Desactivar reintentos para tests más rápidos y predecibles
      },
    },
  });
  return ({ children }: { children: React.ReactNode }) => (
    <QueryClientProvider client={queryClient}>{children}</QueryClientProvider>
  );
};

describe('useEmpleados', () => {
  
  afterEach(() => {
    vi.clearAllMocks(); // Limpiar mocks después de cada prueba
  });

  // Pruebas para la query que obtiene los empleados
  describe('empleadosQuery', () => {
    it('debería obtener los datos de los empleados exitosamente', async () => {
      // Arrange: Preparamos el mock del servicio
      const mockEmpleados: Empleado[] = [{ id: 1, nombre: 'Juan', apellido: 'Perez', email: 'j@p.com', puesto: 'minero', salario: 1, estado: 'ACTIVO', fechaContratacion: '', fechaNacimiento: '' }];
      mockedEmpleadoService.obtenerEmpleados.mockResolvedValue(mockEmpleados);

      // Act: Renderizamos el hook
      const { result } = renderHook(() => useEmpleados(), { wrapper: createWrapper() });

      // Assert: Verificamos los estados
      expect(result.current.empleadosQuery.isLoading).toBe(true);
      await waitFor(() => expect(result.current.empleadosQuery.isSuccess).toBe(true));
      expect(result.current.empleadosQuery.data).toEqual(mockEmpleados);
    });

    it('debería manejar un estado de error', async () => {
      mockedEmpleadoService.obtenerEmpleados.mockRejectedValue(new Error('Error de API'));

      const { result } = renderHook(() => useEmpleados(), { wrapper: createWrapper() });

      await waitFor(() => expect(result.current.empleadosQuery.isError).toBe(true));
      expect(result.current.empleadosQuery.error).toBeInstanceOf(Error);
    });
  });

  // Pruebas para la mutación que crea empleados
  describe('crearEmpleadoMutation', () => {
    it('debería llamar al servicio con los datos correctos al crear', async () => {
      const newEmpleado: EmpleadoRequest = { nombre: 'Nuevo', apellido: 'Empleado', email: 'n@e.com', puesto: 'nuevo', salario: 1, numeroIdentificacion: '123', fechaNacimiento: '' };
      mockedEmpleadoService.crearEmpleado.mockResolvedValue({ id: 1, ...newEmpleado, estado: 'ACTIVO', fechaContratacion: '' });

      const { result } = renderHook(() => useEmpleados(), { wrapper: createWrapper() });

      // Act: Ejecutamos la mutación
      result.current.crearEmpleadoMutation.mutate(newEmpleado);

      await waitFor(() => expect(result.current.crearEmpleadoMutation.isSuccess).toBe(true));
      expect(mockedEmpleadoService.crearEmpleado).toHaveBeenCalledWith(newEmpleado);
    });
  });

  // Pruebas para la mutación que actualiza empleados
  describe('actualizarEmpleadoMutation', () => {
    it('debería llamar al servicio con el id y los datos correctos al actualizar', async () => {
      const empleadoId = 1;
      const updatedEmpleado: EmpleadoRequest = { nombre: 'Actualizado', apellido: 'Empleado', email: 'a@e.com', puesto: 'actualizado', salario: 2, numeroIdentificacion: '456', fechaNacimiento: '' };
      mockedEmpleadoService.actualizarEmpleado.mockResolvedValue({ id: empleadoId, ...updatedEmpleado, estado: 'ACTIVO', fechaContratacion: '' });

      const { result } = renderHook(() => useEmpleados(), { wrapper: createWrapper() });

      result.current.actualizarEmpleadoMutation.mutate({ id: empleadoId, data: updatedEmpleado });

      await waitFor(() => expect(result.current.actualizarEmpleadoMutation.isSuccess).toBe(true));
      expect(mockedEmpleadoService.actualizarEmpleado).toHaveBeenCalledWith(empleadoId, updatedEmpleado);
    });
  });

  // Pruebas para la mutación que elimina empleados
  describe('eliminarEmpleadoMutation', () => {
    it('debería llamar al servicio con el id correcto al eliminar', async () => {
      const empleadoId = 1;
      mockedEmpleadoService.eliminarEmpleado.mockResolvedValue();

      const { result } = renderHook(() => useEmpleados(), { wrapper: createWrapper() });

      result.current.eliminarEmpleadoMutation.mutate(empleadoId);

      await waitFor(() => expect(result.current.eliminarEmpleadoMutation.isSuccess).toBe(true));
      expect(mockedEmpleadoService.eliminarEmpleado).toHaveBeenCalledWith(empleadoId);
    });
  });
});
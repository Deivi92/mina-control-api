import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { empleadoService } from '../services/empleado.service';
import type { EmpleadoRequest } from '../types';

// Clave única para la query de empleados. Se usa para caching, invalidación y re-obtención.
export const EMPLEADOS_QUERY_KEY = 'empleados';

/**
 * Hook personalizado para gestionar las operaciones CRUD de los empleados.
 * Encapsula toda la lógica de estado del servidor (fetching, caching, mutations).
 */
export const useEmpleados = () => {
  const queryClient = useQueryClient();

  // QUERY: para obtener la lista de todos los empleados.
  const empleadosQuery = useQuery({
    queryKey: [EMPLEADOS_QUERY_KEY],
    queryFn: async () => {
      const empleados = await empleadoService.obtenerEmpleados();
      // Filtrar empleados inactivos para que no se muestren en la tabla
      return empleados.filter(empleado => empleado.estado === 'ACTIVO');
    },
  });

  // MUTATION: para crear un nuevo empleado.
  const crearEmpleadoMutation = useMutation({
    mutationFn: (empleadoData: EmpleadoRequest) => 
      empleadoService.crearEmpleado(empleadoData),
    onSuccess: () => {
      // Cuando la mutación es exitosa, invalida la query de empleados.
      // Esto hace que React Query vuelva a obtener los datos actualizados automáticamente.
      queryClient.invalidateQueries({ queryKey: [EMPLEADOS_QUERY_KEY] });
      queryClient.refetchQueries({ queryKey: [EMPLEADOS_QUERY_KEY] });
    },
  });

  // MUTATION: para actualizar un empleado existente.
  const actualizarEmpleadoMutation = useMutation({
    mutationFn: ({ id, data }: { id: number; data: EmpleadoRequest }) =>
      empleadoService.actualizarEmpleado(id, data),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: [EMPLEADOS_QUERY_KEY] });
      queryClient.refetchQueries({ queryKey: [EMPLEADOS_QUERY_KEY] });
    },
  });

  // MUTATION: para eliminar (desactivar) un empleado.
  const eliminarEmpleadoMutation = useMutation({
    mutationFn: (id: number) => empleadoService.eliminarEmpleado(id),
    onSuccess: () => {
      // Invalidar y refetch para asegurar que la UI se actualice correctamente
      queryClient.invalidateQueries({ queryKey: [EMPLEADOS_QUERY_KEY] });
      queryClient.refetchQueries({ queryKey: [EMPLEADOS_QUERY_KEY] });
    },
  });

  return {
    empleadosQuery,
    crearEmpleadoMutation,
    actualizarEmpleadoMutation,
    eliminarEmpleadoMutation,
  };
};
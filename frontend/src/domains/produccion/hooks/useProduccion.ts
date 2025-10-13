import { 
  useQuery, 
  useMutation, 
  useQueryClient 
} from '@tanstack/react-query';
import { 
  produccionService 
} from '../services/produccion.service';
import type { 
  RegistroProduccionCreateDTO, 
  RegistroProduccionUpdateDTO,
  ProduccionFilters 
} from '../types';

export const useProduccion = () => {
  const queryClient = useQueryClient();

  // Query para listar registros de producción
  const listarRegistros = (params?: ProduccionFilters) => {
    return useQuery({
      queryKey: ['produccion', 'listar', params],
      queryFn: () => produccionService.listarRegistros(params),
      staleTime: 5 * 60 * 1000, // 5 minutos
    });
  };

  // Query para obtener un registro por ID
  const obtenerRegistroPorId = (id: number) => {
    return useQuery({
      queryKey: ['produccion', 'detalle', id],
      queryFn: () => produccionService.obtenerRegistroPorId(id),
      staleTime: 5 * 60 * 1000, // 5 minutos
    });
  };

  // Mutación para registrar una nueva producción
  const registrarProduccion = () => {
    return useMutation({
      mutationFn: (data: RegistroProduccionCreateDTO) => 
        produccionService.registrarProduccion(data),
      onSuccess: () => {
        // Invalidar la query de listado para actualizar la tabla
        queryClient.invalidateQueries({ queryKey: ['produccion'] });
        queryClient.invalidateQueries({ queryKey: ['produccion', 'listar'] });
      },
    });
  };

  // Mutación para actualizar un registro existente
  const actualizarRegistro = () => {
    return useMutation({
      mutationFn: ({ id, data }: { id: number; data: RegistroProduccionUpdateDTO }) => 
        produccionService.actualizarRegistro(id, data),
      onSuccess: (_, variables) => {
        // Invalidar la query de listado y la específica del registro actualizado
        queryClient.invalidateQueries({ queryKey: ['produccion'] });
        queryClient.invalidateQueries({ queryKey: ['produccion', 'listar'] });
        queryClient.invalidateQueries({ queryKey: ['produccion', 'detalle', variables.id] });
      },
    });
  };

  // Mutación para eliminar un registro
  const eliminarRegistro = () => {
    return useMutation({
      mutationFn: (id: number) => produccionService.eliminarRegistro(id),
      onSuccess: () => {
        // Invalidar la query de listado para actualizar la tabla
        queryClient.invalidateQueries({ queryKey: ['produccion'] });
        queryClient.invalidateQueries({ queryKey: ['produccion', 'listar'] });
      },
    });
  };

  // Mutación para validar un registro
  const validarRegistro = () => {
    return useMutation({
      mutationFn: (id: number) => produccionService.validarRegistro(id),
      onSuccess: () => {
        // Invalidar la query de listado para actualizar la tabla
        queryClient.invalidateQueries({ queryKey: ['produccion'] });
        queryClient.invalidateQueries({ queryKey: ['produccion', 'listar'] });
      },
    });
  };

  return {
    listarRegistros,
    obtenerRegistroPorId,
    registrarProduccion,
    actualizarRegistro,
    eliminarRegistro,
    validarRegistro
  };
};
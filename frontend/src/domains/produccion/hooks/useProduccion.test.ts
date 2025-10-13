import { describe, it, expect, vi, beforeEach, afterEach } from 'vitest';
import { useProduccion } from './useProduccion';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { produccionService } from '../services/produccion.service';

// Mock de react-query
vi.mock('@tanstack/react-query', async () => {
  const actual = await import('@tanstack/react-query');
  return {
    ...actual,
    useQuery: vi.fn(),
    useMutation: vi.fn(),
    useQueryClient: vi.fn()
  };
});

// Mock del servicio
vi.mock('../services/produccion.service', () => ({
  produccionService: {
    listarRegistros: vi.fn(),
    registrarProduccion: vi.fn(),
    obtenerRegistroPorId: vi.fn(),
    actualizarRegistro: vi.fn(),
    eliminarRegistro: vi.fn(),
    validarRegistro: vi.fn()
  }
}));

describe('useProduccion', () => {
  const mockQueryClient = {
    invalidateQueries: vi.fn()
  };

  beforeEach(() => {
    vi.clearAllMocks();
    (useQueryClient as vi.MockedFunction<typeof useQueryClient>).mockReturnValue(mockQueryClient);
  });

  describe('query: listarRegistros', () => {
    it('debería configurar correctamente la query para listar registros de producción', () => {
      const params = {
        empleadoId: 1,
        fechaInicio: '2024-01-01',
        fechaFin: '2024-01-31'
      };

      (useQuery as vi.MockedFunction<typeof useQuery>).mockImplementation((options: any) => {
        if (options.queryKey[0] === 'produccion' && options.queryKey[1] === 'listar') {
          return { data: [], isLoading: false, error: null, refetch: vi.fn() };
        }
      });

      const result = useProduccion().listarRegistros(params);

      expect(useQuery).toHaveBeenCalledWith({
        queryKey: ['produccion', 'listar', params],
        queryFn: expect.any(Function),
        staleTime: 300000
      });
      expect(result.data).toEqual([]);
    });

    it('debería retornar el estado de carga cuando isLoading es true', () => {
      (useQuery as vi.MockedFunction<typeof useQuery>).mockReturnValue({
        data: undefined,
        isLoading: true,
        error: null,
        refetch: vi.fn()
      });

      const result = useProduccion().listarRegistros();

      expect(result.isLoading).toBe(true);
    });

    it('debería retornar el error cuando hay un error en la consulta', () => {
      const error = new Error('Error de red');
      (useQuery as vi.MockedFunction<typeof useQuery>).mockReturnValue({
        data: undefined,
        isLoading: false,
        error: error,
        refetch: vi.fn()
      });

      const result = useProduccion().listarRegistros();

      expect(result.error).toBe(error);
    });
  });

  describe('query: obtenerRegistroPorId', () => {
    it('debería configurar correctamente la query para obtener un registro por ID', () => {
      (useQuery as vi.MockedFunction<typeof useQuery>).mockImplementation((options: any) => {
        if (options.queryKey[0] === 'produccion' && options.queryKey[1] === 'detalle') {
          return { data: null, isLoading: false, error: null, refetch: vi.fn() };
        }
      });

      const result = useProduccion().obtenerRegistroPorId(1);

      expect(useQuery).toHaveBeenCalledWith({
        queryKey: ['produccion', 'detalle', 1],
        queryFn: expect.any(Function),
        staleTime: 300000
      });
      expect(result.data).toBeNull();
    });
  });

  describe('mutation: registrarProduccion', () => {
    it('debería configurar correctamente la mutación para registrar una nueva producción', () => {
      const mutationData = {
        empleadoId: 1,
        tipoTurnoId: 1,
        fechaRegistro: '2024-01-15',
        cantidadExtraidaToneladas: 15.5,
        ubicacionExtraccion: 'Zona A',
        observaciones: 'Observaciones'
      };

      const mockMutation = {
        mutate: vi.fn(),
        isLoading: false,
        error: null,
        data: null
      };

      (useMutation as vi.MockedFunction<typeof useMutation>).mockReturnValue(mockMutation);

      const result = useProduccion().registrarProduccion();

      expect(useMutation).toHaveBeenCalledWith({
        mutationFn: expect.any(Function),
        onSuccess: expect.any(Function)
      });

      // Verificar que la mutación se puede llamar con los datos correctos
      expect(result.mutate).toBeInstanceOf(Function);
    });

    it('debería llamar a invalidateQueries con la key correcta después de registrar con éxito', async () => {
      const mockRegistro = {
        id: 1,
        empleadoId: 1,
        tipoTurnoId: 1,
        fechaRegistro: '2024-01-15',
        cantidadExtraidaToneladas: 15.5,
        ubicacionExtraccion: 'Zona A',
        observaciones: 'Observaciones',
        validado: false
      };

      (useMutation as vi.MockedFunction<typeof useMutation>).mockImplementation((config: any) => {
        return {
          mutate: vi.fn().mockImplementation(() => {
            config.onSuccess(mockRegistro);
          }),
          isLoading: false,
          error: null,
          data: null
        };
      });

      const { registrarProduccion } = useProduccion();
      const mutation = registrarProduccion();
      mutation.mutate({} as any);

      expect(mockQueryClient.invalidateQueries).toHaveBeenCalledWith({ queryKey: ['produccion'] });
      expect(mockQueryClient.invalidateQueries).toHaveBeenCalledWith({ queryKey: ['produccion', 'listar'] });
    });

    it('debería llamar a invalidateQueries con la key correcta después de registrar con éxito', async () => {
      const mockRegistro = {
        id: 1,
        empleadoId: 1,
        tipoTurnoId: 1,
        fechaRegistro: '2024-01-15',
        cantidadExtraidaToneladas: 15.5,
        ubicacionExtraccion: 'Zona A',
        observaciones: 'Observaciones',
        validado: false
      };

      (useMutation as vi.MockedFunction<typeof useMutation>).mockImplementation((config: any) => {
        return {
          mutate: vi.fn().mockImplementation(() => {
            config.onSuccess(mockRegistro);
          }),
          isLoading: false,
          error: null,
          data: null
        };
      });

      const { registrarProduccion } = useProduccion();
      const mutation = registrarProduccion();
      mutation.mutate({} as any);

      expect(mockQueryClient.invalidateQueries).toHaveBeenCalledWith({ queryKey: ['produccion'] });
    });
  });

  describe('mutation: actualizarRegistro', () => {
    it('debería configurar correctamente la mutación para actualizar un registro existente', () => {
      const mockMutation = {
        mutate: vi.fn(),
        isLoading: false,
        error: null,
        data: null
      };

      (useMutation as vi.MockedFunction<typeof useMutation>).mockReturnValue(mockMutation);

      const result = useProduccion().actualizarRegistro();

      expect(useMutation).toHaveBeenCalledWith({
        mutationFn: expect.any(Function),
        onSuccess: expect.any(Function)
      });
    });

    it('debería llamar a invalidateQueries con las keys correctas después de actualizar con éxito', async () => {
      (useMutation as vi.MockedFunction<typeof useMutation>).mockImplementation((config: any) => {
        return {
          mutate: vi.fn().mockImplementation((variables: any) => {
            config.onSuccess({}, variables);
          }),
          isLoading: false,
          error: null,
          data: null
        };
      });

      const { actualizarRegistro } = useProduccion();
      const mutation = actualizarRegistro();
      mutation.mutate({ id: 1 } as any);

      // Verificar que se invalidan las queries relacionadas
      expect(mockQueryClient.invalidateQueries).toHaveBeenCalledWith({ queryKey: ['produccion'] });
      expect(mockQueryClient.invalidateQueries).toHaveBeenCalledWith({ queryKey: ['produccion', 'listar'] });
      expect(mockQueryClient.invalidateQueries).toHaveBeenCalledWith({ queryKey: ['produccion', 'detalle', 1] });
    });
  });

  describe('mutation: eliminarRegistro', () => {
    it('debería configurar correctamente la mutación para eliminar un registro', () => {
      const mockMutation = {
        mutate: vi.fn(),
        isLoading: false,
        error: null,
        data: null
      };

      (useMutation as vi.MockedFunction<typeof useMutation>).mockReturnValue(mockMutation);

      const result = useProduccion().eliminarRegistro();

      expect(useMutation).toHaveBeenCalledWith({
        mutationFn: expect.any(Function),
        onSuccess: expect.any(Function)
      });
    });

    it('debería llamar a invalidateQueries con las keys correctas después de eliminar con éxito', async () => {
      (useMutation as vi.MockedFunction<typeof useMutation>).mockImplementation((config: any) => {
        return {
          mutate: vi.fn().mockImplementation((variables: any) => {
            config.onSuccess({}, variables);
          }),
          isLoading: false,
          error: null,
          data: null
        };
      });

      const { eliminarRegistro } = useProduccion();
      const mutation = eliminarRegistro();
      mutation.mutate(1);

      // Verificar que se invalidan las queries relacionadas
      expect(mockQueryClient.invalidateQueries).toHaveBeenCalledWith({ queryKey: ['produccion'] });
      expect(mockQueryClient.invalidateQueries).toHaveBeenCalledWith({ queryKey: ['produccion', 'listar'] });
    });
  });

  describe('mutation: validarRegistro', () => {
    it('debería configurar correctamente la mutación para validar un registro', () => {
      const mockMutation = {
        mutate: vi.fn(),
        isLoading: false,
        error: null,
        data: null
      };

      (useMutation as vi.MockedFunction<typeof useMutation>).mockReturnValue(mockMutation);

      const result = useProduccion().validarRegistro();

      expect(useMutation).toHaveBeenCalledWith({
        mutationFn: expect.any(Function),
        onSuccess: expect.any(Function)
      });
    });

    it('debería llamar a invalidateQueries con las keys correctas después de validar con éxito', async () => {
      (useMutation as vi.MockedFunction<typeof useMutation>).mockImplementation((config: any) => {
        return {
          mutate: vi.fn().mockImplementation((variables: any) => {
            config.onSuccess({}, variables);
          }),
          isLoading: false,
          error: null,
          data: null
        };
      });

      const { validarRegistro } = useProduccion();
      const mutation = validarRegistro();
      mutation.mutate(1);

      // Verificar que se invalidan las queries relacionadas
      expect(mockQueryClient.invalidateQueries).toHaveBeenCalledWith({ queryKey: ['produccion'] });
      expect(mockQueryClient.invalidateQueries).toHaveBeenCalledWith({ queryKey: ['produccion', 'listar'] });
    });
  });
});
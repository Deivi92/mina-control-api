
import React from 'react';
import { renderHook, waitFor } from '@testing-library/react';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { useAsistencia } from './useAsistencia';
import { asistenciaService } from '../services/asistencia.service';
import { turnoService } from '../services/turno.service';
import { AsignacionTurnoRequest, ExcepcionAsistenciaRequest, RegistrarAsistenciaRequest, TipoRegistro, EstadoAsistencia } from '../types';

// Mockear la capa de servicio completa
vi.mock('../services/asistencia.service');
vi.mock('../services/turno.service');

const mockedAsistenciaService = vi.mocked(asistenciaService, true);
const mockedTurnoService = vi.mocked(turnoService, true);

const createTestQueryClient = () => new QueryClient({
  defaultOptions: {
    queries: {
      retry: false,
    },
  },
});

describe('useAsistencia', () => {
  let queryClient: QueryClient;

  beforeEach(() => {
    queryClient = createTestQueryClient();
    vi.resetAllMocks();
  });

  const wrapper: React.FC<{ children: React.ReactNode }> = ({ children }) => {
    return React.createElement(QueryClientProvider, { client: queryClient }, children);
  };

  const defaultParams = { fechaInicio: '2024-01-01', fechaFin: '2024-01-31', empleadoId: 1 };

  describe('query: consultarAsistencia', () => {
    it('debería obtener y devolver los registros de asistencia', async () => {
      const mockAsistencia = [{ id: 1, estado: EstadoAsistencia.ASISTIO }];
      mockedAsistenciaService.consultarAsistencia.mockResolvedValue(mockAsistencia as any);

      const { result } = renderHook(() => useAsistencia(defaultParams), { wrapper });

      await waitFor(() => expect(result.current.asistenciaQuery.isSuccess).toBe(true));

      expect(result.current.asistenciaQuery.data).toEqual(mockAsistencia);
      expect(mockedAsistenciaService.consultarAsistencia).toHaveBeenCalledWith(
        defaultParams.empleadoId,
        defaultParams.fechaInicio,
        defaultParams.fechaFin
      );
    });
  });

  describe('mutation: asignarEmpleado', () => {
    it('debería llamar al servicio con los datos correctos e invalidar la query de asistencia', async () => {
      const asignacion: AsignacionTurnoRequest = { 
        empleadoId: 1, 
        tipoTurnoId: 1, 
        fechaInicio: '2024-01-15',
        fechaFin: '2024-01-15'
      };
      mockedTurnoService.asignarEmpleadoATurno.mockResolvedValue({ id: 1, ...asignacion });
      const invalidateQueriesSpy = vi.spyOn(queryClient, 'invalidateQueries');

      const { result } = renderHook(() => useAsistencia(defaultParams), { wrapper });

      result.current.asignarEmpleadoMutation.mutate(asignacion);

      await waitFor(() => expect(result.current.asignarEmpleadoMutation.isSuccess).toBe(true));
      expect(mockedTurnoService.asignarEmpleadoATurno).toHaveBeenCalledWith(asignacion);
      // Verificamos que se haya llamado a invalidateQueries con la queryKey 'asistencia'
      expect(invalidateQueriesSpy).toHaveBeenCalledWith(
        expect.objectContaining({
          queryKey: ['asistencia']
        })
      );
    });
  });

  describe('mutation: registrarAsistencia', () => {
    it('debería llamar al servicio de registro e invalidar la query de asistencia', async () => {
        const registro: RegistrarAsistenciaRequest = { empleadoId: 1, tipo: TipoRegistro.ENTRADA };
        mockedAsistenciaService.registrarAsistencia.mockResolvedValue({} as any);
        const invalidateQueriesSpy = vi.spyOn(queryClient, 'invalidateQueries');
  
        const { result } = renderHook(() => useAsistencia(defaultParams), { wrapper });
  
        result.current.registrarAsistenciaMutation.mutate(registro);
  
        await waitFor(() => expect(result.current.registrarAsistenciaMutation.isSuccess).toBe(true));
        expect(mockedAsistenciaService.registrarAsistencia).toHaveBeenCalledWith(registro);
        // Verificamos que se haya llamado a invalidateQueries con la queryKey 'asistencia'
        expect(invalidateQueriesSpy).toHaveBeenCalledWith(
          expect.objectContaining({
            queryKey: ['asistencia']
          })
        );
      });
  });

  describe('mutation: gestionarExcepcion', () => {
    it('debería llamar al servicio de excepciones e invalidar la query de asistencia', async () => {
        const excepcion: ExcepcionAsistenciaRequest = { empleadoId: 1, fecha: '2024-01-15', estado: EstadoAsistencia.PERMISO, motivo: '' };
        mockedAsistenciaService.gestionarExcepcionAsistencia.mockResolvedValue({} as any);
        const invalidateQueriesSpy = vi.spyOn(queryClient, 'invalidateQueries');
  
        const { result } = renderHook(() => useAsistencia(defaultParams), { wrapper });
  
        result.current.gestionarExcepcionMutation.mutate(excepcion);
  
        await waitFor(() => expect(result.current.gestionarExcepcionMutation.isSuccess).toBe(true));
        expect(mockedAsistenciaService.gestionarExcepcionAsistencia).toHaveBeenCalledWith(excepcion);
        // Verificamos que se haya llamado a invalidateQueries con la queryKey 'asistencia'
        expect(invalidateQueriesSpy).toHaveBeenCalledWith(
          expect.objectContaining({
            queryKey: ['asistencia']
          })
        );
      });
  });

});

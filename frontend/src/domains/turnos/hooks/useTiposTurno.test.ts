
import { renderHook, waitFor } from '@testing-library/react';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { useTiposTurno } from './useTiposTurno';
import { turnoService } from '../services/turno.service';
import { TipoTurno, TipoTurnoRequest } from '../types';

// Mockear la capa de servicio
vi.mock('../services/turno.service');

const mockedTurnoService = vi.mocked(turnoService, true);

// Crear un cliente de React Query para las pruebas
const createTestQueryClient = () => new QueryClient({
  defaultOptions: {
    queries: {
      retry: false, // Desactiva los reintentos para que las pruebas fallen más rápido
    },
  },
});

describe('useTiposTurno', () => {
  let queryClient: QueryClient;

  beforeEach(() => {
    queryClient = createTestQueryClient();
    // Resetear mocks de servicio
    vi.resetAllMocks();
  });

  const wrapper = ({ children }: { children: React.ReactNode }) => (
    <QueryClientProvider client={queryClient}>{children}</QueryClientProvider>
  );

  describe('query: listarTiposDeTurno', () => {
    it('debería obtener los datos y devolverlos correctamente', async () => {
      const mockTurnos: TipoTurno[] = [
        { id: 1, nombre: 'Turno A', horaInicio: '08:00', horaFin: '16:00', color: '#FFF' },
      ];
      mockedTurnoService.listarTiposDeTurno.mockResolvedValue(mockTurnos);

      const { result } = renderHook(() => useTiposTurno(), { wrapper });

      await waitFor(() => expect(result.current.tiposTurnoQuery.isSuccess).toBe(true));

      expect(result.current.tiposTurnoQuery.data).toEqual(mockTurnos);
    });

    it('debería manejar un estado de error si la API falla', async () => {
        mockedTurnoService.listarTiposDeTurno.mockRejectedValue(new Error('API Error'));
  
        const { result } = renderHook(() => useTiposTurno(), { wrapper });
  
        await waitFor(() => expect(result.current.tiposTurnoQuery.isError).toBe(true));

        expect(result.current.tiposTurnoQuery.error).toBeInstanceOf(Error);
      });
  });

  describe('mutation: crearTipoTurno', () => {
    it('debería llamar al servicio con los datos correctos y invalidar la query de la lista', async () => {
      const newTurno: TipoTurnoRequest = { nombre: 'Nuevo Turno', horaInicio: '10:00', horaFin: '18:00', color: '#000' };
      mockedTurnoService.crearTipoTurno.mockResolvedValue({ id: 2, ...newTurno });
      const invalidateQueriesSpy = vi.spyOn(queryClient, 'invalidateQueries');

      const { result } = renderHook(() => useTiposTurno(), { wrapper });

      result.current.crearTipoTurnoMutation.mutate(newTurno);

      await waitFor(() => expect(result.current.crearTipoTurnoMutation.isSuccess).toBe(true));
      expect(mockedTurnoService.crearTipoTurno).toHaveBeenCalledWith(newTurno);
      expect(invalidateQueriesSpy).toHaveBeenCalledWith({ queryKey: ['tiposTurno'] });
    });
  });

  describe('mutation: actualizarTipoTurno', () => {
    it('debería llamar al servicio con el id y los datos correctos', async () => {
        const turnoId = 1;
        const updatedTurno: TipoTurnoRequest = { nombre: 'Turno Actualizado', horaInicio: '10:00', horaFin: '18:00', color: '#000' };
        mockedTurnoService.actualizarTipoTurno.mockResolvedValue({ id: turnoId, ...updatedTurno });
        const invalidateQueriesSpy = vi.spyOn(queryClient, 'invalidateQueries');

        const { result } = renderHook(() => useTiposTurno(), { wrapper });
  
        result.current.actualizarTipoTurnoMutation.mutate({ id: turnoId, data: updatedTurno });
  
        await waitFor(() => expect(result.current.actualizarTipoTurnoMutation.isSuccess).toBe(true));
        expect(mockedTurnoService.actualizarTipoTurno).toHaveBeenCalledWith(turnoId, updatedTurno);
        expect(invalidateQueriesSpy).toHaveBeenCalledWith({ queryKey: ['tiposTurno'] });
      });
  });

  describe('mutation: eliminarTipoTurno', () => {
    it('debería llamar al servicio con el id correcto', async () => {
        const turnoId = 1;
        mockedTurnoService.eliminarTipoTurno.mockResolvedValue(undefined);
        const invalidateQueriesSpy = vi.spyOn(queryClient, 'invalidateQueries');

        const { result } = renderHook(() => useTiposTurno(), { wrapper });
  
        result.current.eliminarTipoTurnoMutation.mutate(turnoId);
  
        await waitFor(() => expect(result.current.eliminarTipoTurnoMutation.isSuccess).toBe(true));
        expect(mockedTurnoService.eliminarTipoTurno).toHaveBeenCalledWith(turnoId);
        expect(invalidateQueriesSpy).toHaveBeenCalledWith({ queryKey: ['tiposTurno'] });
      });
  });

});

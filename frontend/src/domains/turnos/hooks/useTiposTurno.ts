
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { turnoService } from '../services/turno.service';
import type { TipoTurnoRequest } from '../types';

const QUERY_KEY = ['tiposTurno'];

export const useTiposTurno = () => {
  const queryClient = useQueryClient();

  const tiposTurnoQuery = useQuery({
    queryKey: QUERY_KEY,
    queryFn: turnoService.listarTiposDeTurno,
  });

  const crearTipoTurnoMutation = useMutation({
    mutationFn: (data: TipoTurnoRequest) => turnoService.crearTipoTurno(data),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: QUERY_KEY });
    },
  });

  const actualizarTipoTurnoMutation = useMutation({
    mutationFn: ({ id, data }: { id: number; data: TipoTurnoRequest }) =>
      turnoService.actualizarTipoTurno(id, data),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: QUERY_KEY });
    },
  });

  const eliminarTipoTurnoMutation = useMutation({
    mutationFn: (id: number) => turnoService.eliminarTipoTurno(id),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: QUERY_KEY });
    },
  });

  return {
    tiposTurnoQuery,
    crearTipoTurnoMutation,
    actualizarTipoTurnoMutation,
    eliminarTipoTurnoMutation,
  };
};


import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { asistenciaService } from '../services/asistencia.service';
import { turnoService } from '../services/turno.service';
import type { AsignacionTurnoRequest, ExcepcionAsistenciaRequest, RegistrarAsistenciaRequest } from '../types';

const QUERY_KEY = 'asistencia';

interface AsistenciaParams {
  empleadoId?: number;
  fechaInicio: string;
  fechaFin: string;
}

export const useAsistencia = ({ empleadoId, fechaInicio, fechaFin }: AsistenciaParams) => {
  const queryClient = useQueryClient();

  const asistenciaQuery = useQuery({
    queryKey: [QUERY_KEY, { empleadoId, fechaInicio, fechaFin }],
    queryFn: () => asistenciaService.consultarAsistencia(empleadoId, fechaInicio, fechaFin),
  });

  const asignarEmpleadoMutation = useMutation({
    mutationFn: (data: AsignacionTurnoRequest) => turnoService.asignarEmpleadoATurno(data),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: [QUERY_KEY] });
    },
  });

  const registrarAsistenciaMutation = useMutation({
    mutationFn: (data: RegistrarAsistenciaRequest) => asistenciaService.registrarAsistencia(data),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: [QUERY_KEY] });
    },
  });

  const gestionarExcepcionMutation = useMutation({
    mutationFn: (data: ExcepcionAsistenciaRequest) => asistenciaService.gestionarExcepcionAsistencia(data),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: [QUERY_KEY] });
    },
  });

  return {
    asistenciaQuery,
    asignarEmpleadoMutation,
    registrarAsistenciaMutation,
    gestionarExcepcionMutation,
  };
};

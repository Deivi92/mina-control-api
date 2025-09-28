
import { Box, Typography, Paper, Grid, Chip, CircularProgress } from '@mui/material';
import { RegistroAsistencia, EstadoAsistencia } from '../types';
import { Empleado } from '../../empleado/types';

interface AsignacionProcesada {
  empleado: Empleado;
  asistencia: RegistroAsistencia;
}

interface Props {
  asignaciones: Map<number, AsignacionProcesada>;
  isLoading: boolean;
}

const getEstadoChipColor = (estado: EstadoAsistencia) => {
  switch (estado) {
    case EstadoAsistencia.ASISTIO:
      return 'success';
    case EstadoAsistencia.FALTA:
      return 'error';
    case EstadoAsistencia.RETRASO:
      return 'warning';
    case EstadoAsistencia.PERMISO:
      return 'info';
    default:
      return 'default';
  }
};

export const AsignacionesDia = ({ asignaciones, isLoading }: Props) => {
  if (isLoading) {
    return <Box sx={{ display: 'flex', justifyContent: 'center', my: 4 }}><CircularProgress /></Box>;
  }

  if (asignaciones.size === 0) {
    return <Typography sx={{ my: 4, textAlign: 'center' }}>No hay empleados asignados para esta fecha.</Typography>;
  }

  return (
    <Grid container spacing={2}>
      {Array.from(asignaciones.values()).map(({ empleado, asistencia }) => (
        <Grid item xs={12} sm={6} md={4} key={empleado.id}>
          <Paper elevation={2} sx={{ p: 2 }}>
            <Typography variant="h6">{`${empleado.nombre} ${empleado.apellido}`}</Typography>
            <Chip label={asistencia.estado} color={getEstadoChipColor(asistencia.estado)} size="small" />
            <Box sx={{ mt: 1 }}>
              <Typography variant="body2">Entrada: {asistencia.horaEntrada ? new Date(asistencia.horaEntrada).toLocaleTimeString() : '--:--'}</Typography>
              <Typography variant="body2">Salida: {asistencia.horaSalida ? new Date(asistencia.horaSalida).toLocaleTimeString() : '--:--'}</Typography>
            </Box>
          </Paper>
        </Grid>
      ))}
    </Grid>
  );
};

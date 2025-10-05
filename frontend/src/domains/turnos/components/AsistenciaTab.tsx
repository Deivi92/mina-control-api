
import { useState, useMemo } from 'react';
import { Box, Button, Typography, Alert, TextField } from '@mui/material';
import { useAsistencia } from '../hooks/useAsistencia';
import { useEmpleados } from '../../empleado/hooks/useEmpleados';
import { useTiposTurno } from '../hooks/useTiposTurno';
import { AsignacionTurnoForm } from './AsignacionTurnoForm';
import { AsignacionesDia } from './AsignacionesDia';
import { RegistrarAsistenciaForm } from './RegistrarAsistenciaForm';
import type { AsignacionTurnoRequest } from '../types';
import type { Empleado } from '../../empleado/types';

export const AsistenciaTab = () => {
  const [selectedDate, setSelectedDate] = useState(new Date().toISOString().split('T')[0]);
  const [isFormOpen, setIsFormOpen] = useState(false);

  const { asistenciaQuery, asignarEmpleadoMutation, registrarAsistenciaMutation } = useAsistencia({
    fechaInicio: selectedDate,
    fechaFin: selectedDate,
  });

  const { empleadosQuery } = useEmpleados(); // Asume que este hook existe y funciona
  const { tiposTurnoQuery } = useTiposTurno();

  const asignacionesProcesadas = useMemo(() => {
    const mapa = new Map<number, { empleado: Empleado, asistencia: any }>();
    if (asistenciaQuery.data) {
      // Ahora la API devuelve directamente empleadoId en lugar de un objeto anidado
      for (const registro of asistenciaQuery.data) {
        if (registro.empleadoId) { 
          // Buscamos el empleado completo usando el empleadoId
          const empleado = empleadosQuery.data?.find(e => e.id === registro.empleadoId);
          if (empleado) {
            mapa.set(empleado.id, { empleado, asistencia: registro });
          }
        }
      }
    }
    return mapa;
  }, [asistenciaQuery.data, empleadosQuery.data]);

  const handleAsignarSubmit = (data: AsignacionTurnoRequest) => {
    asignarEmpleadoMutation.mutate(data, { onSuccess: () => setIsFormOpen(false) });
  };

  return (
    <Box>
      <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', mb: 2, flexWrap: 'wrap', gap: 2 }}>
        <Typography variant="h6">Control de Asistencia</Typography>
        <Box sx={{ display: 'flex', gap: 2, alignItems: 'center' }}>
          <TextField
            label="Fecha"
            type="date"
            value={selectedDate}
            onChange={(e) => setSelectedDate(e.target.value)}
            InputLabelProps={{ shrink: true }}
          />
          <Button variant="contained" onClick={() => setIsFormOpen(true)}>
            Asignar Turno
          </Button>
        </Box>
      </Box>

      {asistenciaQuery.isError && (
        <Alert severity="error" sx={{ mb: 2 }}>Error al cargar la asistencia.</Alert>
      )}

      <AsignacionesDia
        asignaciones={asignacionesProcesadas} // Usar los datos procesados
        isLoading={asistenciaQuery.isLoading}
      />

      <RegistrarAsistenciaForm 
        empleados={empleadosQuery.data || []}
        onRegister={registrarAsistenciaMutation.mutate}
        isSubmitting={registrarAsistenciaMutation.isPending}
      />

      <AsignacionTurnoForm
        open={isFormOpen}
        onClose={() => setIsFormOpen(false)}
        onSubmit={handleAsignarSubmit}
        empleados={empleadosQuery.data || []}
        tiposTurno={tiposTurnoQuery.data || []}
        isSubmitting={asignarEmpleadoMutation.isPending}
      />
    </Box>
  );
};

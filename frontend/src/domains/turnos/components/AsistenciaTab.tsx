
import { useState, useMemo } from 'react';
import { Box, Button, Typography, Alert, TextField } from '@mui/material';
import { useAsistencia } from '../hooks/useAsistencia';
import { useEmpleados } from '../../empleado/hooks/useEmpleados';
import { useTiposTurno } from './useTiposTurno';
import { AsignacionTurnoForm } from './AsignacionTurnoForm';
import { AsignacionesDia } from './AsignacionesDia';
import { RegistrarAsistenciaForm } from './RegistrarAsistenciaForm';
import { AsignacionTurnoRequest, RegistroAsistencia } from '../types';
import { Empleado } from '../../empleado/types';

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
    const asignacionesMap = new Map();
    if (asistenciaQuery.data && empleadosQuery.data) {
      // Lógica para cruzar datos de asistencia y empleados si es necesario
      // Esto es una simplificación. La API de asistencia ya debería devolver lo necesario.
    }
    // La lógica real dependerá de la estructura de datos devuelta por `asistenciaQuery`
    return new Map(); // Placeholder
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

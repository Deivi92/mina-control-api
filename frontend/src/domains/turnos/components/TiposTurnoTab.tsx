
import { useState } from 'react';
import { Box, Button, Typography, Alert } from '@mui/material';
import { useTiposTurno } from '../hooks/useTiposTurno';
import { TiposTurnoTable } from './TiposTurnoTable';
import { TipoTurnoForm } from './TipoTurnoForm';
import type { TipoTurno, TipoTurnoRequest } from '../types';

export const TiposTurnoTab = () => {
  const { 
    tiposTurnoQuery,
    crearTipoTurnoMutation,
    actualizarTipoTurnoMutation,
    eliminarTipoTurnoMutation 
  } = useTiposTurno();

  const [isFormOpen, setIsFormOpen] = useState(false);
  const [turnoToEdit, setTurnoToEdit] = useState<TipoTurno | undefined>(undefined);

  const handleOpenForm = (turno?: TipoTurno) => {
    setTurnoToEdit(turno);
    setIsFormOpen(true);
  };

  const handleCloseForm = () => {
    setTurnoToEdit(undefined);
    setIsFormOpen(false);
  };

  const handleSubmit = (data: TipoTurnoRequest) => {
    if (turnoToEdit) {
      actualizarTipoTurnoMutation.mutate({ id: turnoToEdit.id, data }, { onSuccess: handleCloseForm });
    } else {
      crearTipoTurnoMutation.mutate(data, { onSuccess: handleCloseForm });
    }
  };

  const handleDelete = (id: number) => {
    // Aquí se podría añadir un diálogo de confirmación
    if (window.confirm('¿Estás seguro de que quieres eliminar este tipo de turno?')) {
      eliminarTipoTurnoMutation.mutate(id);
    }
  };

  return (
    <Box>
      <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', mb: 2 }}>
        <Typography variant="h6">Administrar Tipos de Turno</Typography>
        <Button variant="contained" onClick={() => handleOpenForm()}>
          Crear Nuevo Tipo de Turno
        </Button>
      </Box>

      {tiposTurnoQuery.isError && (
        <Alert severity="error" sx={{ mb: 2 }}>
          Error al cargar los datos. Por favor, intenta de nuevo más tarde.
        </Alert>
      )}

      <TiposTurnoTable
        turnos={tiposTurnoQuery.data || []}
        onEdit={handleOpenForm}
        onDelete={handleDelete}
        isLoading={tiposTurnoQuery.isLoading}
      />

      <TipoTurnoForm
        open={isFormOpen}
        onClose={handleCloseForm}
        onSubmit={handleSubmit}
        initialData={turnoToEdit}
        isSubmitting={crearTipoTurnoMutation.isPending || actualizarTipoTurnoMutation.isPending}
      />
    </Box>
  );
};

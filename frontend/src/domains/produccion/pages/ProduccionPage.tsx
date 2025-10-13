import React, { useState } from 'react';
import { 
  Box, 
  Button, 
  Typography,
  Alert,
  CircularProgress
} from '@mui/material';
import { RegistroProduccionForm } from '../components/RegistroProduccionForm';
import { RegistroProduccionTable } from '../components/RegistroProduccionTable';
import { RegistroProduccionFilter } from '../components/RegistroProduccionFilter';
import { useProduccion } from '../hooks/useProduccion';
import { useEmpleados } from '../../empleado/hooks/useEmpleados';
import { useTiposTurno } from '../../turnos/hooks/useTiposTurno';
import type { RegistroProduccionUpdateDTO, ProduccionFilters } from '../types';

export const ProduccionPage: React.FC = () => {
  const [formOpen, setFormOpen] = useState(false);
  const [editingRegistro, setEditingRegistro] = useState<RegistroProduccionUpdateDTO | null>(null);
  const [filters, setFilters] = useState<ProduccionFilters>({});

  // Hook principal de producción
  const { 
    listarRegistros, 
    registrarProduccion, 
    actualizarRegistro, 
    eliminarRegistro, 
    validarRegistro 
  } = useProduccion();

  // Query para obtener registros
  const { 
    data: registros, 
    isLoading, 
    error 
  } = listarRegistros(filters);

  // Mutaciones
  const { mutate: crearRegistro, isPending: isCreating } = registrarProduccion();
  const { mutate: guardarActualizacion, isPending: isUpdating } = actualizarRegistro();
  const { mutate: ejecutarEliminacion, isPending: isDeleting } = eliminarRegistro();
  const { mutate: ejecutarValidacion, isPending: isValidating } = validarRegistro();

  // Obtener empleados y tipos de turno para los selects del formulario
  const { data: empleados = [] } = useEmpleados().empleadosQuery;
  const { data: tiposTurno = [] } = useTiposTurno().tiposTurnoQuery;

  const handleOpenForm = () => {
    setEditingRegistro(null);
    setFormOpen(true);
  };

  const handleCloseForm = () => {
    setFormOpen(false);
    setEditingRegistro(null);
  };

  const handleSubmit = (data: any) => {
    if (editingRegistro) {
      guardarActualizacion({ id: editingRegistro.id, data });
    } else {
      crearRegistro(data);
    }
    handleCloseForm();
  };

  const handleEdit = (registro: RegistroProduccionUpdateDTO) => {
    setEditingRegistro(registro);
    setFormOpen(true);
  };

  const handleDelete = (id: number) => {
    if (window.confirm('¿Está seguro de que desea eliminar este registro de producción?')) {
      ejecutarEliminacion(id);
    }
  };

  const handleValidate = (id: number) => {
    ejecutarValidacion(id);
  };

  const handleFilter = (newFilters: ProduccionFilters) => {
    setFilters(newFilters);
  };

  if (error) {
    return (
      <Box sx={{ p: 3 }}>
        <Alert severity="error">Error al cargar los registros de producción: {(error as Error).message}</Alert>
      </Box>
    );
  }

  return (
    <Box sx={{ p: 3 }}>
      <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', mb: 3 }}>
        <Typography variant="h4">Producción Diaria</Typography>
        <Button 
          variant="contained" 
          onClick={handleOpenForm}
        >
          Registrar Nueva Producción
        </Button>
      </Box>

      <RegistroProduccionFilter 
        empleados={empleados} 
        tiposTurno={tiposTurno} 
        onFilter={handleFilter}
        initialValues={filters}
      />

      {isLoading ? (
        <Box sx={{ display: 'flex', justifyContent: 'center', my: 4 }}>
          <CircularProgress />
        </Box>
      ) : (
        <RegistroProduccionTable
          registros={registros || []}
          onEdit={handleEdit}
          onDelete={handleDelete}
          onValidate={handleValidate}
          isLoading={isLoading || isDeleting || isValidating}
        />
      )}

      <RegistroProduccionForm
        open={formOpen}
        onClose={handleCloseForm}
        onSubmit={handleSubmit}
        isSubmitting={isCreating || isUpdating}
        initialValues={editingRegistro || undefined}
        empleados={empleados}
        tiposTurno={tiposTurno}
      />
    </Box>
  );
};
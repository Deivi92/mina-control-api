import React, { useState } from 'react';
import { 
  Box, 
  Button, 
  Typography,
  Alert,
  CircularProgress
} from '@mui/material';
import { RegistroProduccionForm } from '../domains/produccion/components/RegistroProduccionForm';
import { RegistroProduccionTable } from '../domains/produccion/components/RegistroProduccionTable';
import { RegistroProduccionFilter } from '../domains/produccion/components/RegistroProduccionFilter';
import { useProduccion } from '../domains/produccion/hooks/useProduccion';
import { useEmpleados } from '../domains/empleado/hooks/useEmpleados';
import { useTiposTurno } from '../domains/turnos/hooks/useTiposTurno';
import type { RegistroProduccionUpdateDTO, ProduccionFilters } from '../domains/produccion/types';

export const ProduccionPage: React.FC = () => {
  const [formOpen, setFormOpen] = useState(false);
  const [editingRegistro, setEditingRegistro] = useState<RegistroProduccionUpdateDTO | null>(null);
  
  // Obtener registros de producción con posibilidad de filtrar
  const [filters, setFilters] = useState<ProduccionFilters>({});
  const { 
    data: registros, 
    isLoading, 
    error,
    refetch
  } = useProduccion().listarRegistros(filters);
  
  // Obtener empleados y tipos de turno para los selects del formulario
  const { empleadosQuery } = useEmpleados();
  const empleados = empleadosQuery.data || [];
  
  const { tiposTurnoQuery } = useTiposTurno();
  const tiposTurno = tiposTurnoQuery.data || [];
  
  // Mutaciones
  const { mutate: crearRegistro, isPending: isCreating } = useProduccion().registrarProduccion();
  const { mutate: actualizarRegistro, isPending: isUpdating } = useProduccion().actualizarRegistro();
  const { mutate: eliminarRegistro, isPending: isDeleting } = useProduccion().eliminarRegistro();
  const { mutate: validarRegistro, isPending: isValidating } = useProduccion().validarRegistro();

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
      // Actualizar registro existente
      actualizarRegistro({ id: editingRegistro.id, data });
    } else {
      // Crear nuevo registro
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
      eliminarRegistro(id);
    }
  };

  const handleValidate = (id: number) => {
    validarRegistro(id);
  };

  const handleFilter = (newFilters: ProduccionFilters) => {
    setFilters(newFilters);
    refetch();
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
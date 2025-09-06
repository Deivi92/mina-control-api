import React, { useState } from 'react';
import {
  Box,
  Button,
  Container,
  Typography,
  Dialog,
  DialogTitle,
  DialogContent,
} from '@mui/material';
import { useEmpleados } from '../domains/empleado/hooks/useEmpleados';
import { EmpleadoTable } from '../domains/empleado/components/EmpleadoTable';
import { EmpleadoForm } from '../domains/empleado/components/EmpleadoForm';
import { DeleteEmpleadoDialog } from '../domains/empleado/components/DeleteEmpleadoDialog';
import type { Empleado, EmpleadoRequest } from '../domains/empleado/types';

/**
 * Página principal para la gestión de Empleados.
 * Orquesta los datos del hook `useEmpleados` y los presenta en los componentes de UI.
 * Maneja el estado de los modales para las operaciones CRUD.
 */
export const EmpleadosPage: React.FC = () => {
  // Estados locales para manejar la visibilidad de los modales y el empleado seleccionado
  const [isFormOpen, setIsFormOpen] = useState(false);
  const [isDeleteConfirmOpen, setIsDeleteConfirmOpen] = useState(false);
  const [selectedEmpleado, setSelectedEmpleado] = useState<Empleado | null>(null);

  // Obtenemos las queries y mutaciones de nuestro hook centralizado
  const {
    empleadosQuery,
    crearEmpleadoMutation,
    actualizarEmpleadoMutation,
    eliminarEmpleadoMutation,
  } = useEmpleados();

  // --- Handlers para las acciones de la UI ---

  const handleCreate = () => {
    setSelectedEmpleado(null); // Nos aseguramos de que no haya datos de un empleado anterior
    setIsFormOpen(true);
  };

  const handleEdit = (empleado: Empleado) => {
    setSelectedEmpleado(empleado);
    setIsFormOpen(true);
  };

  const handleDelete = (empleado: Empleado) => {
    setSelectedEmpleado(empleado);
    setIsDeleteConfirmOpen(true);
  };

  const handleCloseModals = () => {
    setIsFormOpen(false);
    setIsDeleteConfirmOpen(false);
    setSelectedEmpleado(null); // Limpiar selección al cerrar cualquier modal
  };

  const handleFormSubmit = (data: EmpleadoRequest) => {
    if (selectedEmpleado) {
      // Si hay un empleado seleccionado, actualizamos
      actualizarEmpleadoMutation.mutate({ id: selectedEmpleado.id, data });
    } else {
      // Si no, creamos uno nuevo
      crearEmpleadoMutation.mutate(data);
    }
    handleCloseModals();
  };

  const handleDeleteConfirm = () => {
    if (selectedEmpleado) {
      eliminarEmpleadoMutation.mutate(selectedEmpleado.id);
    }
    handleCloseModals();
  };

  return (
    <Container maxWidth="lg" sx={{ mt: 4, mb: 4 }}>
      <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', mb: 2 }}>
        <Typography variant="h4" component="h1">
          Gestión de Empleados
        </Typography>
        <Button variant="contained" onClick={handleCreate}>
          Crear Nuevo Empleado
        </Button>
      </Box>

      <EmpleadoTable
        empleados={empleadosQuery.data || []}
        isLoading={empleadosQuery.isLoading}
        isError={empleadosQuery.isError}
        onEdit={handleEdit}
        onDelete={handleDelete}
      />

      {/* Modal del Formulario (Crear/Editar) */}
      <Dialog open={isFormOpen} onClose={handleCloseModals} maxWidth="sm" fullWidth>
        <DialogTitle>{selectedEmpleado ? 'Editar Empleado' : 'Crear Nuevo Empleado'}</DialogTitle>
        <DialogContent>
          <EmpleadoForm
            initialData={selectedEmpleado}
            onSubmit={handleFormSubmit}
            onCancel={handleCloseModals}
            isSubmitting={crearEmpleadoMutation.isPending || actualizarEmpleadoMutation.isPending}
          />
        </DialogContent>
      </Dialog>

      {/* Modal de Confirmación de Borrado */}
      <DeleteEmpleadoDialog
        open={isDeleteConfirmOpen}
        onConfirm={handleDeleteConfirm}
        onCancel={handleCloseModals}
        empleadoNombre={selectedEmpleado ? `${selectedEmpleado.nombre} ${selectedEmpleado.apellido}` : null}
      />
    </Container>
  );
};

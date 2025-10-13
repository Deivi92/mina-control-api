import React, { useState } from 'react';
import {
  Box,
  Button,
  Container,
  Typography,
  Dialog,
  DialogTitle,
  DialogContent,
  Alert,
} from '@mui/material';
import { AxiosError } from 'axios';
import { useEmpleados } from '../hooks/useEmpleados';
import { EmpleadoTable } from '../components/EmpleadoTable';
import { EmpleadoForm } from '../components/EmpleadoForm';
import { DeleteEmpleadoDialog } from '../components/DeleteEmpleadoDialog';
import type { Empleado, EmpleadoRequest } from '../types';

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
  const [error, setError] = useState<string | null>(null);

  // Obtenemos las queries y mutaciones de nuestro hook centralizado
  const {
    empleadosQuery,
    crearEmpleadoMutation,
    actualizarEmpleadoMutation,
    eliminarEmpleadoMutation,
  } = useEmpleados();

  // --- Handlers para las acciones de la UI -- -

  const handleCreate = () => {
    setSelectedEmpleado(null); // Nos aseguramos de que no haya datos de un empleado anterior
    setError(null);
    setIsFormOpen(true);
  };

  const handleEdit = (empleado: Empleado) => {
    setSelectedEmpleado(empleado);
    setError(null);
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
    setError(null);
  };

  const handleFormSubmit = (data: EmpleadoRequest) => {
    setError(null); // Limpiar error previo

    const options = {
      onSuccess: () => {
        // Pequeño retraso para dar tiempo a React a procesar la actualización de la tabla
        // antes de desmontar el modal, evitando race conditions de renderizado.
        setTimeout(() => {
          handleCloseModals();
        }, 100); // 100ms es un retraso seguro e imperceptible
      },
      onError: (err: unknown) => {
        if (err instanceof AxiosError && err.response?.data?.message) {
          setError(err.response.data.message);
        } else {
          setError('Ocurrió un error inesperado. Por favor, inténtalo de nuevo.');
        }
      },
    };

    if (selectedEmpleado) {
      // Si hay un empleado seleccionado, actualizamos
      actualizarEmpleadoMutation.mutate({ id: selectedEmpleado.id, data }, options);
    } else {
      // Si no, creamos uno nuevo
      crearEmpleadoMutation.mutate(data, options);
    }
  };

  const handleDeleteConfirm = () => {
    if (selectedEmpleado) {
      eliminarEmpleadoMutation.mutate(selectedEmpleado.id, {
        onSuccess: () => handleCloseModals(),
      });
    }
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
          {error && (
            <Alert severity="error" sx={{ mb: 2 }}>
              {error}
            </Alert>
          )}
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
        empleadoNombre={selectedEmpleado ? `${selectedEmpleado.nombres} ${selectedEmpleado.apellidos}` : null}
      />
    </Container>
  );
};

import React from 'react';
import {
  Dialog,
  DialogActions,
  DialogContent,
  DialogContentText,
  DialogTitle,
  Button,
} from '@mui/material';

interface DeleteEmpleadoDialogProps {
  open: boolean;
  onConfirm: () => void;
  onCancel: () => void;
  empleadoNombre: string | null; // Puede ser null si no hay empleado seleccionado
}

/**
 * Diálogo de confirmación para el borrado lógico de un empleado.
 */
export const DeleteEmpleadoDialog: React.FC<DeleteEmpleadoDialogProps> = ({
  open,
  onConfirm,
  onCancel,
  empleadoNombre,
}) => {
  // Para evitar un flash de contenido incorrecto, no renderizamos si no hay empleado
  if (!empleadoNombre) {
    return null;
  }

  return (
    <Dialog open={open} onClose={onCancel} aria-labelledby="confirm-delete-dialog-title">
      <DialogTitle id="confirm-delete-dialog-title">Confirmar Eliminación</DialogTitle>
      <DialogContent>
        <DialogContentText>
          ¿Estás seguro de que deseas eliminar al empleado {empleadoNombre}? Esta acción marcará al empleado como inactivo.
        </DialogContentText>
      </DialogContent>
      <DialogActions>
        <Button onClick={onCancel}>Cancelar</Button>
        <Button onClick={onConfirm} color="error" autoFocus>
          Eliminar
        </Button>
      </DialogActions>
    </Dialog>
  );
};
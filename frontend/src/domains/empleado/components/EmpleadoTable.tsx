import React from 'react';
import {
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  Paper,
  CircularProgress,
  IconButton,
  Box,
  Typography,
  Chip,
} from '@mui/material';
import EditIcon from '@mui/icons-material/Edit';
import DeleteIcon from '@mui/icons-material/Delete';
import type { Empleado } from '../types';

interface EmpleadoTableProps {
  empleados: Empleado[];
  isLoading: boolean;
  isError: boolean;
  onEdit: (empleado: Empleado) => void;
  onDelete: (empleado: Empleado) => void;
}

/**
 * Componente para mostrar una lista de empleados en una tabla.
 * Maneja y muestra los estados de carga, error y datos vacíos.
 */
export const EmpleadoTable: React.FC<EmpleadoTableProps> = ({
  empleados,
  isLoading,
  isError,
  onEdit,
  onDelete,
}) => {
  // 1. Estado de Carga
  if (isLoading) {
    return (
      <Box sx={{ display: 'flex', justifyContent: 'center', p: 4 }}>
        <CircularProgress />
      </Box>
    );
  }

  // 2. Estado de Error
  if (isError) {
    return (
      <Typography color="error" align="center" sx={{ p: 4 }}>
        Error al cargar los empleados. Por favor, intente de nuevo más tarde.
      </Typography>
    );
  }

  // 3. Estado de Datos Vacíos
  if (empleados.length === 0) {
    return (
      <Typography align="center" sx={{ p: 4 }}>
        No se encontraron empleados.
      </Typography>
    );
  }

  // 4. Estado con Datos
  return (
    <TableContainer component={Paper}>
      <Table>
        <TableHead>
          <TableRow>
            <TableCell>Nombre</TableCell>
            <TableCell>Puesto</TableCell>
            <TableCell>Email</TableCell>
            <TableCell>Estado</TableCell>
            <TableCell align="right">Acciones</TableCell>
          </TableRow>
        </TableHead>
        <TableBody>
          {empleados.map((empleado) => (
            <TableRow key={empleado.id} hover>
              <TableCell>{`${empleado.nombre} ${empleado.apellido}`}</TableCell>
              <TableCell>{empleado.puesto}</TableCell>
              <TableCell>{empleado.email}</TableCell>
              <TableCell>
                <Chip
                  label={empleado.estado}
                  color={empleado.estado === 'ACTIVO' ? 'success' : 'default'}
                  size="small"
                />
              </TableCell>
              <TableCell align="right">
                <IconButton onClick={() => onEdit(empleado)} aria-label="editar">
                  <EditIcon />
                </IconButton>
                <IconButton onClick={() => onDelete(empleado)} aria-label="eliminar">
                  <DeleteIcon />
                </IconButton>
              </TableCell>
            </TableRow>
          ))}
        </TableBody>
      </Table>
    </TableContainer>
  );
};
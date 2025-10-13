import React from 'react';
import {
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  Paper,
  IconButton,
  CircularProgress,
  Typography,
  Box,
} from '@mui/material';
import type { RegistroProduccionDTO } from '../types';
import CheckIcon from '@mui/icons-material/Check';
import EditIcon from '@mui/icons-material/Edit';
import DeleteIcon from '@mui/icons-material/Delete';

interface RegistroProduccionTableProps {
  registros: RegistroProduccionDTO[];
  onEdit: (registro: RegistroProduccionDTO) => void;
  onDelete: (id: number) => void;
  onValidate: (id: number) => void;
  isLoading?: boolean;
}

export const RegistroProduccionTable: React.FC<RegistroProduccionTableProps> = ({
  registros,
  onEdit,
  onDelete,
  onValidate,
  isLoading = false,
}) => {
  if (isLoading) {
    return (
      <Box sx={{ display: 'flex', justifyContent: 'center', my: 4 }}>
        <CircularProgress />
      </Box>
    );
  }

  if (registros.length === 0) {
    return (
      <Typography variant="body1" align="center" sx={{ my: 4 }}>
        No se encontraron registros de producción
      </Typography>
    );
  }

  return (
    <TableContainer component={Paper} data-testid="registro-produccion-table">
      <Table>
        <TableHead>
          <TableRow>
            <TableCell>Empleado</TableCell>
            <TableCell>Fecha</TableCell>
            <TableCell>Turno</TableCell>
            <TableCell>Cantidad (t)</TableCell>
            <TableCell>Ubicación</TableCell>
            <TableCell>Validado</TableCell>
            <TableCell>Acciones</TableCell>
          </TableRow>
        </TableHead>
        <TableBody>
          {registros.map((registro) => (
            <TableRow key={registro.id}>
              <TableCell>{registro.nombreEmpleado}</TableCell>
              <TableCell>{registro.fechaRegistro}</TableCell>
              <TableCell>{registro.nombreTurno}</TableCell>
              <TableCell>{registro.cantidadExtraidaToneladas}</TableCell>
              <TableCell>{registro.ubicacionExtraccion}</TableCell>
              <TableCell>
                {registro.validado ? (
                  <CheckIcon color="success" />
                ) : (
                  '-'
                )}
              </TableCell>
              <TableCell>
                <IconButton
                  color="primary"
                  onClick={() => onValidate(registro.id)}
                  title="Validar registro"
                  size="small"
                >
                  <CheckIcon />
                </IconButton>
                <IconButton
                  color="primary"
                  onClick={() => onEdit(registro)}
                  title="Editar registro"
                  size="small"
                >
                  <EditIcon />
                </IconButton>
                <IconButton
                  color="secondary"
                  onClick={() => onDelete(registro.id)}
                  title="Eliminar registro"
                  size="small"
                >
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
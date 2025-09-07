import React, { useState, useEffect } from 'react';
import {
  TextField,
  Button,
  Box,
  Grid,
  CircularProgress,
  Select,
  MenuItem,
  FormControl,
  InputLabel,
  type SelectChangeEvent,
} from '@mui/material';
import type { Empleado, EmpleadoRequest, RolSistema } from '../types';

interface EmpleadoFormProps {
  initialData?: Empleado | null;
  isSubmitting?: boolean;
  onSubmit: (data: EmpleadoRequest) => void;
  onCancel: () => void;
}

// Valores por defecto para un formulario de creación, alineados con el DTO del backend
const defaultValues: EmpleadoRequest = {
  nombres: '',
  apellidos: '',
  numeroIdentificacion: '',
  email: '',
  telefono: '',
  cargo: '',
  fechaContratacion: '',
  salarioBase: 0,
  rolSistema: 'EMPLEADO', // Valor por defecto para el rol
};

/**
 * Formulario para crear o editar un empleado.
 * Maneja su propio estado interno para los campos del formulario.
 */
export const EmpleadoForm: React.FC<EmpleadoFormProps> = ({
  initialData,
  isSubmitting = false,
  onSubmit,
  onCancel,
}) => {
  const [formData, setFormData] = useState<EmpleadoRequest>(defaultValues);

  // useEffect se dispara cuando `initialData` cambia (modo edición).
  useEffect(() => {
    if (initialData) {
      // Si hay datos iniciales, los cargamos en el formulario.
      setFormData({
        nombres: initialData.nombres,
        apellidos: initialData.apellidos,
        numeroIdentificacion: initialData.numeroIdentificacion,
        email: initialData.email,
        telefono: initialData.telefono || '',
        cargo: initialData.cargo,
        // Formateamos la fecha para que sea compatible con el input type="date" (YYYY-MM-DD).
        fechaContratacion: initialData.fechaContratacion.split('T')[0],
        salarioBase: initialData.salarioBase,
        rolSistema: initialData.rolSistema,
      });
    } else {
      // Si no (modo creación), reseteamos al estado por defecto.
      setFormData(defaultValues);
    }
  }, [initialData]);

 const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value, type } = e.target;
    setFormData((prev) => ({
      ...prev,
      [name]: type === 'number' ? parseFloat(value) || 0 : value,
    }));
  };

  const handleSelectChange = (e: SelectChangeEvent) => {
    const { name, value } = e.target;
    setFormData((prev) => ({
      ...prev,
      [name]: value as RolSistema,
    }));
  };


  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    onSubmit(formData);
  };

  return (
    <Box component="form" onSubmit={handleSubmit} noValidate>
      <Grid container spacing={2}>
        <Grid item xs={12} sm={6}>
          <TextField
            name="nombres"
            label="Nombres"
            value={formData.nombres}
            onChange={handleChange}
            fullWidth
            required
            autoFocus
          />
        </Grid>
        <Grid item xs={12} sm={6}>
          <TextField
            name="apellidos"
            label="Apellidos"
            value={formData.apellidos}
            onChange={handleChange}
            fullWidth
            required
          />
        </Grid>
        <Grid item xs={12}>
          <TextField
            name="email"
            label="Correo Electrónico"
            type="email"
            value={formData.email}
            onChange={handleChange}
            fullWidth
            required
          />
        </Grid>
        <Grid item xs={12} sm={6}>
          <TextField
            name="numeroIdentificacion"
            label="Número de Identificación"
            value={formData.numeroIdentificacion}
            onChange={handleChange}
            fullWidth
            required
          />
        </Grid>
        <Grid item xs={12} sm={6}>
           <TextField
            name="fechaContratacion"
            label="Fecha de Contratación"
            type="date"
            value={formData.fechaContratacion}
            onChange={handleChange}
            fullWidth
            required
            InputLabelProps={{ shrink: true }}
          />
        </Grid>
        <Grid item xs={12} sm={6}>
          <TextField
            name="cargo"
            label="Cargo"
            value={formData.cargo}
            onChange={handleChange}
            fullWidth
            required
          />
        </Grid>
        <Grid item xs={12} sm={6}>
          <TextField
            name="salarioBase"
            label="Salario Base"
            type="number"
            value={formData.salarioBase}
            onChange={handleChange}
            fullWidth
            required
          />
        </Grid>
        <Grid item xs={12} sm={6}>
          <TextField
            name="telefono"
            label="Teléfono (Opcional)"
            value={formData.telefono || ''}
            onChange={handleChange}
            fullWidth
          />
        </Grid>
        <Grid item xs={12} sm={6}>
            <FormControl fullWidth required>
                <InputLabel id="rol-sistema-label">Rol en el Sistema</InputLabel>
                <Select
                    labelId="rol-sistema-label"
                    id="rolSistema"
                    name="rolSistema"
                    value={formData.rolSistema}
                    label="Rol en el Sistema"
                    onChange={handleSelectChange}
                >
                    <MenuItem value="EMPLEADO">Empleado</MenuItem>
                    <MenuItem value="SUPERVISOR">Supervisor</MenuItem>
                    <MenuItem value="ADMINISTRADOR">Administrador</MenuItem>
                </Select>
            </FormControl>
        </Grid>
      </Grid>
      <Box sx={{ display: 'flex', justifyContent: 'flex-end', mt: 3 }}>
        <Button onClick={onCancel} sx={{ mr: 1 }}>
          Cancelar
        </Button>
        <Button type="submit" variant="contained" disabled={isSubmitting} aria-label="Guardar">
          {isSubmitting ? <CircularProgress size={24} /> : 'Guardar'}
        </Button>
      </Box>
    </Box>
  );
};
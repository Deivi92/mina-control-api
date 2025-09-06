import React, { useState, useEffect } from 'react';
import {
  TextField,
  Button,
  Box,
  Grid,
  CircularProgress,
} from '@mui/material';
import type { Empleado, EmpleadoRequest } from '../types';

interface EmpleadoFormProps {
  initialData?: Empleado | null; // Datos para el modo edición, opcional
  isSubmitting?: boolean; // Para mostrar el estado de carga en el botón
  onSubmit: (data: EmpleadoRequest) => void;
  onCancel: () => void;
}

// Valores por defecto para un formulario de creación
const defaultValues: EmpleadoRequest = {
  nombre: '',
  apellido: '',
  numeroIdentificacion: '',
  email: '',
  fechaNacimiento: '',
  puesto: '',
  salario: 0,
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

  // useEffect se dispara cuando `initialData` cambia.
  useEffect(() => {
    if (initialData) {
      // Si hay datos iniciales (modo edición), los cargamos en el formulario.
      // Formateamos la fecha para que sea compatible con el input type="date" (YYYY-MM-DD).
      setFormData({
        nombre: initialData.nombre,
        apellido: initialData.apellido,
        numeroIdentificacion: initialData.numeroIdentificacion,
        email: initialData.email,
        fechaNacimiento: initialData.fechaNacimiento.split('T')[0],
        puesto: initialData.puesto,
        salario: initialData.salario,
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

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    // En una aplicación real, aquí iría la lógica de validación (con Zod, Yup, etc.)
    onSubmit(formData);
  };

  return (
    <Box component="form" onSubmit={handleSubmit} noValidate>
      <Grid container spacing={2}>
        <Grid item xs={12} sm={6}>
          <TextField
            name="nombre"
            label="Nombre"
            value={formData.nombre}
            onChange={handleChange}
            fullWidth
            required
            autoFocus
          />
        </Grid>
        <Grid item xs={12} sm={6}>
          <TextField
            name="apellido"
            label="Apellido"
            value={formData.apellido}
            onChange={handleChange}
            fullWidth
            required
          />
        </Grid>
        <Grid item xs={12}>
          <TextField
            name="email"
            label="Email"
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
            name="fechaNacimiento"
            label="Fecha de Nacimiento"
            type="date"
            value={formData.fechaNacimiento}
            onChange={handleChange}
            fullWidth
            required
            InputLabelProps={{ shrink: true }} // Para que el label no se superponga con la fecha
          />
        </Grid>
        <Grid item xs={12} sm={6}>
          <TextField
            name="puesto"
            label="Puesto"
            value={formData.puesto}
            onChange={handleChange}
            fullWidth
            required
          />
        </Grid>
        <Grid item xs={12} sm={6}>
          <TextField
            name="salario"
            label="Salario"
            type="number"
            value={formData.salario}
            onChange={handleChange}
            fullWidth
            required
          />
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
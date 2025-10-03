import React, { useEffect } from 'react';
import {
  Button,
  Box,
  Grid,
  CircularProgress,
  Select,
  MenuItem,
  FormControl,
  InputLabel,
  FormHelperText,
} from '@mui/material';
import { useForm, Controller } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { empleadoValidationSchema } from '../validation/empleado.zod';
import type { EmpleadoFormData } from '../validation/empleado.zod';
import { TextField } from '@mui/material';
import type { Empleado, EmpleadoRequest } from '../types';

interface EmpleadoFormProps {
  initialData?: Empleado | null;
  onSubmit: (data: EmpleadoRequest) => void;
  onCancel: () => void;
  isSubmitting?: boolean;
}

interface EmpleadoFormProps {
  initialData?: Empleado | null;
  onSubmit: (data: EmpleadoRequest) => void;
  onCancel: () => void;
  isSubmitting?: boolean;
}

export const EmpleadoForm: React.FC<EmpleadoFormProps> = ({
  initialData,
  onSubmit,
  onCancel,
  isSubmitting = false,
}) => {
  const {
    control,
    handleSubmit,
    formState: { errors },
    reset
  } = useForm<EmpleadoFormData>({
    resolver: zodResolver(empleadoValidationSchema),
    mode: 'onChange',
    defaultValues: {
      nombres: '',
      apellidos: '',
      numeroIdentificacion: '',
      email: '',
      telefono: '',
      cargo: '',
      fechaContratacion: '',
      salarioBase: '',
      rolSistema: 'EMPLEADO',
    }
  });

  useEffect(() => {
    if (initialData) {
      // Convertir el salario a string para el formulario y la fecha a formato string adecuado
      reset({
        ...initialData,
        telefono: initialData.telefono || '',
        fechaContratacion: initialData.fechaContratacion.split('T')[0],
        salarioBase: initialData.salarioBase.toString(), // Convertir número a string para el formulario
      });
    } else {
      reset({
        nombres: '',
        apellidos: '',
        numeroIdentificacion: '',
        email: '',
        telefono: '',
        cargo: '',
        fechaContratacion: '',
        salarioBase: '',
        rolSistema: 'EMPLEADO',
      });
    }
  }, [initialData, reset]);

  const handleFormSubmit = (data: EmpleadoFormData) => {
    // Convertir los campos necesarios a los tipos correctos para el backend
    const processedData: EmpleadoRequest = {
      ...data,
      numeroIdentificacion: data.numeroIdentificacion, // El backend espera string
      salarioBase: parseFloat(data.salarioBase), // Convertir string a número
      rolSistema: data.rolSistema as 'EMPLEADO' | 'ADMINISTRADOR' | 'SUPERVISOR' // Asegurar el tipo correcto
    };
    
    onSubmit(processedData);
  };

  return (
    <form onSubmit={handleSubmit(handleFormSubmit)} noValidate>
      <Grid container spacing={2}>
        <Grid item xs={12} sm={6}>
          <Controller
            name="nombres"
            control={control}
            render={({ field }) => (
              <TextField
                {...field}
                label="Nombres"
                fullWidth
                required
                autoFocus
                error={!!errors.nombres}
                helperText={errors.nombres?.message}
              />
            )}
          />
        </Grid>
        <Grid item xs={12} sm={6}>
          <Controller
            name="apellidos"
            control={control}
            render={({ field }) => (
              <TextField
                {...field}
                label="Apellidos"
                fullWidth
                required
                error={!!errors.apellidos}
                helperText={errors.apellidos?.message}
              />
            )}
          />
        </Grid>
        <Grid item xs={12}>
          <Controller
            name="email"
            control={control}
            render={({ field }) => (
              <TextField
                {...field}
                label="Correo Electrónico"
                type="email"
                fullWidth
                required
                error={!!errors.email}
                helperText={errors.email?.message}
              />
            )}
          />
        </Grid>
        <Grid item xs={12} sm={6}>
          <Controller
            name="numeroIdentificacion"
            control={control}
            render={({ field }) => (
              <TextField
                {...field}
                label="Número de Identificación"
                type="text"
                fullWidth
                required
                error={!!errors.numeroIdentificacion}
                helperText={errors.numeroIdentificacion?.message}
              />
            )}
          />
        </Grid>
        <Grid item xs={12} sm={6}>
          <Controller
            name="fechaContratacion"
            control={control}
            render={({ field }) => (
              <TextField
                {...field}
                label="Fecha de Contratación"
                type="date"
                fullWidth
                required
                InputLabelProps={{ shrink: true }}
                error={!!errors.fechaContratacion}
                helperText={errors.fechaContratacion?.message}
              />
            )}
          />
        </Grid>
        <Grid item xs={12} sm={6}>
          <Controller
            name="cargo"
            control={control}
            render={({ field }) => (
              <TextField
                {...field}
                label="Cargo"
                fullWidth
                required
                error={!!errors.cargo}
                helperText={errors.cargo?.message}
              />
            )}
          />
        </Grid>
        <Grid item xs={12} sm={6}>
          <Controller
            name="salarioBase"
            control={control}
            render={({ field }) => (
              <TextField
                {...field}
                label="Salario Base"
                type="number"
                fullWidth
                required
                error={!!errors.salarioBase}
                helperText={errors.salarioBase?.message}
              />
            )}
          />
        </Grid>
        <Grid item xs={12} sm={6}>
          <Controller
            name="telefono"
            control={control}
            render={({ field }) => (
              <TextField
                {...field}
                label="Teléfono"
                fullWidth
                required
                error={!!errors.telefono}
                helperText={errors.telefono?.message}
              />
            )}
          />
        </Grid>
        <Grid item xs={12} sm={6}>
          <Controller
            name="rolSistema"
            control={control}
            render={({ field }) => (
              <FormControl fullWidth required error={!!errors.rolSistema}>
                <InputLabel id="rol-sistema-label">Rol en el Sistema</InputLabel>
                <Select
                  {...field}
                  labelId="rol-sistema-label"
                  id="rol-sistema-select"
                  label="Rol en el Sistema"
                  value={field.value || 'EMPLEADO'} // Asegurar que siempre hay un valor
                >
                  <MenuItem value="EMPLEADO">Empleado</MenuItem>
                  <MenuItem value="SUPERVISOR">Supervisor</MenuItem>
                  <MenuItem value="ADMINISTRADOR">Administrador</MenuItem>
                </Select>
                <FormHelperText>{errors.rolSistema?.message}</FormHelperText>
              </FormControl>
            )}
          />
        </Grid>
      </Grid>
      <Box sx={{ display: 'flex', justifyContent: 'flex-end', mt: 3 }}>
        <Button onClick={onCancel} sx={{ mr: 1 }} disabled={isSubmitting}>
          Cancelar
        </Button>
        <Button 
          type="submit" 
          variant="contained" 
          disabled={isSubmitting /* || !isValid - isValid se maneja por el resolver */} 
          aria-label="Guardar"
        >
          {isSubmitting ? <CircularProgress size={24} /> : 'Guardar'}
        </Button>
      </Box>
    </form>
  );
};
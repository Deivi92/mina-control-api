import React, { useEffect } from 'react';
import {
  Box,
  Button,
  Dialog,
  DialogActions,
  DialogContent,
  DialogTitle,
  FormControl,
  InputLabel,
  MenuItem,
  Select,
  TextField,
  CircularProgress,
} from '@mui/material';
import { useForm, Controller } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { z } from 'zod';
import type {
  RegistroProduccionCreateDTO,
  RegistroProduccionUpdateDTO,
} from '../types';

interface RegistroProduccionFormProps {
  open: boolean;
  onClose: () => void;
  onSubmit: (data: RegistroProduccionCreateDTO | RegistroProduccionUpdateDTO) => void;
  isSubmitting?: boolean;
  initialValues?: RegistroProduccionUpdateDTO;
  empleados: { id: number; nombres: string; apellidos: string; numeroIdentificacion: string }[];
  tiposTurno: { id: number; nombre: string; descripcion: string; activo: boolean }[];
}

// Definición del esquema de validación con Zod
const validationSchema = z.object({
  empleadoId: z.number().min(1, 'Empleado es obligatorio'),
  fechaRegistro: z.string().min(1, 'Fecha de registro es obligatoria'),
  tipoTurnoId: z.number().min(1, 'Turno es obligatorio'),
  cantidadExtraidaToneladas: z.number()
    .min(0, 'La cantidad debe ser mayor o igual a 0'),
  ubicacionExtraccion: z.string().min(1, 'Ubicación de extracción es obligatoria'),
  observaciones: z.string().optional(),
});

type FormValues = z.infer<typeof validationSchema>;

export const RegistroProduccionForm: React.FC<RegistroProduccionFormProps> = ({
  open,
  onClose,
  onSubmit,
  isSubmitting = false,
  initialValues,
  empleados,
  tiposTurno,
}) => {
  const {
    register,
    handleSubmit,
    control,
    reset,
    formState: { errors },
    setValue,
  } = useForm<FormValues>({
    resolver: zodResolver(validationSchema),
    defaultValues: {
      empleadoId: 0,
      fechaRegistro: '',
      tipoTurnoId: 0,
      cantidadExtraidaToneladas: 0,
      ubicacionExtraccion: '',
      observaciones: '',
    },
  });

  useEffect(() => {
    if (initialValues) {
      setValue('empleadoId', initialValues.empleadoId);
      setValue('fechaRegistro', initialValues.fechaRegistro);
      setValue('tipoTurnoId', initialValues.tipoTurnoId);
      setValue('cantidadExtraidaToneladas', initialValues.cantidadExtraidaToneladas);
      setValue('ubicacionExtraccion', initialValues.ubicacionExtraccion);
      setValue('observaciones', initialValues.observaciones || '');
    } else {
      reset();
    }
  }, [initialValues, reset, setValue]);

  const handleFormSubmit = (data: FormValues) => {
    onSubmit(data);
  };

  return (
    <Dialog open={open} onClose={onClose} maxWidth="sm" fullWidth>
      <form onSubmit={handleSubmit(handleFormSubmit)}>
        <DialogTitle>
          {initialValues ? 'Editar Registro de Producción' : 'Registrar Nueva Producción'}
        </DialogTitle>
        <DialogContent>
          <Box sx={{ display: 'flex', flexDirection: 'column', gap: 2, mt: 2 }}>
            <FormControl fullWidth required error={!!errors.empleadoId}>
              <InputLabel id="empleado-label">Empleado</InputLabel>
              <Controller
                name="empleadoId"
                control={control}
                render={({ field }) => (
                  <Select
                    {...field}
                    labelId="empleado-label"
                    label="Empleado"
                    value={field.value || ''}
                    onChange={(e) => field.onChange(Number(e.target.value))}
                  >
                    {empleados.map((empleado) => (
                      <MenuItem key={empleado.id} value={empleado.id}>
                        {empleado.nombres} {empleado.apellidos} ({empleado.numeroIdentificacion})
                      </MenuItem>
                    ))}
                  </Select>
                )}
              />
              {errors.empleadoId && (
                <p role="alert" style={{ color: 'red', fontSize: '0.75rem' }}>
                  {errors.empleadoId.message}
                </p>
              )}
            </FormControl>

            <TextField
              label="Fecha de Registro"
              type="date"
              {...register('fechaRegistro')}
              error={!!errors.fechaRegistro}
              helperText={errors.fechaRegistro?.message}
              InputLabelProps={{ shrink: true }}
              required
            />

            <FormControl fullWidth required error={!!errors.tipoTurnoId}>
              <InputLabel id="turno-label">Turno</InputLabel>
              <Controller
                name="tipoTurnoId"
                control={control}
                render={({ field }) => (
                  <Select
                    {...field}
                    labelId="turno-label"
                    label="Turno"
                    value={field.value || ''}
                    onChange={(e) => field.onChange(Number(e.target.value))}
                  >
                    {tiposTurno.map((turno) => (
                      <MenuItem key={turno.id} value={turno.id}>
                        {turno.nombre} - {turno.descripcion}
                      </MenuItem>
                    ))}
                  </Select>
                )}
              />
              {errors.tipoTurnoId && (
                <p role="alert" style={{ color: 'red', fontSize: '0.75rem' }}>
                  {errors.tipoTurnoId.message}
                </p>
              )}
            </FormControl>

            <TextField
              label="Cantidad Extraída (toneladas)"
              type="number"
              {...register('cantidadExtraidaToneladas', { valueAsNumber: true })}
              error={!!errors.cantidadExtraidaToneladas}
              helperText={errors.cantidadExtraidaToneladas?.message}
              required
              InputProps={{
                inputProps: {
                  step: 0.01,
                  min: 0
                }
              }}
            />

            <TextField
              label="Ubicación de Extracción"
              {...register('ubicacionExtraccion')}
              error={!!errors.ubicacionExtraccion}
              helperText={errors.ubicacionExtraccion?.message}
              required
            />

            <TextField
              label="Observaciones"
              {...register('observaciones')}
              multiline
              rows={3}
              helperText="Observaciones adicionales sobre el registro de producción"
            />
          </Box>
        </DialogContent>
        <DialogActions>
          <Button onClick={onClose} disabled={isSubmitting}>
            Cancelar
          </Button>
          <Button type="submit" variant="contained" disabled={isSubmitting}>
            {isSubmitting ? <CircularProgress size={24} /> : 'Guardar'}
          </Button>
        </DialogActions>
      </form>
    </Dialog>
  );
};
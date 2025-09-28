
import { useEffect } from 'react';
import { useForm, Controller } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { z } from 'zod';
import { Dialog, DialogTitle, DialogContent, DialogActions, Button, TextField, Grid } from '@mui/material';
import { TipoTurnoRequest } from '../types';

// Esquema de validación con Zod
const validationSchema = z.object({
  nombre: z.string().min(1, 'El nombre es requerido').max(50, 'El nombre no puede exceder los 50 caracteres'),
  horaInicio: z.string().regex(/^([0-1]?[0-9]|2[0-3]):[0-5][0-9]$/, 'Formato de hora inválido (HH:mm)'),
  horaFin: z.string().regex(/^([0-1]?[0-9]|2[0-3]):[0-5][0-9]$/, 'Formato de hora inválido (HH:mm)'),
  color: z.string().startsWith('#', 'El color debe ser un código hexadecimal').length(7, 'El código de color debe tener 7 caracteres'),
});

interface Props {
  open: boolean;
  onClose: () => void;
  onSubmit: (data: TipoTurnoRequest) => void;
  initialData?: TipoTurnoRequest;
  isSubmitting: boolean;
}

export const TipoTurnoForm = ({ open, onClose, onSubmit, initialData, isSubmitting }: Props) => {
  const { control, handleSubmit, reset, formState: { errors } } = useForm<TipoTurnoRequest>({
    resolver: zodResolver(validationSchema),
    defaultValues: {
      nombre: '',
      horaInicio: '',
      horaFin: '',
      color: '#FFFFFF',
    },
  });

  useEffect(() => {
    if (open) {
      reset(initialData || { nombre: '', horaInicio: '', horaFin: '', color: '#FFFFFF' });
    }
  }, [open, initialData, reset]);

  return (
    <Dialog open={open} onClose={onClose} maxWidth="sm" fullWidth>
      <DialogTitle>{initialData ? 'Editar Tipo de Turno' : 'Crear Nuevo Tipo de Turno'}</DialogTitle>
      <form onSubmit={handleSubmit(onSubmit)} noValidate>
        <DialogContent>
          <Grid container spacing={2} sx={{ mt: 1 }}>
            <Grid item xs={12}>
              <Controller
                name="nombre"
                control={control}
                render={({ field }) => (
                  <TextField
                    {...field}
                    label="Nombre del Turno"
                    fullWidth
                    required
                    error={!!errors.nombre}
                    helperText={errors.nombre?.message}
                  />
                )}
              />
            </Grid>
            <Grid item xs={12} sm={6}>
              <Controller
                name="horaInicio"
                control={control}
                render={({ field }) => (
                  <TextField
                    {...field}
                    label="Hora de Inicio"
                    type="time"
                    fullWidth
                    required
                    InputLabelProps={{ shrink: true }}
                    error={!!errors.horaInicio}
                    helperText={errors.horaInicio?.message}
                  />
                )}
              />
            </Grid>
            <Grid item xs={12} sm={6}>
              <Controller
                name="horaFin"
                control={control}
                render={({ field }) => (
                  <TextField
                    {...field}
                    label="Hora de Fin"
                    type="time"
                    fullWidth
                    required
                    InputLabelProps={{ shrink: true }}
                    error={!!errors.horaFin}
                    helperText={errors.horaFin?.message}
                  />
                )}
              />
            </Grid>
            <Grid item xs={12} sm={6}>
              <Controller
                name="color"
                control={control}
                render={({ field }) => (
                  <TextField
                    {...field}
                    label="Color"
                    type="color"
                    fullWidth
                    sx={{ height: '100%' }}
                    error={!!errors.color}
                    helperText={errors.color?.message}
                  />
                )}
              />
            </Grid>
          </Grid>
        </DialogContent>
        <DialogActions>
          <Button onClick={onClose} disabled={isSubmitting}>Cancelar</Button>
          <Button type="submit" variant="contained" disabled={isSubmitting}>
            {isSubmitting ? 'Guardando...' : 'Guardar'}
          </Button>
        </DialogActions>
      </form>
    </Dialog>
  );
};

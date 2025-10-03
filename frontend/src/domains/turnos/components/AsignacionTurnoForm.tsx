
import { useEffect } from 'react';
import { useForm, Controller } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { z } from 'zod';
import { Dialog, DialogTitle, DialogContent, DialogActions, Button, TextField, Grid, Autocomplete } from '@mui/material';
import type { AsignacionTurnoRequest, TipoTurno } from '../types';
import type { Empleado } from '../../empleado/types';

const validationSchema = z.object({
  empleadoId: z.number().min(1, 'Debes seleccionar un empleado'),
  tipoTurnoId: z.number().min(1, 'Debes seleccionar un tipo de turno'),
  fecha: z.string().min(1, 'La fecha es requerida'),
});

interface Props {
  open: boolean;
  onClose: () => void;
  onSubmit: (data: AsignacionTurnoRequest) => void;
  empleados: Empleado[];
  tiposTurno: TipoTurno[];
  isSubmitting: boolean;
}

export const AsignacionTurnoForm = ({ open, onClose, onSubmit, empleados, tiposTurno, isSubmitting }: Props) => {
  const { control, handleSubmit, reset, formState: { errors } } = useForm<AsignacionTurnoRequest>({
    resolver: zodResolver(validationSchema),
    defaultValues: { fecha: new Date().toISOString().split('T')[0] },
  });

  useEffect(() => {
    if (open) {
      reset({ fecha: new Date().toISOString().split('T')[0] });
    }
  }, [open, reset]);

  return (
    <Dialog open={open} onClose={onClose} maxWidth="sm" fullWidth>
      <DialogTitle>Asignar Empleado a Turno</DialogTitle>
      <form onSubmit={handleSubmit(onSubmit)} noValidate>
        <DialogContent>
          <Grid container spacing={2} sx={{ mt: 1 }}>
            <Grid item xs={12}>
              <Controller
                name="empleadoId"
                control={control}
                render={({ field }) => (
                  <Autocomplete
                    options={empleados}
                    getOptionLabel={(option) => `${option.nombres} ${option.apellidos}`}
                    onChange={(_, newValue) => field.onChange(newValue?.id || null)}
                    renderInput={(params) => (
                      <TextField
                        {...params}
                        label="Empleado"
                        required
                        error={!!errors.empleadoId}
                        helperText={errors.empleadoId?.message}
                      />
                    )}
                  />
                )}
              />
            </Grid>
            <Grid item xs={12}>
              <Controller
                name="tipoTurnoId"
                control={control}
                render={({ field }) => (
                  <Autocomplete
                    options={tiposTurno}
                    getOptionLabel={(option) => option.nombre}
                    onChange={(_, newValue) => field.onChange(newValue?.id || null)}
                    renderInput={(params) => (
                      <TextField
                        {...params}
                        label="Tipo de Turno"
                        required
                        error={!!errors.tipoTurnoId}
                        helperText={errors.tipoTurnoId?.message}
                      />
                    )}
                  />
                )}
              />
            </Grid>
            <Grid item xs={12}>
              <Controller
                name="fecha"
                control={control}
                render={({ field }) => (
                  <TextField
                    {...field}
                    label="Fecha"
                    type="date"
                    fullWidth
                    required
                    InputLabelProps={{ shrink: true }}
                    error={!!errors.fecha}
                    helperText={errors.fecha?.message}
                  />
                )}
              />
            </Grid>
          </Grid>
        </DialogContent>
        <DialogActions>
          <Button onClick={onClose} disabled={isSubmitting}>Cancelar</Button>
          <Button type="submit" variant="contained" disabled={isSubmitting}>
            {isSubmitting ? 'Asignando...' : 'Asignar'}
          </Button>
        </DialogActions>
      </form>
    </Dialog>
  );
};

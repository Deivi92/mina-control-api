import React from 'react';
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
import { Formik, Form, Field } from 'formik';
import { empleadoValidationSchema } from '../validation/empleado.validation';
import { FormikTextField } from '../../../shared/components/FormikTextField';
import type { Empleado, EmpleadoRequest } from '../types';

interface EmpleadoFormProps {
  initialData?: Empleado | null;
  onSubmit: (data: EmpleadoRequest) => void;
  onCancel: () => void;
  isSubmitting?: boolean;
}

const defaultValues: EmpleadoRequest = {
  nombres: '',
  apellidos: '',
  numeroIdentificacion: '',
  email: '',
  telefono: '',
  cargo: '',
  fechaContratacion: '',
  salarioBase: 0,
  rolSistema: 'EMPLEADO',
};

export const EmpleadoForm: React.FC<EmpleadoFormProps> = ({
  initialData,
  onSubmit,
  onCancel,
  isSubmitting = false,
}) => {
  const initialFormValues = initialData
    ? {
        ...initialData,
        telefono: initialData.telefono || '',
        fechaContratacion: initialData.fechaContratacion.split('T')[0],
      }
    : defaultValues;

  return (
    <Formik
      initialValues={initialFormValues}
      validationSchema={empleadoValidationSchema}
      onSubmit={(values) => {
        onSubmit(values);
      }}
      enableReinitialize // Permite que el formulario se reinicie con nuevos `initialData`
    >
      {({ errors, touched, values, handleChange, isValid }) => (
        <Form noValidate>
          <Grid container spacing={2}>
            <Grid item xs={12} sm={6}>
              <FormikTextField name="nombres" label="Nombres" required autoFocus />
            </Grid>
            <Grid item xs={12} sm={6}>
              <FormikTextField name="apellidos" label="Apellidos" required />
            </Grid>
            <Grid item xs={12}>
              <FormikTextField name="email" label="Correo Electrónico" type="email" required />
            </Grid>
            <Grid item xs={12} sm={6}>
              <FormikTextField name="numeroIdentificacion" label="Número de Identificación" type="number" required />
            </Grid>
            <Grid item xs={12} sm={6}>
              <FormikTextField
                name="fechaContratacion"
                label="Fecha de Contratación"
                type="date"
                required
                InputLabelProps={{ shrink: true }}
              />
            </Grid>
            <Grid item xs={12} sm={6}>
              <FormikTextField name="cargo" label="Cargo" required />
            </Grid>
            <Grid item xs={12} sm={6}>
              <FormikTextField name="salarioBase" label="Salario Base" type="number" required />
            </Grid>
            <Grid item xs={12} sm={6}>
              <FormikTextField name="telefono" label="Teléfono" required />
            </Grid>
            <Grid item xs={12} sm={6}>
              <FormControl fullWidth required error={touched.rolSistema && Boolean(errors.rolSistema)}>
                <InputLabel id="rol-sistema-label">Rol en el Sistema</InputLabel>
                <Field
                  as={Select}
                  labelId="rol-sistema-label"
                  id="rolSistema"
                  name="rolSistema"
                  value={values.rolSistema}
                  label="Rol en el Sistema"
                  onChange={handleChange}
                >
                  <MenuItem value="EMPLEADO">Empleado</MenuItem>
                  <MenuItem value="SUPERVISOR">Supervisor</MenuItem>
                  <MenuItem value="ADMINISTRADOR">Administrador</MenuItem>
                </Field>
                <FormHelperText>{touched.rolSistema && errors.rolSistema ? errors.rolSistema : ' '}</FormHelperText>
              </FormControl>
            </Grid>
          </Grid>
          <Box sx={{ display: 'flex', justifyContent: 'flex-end', mt: 3 }}>
            <Button onClick={onCancel} sx={{ mr: 1 }}>
              Cancelar
            </Button>
            <Button type="submit" variant="contained" disabled={isSubmitting || !isValid} aria-label="Guardar">
              {isSubmitting ? <CircularProgress size={24} /> : 'Guardar'}
            </Button>
          </Box>
        </Form>
      )}
    </Formik>
  );
};
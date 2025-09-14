import { TextField, type TextFieldProps } from '@mui/material';
import { useField } from 'formik';

// Nuevo tipo que combina las props de TextField y requiere 'name'
type FormikTextFieldProps = TextFieldProps & {
  name: string;
};

/**
 * Un componente TextField de Material-UI integrado con Formik.
 * Muestra automáticamente los errores de validación y gestiona el estado del campo.
 */
export const FormikTextField: React.FC<FormikTextFieldProps> = ({ name, ...props }) => {
  const [field, meta] = useField(name);

  const configTextField: TextFieldProps = {
    ...field,
    ...props,
    fullWidth: true,
    error: meta.touched && Boolean(meta.error),
    helperText: meta.touched && meta.error ? meta.error : ' ',
  };

  return <TextField {...configTextField} />;
};

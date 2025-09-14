import * as yup from 'yup';

// Corresponde a las validaciones del EmpleadoRequest DTO en el backend
export const empleadoValidationSchema = yup.object({
  nombres: yup
    .string()
    .max(100, 'El nombre no puede tener más de 100 caracteres')
    .required('El nombre es requerido'),
  apellidos: yup
    .string()
    .max(100, 'El apellido no puede tener más de 100 caracteres')
    .required('El apellido es requerido'),
  numeroIdentificacion: yup
    .string()
    .matches(/^[0-9]+$/, "El número de identificación solo debe contener dígitos")
    .max(20, 'El número de identificación no puede tener más de 20 caracteres')
    .required('El número de identificación es requerido'),
  email: yup
    .string()
    .email('El formato del email no es válido')
    .required('El email es requerido'),
  telefono: yup
    .string()
    .matches(/^[0-9\s+()-]*$/, 'El teléfono solo puede contener números y caracteres válidos')
    .min(7, 'El teléfono debe tener al menos 7 dígitos')
    .max(15, 'El teléfono no puede tener más de 15 caracteres')
    .required('El teléfono es requerido'),
  cargo: yup
    .string()
    .required('El cargo es requerido'),
  fechaContratacion: yup
    .date()
    .max(new Date(), 'La fecha de contratación no puede ser en el futuro')
    .required('La fecha de contratación es requerida'),
  salarioBase: yup
    .number()
    .typeError('El salario debe ser un número')
    .positive('El salario base debe ser un número positivo')
    .required('El salario base es requerido'),
  rolSistema: yup
    .string()
    .required('El rol del sistema es requerido'),
});

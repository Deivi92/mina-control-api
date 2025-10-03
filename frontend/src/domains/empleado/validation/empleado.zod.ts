import { z } from 'zod';

// Esquema de validación con Zod para el formulario (campos como strings para manejo en UI)
export const empleadoValidationSchema = z.object({
  nombres: z.string().min(1, 'El nombre es requerido').max(100, 'El nombre no puede tener más de 100 caracteres'),
  apellidos: z.string().min(1, 'El apellido es requerido').max(100, 'El apellido no puede tener más de 100 caracteres'),
  numeroIdentificacion: z
    .string()
    .min(1, 'El número de identificación es requerido')
    .regex(/^\d+$/, "El número de identificación solo debe contener dígitos")
    .max(20, 'El número de identificación no puede tener más de 20 caracteres'),
  email: z.string().min(1, 'El email es requerido').email('El formato del email no es válido'),
  telefono: z
    .string()
    .min(1, 'El teléfono es requerido')
    .regex(/^[\d\s+\-()]*$/, 'El teléfono solo puede contener números y caracteres válidos')
    .min(7, 'El teléfono debe tener al menos 7 dígitos')
    .max(15, 'El teléfono no puede tener más de 15 caracteres'),
  cargo: z.string().min(1, 'El cargo es requerido'),
  fechaContratacion: z.string().min(1, 'La fecha de contratación es requerida'),
  salarioBase: z.string().min(1, 'El salario base es requerido').regex(/^\d+(\.\d+)?$/, 'El salario debe ser un número válido'),
  rolSistema: z.string().min(1, 'El rol del sistema es requerido'),
});

export type EmpleadoFormData = z.infer<typeof empleadoValidationSchema>;
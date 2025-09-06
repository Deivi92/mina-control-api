// frontend/src/domains/empleado/types.ts

/**
 * Basado en el DTO EmpleadoResponse del backend.
 * Representa a un empleado tal como lo devuelve la API.
 */
export interface Empleado {
  id: number;
  nombre: string;
  apellido: string;
  numeroIdentificacion: string;
  email: string;
  fechaNacimiento: string; // Formato de fecha ISO 8601
  fechaContratacion: string; // Formato de fecha ISO 8601
  puesto: string;
  salario: number;
  estado: 'ACTIVO' | 'INACTIVO';
}

/**
 * Basado en el DTO EmpleadoRequest del backend.
 * Representa los datos necesarios para crear o actualizar un empleado.
 * Usamos Omit para derivar este tipo desde Empleado, asegurando la consistencia.
 */
export type EmpleadoRequest = Omit<Empleado, 'id' | 'estado' | 'fechaContratacion'>;

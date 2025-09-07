// frontend/src/domains/empleado/types.ts

/**
 * Representa los roles de sistema disponibles en el backend.
 */
export type RolSistema = 'EMPLEADO' | 'ADMINISTRADOR' | 'SUPERVISOR';

/**
 * Representa el estado de un empleado.
 */
export type EstadoEmpleado = 'ACTIVO' | 'INACTIVO';

/**
 * Basado en el DTO EmpleadoResponse del backend.
 * Representa a un empleado tal como lo devuelve la API.
 */
export interface Empleado {
  id: number;
  nombres: string;
  apellidos: string;
  numeroIdentificacion: string;
  email: string;
  telefono: string | null;
  cargo: string;
  fechaContratacion: string; // Formato de fecha ISO 8601 (YYYY-MM-DD)
  salarioBase: number;
  rolSistema: RolSistema;
  estado: EstadoEmpleado;
}

/**
 * Basado en el DTO EmpleadoRequest del backend.
 * Representa los datos necesarios para crear o actualizar un empleado.
 */
export interface EmpleadoRequest {
  nombres: string;
  apellidos: string;
  numeroIdentificacion: string;
  email: string;
  telefono?: string | null;
  cargo: string;
  fechaContratacion: string; // Formato YYYY-MM-DD
  salarioBase: number;
  rolSistema: RolSistema;
}
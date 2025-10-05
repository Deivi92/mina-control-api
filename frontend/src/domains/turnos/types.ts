// =================================================================
// INTERFACES PARA TIPOS DE TURNO
// =================================================================

/**
 * Representa la estructura de un tipo de turno, tal como se recibe de la API.
 * Corresponde al TipoTurnoDTO del backend.
 */
export interface TipoTurno {
  id: number;
  nombre: string;
  horaInicio: string; // Formato "HH:mm:ss"
  horaFin: string; // Formato "HH:mm:ss"
  color: string;
}

/**
 * DTO para crear o actualizar un tipo de turno.
 * Corresponde al TipoTurnoCreateDTO del backend.
 */
export interface TipoTurnoRequest {
  nombre: string;
  horaInicio: string; // Formato "HH:mm"
  horaFin: string; // Formato "HH:mm"
  color: string;
}

// =================================================================
// INTERFACES PARA ASIGNACIONES DE TURNO
// =================================================================

/**
 * Representa una asignación de un empleado a un turno en un rango de fechas.
 * Corresponde al AsignacionTurnoDTO del backend.
 */
export interface AsignacionTurno {
  id: number;
  empleadoId: number;
  tipoTurnoId: number;
  fechaInicio: string; // Formato "YYYY-MM-DD"
  fechaFin: string; // Formato "YYYY-MM-DD"
}

/**
 * DTO para crear una nueva asignación de turno.
 * Corresponde al AsignacionTurnoCreateDTO del backend.
 */
export interface AsignacionTurnoRequest {
  empleadoId: number;
  tipoTurnoId: number;
  fechaInicio: string; // Formato "YYYY-MM-DD"
  fechaFin: string; // Formato "YYYY-MM-DD"
}

// =================================================================
// INTERFACES PARA REGISTRO DE ASISTENCIA
// =================================================================

/**
 * Enum para el tipo de registro de asistencia.
 * Corresponde al TipoRegistro del backend.
 */
export const TipoRegistro = {
  ENTRADA: 'ENTRADA',
  SALIDA: 'SALIDA',
} as const;

export type TipoRegistro = typeof TipoRegistro[keyof typeof TipoRegistro];

/**
 * Enum para el estado de la asistencia.
 * Corresponde al EstadoAsistencia del backend.
 */
export const EstadoAsistencia = {
  PENDIENTE: 'PENDIENTE',
  ASISTIO: 'ASISTIO',
  FALTA: 'FALTA',
  RETRASO: 'RETRASO',
  PERMISO: 'PERMISO',
} as const;

export type EstadoAsistencia = typeof EstadoAsistencia[keyof typeof EstadoAsistencia];

/**
 * Representa un registro de asistencia de un empleado.
 * Corresponde al RegistroAsistenciaDTO del backend.
 */
export interface RegistroAsistencia {
  id: number;
  empleadoId: number;
  fecha: string; // Formato "YYYY-MM-DD"
  horaEntrada: string | null; // Formato "HH:mm:ss"
  horaSalida: string | null; // Formato "HH:mm:ss"
  horasTrabajadas: number | null;
  estado: EstadoAsistencia;
  motivo: string | null;
}

/**
 * DTO para registrar una entrada o salida.
 * Corresponde al RegistrarAsistenciaDTO del backend.
 */
export interface RegistrarAsistenciaRequest {
  empleadoId: number;
  tipo: TipoRegistro;
}

/**
 * DTO para gestionar una excepción de asistencia.
 * Corresponde al ExcepcionAsistenciaDTO del backend.
 */
export interface ExcepcionAsistenciaRequest {
  empleadoId: number;
  fecha: string; // Formato "YYYY-MM-DD"
  estado: EstadoAsistencia;
  motivo: string;
}
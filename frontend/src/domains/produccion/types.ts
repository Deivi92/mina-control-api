// Tipos para el dominio de Producción

export interface RegistroProduccionCreateDTO {
  empleadoId: number;
  tipoTurnoId: number;
  fechaRegistro: string; // formato: date
  cantidadExtraidaToneladas: number;
  ubicacionExtraccion: string;
  observaciones?: string;
}

export interface RegistroProduccionUpdateDTO {
  id: number;
  empleadoId: number;
  tipoTurnoId: number;
  fechaRegistro: string; // formato: date
  cantidadExtraidaToneladas: number;
  ubicacionExtraccion: string;
  observaciones?: string;
}

export interface RegistroProduccionDTO {
  id: number;
  empleadoId: number;
  nombreEmpleado: string;
  tipoTurnoId: number;
  nombreTurno: string;
  fechaRegistro: string; // formato: date
  cantidadExtraidaToneladas: number;
  ubicacionExtraccion: string;
  observaciones?: string;
  validado: boolean;
}

// Tipo auxiliar para los parámetros de filtrado
export interface ProduccionFilters {
  empleadoId?: number;
  fechaInicio?: string; // formato: date
  fechaFin?: string; // formato: date
  tipoTurnoId?: number;
}
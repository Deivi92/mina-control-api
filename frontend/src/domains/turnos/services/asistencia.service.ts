
import axios from 'axios';
import type { RegistrarAsistenciaRequest, ExcepcionAsistenciaRequest } from '../types';

const API_URL = '/api/v1/asistencia';

const registrarAsistencia = async (data: RegistrarAsistenciaRequest) => {
  const response = await axios.post(`${API_URL}/registrar`, data);
  return response.data;
};

const consultarAsistencia = async (empleadoId: number | undefined, fechaInicio: string, fechaFin: string) => {
  const params = {
    empleadoId,
    fechaInicio,
    fechaFin,
  };
  const response = await axios.get(`${API_URL}/consultar`, { params });
  return response.data;
};

const gestionarExcepcionAsistencia = async (data: ExcepcionAsistenciaRequest) => {
  const response = await axios.post(`${API_URL}/excepciones`, data);
  return response.data;
};

export const asistenciaService = {
  registrarAsistencia,
  consultarAsistencia,
  gestionarExcepcionAsistencia,
};


import axios from 'axios';
import { AsignacionTurnoRequest, TipoTurnoRequest } from '../types';

const API_URL = '/api/v1/turnos';

const crearTipoTurno = async (data: TipoTurnoRequest) => {
  const response = await axios.post(API_URL, data);
  return response.data;
};

const listarTiposDeTurno = async () => {
  const response = await axios.get(API_URL);
  return response.data;
};

const obtenerTipoTurnoPorId = async (id: number) => {
  const response = await axios.get(`${API_URL}/${id}`);
  return response.data;
};

const actualizarTipoTurno = async (id: number, data: TipoTurnoRequest) => {
  const response = await axios.put(`${API_URL}/${id}`, data);
  return response.data;
};

const eliminarTipoTurno = async (id: number) => {
  await axios.delete(`${API_URL}/${id}`);
};

const asignarEmpleadoATurno = async (data: AsignacionTurnoRequest) => {
  const response = await axios.post(`${API_URL}/asignaciones`, data);
  return response.data;
};

export const turnoService = {
  crearTipoTurno,
  listarTiposDeTurno,
  obtenerTipoTurnoPorId,
  actualizarTipoTurno,
  eliminarTipoTurno,
  asignarEmpleadoATurno,
};

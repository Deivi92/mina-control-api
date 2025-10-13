import axios from 'axios';
import type { 
  RegistroProduccionCreateDTO, 
  RegistroProduccionUpdateDTO, 
  RegistroProduccionDTO,
  ProduccionFilters
} from '../types';

const API_BASE_URL = '/api/v1/produccion';

export const produccionService = {
  listarRegistros: async (params?: ProduccionFilters): Promise<RegistroProduccionDTO[]> => {
    const response = await axios.get<RegistroProduccionDTO[]>(API_BASE_URL, { params });
    return response.data;
  },

  registrarProduccion: async (data: RegistroProduccionCreateDTO): Promise<RegistroProduccionDTO> => {
    const response = await axios.post<RegistroProduccionDTO>(API_BASE_URL, data);
    return response.data;
  },

  obtenerRegistroPorId: async (id: number): Promise<RegistroProduccionDTO> => {
    const response = await axios.get<RegistroProduccionDTO>(`${API_BASE_URL}/${id}`);
    return response.data;
  },

  actualizarRegistro: async (id: number, data: RegistroProduccionUpdateDTO): Promise<RegistroProduccionDTO> => {
    const response = await axios.put<RegistroProduccionDTO>(`${API_BASE_URL}/${id}`, data);
    return response.data;
  },

  eliminarRegistro: async (id: number): Promise<void> => {
    await axios.delete(`${API_BASE_URL}/${id}`);
  },

  validarRegistro: async (id: number): Promise<RegistroProduccionDTO> => {
    const response = await axios.patch<RegistroProduccionDTO>(`${API_BASE_URL}/${id}/validar`);
    return response.data;
  }
};
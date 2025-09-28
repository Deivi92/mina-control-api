
import axios from 'axios';
import { asistenciaService } from './asistencia.service';
import { RegistrarAsistenciaRequest, ExcepcionAsistenciaRequest, TipoRegistro, EstadoAsistencia } from '../types';

// Mockear axios
vi.mock('axios');
const mockedAxios = vi.mocked(axios, true);

describe('AsistenciaService', () => {
  const API_URL = '/api/v1/asistencia';

  beforeEach(() => {
    mockedAxios.post.mockReset();
    mockedAxios.get.mockReset();
  });

  describe('registrarAsistencia', () => {
    it('debería llamar a POST en el endpoint de registrar con los datos correctos', async () => {
      const requestData: RegistrarAsistenciaRequest = {
        empleadoId: 1,
        tipo: TipoRegistro.ENTRADA,
      };
      const mockResponse = { data: { id: 1, estado: EstadoAsistencia.ASISTIO } };
      mockedAxios.post.mockResolvedValue(mockResponse);

      await asistenciaService.registrarAsistencia(requestData);

      expect(mockedAxios.post).toHaveBeenCalledWith(`${API_URL}/registrar`, requestData);
    });
  });

  describe('consultarAsistencia', () => {
    it('debería llamar a GET en el endpoint de consultar con los parámetros correctos', async () => {
      const params = {
        empleadoId: 1,
        fechaInicio: '2024-05-01',
        fechaFin: '2024-05-31',
      };
      const mockResponse = { data: [] };
      mockedAxios.get.mockResolvedValue(mockResponse);

      await asistenciaService.consultarAsistencia(params.empleadoId, params.fechaInicio, params.fechaFin);

      expect(mockedAxios.get).toHaveBeenCalledWith(`${API_URL}/consultar`, { params });
    });

    it('debería manejar la consulta sin empleadoId', async () => {
        const params = {
            fechaInicio: '2024-05-01',
            fechaFin: '2024-05-31',
          };
        const mockResponse = { data: [] };
        mockedAxios.get.mockResolvedValue(mockResponse);
  
        await asistenciaService.consultarAsistencia(undefined, params.fechaInicio, params.fechaFin);
  
        expect(mockedAxios.get).toHaveBeenCalledWith(`${API_URL}/consultar`, { params: {empleadoId: undefined, ...params} });
      });
  });

  describe('gestionarExcepcionAsistencia', () => {
    it('debería llamar a POST en el endpoint de excepciones con los datos correctos', async () => {
      const requestData: ExcepcionAsistenciaRequest = {
        empleadoId: 1,
        fecha: '2024-05-23',
        estado: EstadoAsistencia.PERMISO,
        motivo: 'Cita médica',
      };
      const mockResponse = { data: { id: 1, estado: EstadoAsistencia.PERMISO } };
      mockedAxios.post.mockResolvedValue(mockResponse);

      await asistenciaService.gestionarExcepcionAsistencia(requestData);

      expect(mockedAxios.post).toHaveBeenCalledWith(`${API_URL}/excepciones`, requestData);
    });
  });
});

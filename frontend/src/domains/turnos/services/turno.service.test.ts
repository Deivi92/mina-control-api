
import axios from 'axios';
import { turnoService } from './turno.service';
import { TipoTurnoRequest } from '../types';

// Mockear axios para aislar las pruebas del servicio de llamadas de red reales
vi.mock('axios');
const mockedAxios = vi.mocked(axios, true);

describe('TurnoService', () => {
  const API_URL = '/api/v1/turnos';

  // Resetear los mocks antes de cada prueba
  beforeEach(() => {
    mockedAxios.get.mockReset();
    mockedAxios.post.mockReset();
    mockedAxios.put.mockReset();
    mockedAxios.delete.mockReset();
  });

  describe('crearTipoTurno', () => {
    it('debería llamar a POST en el endpoint correcto con los datos del turno', async () => {
      const turnoData: TipoTurnoRequest = {
        nombre: 'Turno de Prueba',
        horaInicio: '08:00',
        horaFin: '16:00',
        color: '#FF0000',
      };
      const mockResponse = { data: { id: 1, ...turnoData } };
      mockedAxios.post.mockResolvedValue(mockResponse);

      await turnoService.crearTipoTurno(turnoData);

      expect(mockedAxios.post).toHaveBeenCalledWith(API_URL, turnoData);
    });
  });

  describe('listarTiposDeTurno', () => {
    it('debería llamar a GET en el endpoint correcto', async () => {
      const mockResponse = { data: [] };
      mockedAxios.get.mockResolvedValue(mockResponse);

      await turnoService.listarTiposDeTurno();

      expect(mockedAxios.get).toHaveBeenCalledWith(API_URL);
    });
  });

  describe('obtenerTipoTurnoPorId', () => {
    it('debería llamar a GET en el endpoint con el ID correcto', async () => {
      const turnoId = 123;
      const mockResponse = { data: {} };
      mockedAxios.get.mockResolvedValue(mockResponse);

      await turnoService.obtenerTipoTurnoPorId(turnoId);

      expect(mockedAxios.get).toHaveBeenCalledWith(`${API_URL}/${turnoId}`);
    });
  });

  describe('actualizarTipoTurno', () => {
    it('debería llamar a PUT en el endpoint con el ID y los datos correctos', async () => {
      const turnoId = 123;
      const turnoData: TipoTurnoRequest = {
        nombre: 'Turno Actualizado',
        horaInicio: '09:00',
        horaFin: '17:00',
        color: '#00FF00',
      };
      const mockResponse = { data: { id: turnoId, ...turnoData } };
      mockedAxios.put.mockResolvedValue(mockResponse);

      await turnoService.actualizarTipoTurno(turnoId, turnoData);

      expect(mockedAxios.put).toHaveBeenCalledWith(`${API_URL}/${turnoId}`, turnoData);
    });
  });

  describe('eliminarTipoTurno', () => {
    it('debería llamar a DELETE en el endpoint con el ID correcto', async () => {
      const turnoId = 123;
      mockedAxios.delete.mockResolvedValue({});

      await turnoService.eliminarTipoTurno(turnoId);

      expect(mockedAxios.delete).toHaveBeenCalledWith(`${API_URL}/${turnoId}`);
    });
  });

  describe('asignarEmpleadoATurno', () => {
    it('debería llamar a POST en el endpoint de asignaciones con los datos correctos', async () => {
      const asignacionData = {
        empleadoId: 1,
        tipoTurnoId: 2,
        fecha: '2024-05-23',
      };
      const mockResponse = { data: { id: 1, ...asignacionData } };
      mockedAxios.post.mockResolvedValue(mockResponse);

      await turnoService.asignarEmpleadoATurno(asignacionData);

      expect(mockedAxios.post).toHaveBeenCalledWith(`${API_URL}/asignaciones`, asignacionData);
    });
  });
});

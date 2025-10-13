import { describe, it, expect, vi, beforeEach } from 'vitest';
import { produccionService } from './produccion.service';
import axios from 'axios';

// Mock de axios
vi.mock('axios');

describe('produccion.service', () => {
  beforeEach(() => {
    vi.clearAllMocks();
  });

  describe('listarRegistros', () => {
    it('debería llamar a la API con los parámetros correctos cuando se pasan filtros', async () => {
      // Mock de la respuesta de la API
      const mockResponse = {
        data: [
          {
            id: 1,
            empleadoId: 1,
            nombreEmpleado: 'Juan Pérez',
            tipoTurnoId: 1,
            nombreTurno: 'Turno Día',
            fechaRegistro: '2024-01-15',
            cantidadExtraidaToneladas: 15.5,
            ubicacionExtraccion: 'Zona A',
            observaciones: 'Observaciones',
            validado: true
          }
        ]
      };
      (axios.get as vi.MockedFunction<typeof axios.get>).mockResolvedValue(mockResponse);

      const params = {
        empleadoId: 1,
        fechaInicio: '2024-01-01',
        fechaFin: '2024-01-31'
      };

      const result = await produccionService.listarRegistros(params);

      expect(axios.get).toHaveBeenCalledWith('/api/v1/produccion', { params });
      expect(result).toEqual(mockResponse.data);
    });

    it('debería llamar a la API sin parámetros cuando no se pasan filtros', async () => {
      // Mock de la respuesta de la API
      const mockResponse = { data: [] };
      (axios.get as vi.MockedFunction<typeof axios.get>).mockResolvedValue(mockResponse);

      const result = await produccionService.listarRegistros();

      expect(axios.get).toHaveBeenCalledWith('/api/v1/produccion', { params: undefined });
      expect(result).toEqual(mockResponse.data);
    });
  });

  describe('registrarProduccion', () => {
    it('debería llamar a la API con los datos correctos para crear un nuevo registro', async () => {
      const mockData = {
        empleadoId: 1,
        tipoTurnoId: 1,
        fechaRegistro: '2024-01-15',
        cantidadExtraidaToneladas: 15.5,
        ubicacionExtraccion: 'Zona A',
        observaciones: 'Observaciones'
      };
      const mockResponse = {
        data: {
          id: 1,
          ...mockData,
          nombreEmpleado: 'Juan Pérez',
          nombreTurno: 'Turno Día',
          validado: false
        }
      };
      (axios.post as vi.MockedFunction<typeof axios.post>).mockResolvedValue(mockResponse);

      const result = await produccionService.registrarProduccion(mockData);

      expect(axios.post).toHaveBeenCalledWith('/api/v1/produccion', mockData);
      expect(result).toEqual(mockResponse.data);
    });
  });

  describe('obtenerRegistroPorId', () => {
    it('debería llamar a la API con el ID correcto para obtener un registro específico', async () => {
      const mockResponse = {
        data: {
          id: 1,
          empleadoId: 1,
          nombreEmpleado: 'Juan Pérez',
          tipoTurnoId: 1,
          nombreTurno: 'Turno Día',
          fechaRegistro: '2024-01-15',
          cantidadExtraidaToneladas: 15.5,
          ubicacionExtraccion: 'Zona A',
          observaciones: 'Observaciones',
          validado: true
        }
      };
      (axios.get as vi.MockedFunction<typeof axios.get>).mockResolvedValue(mockResponse);

      const result = await produccionService.obtenerRegistroPorId(1);

      expect(axios.get).toHaveBeenCalledWith('/api/v1/produccion/1');
      expect(result).toEqual(mockResponse.data);
    });
  });

  describe('actualizarRegistro', () => {
    it('debería llamar a la API con los datos correctos para actualizar un registro existente', async () => {
      const mockData = {
        empleadoId: 1,
        tipoTurnoId: 1,
        fechaRegistro: '2024-01-15',
        cantidadExtraidaToneladas: 16.5,
        ubicacionExtraccion: 'Zona B',
        observaciones: 'Observaciones actualizadas'
      };
      const mockResponse = {
        data: {
          id: 1,
          ...mockData,
          nombreEmpleado: 'Juan Pérez',
          nombreTurno: 'Turno Día',
          validado: false
        }
      };
      (axios.put as vi.MockedFunction<typeof axios.put>).mockResolvedValue(mockResponse);

      const result = await produccionService.actualizarRegistro(1, mockData);

      expect(axios.put).toHaveBeenCalledWith('/api/v1/produccion/1', mockData);
      expect(result).toEqual(mockResponse.data);
    });
  });

  describe('eliminarRegistro', () => {
    it('debería llamar a la API con el ID correcto para eliminar un registro existente', async () => {
      const mockResponse = { data: {} };
      (axios.delete as vi.MockedFunction<typeof axios.delete>).mockResolvedValue(mockResponse);

      await produccionService.eliminarRegistro(1);

      expect(axios.delete).toHaveBeenCalledWith('/api/v1/produccion/1');
    });
  });

  describe('validarRegistro', () => {
    it('debería llamar a la API con el ID correcto para validar un registro', async () => {
      const mockResponse = {
        data: {
          id: 1,
          empleadoId: 1,
          nombreEmpleado: 'Juan Pérez',
          tipoTurnoId: 1,
          nombreTurno: 'Turno Día',
          fechaRegistro: '2024-01-15',
          cantidadExtraidaToneladas: 15.5,
          ubicacionExtraccion: 'Zona A',
          observaciones: 'Observaciones',
          validado: true
        }
      };
      (axios.patch as vi.MockedFunction<typeof axios.patch>).mockResolvedValue(mockResponse);

      const result = await produccionService.validarRegistro(1);

      expect(axios.patch).toHaveBeenCalledWith('/api/v1/produccion/1/validar');
      expect(result).toEqual(mockResponse.data);
    });
  });
});
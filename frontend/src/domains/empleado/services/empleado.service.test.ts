import axios from 'axios';
import { empleadoService } from './empleado.service';
import { EmpleadoRequest } from '../types';

// Mockear axios para aislar el servicio de la red real
vi.mock('axios');
const mockedAxios = axios as vi.Mocked<typeof axios>;

describe('EmpleadoService', () => {

  // Limpiar mocks después de cada prueba para evitar interferencias
  afterEach(() => {
    vi.clearAllMocks();
  });

  describe('obtenerEmpleados', () => {
    it('debería llamar a GET /api/empleados y devolver los datos', async () => {
      // Arrange: Preparamos los datos falsos que simulará la API
      const mockData = [{ id: 1, nombre: 'Test' }];
      mockedAxios.get.mockResolvedValue({ data: mockData });

      // Act: Ejecutamos la función que queremos probar
      const result = await empleadoService.obtenerEmpleados();

      // Assert: Verificamos que todo ocurrió como esperábamos
      expect(mockedAxios.get).toHaveBeenCalledWith('/api/empleados');
      expect(result).toEqual(mockData);
    });
  });

  describe('obtenerEmpleadoPorId', () => {
    it('debería llamar a GET /api/empleados/{id} y devolver un empleado', async () => {
      const empleadoId = 1;
      const mockData = { id: empleadoId, nombre: 'Test' };
      mockedAxios.get.mockResolvedValue({ data: mockData });

      const result = await empleadoService.obtenerEmpleadoPorId(empleadoId);

      expect(mockedAxios.get).toHaveBeenCalledWith(`/api/empleados/${empleadoId}`);
      expect(result).toEqual(mockData);
    });
  });

  describe('crearEmpleado', () => {
    it('debería llamar a POST /api/empleados con los datos correctos', async () => {
      const newEmpleado: EmpleadoRequest = { nombre: 'Nuevo', apellido: 'Empleado', email: 'nuevo@test.com', fechaNacimiento: '', puesto: '', salario: 0, numeroIdentificacion: '' };
      const mockResponse = { id: 1, ...newEmpleado, estado: 'ACTIVO', fechaContratacion: '' };
      mockedAxios.post.mockResolvedValue({ data: mockResponse });

      await empleadoService.crearEmpleado(newEmpleado);

      expect(mockedAxios.post).toHaveBeenCalledWith('/api/empleados', newEmpleado);
    });
  });

  describe('actualizarEmpleado', () => {
    it('debería llamar a PUT /api/empleados/{id} con los datos correctos', async () => {
      const empleadoId = 1;
      const updatedEmpleado: EmpleadoRequest = { nombre: 'Actualizado', apellido: 'Empleado', email: 'actualizado@test.com', fechaNacimiento: '', puesto: '', salario: 0, numeroIdentificacion: '' };
      mockedAxios.put.mockResolvedValue({ data: { id: empleadoId, ...updatedEmpleado } });

      await empleadoService.actualizarEmpleado(empleadoId, updatedEmpleado);

      expect(mockedAxios.put).toHaveBeenCalledWith(`/api/empleados/${empleadoId}`, updatedEmpleado);
    });
  });

  describe('eliminarEmpleado', () => {
    it('debería llamar a DELETE /api/empleados/{id}', async () => {
      const empleadoId = 1;
      mockedAxios.delete.mockResolvedValue({});

      await empleadoService.eliminarEmpleado(empleadoId);

      expect(mockedAxios.delete).toHaveBeenCalledWith(`/api/empleados/${empleadoId}`);
    });
  });
});
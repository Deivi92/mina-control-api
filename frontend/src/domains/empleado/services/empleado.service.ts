import axios from 'axios';
import type { Empleado, EmpleadoRequest } from '../types';

const API_URL = '/api/empleados'; // URL base para los endpoints de empleados

/**
 * Objeto de servicio para las llamadas a la API relacionadas con los empleados.
 */
export const empleadoService = {
  /**
   * Obtiene todos los empleados.
   * @returns Una promesa que se resuelve a un array de objetos Empleado.
   */
  obtenerEmpleados: async (): Promise<Empleado[]> => {
    const { data } = await axios.get<Empleado[]>(API_URL);
    return data;
  },

  /**
   * Obtiene un solo empleado por su ID.
   * @param id - El ID del empleado a obtener.
   * @returns Una promesa que se resuelve a un objeto Empleado.
   */
  obtenerEmpleadoPorId: async (id: number): Promise<Empleado> => {
    const { data } = await axios.get<Empleado>(`${API_URL}/${id}`);
    return data;
  },

  /**
   * Crea un nuevo empleado.
   * @param empleadoData - Los datos para el nuevo empleado.
   * @returns Una promesa que se resuelve al objeto Empleado recién creado.
   */
  crearEmpleado: async (empleadoData: EmpleadoRequest): Promise<Empleado> => {
    const { data } = await axios.post<Empleado>(API_URL, empleadoData);
    return data;
  },

  /**
   * Actualiza un empleado existente.
   * @param id - El ID del empleado a actualizar.
   * @param empleadoData - Los nuevos datos para el empleado.
   * @returns Una promesa que se resuelve al objeto Empleado actualizado.
   */
  actualizarEmpleado: async (id: number, empleadoData: EmpleadoRequest): Promise<Empleado> => {
    const { data } = await axios.put<Empleado>(`${API_URL}/${id}`, empleadoData);
    return data;
  },

  /**
   * Elimina un empleado por su ID (borrado lógico).
   * @param id - El ID del empleado a eliminar.
   * @returns Una promesa que se resuelve cuando la operación se completa.
   */
  eliminarEmpleado: async (id: number): Promise<void> => {
    await axios.delete(`${API_URL}/${id}`);
  },
};
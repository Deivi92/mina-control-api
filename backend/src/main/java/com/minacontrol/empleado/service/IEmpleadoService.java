package com.minacontrol.empleado.service;

import com.minacontrol.empleado.dto.request.EmpleadoRequest;
import com.minacontrol.empleado.dto.response.EmpleadoResponse;
import com.minacontrol.empleado.enums.EstadoEmpleado;

import java.util.List;

public interface IEmpleadoService {

    EmpleadoResponse crearEmpleado(EmpleadoRequest request);

    List<EmpleadoResponse> listarEmpleados(EstadoEmpleado estado, String cargo);

    EmpleadoResponse obtenerEmpleadoPorId(Long id);

    EmpleadoResponse actualizarEmpleado(Long id, EmpleadoRequest request);

    void eliminarEmpleado(Long id);

    EmpleadoResponse cambiarEstadoEmpleado(Long id, EstadoEmpleado nuevoEstado);

    EmpleadoResponse obtenerPerfilPersonal(String username);

    List<Long> obtenerIdsEmpleadosActivos();
}

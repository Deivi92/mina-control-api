package com.minacontrol.empleado.service;

import com.minacontrol.empleado.dto.EmpleadoRequestDTO;
import com.minacontrol.empleado.dto.EmpleadoResponseDTO;
import java.util.List;
import java.util.Optional;

public interface EmpleadoService {
    EmpleadoResponseDTO crearEmpleado(EmpleadoRequestDTO empleadoRequestDTO);
    Optional<EmpleadoResponseDTO> obtenerEmpleadoPorId(Long id);
    List<EmpleadoResponseDTO> obtenerTodosLosEmpleados();
    Optional<EmpleadoResponseDTO> actualizarEmpleado(Long id, EmpleadoRequestDTO empleadoRequestDTO);
    boolean eliminarEmpleado(Long id);
}

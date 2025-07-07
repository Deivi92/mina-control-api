package com.minacontrol.empleado.service.impl;

import com.minacontrol.empleado.dto.request.EmpleadoRequest;
import com.minacontrol.empleado.dto.response.EmpleadoResponse;
import com.minacontrol.empleado.entity.Empleado;
import com.minacontrol.empleado.enums.EstadoEmpleado;
import com.minacontrol.empleado.exception.EmpleadoAlreadyExistsException;
import com.minacontrol.empleado.exception.EmpleadoNotFoundException;
import com.minacontrol.empleado.mapper.EmpleadoMapper;
import com.minacontrol.empleado.repository.EmpleadoRepository;
import com.minacontrol.empleado.service.IEmpleadoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmpleadoServiceImpl implements IEmpleadoService {

    private final EmpleadoRepository empleadoRepository;
    private final EmpleadoMapper empleadoMapper;

    @Override
    @Transactional
    public EmpleadoResponse crearEmpleado(EmpleadoRequest request) {
        if (empleadoRepository.existsByNumeroIdentificacion(request.numeroIdentificacion())) {
            throw new EmpleadoAlreadyExistsException("Ya existe un empleado con el número de identificación " + request.numeroIdentificacion());
        }
        if (empleadoRepository.existsByEmail(request.email())) {
            throw new EmpleadoAlreadyExistsException("Ya existe un empleado con el email " + request.email());
        }

        Empleado empleado = empleadoMapper.toEntity(request);
        empleado.setEstado(EstadoEmpleado.ACTIVO); // Estado por defecto
        Empleado nuevoEmpleado = empleadoRepository.save(empleado);
        return empleadoMapper.toResponse(nuevoEmpleado);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmpleadoResponse> listarEmpleados(EstadoEmpleado estado, String cargo) {
        List<Empleado> empleados;
        if (estado != null && cargo != null) {
            empleados = empleadoRepository.findByEstadoAndCargoContainingIgnoreCase(estado, cargo);
        } else if (estado != null) {
            empleados = empleadoRepository.findByEstado(estado);
        } else if (cargo != null) {
            empleados = empleadoRepository.findByCargoContainingIgnoreCase(cargo);
        } else {
            empleados = empleadoRepository.findAll();
        }
        return empleados.stream()
                .map(empleadoMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public EmpleadoResponse obtenerEmpleadoPorId(Long id) {
        return empleadoRepository.findById(id)
                .map(empleadoMapper::toResponse)
                .orElseThrow(() -> new EmpleadoNotFoundException("Empleado no encontrado con ID: " + id));
    }

    @Override
    @Transactional
    public EmpleadoResponse actualizarEmpleado(Long id, EmpleadoRequest request) {
        Empleado empleado = empleadoRepository.findById(id)
                .orElseThrow(() -> new EmpleadoNotFoundException("Empleado no encontrado con id: " + id));

        // Validar que el email no exista en otro empleado diferente
        empleadoRepository.findByEmail(request.email()).ifPresent(empleadoExistente -> {
            if (!empleadoExistente.getId().equals(id)) {
                throw new EmpleadoAlreadyExistsException("El email " + request.email() + " ya está en uso por otro empleado.");
            }
        });

        empleadoMapper.updateEntityFromRequest(request, empleado);
        Empleado empleadoActualizado = empleadoRepository.save(empleado);
        return empleadoMapper.toResponse(empleadoActualizado);
    }

    @Override
    @Transactional
    public void eliminarEmpleado(Long id) {
        Empleado empleado = empleadoRepository.findById(id)
                .orElseThrow(() -> new EmpleadoNotFoundException("Empleado no encontrado con id: " + id));

        if (empleado.getEstado() == EstadoEmpleado.INACTIVO) {
            throw new com.minacontrol.empleado.exception.EmpleadoAlreadyInactiveException("El empleado con ID " + id + " ya está inactivo.");
        }

        empleado.setEstado(EstadoEmpleado.INACTIVO);
        empleadoRepository.save(empleado);
    }

    @Override
    @Transactional
    public EmpleadoResponse cambiarEstadoEmpleado(Long id, EstadoEmpleado nuevoEstado) {
        Empleado empleado = empleadoRepository.findById(id)
                .orElseThrow(() -> new EmpleadoNotFoundException("Empleado no encontrado con ID: " + id));
        empleado.setEstado(nuevoEstado);
        Empleado updatedEmpleado = empleadoRepository.save(empleado);
        return empleadoMapper.toResponse(updatedEmpleado);
    }

    @Override
    @Transactional(readOnly = true)
    public EmpleadoResponse obtenerPerfilPersonal(String username) {
        return empleadoRepository.findByEmail(username)
                .map(empleadoMapper::toResponse)
                .orElseThrow(() -> new EmpleadoNotFoundException("Empleado no encontrado para el usuario: " + username));
    }
}

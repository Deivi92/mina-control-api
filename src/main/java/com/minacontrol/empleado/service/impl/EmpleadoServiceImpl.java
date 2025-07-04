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
            throw new EmpleadoAlreadyExistsException("El número de identificación ya existe: " + request.numeroIdentificacion());
        }
        if (empleadoRepository.existsByEmail(request.email())) {
            throw new EmpleadoAlreadyExistsException("El email ya existe: " + request.email());
        }

        Empleado empleado = empleadoMapper.toEntity(request);
        Empleado savedEmpleado = empleadoRepository.save(empleado);
        return empleadoMapper.toResponse(savedEmpleado);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmpleadoResponse> listarEmpleados(EstadoEmpleado estado, String cargo) {
        List<Empleado> empleados = empleadoRepository.findAll(); // Simplificado, se puede mejorar con Criteria API
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
                .orElseThrow(() -> new EmpleadoNotFoundException("Empleado no encontrado con ID: " + id));

        empleadoMapper.updateEntityFromRequest(request, empleado);
        Empleado updatedEmpleado = empleadoRepository.save(empleado);
        return empleadoMapper.toResponse(updatedEmpleado);
    }
}

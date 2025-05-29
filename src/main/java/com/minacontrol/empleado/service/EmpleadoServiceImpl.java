package com.minacontrol.empleado.service;

import com.minacontrol.empleado.dto.EmpleadoRequestDTO;
import com.minacontrol.empleado.dto.EmpleadoResponseDTO;
import com.minacontrol.exception.ResourceNotFoundException;
import com.minacontrol.empleado.model.Empleado;
import com.minacontrol.empleado.repository.EmpleadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service("empleadoService") // Se añade un calificador único
public class EmpleadoServiceImpl implements EmpleadoService {

    private final EmpleadoRepository empleadoRepository;

    @Autowired
    public EmpleadoServiceImpl(@Qualifier("empleadoRepository") EmpleadoRepository empleadoRepository) {
        this.empleadoRepository = empleadoRepository;
    }

    @Override
    public EmpleadoResponseDTO crearEmpleado(EmpleadoRequestDTO empleadoRequestDTO) {
        Empleado empleado = new Empleado();
        // Mapeo de DTO a Entidad
        empleado.setNumeroDocumento(empleadoRequestDTO.getNumeroDocumento());
        empleado.setNombres(empleadoRequestDTO.getNombre()); // Ajustado a nombres
        empleado.setApellidos(empleadoRequestDTO.getApellido()); // Ajustado a apellidos
        empleado.setFechaNacimiento(empleadoRequestDTO.getFechaNacimiento());
        empleado.setCargo(empleadoRequestDTO.getCargo());
        empleado.setFechaIngreso(empleadoRequestDTO.getFechaContratacion()); // Ajustado a fechaIngreso
        empleado.setSalarioBase(empleadoRequestDTO.getSalario()); // Ajustado a salarioBase
        empleado.setEstado(empleadoRequestDTO.getEstado());
        empleado.setEmail(empleadoRequestDTO.getEmail());
        empleado.setTelefono(empleadoRequestDTO.getTelefono());

        Empleado nuevoEmpleado = empleadoRepository.save(empleado);
        return convertirAEntidadResponseDTO(nuevoEmpleado);
    }

    @Override
    public Optional<EmpleadoResponseDTO> obtenerEmpleadoPorId(Long id) {
        return empleadoRepository.findById(id)
                .map(this::convertirAEntidadResponseDTO);
    }

    @Override
    public List<EmpleadoResponseDTO> obtenerTodosLosEmpleados() {
        return empleadoRepository.findAll().stream()
                .map(this::convertirAEntidadResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<EmpleadoResponseDTO> actualizarEmpleado(Long id, EmpleadoRequestDTO empleadoRequestDTO) {
        return empleadoRepository.findById(id)
                .map(empleadoExistente -> {
                    // Mapeo de DTO a Entidad para actualización
                    empleadoExistente.setNumeroDocumento(empleadoRequestDTO.getNumeroDocumento());
                    empleadoExistente.setNombres(empleadoRequestDTO.getNombre()); // Ajustado
                    empleadoExistente.setApellidos(empleadoRequestDTO.getApellido()); // Ajustado
                    empleadoExistente.setFechaNacimiento(empleadoRequestDTO.getFechaNacimiento());
                    empleadoExistente.setCargo(empleadoRequestDTO.getCargo());
                    empleadoExistente.setFechaIngreso(empleadoRequestDTO.getFechaContratacion()); // Ajustado
                    empleadoExistente.setSalarioBase(empleadoRequestDTO.getSalario()); // Ajustado
                    empleadoExistente.setEstado(empleadoRequestDTO.getEstado());
                    empleadoExistente.setEmail(empleadoRequestDTO.getEmail());
                    empleadoExistente.setTelefono(empleadoRequestDTO.getTelefono());
                    Empleado empleadoActualizado = empleadoRepository.save(empleadoExistente);
                    return convertirAEntidadResponseDTO(empleadoActualizado);
                });
    }

    @Override
    public boolean eliminarEmpleado(Long id) {
        if (empleadoRepository.findById(id).isPresent()) {
            empleadoRepository.deleteById(id);
            return true;
        }
        return false;
    }

    private EmpleadoResponseDTO convertirAEntidadResponseDTO(Empleado empleado) {
        // Mapeo de Entidad a DTO de respuesta
        return new EmpleadoResponseDTO(
                empleado.getId(),
                empleado.getNumeroDocumento(),
                empleado.getNombres(), // Ajustado
                empleado.getApellidos(), // Ajustado
                empleado.getFechaNacimiento(),
                empleado.getCargo(),
                empleado.getFechaIngreso(), // Ajustado
                empleado.getSalarioBase(), // Ajustado
                empleado.getEstado(),
                empleado.getEmail(),
                empleado.getTelefono()
        );
    }
}

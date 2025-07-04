package com.minacontrol.empleado.mapper;

import com.minacontrol.empleado.dto.request.EmpleadoRequest;
import com.minacontrol.empleado.dto.response.EmpleadoResponse;
import com.minacontrol.empleado.entity.Empleado;
import org.springframework.stereotype.Component;

@Component
public class EmpleadoMapper {

    public Empleado toEntity(EmpleadoRequest dto) {
        if (dto == null) {
            return null;
        }
        return Empleado.builder()
                .nombres(dto.nombres())
                .apellidos(dto.apellidos())
                .numeroIdentificacion(dto.numeroIdentificacion())
                .email(dto.email())
                .telefono(dto.telefono())
                .cargo(dto.cargo())
                .fechaContratacion(dto.fechaContratacion())
                .salarioBase(dto.salarioBase())
                .rolSistema(dto.rolSistema())
                .build();
    }

    public EmpleadoResponse toResponse(Empleado entity) {
        if (entity == null) {
            return null;
        }
        return new EmpleadoResponse(
                entity.getId(),
                entity.getNombres(),
                entity.getApellidos(),
                entity.getNumeroIdentificacion(),
                entity.getEmail(),
                entity.getTelefono(),
                entity.getCargo(),
                entity.getFechaContratacion(),
                entity.getSalarioBase(),
                entity.getEstado(),
                entity.getRolSistema(),
                entity.getTieneUsuario(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    public void updateEntityFromRequest(EmpleadoRequest request, Empleado entity) {
        if (request == null || entity == null) {
            return;
        }
        entity.setNombres(request.nombres());
        entity.setApellidos(request.apellidos());
        entity.setNumeroIdentificacion(request.numeroIdentificacion());
        entity.setEmail(request.email());
        entity.setTelefono(request.telefono());
        entity.setCargo(request.cargo());
        entity.setFechaContratacion(request.fechaContratacion());
        entity.setSalarioBase(request.salarioBase());
        entity.setRolSistema(request.rolSistema());
    }
}
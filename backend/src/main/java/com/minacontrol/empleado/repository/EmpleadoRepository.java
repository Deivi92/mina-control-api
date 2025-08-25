package com.minacontrol.empleado.repository;

import com.minacontrol.empleado.entity.Empleado;
import com.minacontrol.empleado.enums.EstadoEmpleado;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EmpleadoRepository extends JpaRepository<Empleado, Long> {
    

    Optional<Empleado> findByEmail(String email);

    boolean existsByNumeroIdentificacion(String numeroIdentificacion);

    boolean existsByEmail(String email);

    List<Empleado> findByEstadoAndCargoContainingIgnoreCase(EstadoEmpleado estado, String cargo);

    List<Empleado> findByEstado(EstadoEmpleado estado);

    List<Empleado> findByCargoContainingIgnoreCase(String cargo);
}
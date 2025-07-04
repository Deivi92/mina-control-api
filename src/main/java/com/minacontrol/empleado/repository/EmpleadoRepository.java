package com.minacontrol.empleado.repository;

import com.minacontrol.empleado.entity.Empleado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmpleadoRepository extends JpaRepository<Empleado, Long> {
    Optional<Empleado> findByEmail(String email);
    Boolean existsByNumeroIdentificacion(String numeroIdentificacion);
    Boolean existsByEmail(String email);
}

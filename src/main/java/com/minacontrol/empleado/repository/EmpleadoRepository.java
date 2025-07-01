package com.minacontrol.empleado.repository;

import com.minacontrol.empleado.model.Empleado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * TODO: PLACEHOLDER - Esta interfaz es un placeholder temporal para permitir la compilación del dominio de autenticación.
 * Será reemplazada por la implementación completa del repositorio de Empleado cuando se desarrolle el dominio de Empleados.
 */
@Repository
public interface EmpleadoRepository extends JpaRepository<Empleado, Long> {
    Optional<Empleado> findByEmail(String email);
}

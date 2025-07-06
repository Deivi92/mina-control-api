
package com.minacontrol.turnos.repository;

import com.minacontrol.turnos.entity.RegistroAsistencia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface RegistroAsistenciaRepository extends JpaRepository<RegistroAsistencia, Long> {
    Optional<RegistroAsistencia> findByEmpleadoIdAndFecha(Long empleadoId, LocalDate fecha);
}

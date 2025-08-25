
package com.minacontrol.turnos.repository;

import com.minacontrol.turnos.entity.AsignacionTurno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface AsignacionTurnoRepository extends JpaRepository<AsignacionTurno, Long> {
    boolean existsByTipoTurnoId(Long tipoTurnoId);

    @Query("SELECT a FROM AsignacionTurno a WHERE a.empleadoId = :empleadoId AND a.fechaFin >= :fechaInicio AND a.fechaInicio <= :fechaFin")
    List<AsignacionTurno> findConflictosDeHorario(Long empleadoId, LocalDate fechaInicio, LocalDate fechaFin);
}

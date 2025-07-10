package com.minacontrol.produccion.repository;

import com.minacontrol.produccion.entity.RegistroProduccion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface RegistroProduccionRepository extends JpaRepository<RegistroProduccion, Long> {

    boolean existsByEmpleadoIdAndTipoTurnoIdAndFechaRegistro(Long empleadoId, Long tipoTurnoId, LocalDate fechaRegistro);

    List<RegistroProduccion> findByEmpleadoId(Long empleadoId);

    List<RegistroProduccion> findByFechaRegistroBetween(LocalDate fechaInicio, LocalDate fechaFin);

    List<RegistroProduccion> findByEmpleadoIdAndFechaRegistroBetween(Long empleadoId, LocalDate fechaInicio, LocalDate fechaFin);
}

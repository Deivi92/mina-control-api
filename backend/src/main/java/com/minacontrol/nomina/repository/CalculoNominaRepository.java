package com.minacontrol.nomina.repository;

import com.minacontrol.nomina.entity.CalculoNomina;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface CalculoNominaRepository extends JpaRepository<CalculoNomina, Long> {
    List<CalculoNomina> findByPeriodoId(Long periodoId);

    @Query("SELECT c FROM CalculoNomina c WHERE c.empleadoId = :empleadoId AND c.periodo.fechaInicio >= :fechaInicio AND c.periodo.fechaFin <= :fechaFin")
    List<CalculoNomina> findByEmpleadoIdAndFechas(@Param("empleadoId") Long empleadoId, @Param("fechaInicio") LocalDate fechaInicio, @Param("fechaFin") LocalDate fechaFin);
}

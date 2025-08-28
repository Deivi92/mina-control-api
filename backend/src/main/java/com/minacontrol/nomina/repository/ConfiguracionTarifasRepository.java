package com.minacontrol.nomina.repository;

import com.minacontrol.nomina.entity.ConfiguracionTarifas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ConfiguracionTarifasRepository extends JpaRepository<ConfiguracionTarifas, Long> {

    @Query("SELECT ct FROM ConfiguracionTarifas ct WHERE ct.fechaVigencia <= :fecha AND ct.moneda = :moneda ORDER BY ct.fechaVigencia DESC")
    List<ConfiguracionTarifas> findConfiguracionesVigentes(@Param("fecha") LocalDate fecha, @Param("moneda") String moneda);

    @Query("SELECT ct FROM ConfiguracionTarifas ct WHERE ct.fechaVigencia <= :fecha AND ct.moneda = :moneda ORDER BY ct.fechaVigencia DESC")
    Optional<ConfiguracionTarifas> findConfiguracionVigente(@Param("fecha") LocalDate fecha, @Param("moneda") String moneda);
}
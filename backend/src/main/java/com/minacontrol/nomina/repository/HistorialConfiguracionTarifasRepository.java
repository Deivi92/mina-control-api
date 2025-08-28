package com.minacontrol.nomina.repository;

import com.minacontrol.nomina.entity.HistorialConfiguracionTarifas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistorialConfiguracionTarifasRepository extends JpaRepository<HistorialConfiguracionTarifas, Long> {
    List<HistorialConfiguracionTarifas> findByOrderByFechaCreacionDesc();
}
package com.minacontrol.logistica.repository;

import com.minacontrol.logistica.entity.Despacho;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface DespachoRepository extends JpaRepository<Despacho, Long>, JpaSpecificationExecutor<Despacho> {
    List<Despacho> findByFechaProgramadaBetween(LocalDate fechaInicio, LocalDate fechaFin);
}

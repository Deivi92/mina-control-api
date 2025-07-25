package com.minacontrol.reportes.repository;

import com.minacontrol.reportes.entity.ReporteGenerado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReporteGeneradoRepository extends JpaRepository<ReporteGenerado, Long> {
}

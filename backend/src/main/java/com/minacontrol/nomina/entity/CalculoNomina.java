package com.minacontrol.nomina.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "calculos_nomina")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CalculoNomina {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "periodo_id", nullable = false)
    private PeriodoNomina periodo;

    @Column(name = "empleado_id", nullable = false)
    private Long empleadoId;

    @Column(name = "salario_base", nullable = false)
    private BigDecimal salarioBase;

    @Column(name = "bonificaciones", nullable = false)
    private BigDecimal bonificaciones;

    @Column(name = "deducciones", nullable = false)
    private BigDecimal deducciones;

    @Column(name = "total_bruto", nullable = false)
    private BigDecimal totalBruto;

    @Column(name = "total_neto", nullable = false)
    private BigDecimal totalNeto;

    @Column(columnDefinition = "TEXT")
    private String observaciones;
}

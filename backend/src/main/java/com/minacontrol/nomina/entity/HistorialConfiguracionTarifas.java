package com.minacontrol.nomina.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "historial_configuracion_tarifas")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HistorialConfiguracionTarifas {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tarifa_por_hora", precision = 10, scale = 2, nullable = false)
    private BigDecimal tarifaPorHora;

    @Column(name = "bono_por_tonelada", precision = 10, scale = 2)
    private BigDecimal bonoPorTonelada;

    @Column(name = "moneda", length = 3, nullable = false)
    private String moneda;

    @Column(name = "fecha_vigencia", nullable = false)
    private java.time.LocalDate fechaVigencia;

    @Column(name = "creado_por", nullable = false)
    private Long creadoPor;

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion;
    
    @Column(name = "accion", nullable = false)
    private String accion; // Puede ser "CREADO", "ACTUALIZADO", "ELIMINADO"
}
package com.minacontrol.nomina.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "configuracion_tarifas")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConfiguracionTarifas {

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
    private LocalDate fechaVigencia;

    @Column(name = "creado_por", nullable = false)
    private Long creadoPor;

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDate fechaCreacion;
}
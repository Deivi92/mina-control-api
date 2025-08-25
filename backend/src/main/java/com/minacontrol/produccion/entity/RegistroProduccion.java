package com.minacontrol.produccion.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "registros_produccion")
public class RegistroProduccion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "empleado_id", nullable = false)
    private Long empleadoId;

    @NotNull
    @Column(name = "tipo_turno_id", nullable = false)
    private Long tipoTurnoId;

    @NotNull
    @Column(name = "fecha_registro", nullable = false)
    private LocalDate fechaRegistro;

    @NotNull
    @Positive
    @Column(name = "cantidad_extraida_toneladas", nullable = false, precision = 10, scale = 3)
    private BigDecimal cantidadExtraidaToneladas;

    @NotBlank
    @Column(name = "ubicacion_extraccion", nullable = false, length = 100)
    private String ubicacionExtraccion;

    @Column(columnDefinition = "TEXT")
    private String observaciones;

    @Builder.Default
    @Column(nullable = false)
    private boolean validado = false;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Instant updatedAt;
}

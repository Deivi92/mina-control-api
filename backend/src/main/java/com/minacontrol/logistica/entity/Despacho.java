package com.minacontrol.logistica.entity;

import com.minacontrol.logistica.domain.EstadoDespacho;
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
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "despachos")
public class Despacho {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "numero_despacho", unique = true, nullable = false)
    private String numeroDespacho;

    @NotBlank
    @Column(name = "nombre_conductor", nullable = false)
    private String nombreConductor;

    @NotBlank
    @Column(name = "placa_vehiculo", nullable = false)
    private String placaVehiculo;

    @NotNull
    @Positive
    @Column(name = "cantidad_despachada_toneladas", nullable = false)
    private BigDecimal cantidadDespachadaToneladas;

    @NotBlank
    @Column(nullable = false)
    private String destino;

    @NotNull
    @Column(name = "fecha_programada", nullable = false)
    private LocalDate fechaProgramada;

    @Column(name = "fecha_salida")
    private LocalDateTime fechaSalida;

    @Column(name = "fecha_entrega")
    private LocalDateTime fechaEntrega;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoDespacho estado;

    @Column(columnDefinition = "TEXT")
    private String observaciones;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Instant updatedAt;
}

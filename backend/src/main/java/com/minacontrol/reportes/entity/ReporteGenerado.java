package com.minacontrol.reportes.entity;

import com.minacontrol.reportes.enums.FormatoReporte;
import com.minacontrol.reportes.enums.TipoReporte;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "reportes_generados")
public class ReporteGenerado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_reporte", nullable = false)
    private TipoReporte tipoReporte;

    @NotBlank
    @Column(name = "nombre_reporte", nullable = false)
    private String nombreReporte;

    @Column(name = "parametros_json", columnDefinition = "TEXT")
    private String parametrosJson;

    @NotNull
    @Column(name = "fecha_generacion", nullable = false)
    private LocalDateTime fechaGeneracion;

    @Column(name = "fecha_inicio_datos")
    private LocalDate fechaInicioDatos;

    @Column(name = "fecha_fin_datos")
    private LocalDate fechaFinDatos;

    @NotBlank
    @Column(name = "ruta_archivo", nullable = false)
    private String rutaArchivo;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FormatoReporte formato;

    @NotNull
    @Column(name = "generado_por", nullable = false)
    private Long generadoPor;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}

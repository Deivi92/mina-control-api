package com.minacontrol.nomina.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "comprobantes_pago")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ComprobantePago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "calculo_id", nullable = false, unique = true)
    private CalculoNomina calculo;

    @Column(name = "numero_comprobante", nullable = false, unique = true)
    private String numeroComprobante;

    @Column(name = "fecha_emision", nullable = false)
    private LocalDateTime fechaEmision;

    @Column(name = "ruta_archivo_pdf", nullable = false)
    private String rutaArchivoPdf;
}

package com.minacontrol.empleado.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * TODO: PLACEHOLDER - Esta clase es un placeholder temporal para permitir la compilación del dominio de autenticación.
 * Será reemplazada por la implementación completa de la entidad Empleado cuando se desarrolle el dominio de Empleados.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "empleados")
public class Empleado {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private Boolean tieneUsuario = false;

    @Version
    private Integer version;

    // Getters y Setters (Lombok @Data se encarga de esto)
}

package com.minacontrol.autenticacion.model;

import com.minacontrol.empleado.entity.Empleado;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "empleado_id", referencedColumnName = "id", nullable = false)
    private Empleado empleado;

    @Column(name = "reset_token")
    private String resetToken;

    @Column(name = "reset_token_expiry")
    private Long resetTokenExpiry;

    @Version
    private Integer version;

    // Constructor para el registro inicial
    public Usuario(String email, String password, Empleado empleado) {
        this.email = email;
        this.password = password;
        this.empleado = empleado;
        this.version = 0;
    }
}

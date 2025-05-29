package com.minacontrol.empleado.dto;

import com.minacontrol.empleado.model.EstadoEmpleado;
import java.math.BigDecimal;
import java.time.LocalDate;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.PastOrPresent;

public class EmpleadoRequestDTO {

    @NotBlank(message = "El número de documento no puede estar vacío")
    private String numeroDocumento;

    @NotBlank(message = "El nombre no puede estar vacío")
    private String nombre;

    @NotBlank(message = "El apellido no puede estar vacío")
    private String apellido;

    @NotNull(message = "La fecha de nacimiento no puede ser nula")
    @Past(message = "La fecha de nacimiento debe ser una fecha pasada")
    private LocalDate fechaNacimiento;

    @NotBlank(message = "El cargo no puede estar vacío")
    private String cargo;

    @NotNull(message = "La fecha de contratación no puede ser nula")
    @PastOrPresent(message = "La fecha de contratación debe ser una fecha pasada o presente")
    private LocalDate fechaContratacion; // Cambiado de fechaIngreso para consistencia con el JSON de prueba

    @NotNull(message = "El salario no puede ser nulo")
    @DecimalMin(value = "0.0", inclusive = false, message = "El salario debe ser mayor que cero")
    private BigDecimal salario; // Cambiado de salarioBase para consistencia con el JSON de prueba

    @NotNull(message = "El estado no puede ser nulo")
    private EstadoEmpleado estado;

    @Email(message = "El formato del email no es válido")
    private String email; 

    private String telefono; 


    public EmpleadoRequestDTO() {
    }

    // Constructor actualizado
    public EmpleadoRequestDTO(String numeroDocumento, String nombre, String apellido, LocalDate fechaNacimiento, String cargo, LocalDate fechaContratacion, BigDecimal salario, EstadoEmpleado estado, String email, String telefono) {
        this.numeroDocumento = numeroDocumento;
        this.nombre = nombre;
        this.apellido = apellido;
        this.fechaNacimiento = fechaNacimiento;
        this.cargo = cargo;
        this.fechaContratacion = fechaContratacion;
        this.salario = salario;
        this.estado = estado;
        this.email = email;
        this.telefono = telefono;
    }

    // Getters y Setters
    public String getNumeroDocumento() { 
        return numeroDocumento;
    }

    public void setNumeroDocumento(String numeroDocumento) { 
        this.numeroDocumento = numeroDocumento;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public LocalDate getFechaNacimiento() { 
        return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) { 
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public LocalDate getFechaContratacion() {
        return fechaContratacion;
    }

    public void setFechaContratacion(LocalDate fechaContratacion) {
        this.fechaContratacion = fechaContratacion;
    }

    public BigDecimal getSalario() {
        return salario;
    }

    public void setSalario(BigDecimal salario) {
        this.salario = salario;
    }

    public EstadoEmpleado getEstado() {
        return estado;
    }

    public void setEstado(EstadoEmpleado estado) {
        this.estado = estado;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
}

package com.minacontrol.empleado.model;

import java.math.BigDecimal;
import java.time.LocalDate;

// import javax.validation.constraints.Email; // Comentado porque no se usa directamente aquí
// import javax.validation.constraints.NotBlank; // Comentado
// import javax.validation.constraints.NotNull; // Comentado
// import javax.validation.constraints.PastOrPresent; // Comentado
// import javax.validation.constraints.Positive; // Comentado
// import javax.validation.constraints.Size; // Comentado


public class Empleado {

    private Long id;
    // @NotBlank(message = "El número de documento no puede estar vacío")
    // @Size(min = 5, max = 20, message = "El número de documento debe tener entre 5 y 20 caracteres")
    private String numeroDocumento;

    // @NotBlank(message = "Los nombres no pueden estar vacíos")
    // @Size(max = 100, message = "Los nombres no deben exceder los 100 caracteres")
    private String nombres;

    // @NotBlank(message = "Los apellidos no pueden estar vacíos")
    // @Size(max = 100, message = "Los apellidos no deben exceder los 100 caracteres")
    private String apellidos;

    // @NotNull(message = "La fecha de nacimiento no puede ser nula")
    // @PastOrPresent(message = "La fecha de nacimiento debe ser en el pasado o presente")
    private LocalDate fechaNacimiento;

    // @NotBlank(message = "El cargo no puede estar vacío")
    // @Size(max = 50, message = "El cargo no debe exceder los 50 caracteres")
    private String cargo;

    // @NotNull(message = "La fecha de ingreso no puede ser nula")
    // @PastOrPresent(message = "La fecha de ingreso debe ser en el pasado o presente")
    private LocalDate fechaIngreso;

    // @NotNull(message = "El salario base no puede ser nulo")
    // @Positive(message = "El salario base debe ser un valor positivo")
    private BigDecimal salarioBase;

    // @NotNull(message = "El estado no puede ser nulo")
    private EstadoEmpleado estado; // Usando el Enum directamente

    // @Email(message = "El formato del email no es válido")
    // @Size(max = 100, message = "El email no debe exceder los 100 caracteres")
    private String email;

    // @Size(max = 20, message = "El teléfono no debe exceder los 20 caracteres")
    private String telefono;

    // Constructor por defecto
    public Empleado() {
    }

    // Constructor con todos los argumentos (útil para creación y pruebas)
    public Empleado(Long id, String numeroDocumento, String nombres, String apellidos, LocalDate fechaNacimiento, String cargo, LocalDate fechaIngreso, BigDecimal salarioBase, EstadoEmpleado estado, String email, String telefono) {
        this.id = id;
        this.numeroDocumento = numeroDocumento;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.fechaNacimiento = fechaNacimiento;
        this.cargo = cargo;
        this.fechaIngreso = fechaIngreso;
        this.salarioBase = salarioBase;
        this.estado = estado;
        this.email = email;
        this.telefono = telefono;
    }


    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumeroDocumento() {
        return numeroDocumento;
    }

    public void setNumeroDocumento(String numeroDocumento) {
        this.numeroDocumento = numeroDocumento;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
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

    public LocalDate getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(LocalDate fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public BigDecimal getSalarioBase() {
        return salarioBase;
    }

    public void setSalarioBase(BigDecimal salarioBase) {
        this.salarioBase = salarioBase;
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

    @Override
    public String toString() {
        return "Empleado{" +
                "id=" + id +
                ", numeroDocumento='" + numeroDocumento + "'" +
                ", nombres='" + nombres + "'" +
                ", apellidos='" + apellidos + "'" +
                ", estado=" + estado +
                '}';
    }
}

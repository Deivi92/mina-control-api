# CU-EMP-001: Crear Empleado

## 1. Dominio
Empleados

## 2. Descripción
Permite a un usuario administrador registrar un nuevo empleado en el sistema, incluyendo sus datos personales, laborales y de contacto.

## 3. Actor(es)
*   Usuario Administrador

## 4. Precondiciones
*   El usuario debe tener el rol de `ADMINISTRADOR`.
*   No debe existir otro empleado con el mismo `numeroIdentificacion` o `email`.

## 5. Seguridad y Autorización
*   **Autenticación Requerida:** Sí
*   **Roles Permitidos:** `ADMINISTRADOR`
*   **Condiciones de Propiedad:** N/A

## 6. Flujo Principal (Happy Path)
1.  El Usuario Administrador envía una solicitud `POST` a `/api/empleados` con un `EmpleadoCreateDTO` que contiene los datos del nuevo empleado.
2.  El sistema valida el formato del `EmpleadoCreateDTO` (campos requeridos, tipos de datos).
3.  El sistema verifica que el `numeroIdentificacion` no exista previamente en la base de datos.
4.  El sistema verifica que el `email` no exista previamente en la base de datos.
5.  El sistema convierte el `EmpleadoCreateDTO` a una entidad `Empleado`, asignando `createdAt`, `updatedAt`, `estado` (por defecto `ACTIVO`) y `rolSistema` (según el DTO).
6.  El sistema persiste la nueva entidad `Empleado` en la base de datos.
7.  El sistema devuelve una respuesta `201 Created` con el `EmpleadoDTO` del empleado recién creado.

## 7. Flujos Alternativos
*   N/A

## 8. Validaciones

### 8.1. Validaciones de Formato/Sintaxis (Capa de DTO)
*   `nombres`: `@NotBlank`, `@Size(max=100)`
*   `apellidos`: `@NotBlank`, `@Size(max=100)`
*   `numeroIdentificacion`: `@NotBlank`, `@Size(max=20)`
*   `email`: `@NotBlank`, `@Email`, `@Size(max=100)`
*   `telefono`: `@Size(max=15)`
*   `cargo`: `@NotBlank`, `@Size(max=50)`
*   `fechaContratacion`: `@NotNull`, `@PastOrPresent`
*   `salarioBase`: `@NotNull`, `@DecimalMin("0.01")`
*   `rolSistema`: `@NotNull`, debe ser uno de los valores del enum `RolSistema` (EMPLEADO, ADMINISTRADOR, GERENTE).

### 8.2. Validaciones de Reglas de Negocio (Capa de Servicio)
*   El `numeroIdentificacion` debe ser único en la base de datos.
*   El `email` debe ser único en la base de datos.
*   La `fechaContratacion` no puede ser una fecha futura.

## 9. Flujos de Excepción

*   **9.1. Datos de Entrada Inválidos**
    *   **Condición:** El `EmpleadoCreateDTO` no cumple con las validaciones de formato/sintaxis.
    *   **Excepción Java:** `MethodArgumentNotValidException` (manejada por `@ControllerAdvice`)
    *   **Respuesta HTTP:** `400 Bad Request`
    *   **Cuerpo de Respuesta (ErrorResponseDTO):**
        ```json
        {
            "timestamp": "...",
            "status": 400,
            "error": "Bad Request",
            "message": "Mensaje de validación detallado.",
            "path": "/api/empleados"
        }
        ```

*   **9.2. Empleado Ya Existe (Número de Identificación o Email Duplicado)**
    *   **Condición:** Ya existe un empleado con el `numeroIdentificacion` o `email` proporcionado.
    *   **Excepción Java:** `EmpleadoAlreadyExistsException` (personalizada)
    *   **Respuesta HTTP:** `409 Conflict`
    *   **Cuerpo de Respuesta (ErrorResponseDTO):**
        ```json
        {
            "timestamp": "...",
            "status": 409,
            "error": "Conflict",
            "message": "Ya existe un empleado con ese número de identificación/email.",
            "path": "/api/empleados"
        }
        ```

*   **9.3. Error Interno del Servidor**
    *   **Condición:** Cualquier otro error inesperado durante el procesamiento (ej. problema de conexión a la BD).
    *   **Excepción Java:** `RuntimeException` (o subclase específica, manejada por `@ControllerAdvice`)
    *   **Respuesta HTTP:** `500 Internal Server Error`
    *   **Cuerpo de Respuesta (ErrorResponseDTO):**
        ```json
        {
            "timestamp": "...",
            "status": 500,
            "error": "Internal Server Error",
            "message": "Error interno del servidor.",
            "path": "/api/empleados"
        }
        ```

## 10. Postcondiciones
*   Se crea una nueva entrada en la tabla `empleados` en la base de datos.
*   El nuevo empleado tiene el estado `ACTIVO` por defecto.

## 11. DTOs Involucrados
*   **Request DTO:** `EmpleadoCreateDTO`
*   **Response DTO:** `EmpleadoDTO`

## 12. Endpoint REST
*   **Método:** `POST`
*   **URL:** `/api/empleados`

## 13. Referencias
*   **Diagrama de Secuencia:** `docs/diagrams/empleados/sequence_crear_empleado.puml`
*   **Diagrama de Clases:** `docs/diagrams/general/class_diagram_completo.puml`
*   **Diagrama ER:** `docs/diagrams/general/er_diagram_completo.puml`

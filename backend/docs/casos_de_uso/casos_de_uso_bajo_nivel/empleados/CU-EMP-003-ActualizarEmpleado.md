# CU-EMP-003: Actualizar Empleado

## 1. Dominio
Empleados

## 2. Descripción
Permite a un usuario administrador actualizar la información de un empleado existente en el sistema.

## 3. Actor(es)
*   Usuario Administrador

## 4. Precondiciones
*   El usuario debe tener el rol de `ADMINISTRADOR`.
*   El empleado a actualizar debe existir en la base de datos.

## 5. Seguridad y Autorización
*   **Autenticación Requerida:** Sí
*   **Roles Permitidos:** `ADMINISTRADOR`
*   **Condiciones de Propiedad:** N/A

## 6. Flujo Principal (Happy Path)
1.  El Usuario Administrador envía una solicitud `PUT` a `/api/empleados/{id}` con un `EmpleadoUpdateDTO` que contiene los datos a actualizar.
2.  El sistema valida el formato del `EmpleadoUpdateDTO` (campos requeridos, tipos de datos).
3.  El sistema busca el empleado por el `id` proporcionado en la URL.
4.  Si el empleado existe, el sistema actualiza los campos del empleado con los valores proporcionados en el DTO.
5.  El sistema persiste los cambios en la base de datos.
6.  El sistema devuelve una respuesta `200 OK` con el `EmpleadoDTO` del empleado actualizado.

## 7. Flujos Alternativos
*   N/A

## 8. Validaciones

### 8.1. Validaciones de Formato/Sintaxis (Capa de DTO)
*   `nombres`: `@Size(max=100)`
*   `apellidos`: `@Size(max=100)`
*   `telefono`: `@Size(max=15)`
*   `cargo`: `@Size(max=50)`
*   `fechaContratacion`: `@PastOrPresent`
*   `salarioBase`: `@DecimalMin("0.01")`
*   `rolSistema`: Debe ser uno de los valores del enum `RolSistema` (EMPLEADO, ADMINISTRADOR, GERENTE).

### 8.2. Validaciones de Reglas de Negocio (Capa de Servicio)
*   El `id` del empleado debe corresponder a un empleado existente.
*   Si se actualiza el `email` o `numeroIdentificacion`, estos deben seguir siendo únicos (no pueden duplicar los de otro empleado existente).

## 9. Flujos de Excepción

*   **9.1. Datos de Entrada Inválidos**
    *   **Condición:** El `EmpleadoUpdateDTO` no cumple con las validaciones de formato/sintaxis.
    *   **Excepción Java:** `MethodArgumentNotValidException` (manejada por `@ControllerAdvice`)
    *   **Respuesta HTTP:** `400 Bad Request`
    *   **Cuerpo de Respuesta (ErrorResponseDTO):**
        ```json
        {
            "timestamp": "...",
            "status": 400,
            "error": "Bad Request",
            "message": "Mensaje de validación detallado.",
            "path": "/api/empleados/{id}"
        }
        ```

*   **9.2. Empleado No Encontrado**
    *   **Condición:** El `id` proporcionado no corresponde a ningún empleado existente.
    *   **Excepción Java:** `EmpleadoNotFoundException` (personalizada)
    *   **Respuesta HTTP:** `404 Not Found`
    *   **Cuerpo de Respuesta (ErrorResponseDTO):**
        ```json
        {
            "timestamp": "...",
            "status": 404,
            "error": "Not Found",
            "message": "Empleado con ID {id} no encontrado.",
            "path": "/api/empleados/{id}"
        }
        ```

*   **9.3. Conflicto de Datos (Email o Número de Identificación Duplicado)**
    *   **Condición:** Se intenta actualizar el `email` o `numeroIdentificacion` a un valor que ya pertenece a otro empleado.
    *   **Excepción Java:** `EmpleadoAlreadyExistsException` (personalizada)
    *   **Respuesta HTTP:** `409 Conflict`
    *   **Cuerpo de Respuesta (ErrorResponseDTO):**
        ```json
        {
            "timestamp": "...",
            "status": 409,
            "error": "Conflict",
            "message": "El email/número de identificación ya está en uso por otro empleado.",
            "path": "/api/empleados/{id}"
        }
        ```

*   **9.4. Error Interno del Servidor**
    *   **Condición:** Cualquier otro error inesperado durante el procesamiento.
    *   **Excepción Java:** `RuntimeException` (o subclase específica, manejada por `@ControllerAdvice`)
    *   **Respuesta HTTP:** `500 Internal Server Error`
    *   **Cuerpo de Respuesta (ErrorResponseDTO):**
        ```json
        {
            "timestamp": "...",
            "status": 500,
            "error": "Internal Server Error",
            "message": "Error interno del servidor.",
            "path": "/api/empleados/{id}"
        }
        ```

## 10. Postcondiciones
*   La información del empleado con el `id` especificado se actualiza en la base de datos.

## 11. DTOs Involucrados
*   **Request DTO:** `EmpleadoUpdateDTO` (se asume un DTO específico para actualización, o se usa `EmpleadoDTO` con validaciones de actualización)
*   **Response DTO:** `EmpleadoDTO`

## 12. Endpoint REST
*   **Método:** `PUT`
*   **URL:** `/api/empleados/{id}`

## 13. Referencias
*   **Diagrama de Secuencia:** `docs/diagrams/empleados/sequence_actualizar_empleado.puml`
*   **Diagrama de Clases:** `docs/diagrams/general/class_diagram_completo.puml`
*   **Diagrama ER:** `docs/diagrams/general/er_diagram_completo.puml`

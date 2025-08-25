# CU-EMP-006: Eliminar (Desactivar) Empleado

## 1. Dominio
Empleados

## 2. Descripción
Permite a un usuario administrador desactivar lógicamente un empleado en el sistema, cambiando su estado a INACTIVO. Esto no elimina el registro físicamente.

## 3. Actor(es)
*   Usuario Administrador

## 4. Precondiciones
*   El usuario debe tener el rol de `ADMINISTRADOR`.
*   El empleado a desactivar debe existir en la base de datos y estar `ACTIVO`.

## 5. Seguridad y Autorización
*   **Autenticación Requerida:** Sí
*   **Roles Permitidos:** `ADMINISTRADOR`
*   **Condiciones de Propiedad:** N/A

## 6. Flujo Principal (Happy Path)
1.  El Usuario Administrador envía una solicitud `DELETE` a `/api/empleados/{id}`.
2.  El sistema busca el empleado por el `id` proporcionado.
3.  Si el empleado existe y está `ACTIVO`, el sistema cambia su `estado` a `INACTIVO`.
4.  El sistema persiste los cambios en la base de datos.
5.  El sistema devuelve una respuesta `204 No Content`.

## 7. Flujos Alternativos
*   N/A

## 8. Validaciones

### 8.1. Validaciones de Formato/Sintaxis (Capa de DTO)
*   N/A

### 8.2. Validaciones de Reglas de Negocio (Capa de Servicio)
*   El `id` del empleado debe corresponder a un empleado existente.
*   El empleado debe estar en estado `ACTIVO` para poder ser desactivado.

## 9. Flujos de Excepción

*   **9.1. Empleado No Encontrado**
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

*   **9.2. Empleado Ya Inactivo**
    *   **Condición:** El empleado con el `id` proporcionado ya se encuentra en estado `INACTIVO`.
    *   **Excepción Java:** `EmpleadoAlreadyInactiveException` (personalizada, si se desea una excepción específica para este caso)
    *   **Respuesta HTTP:** `409 Conflict` (o `400 Bad Request`)
    *   **Cuerpo de Respuesta (ErrorResponseDTO):**
        ```json
        {
            "timestamp": "...",
            "status": 409,
            "error": "Conflict",
            "message": "El empleado con ID {id} ya está inactivo.",
            "path": "/api/empleados/{id}"
        }
        ```

*   **9.3. Error Interno del Servidor**
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
*   El estado del empleado con el `id` especificado se cambia a `INACTIVO` en la base de datos.

## 11. DTOs Involucrados
*   **Request DTO:** N/A
*   **Response DTO:** N/A (204 No Content)

## 12. Endpoint REST
*   **Método:** `DELETE`
*   **URL:** `/api/empleados/{id}`

## 13. Referencias
*   **Diagrama de Secuencia:** `docs/diagrams/empleados/sequence_eliminar_empleado.puml`
*   **Diagrama de Clases:** `docs/diagrams/general/class_diagram_completo.puml`
*   **Diagrama ER:** `docs/diagrams/general/er_diagram_completo.puml`

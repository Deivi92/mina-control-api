# CU-EMP-004: Cambiar Estado de Empleado

## 1. Dominio
Empleados

## 2. Descripción
Permite a un usuario administrador cambiar el estado de un empleado (ACTIVO/INACTIVO) en el sistema. Esto representa un borrado lógico.

## 3. Actor(es)
*   Usuario Administrador

## 4. Precondiciones
*   El usuario debe tener el rol de `ADMINISTRADOR`.
*   El empleado debe existir en la base de datos.

## 5. Seguridad y Autorización
*   **Autenticación Requerida:** Sí
*   **Roles Permitidos:** `ADMINISTRADOR`
*   **Condiciones de Propiedad:** N/A

## 6. Flujo Principal (Happy Path)
1.  El Usuario Administrador envía una solicitud `PATCH` a `/api/empleados/{id}/estado` con el nuevo estado (ej. `ACTIVO` o `INACTIVO`).
2.  El sistema busca el empleado por el `id` proporcionado.
3.  Si el empleado existe, el sistema actualiza el campo `estado` del empleado con el nuevo valor.
4.  El sistema persiste los cambios en la base de datos.
5.  El sistema devuelve una respuesta `200 OK` con el `EmpleadoDTO` del empleado con el estado actualizado.

## 7. Flujos Alternativos
*   N/A

## 8. Validaciones

### 8.1. Validaciones de Formato/Sintaxis (Capa de DTO)
*   El estado proporcionado debe ser un valor válido del enum `EstadoEmpleado` (ACTIVO, INACTIVO).

### 8.2. Validaciones de Reglas de Negocio (Capa de Servicio)
*   El `id` del empleado debe corresponder a un empleado existente.
*   No se puede cambiar el estado de un empleado a su estado actual (aunque esto no es un error crítico, puede ser una validación).

## 9. Flujos de Excepción

*   **9.1. Datos de Entrada Inválidos**
    *   **Condición:** El estado proporcionado no es un valor válido.
    *   **Excepción Java:** `IllegalArgumentException` (o similar, manejada por `@ControllerAdvice`)
    *   **Respuesta HTTP:** `400 Bad Request`
    *   **Cuerpo de Respuesta (ErrorResponseDTO):**
        ```json
        {
            "timestamp": "...",
            "status": 400,
            "error": "Bad Request",
            "message": "Estado de empleado inválido.",
            "path": "/api/empleados/{id}/estado"
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
            "path": "/api/empleados/{id}/estado"
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
            "path": "/api/empleados/{id}/estado"
        }
        ```

## 10. Postcondiciones
*   El estado del empleado con el `id` especificado se actualiza en la base de datos.

## 11. DTOs Involucrados
*   **Request DTO:** N/A (el estado se pasa como parte de la URL o cuerpo simple)
*   **Response DTO:** `EmpleadoDTO`

## 12. Endpoint REST
*   **Método:** `PATCH`
*   **URL:** `/api/empleados/{id}/estado`

## 13. Referencias
*   **Diagrama de Secuencia:** `docs/diagrams/empleados/sequence_cambiar_estado_empleado.puml`
*   **Diagrama de Clases:** `docs/diagrams/general/class_diagram_completo.puml`
*   **Diagrama ER:** `docs/diagrams/general/er_diagram_completo.puml`

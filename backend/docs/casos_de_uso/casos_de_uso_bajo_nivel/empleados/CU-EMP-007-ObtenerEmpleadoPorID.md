# CU-EMP-007: Obtener Empleado por ID

## 1. Dominio
Empleados

## 2. Descripción
Permite a un usuario administrador o gerente consultar los detalles de un empleado específico utilizando su ID.

## 3. Actor(es)
*   Usuario Administrador
*   Usuario Gerente

## 4. Precondiciones
*   El usuario debe tener el rol de `ADMINISTRADOR` o `GERENTE`.
*   El empleado debe existir en la base de datos.

## 5. Seguridad y Autorización
*   **Autenticación Requerida:** Sí
*   **Roles Permitidos:** `ADMINISTRADOR`, `GERENTE`
*   **Condiciones de Propiedad:** N/A

## 6. Flujo Principal (Happy Path)
1.  El Usuario (Administrador/Gerente) envía una solicitud `GET` a `/api/empleados/{id}`.
2.  El sistema busca el empleado por el `id` proporcionado.
3.  Si el empleado existe, el sistema convierte la entidad `Empleado` a un `EmpleadoDTO`.
4.  El sistema devuelve una respuesta `200 OK` con el `EmpleadoDTO` del empleado encontrado.

## 7. Flujos Alternativos
*   N/A

## 8. Validaciones

### 8.1. Validaciones de Formato/Sintaxis (Capa de DTO)
*   N/A

### 8.2. Validaciones de Reglas de Negocio (Capa de Servicio)
*   El `id` proporcionado debe ser un ID de empleado válido y existente.

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

*   **9.2. Error Interno del Servidor**
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
*   El sistema devuelve la información detallada del empleado solicitado.

## 11. DTOs Involucrados
*   **Request DTO:** N/A
*   **Response DTO:** `EmpleadoDTO`

## 12. Endpoint REST
*   **Método:** `GET`
*   **URL:** `/api/empleados/{id}`

## 13. Referencias
*   **Diagrama de Secuencia:** `docs/diagrams/empleados/sequence_obtener_empleado_por_id.puml`
*   **Diagrama de Clases:** `docs/diagrams/general/class_diagram_completo.puml`
*   **Diagrama ER:** `docs/diagrams/general/er_diagram_completo.puml`

# CU-EMP-005: Consultar Perfil Personal

## 1. Dominio
Empleados

## 2. Descripción
Permite a un empleado autenticado consultar su propio perfil personal en el sistema.

## 3. Actor(es)
*   Empleado

## 4. Precondiciones
*   El empleado debe estar autenticado en el sistema.
*   El ID del empleado debe poder extraerse del token de autenticación.

## 5. Seguridad y Autorización
*   **Autenticación Requerida:** Sí
*   **Roles Permitidos:** `EMPLEADO`, `ADMINISTRADOR`, `GERENTE` (cualquier rol autenticado)
*   **Condiciones de Propiedad:** Un empleado solo puede consultar su propio perfil.

## 6. Flujo Principal (Happy Path)
1.  El Empleado envía una solicitud `GET` a `/api/empleados/perfil` con su token JWT.
2.  El sistema extrae el ID del empleado del token JWT.
3.  El sistema busca el empleado por el ID extraído del token.
4.  Si el empleado existe, el sistema convierte la entidad `Empleado` a un `EmpleadoDTO` (con los datos relevantes para el perfil personal).
5.  El sistema devuelve una respuesta `200 OK` con el `EmpleadoDTO` del perfil personal.

## 7. Flujos Alternativos
*   N/A

## 8. Validaciones

### 8.1. Validaciones de Formato/Sintaxis (Capa de DTO)
*   N/A

### 8.2. Validaciones de Reglas de Negocio (Capa de Servicio)
*   El ID del empleado extraído del token debe corresponder a un empleado existente.

## 9. Flujos de Excepción

*   **9.1. Acceso Denegado / No Autorizado**
    *   **Condición:** El usuario no está autenticado o el token es inválido/expirado.
    *   **Excepción Java:** `AccessDeniedException` o `AuthenticationException` (manejada por Spring Security)
    *   **Respuesta HTTP:** `401 Unauthorized`
    *   **Cuerpo de Respuesta (ErrorResponseDTO):**
        ```json
        {
            "timestamp": "...",
            "status": 401,
            "error": "Unauthorized",
            "message": "Acceso denegado o token inválido.",
            "path": "/api/empleados/perfil"
        }
        ```

*   **9.2. Empleado No Encontrado (por ID del Token)**
    *   **Condición:** El ID extraído del token no corresponde a un empleado existente (situación inusual, pero posible si el empleado fue eliminado después de la emisión del token).
    *   **Excepción Java:** `EmpleadoNotFoundException` (personalizada)
    *   **Respuesta HTTP:** `404 Not Found`
    *   **Cuerpo de Respuesta (ErrorResponseDTO):**
        ```json
        {
            "timestamp": "...",
            "status": 404,
            "error": "Not Found",
            "message": "Perfil de empleado no encontrado.",
            "path": "/api/empleados/perfil"
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
            "path": "/api/empleados/perfil"
        }
        ```

## 10. Postcondiciones
*   El sistema devuelve la información del perfil personal del empleado autenticado.

## 11. DTOs Involucrados
*   **Request DTO:** N/A
*   **Response DTO:** `EmpleadoDTO`

## 12. Endpoint REST
*   **Método:** `GET`
*   **URL:** `/api/empleados/perfil`

## 13. Referencias
*   **Diagrama de Secuencia:** `docs/diagrams/empleados/sequence_consultar_perfil_personal.puml`
*   **Diagrama de Clases:** `docs/diagrams/general/class_diagram_completo.puml`
*   **Diagrama ER:** `docs/diagrams/general/er_diagram_completo.puml`

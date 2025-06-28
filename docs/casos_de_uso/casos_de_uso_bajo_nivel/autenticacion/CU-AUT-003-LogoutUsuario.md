# CU-AUT-003: Logout de Usuario

## 1. Dominio
Autenticación

## 2. Descripción
Invalida el token de refresco de un usuario para cerrar su sesión de forma segura en el lado del servidor. Esto previene que el token de refresco pueda ser usado para generar nuevos tokens de acceso.

## 3. Actor(es)
*   Usuario autenticado

## 4. Precondiciones
*   El usuario debe estar autenticado y poseer un token de refresco válido.

## 5. Seguridad y Autorización
*   **Autenticación Requerida:** Sí
*   **Roles Permitidos:** `ADMIN`, `SUPERVISOR`, `EMPLEADO` (cualquier rol autenticado)
*   **Condiciones de Propiedad:** Un usuario solo puede cerrar su propia sesión.

## 6. Flujo Principal (Happy Path)
1.  El usuario envía una solicitud `POST` a `/api/auth/logout`.
2.  El sistema extrae el token de refresco del cuerpo de la solicitud o de una cookie segura.
3.  El sistema valida el token de refresco.
4.  El sistema invalida el token (ej. añadiéndolo a una lista negra en cache o eliminándolo de la base de datos si es persistente).
5.  El sistema devuelve una respuesta `200 OK` sin contenido.

## 7. Flujos Alternativos
*   N/A

## 8. Validaciones

### 8.1. Validaciones de Formato/Sintaxis (Capa de DTO)
*   `refreshToken`: `@NotBlank`

### 8.2. Validaciones de Reglas de Negocio (Capa de Servicio)
*   El token de refresco debe ser válido y no haber expirado.
*   El token de refresco no debe haber sido ya invalidado.

## 9. Flujos de Excepción

*   **9.1. Token Inválido o Expirado**
    *   **Condición:** El token de refresco proporcionado no es válido.
    *   **Excepción Java:** `InvalidTokenException`
    *   **Respuesta HTTP:** `401 Unauthorized`
    *   **Cuerpo de Respuesta (ErrorResponseDTO):**
        ```json
        {
            "timestamp": "...",
            "status": 401,
            "error": "Unauthorized",
            "message": "El token de refresco es inválido o ha expirado.",
            "path": "/api/auth/logout"
        }
        ```

## 10. Postcondiciones
*   El token de refresco del usuario queda invalidado y no puede ser reutilizado.

## 11. DTOs Involucrados
*   **Request DTO:** `LogoutRequestDTO`
*   **Response DTO:** N/A

## 12. Endpoint REST
*   **Método:** `POST`
*   **URL:** `/api/auth/logout`

## 13. Referencias
*   **Diagrama de Secuencia:** `docs/diagrams/autenticacion/sequence_logout.puml`

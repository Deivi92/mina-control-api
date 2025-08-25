# CU-AUT-005: Refresh Token

## 1. Dominio
Autenticación

## 2. Descripción
Permite a un usuario con un token de acceso expirado obtener un nuevo token de acceso utilizando un token de refresco válido, sin necesidad de volver a introducir sus credenciales.

## 3. Actor(es)
*   Usuario autenticado (con token de acceso expirado)

## 4. Precondiciones
*   El usuario debe poseer un token de refresco válido y no expirado.

## 5. Seguridad y Autorización
*   **Autenticación Requerida:** No (la validación se hace sobre el token de refresco)
*   **Roles Permitidos:** N/A
*   **Condiciones de Propiedad:** N/A

## 6. Flujo Principal (Happy Path)
1.  El usuario envía una solicitud `POST` a `/api/auth/refresh-token` con su token de refresco.
2.  El sistema valida la firma, la expiración y la validez del token de refresco.
3.  Si el token es válido, el sistema extrae la información del usuario (ej. username).
4.  El sistema genera un nuevo token de acceso (JWT) de corta duración.
5.  Opcionalmente, se puede rotar el token de refresco, generando uno nuevo e invalidando el anterior.
6.  El sistema devuelve una respuesta `200 OK` con el nuevo token de acceso (y el nuevo token de refresco si se rota).

## 7. Flujos Alternativos
*   N/A

## 8. Validaciones

### 8.1. Validaciones de Formato/Sintaxis (Capa de DTO)
*   `refreshToken`: `@NotBlank`

### 8.2. Validaciones de Reglas de Negocio (Capa de Servicio)
*   El token de refresco no debe haber expirado.
*   El token de refresco no debe estar en la lista negra (si aplica).
*   La firma del token debe ser válida.

## 9. Flujos de Excepción

*   **9.1. Token de Refresco Inválido o Expirado**
    *   **Condición:** El token proporcionado no es válido, ha expirado o ha sido revocado.
    *   **Excepción Java:** `InvalidTokenException`
    *   **Respuesta HTTP:** `401 Unauthorized`
    *   **Cuerpo de Respuesta (ErrorResponseDTO):**
        ```json
        {
            "timestamp": "...",
            "status": 401,
            "error": "Unauthorized",
            "message": "El token de refresco es inválido o ha expirado.",
            "path": "/api/auth/refresh-token"
        }
        ```

## 10. Postcondiciones
*   Se emite un nuevo token de acceso.

## 11. DTOs Involucrados
*   **Request DTO:** `RefreshTokenRequestDTO`
*   **Response DTO:** `RefreshTokenResponseDTO`

## 12. Endpoint REST
*   **Método:** `POST`
*   **URL:** `/api/auth/refresh-token`

## 13. Referencias
*   **Diagrama de Secuencia:** `docs/diagrams/autenticacion/sequence_refresh_token.puml`

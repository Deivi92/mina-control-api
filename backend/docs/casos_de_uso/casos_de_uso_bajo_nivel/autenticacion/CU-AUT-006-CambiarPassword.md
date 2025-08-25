# CU-AUT-006: Cambiar Contraseña

## 1. Dominio
Autenticación

## 2. Descripción
Permite a un usuario autenticado cambiar su propia contraseña. También se usa para establecer una nueva contraseña después de un flujo de recuperación.

## 3. Actor(es)
*   Usuario autenticado
*   Usuario que ha solicitado recuperar su contraseña

## 4. Precondiciones
*   **Flujo 1 (Cambio normal):** El usuario debe estar autenticado.
*   **Flujo 2 (Recuperación):** El usuario debe poseer un token de reseteo de contraseña válido.

## 5. Seguridad y Autorización
*   **Autenticación Requerida:** Sí (para cambio normal) / No (para flujo de recuperación, se valida con token)
*   **Roles Permitidos:** Cualquier rol autenticado.
*   **Condiciones de Propiedad:** Un usuario solo puede cambiar su propia contraseña.

## 6. Flujo Principal (Happy Path)
1.  El usuario envía una solicitud `POST` a `/api/auth/change-password`.
2.  **Si es cambio normal:** El usuario proporciona su contraseña actual y la nueva.
3.  **Si es recuperación:** El usuario proporciona el token de reseteo y la nueva contraseña.
4.  El sistema valida la contraseña actual o el token de reseteo.
5.  El sistema valida que la nueva contraseña cumpla con las políticas de seguridad.
6.  El sistema crea un hash seguro de la nueva contraseña.
7.  El sistema actualiza la contraseña del usuario en la base de datos.
8.  Si se usó un token de reseteo, se invalida.
9.  El sistema devuelve una respuesta `200 OK`.

## 7. Flujos Alternativos
*   N/A

## 8. Validaciones

### 8.1. Validaciones de Formato/Sintaxis (Capa de DTO)
*   `oldPassword` / `token`: `@NotBlank` (depende del flujo)
*   `newPassword`: `@NotBlank`, `@Size(min=8, max=100)`

### 8.2. Validaciones de Reglas de Negocio (Capa de Servicio)
*   La `oldPassword` debe coincidir con la del usuario autenticado.
*   El `token` de reseteo debe ser válido, no expirado y corresponder al usuario.
*   La `newPassword` no debe ser igual a la contraseña anterior.

## 9. Flujos de Excepción

*   **9.1. Contraseña Anterior Incorrecta**
    *   **Condición:** La `oldPassword` no coincide.
    *   **Excepción Java:** `InvalidPasswordException`
    *   **Respuesta HTTP:** `400 Bad Request`

*   **9.2. Token de Reseteo Inválido**
    *   **Condición:** El token es inválido, ha expirado o no corresponde al usuario.
    *   **Excepción Java:** `InvalidTokenException`
    *   **Respuesta HTTP:** `400 Bad Request`

## 10. Postcondiciones
*   La contraseña del usuario ha sido actualizada en la base de datos.

## 11. DTOs Involucrados
*   **Request DTO:** `ChangePasswordRequestDTO`
*   **Response DTO:** N/A

## 12. Endpoint REST
*   **Método:** `POST`
*   **URL:** `/api/auth/change-password`

## 13. Referencias
*   **Diagrama de Secuencia:** `docs/diagrams/autenticacion/sequence_change_password.puml`

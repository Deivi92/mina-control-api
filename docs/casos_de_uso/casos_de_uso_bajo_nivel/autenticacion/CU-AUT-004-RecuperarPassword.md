# CU-AUT-004: Recuperar Contraseña

## 1. Dominio
Autenticación

## 2. Descripción
Inicia el flujo para que un usuario que ha olvidado su contraseña pueda recuperarla. El sistema genera un token de reseteo seguro y se lo envía al usuario por correo electrónico.

## 3. Actor(es)
*   Usuario (que olvidó su contraseña)

## 4. Precondiciones
*   El usuario debe tener una cuenta registrada con un correo electrónico verificado.

## 5. Seguridad y Autorización
*   **Autenticación Requerida:** No
*   **Roles Permitidos:** N/A (Endpoint público)
*   **Condiciones de Propiedad:** N/A

## 6. Flujo Principal (Happy Path)
1.  El usuario envía una solicitud `POST` a `/api/auth/recover-password` con su dirección de correo electrónico.
2.  El sistema verifica que el correo electrónico corresponde a un usuario registrado.
3.  El sistema genera un token de reseteo de contraseña único y con tiempo de expiración.
4.  El sistema almacena el token (hasheado) asociado al usuario y su fecha de expiración.
5.  El sistema envía un correo electrónico al usuario con un enlace para restablecer su contraseña (el enlace contiene el token).
6.  El sistema devuelve una respuesta `200 OK` con un mensaje indicando que se ha enviado un correo si el usuario existe.

## 7. Flujos Alternativos
*   N/A

## 8. Validaciones

### 8.1. Validaciones de Formato/Sintaxis (Capa de DTO)
*   `email`: `@NotBlank`, `@Email`

### 8.2. Validaciones de Reglas de Negocio (Capa de Servicio)
*   El correo electrónico debe estar asociado a una cuenta de usuario existente y activa.

## 9. Flujos de Excepción
*   **Nota:** Por seguridad, este endpoint siempre debe devolver una respuesta genérica `200 OK` para no revelar si un correo electrónico está o no registrado en el sistema.

## 10. Postcondiciones
*   Se genera y almacena un token de reseteo de contraseña para el usuario.
*   Se envía un correo electrónico al usuario.

## 11. DTOs Involucrados
*   **Request DTO:** `RecoverPasswordRequestDTO`
*   **Response DTO:** N/A

## 12. Endpoint REST
*   **Método:** `POST`
*   **URL:** `/api/auth/recover-password`

## 13. Referencias
*   **Diagrama de Secuencia:** `docs/diagrams/autenticacion/sequence_recuperar_password.puml`

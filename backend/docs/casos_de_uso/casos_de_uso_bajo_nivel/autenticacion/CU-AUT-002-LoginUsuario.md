# CU-AUT-002: Login de Usuario

## 1. Dominio
Autenticación

## 2. Descripción
Permite a un usuario registrado iniciar sesión en el sistema proporcionando sus credenciales (email y contraseña). Si las credenciales son válidas, el sistema genera y devuelve tokens de acceso y de refresco.

## 3. Actor(es)
*   Usuario registrado

## 4. Precondiciones
*   El usuario debe tener una cuenta activa y registrada en el sistema.

## 5. Seguridad y Autorización
*   **Autenticación Requerida:** No
*   **Roles Permitidos:** N/A (Endpoint público)
*   **Condiciones de Propiedad:** N/A

## 6. Flujo Principal (Happy Path)
1.  El usuario envía una solicitud `POST` a `/api/auth/login` con su `email` y `password`.
2.  El sistema valida el formato de la solicitud (DTO).
3.  El sistema busca al usuario por su `email`.
4.  El sistema compara el hash de la `password` proporcionada con el hash almacenado en la base de datos.
5.  Si la contraseña es correcta, el sistema genera un token de acceso (JWT) de corta duración.
6.  El sistema genera un token de refresco (JWT) de larga duración.
7.  El sistema devuelve una respuesta `200 OK` con un DTO que contiene ambos tokens.

## 7. Flujos Alternativos
*   N/A

## 8. Validaciones

### 8.1. Validaciones de Formato/Sintaxis (Capa de DTO)
*   `email`: `@NotBlank`, `@Email`
*   `password`: `@NotBlank`

### 8.2. Validaciones de Reglas de Negocio (Capa de Servicio)
*   El `email` debe corresponder a un usuario existente.
*   La `password` debe coincidir con la almacenada para ese usuario.
*   El usuario no debe estar en un estado inactivo o bloqueado.

## 9. Flujos de Excepción

*   **9.1. Credenciales Inválidas**
    *   **Condición:** El `email` no existe o la `password` es incorrecta.
    *   **Excepción Java:** `BadCredentialsException` (de Spring Security)
    *   **Respuesta HTTP:** `401 Unauthorized`
    *   **Cuerpo de Respuesta (ErrorResponseDTO):**
        ```json
        {
            "timestamp": "...",
            "status": 401,
            "error": "Unauthorized",
            "message": "Credenciales de acceso inválidas.",
            "path": "/api/auth/login"
        }
        ```

## 10. Postcondiciones
*   No hay cambios de estado persistentes en la base de datos (a menos que se registre el último acceso, lo cual es opcional).

## 11. DTOs Involucrados
*   **Request DTO:** `LoginRequestDTO`
*   **Response DTO:** `LoginResponseDTO`

## 12. Endpoint REST
*   **Método:** `POST`
*   **URL:** `/api/auth/login`

## 13. Referencias
*   **Diagrama de Secuencia:** `docs/diagrams/autenticacion/sequence_login.puml`

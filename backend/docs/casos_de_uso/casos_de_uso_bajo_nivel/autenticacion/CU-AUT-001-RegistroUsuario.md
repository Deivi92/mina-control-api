# CU-AUT-001: Registro de Nuevo Usuario

## 1. Dominio
Autenticación

## 2. Descripción
Permite a un empleado sin cuenta registrarse en el sistema utilizando su email. El sistema valida sus datos, crea una nueva cuenta de usuario y la asocia con su registro de empleado existente.

## 3. Actor(es)
*   Empleado (no registrado en el sistema)

## 4. Precondiciones
*   El actor debe ser un empleado activo y existente en la base de datos.
*   El empleado no debe tener una cuenta de usuario previamente registrada.

## 5. Seguridad y Autorización
*   **Autenticación Requerida:** No
*   **Roles Permitidos:** N/A (Endpoint público)
*   **Condiciones de Propiedad:** N/A

## 6. Flujo Principal (Happy Path)
1.  El empleado envía una solicitud `POST` a `/api/auth/register` con su email y contraseña.
2.  El sistema valida el formato de la solicitud (DTO).
3.  El sistema verifica que el `email` corresponde a un empleado existente y activo.
4.  El sistema verifica que el empleado no tiene ya una cuenta de usuario.
5.  El sistema comprueba que el `email` no esté ya en uso como usuario.
6.  El sistema crea un hash seguro de la contraseña.
7.  El sistema crea una nueva entidad `Usuario` con los datos proporcionados y la asocia al `Empleado` correspondiente.
8.  El sistema persiste la nueva entidad `Usuario` en la base de datos.
9.  El sistema devuelve una respuesta `201 Created` con el `UsuarioDTO` del nuevo usuario.

## 7. Flujos Alternativos
*   N/A

## 8. Validaciones

### 8.1. Validaciones de Formato/Sintaxis (Capa de DTO)
*   `email`: `@NotBlank`, `@Email`
*   `password`: `@NotBlank`, `@Size(min=8, max=100)`, debe cumplir un patrón de seguridad (ej. mayúscula, minúscula, número).

### 8.2. Validaciones de Reglas de Negocio (Capa de Servicio)
*   El `email` debe corresponder a un `Empleado` existente en la BD.
*   El `Empleado` asociado no debe tener ya un `Usuario` vinculado.
*   El `email` no debe existir en la tabla de `usuarios`.

## 9. Flujos de Excepción

*   **9.1. Empleado no Encontrado**
    *   **Condición:** El `email` no existe como empleado.
    *   **Excepción Java:** `EmpleadoNotFoundException`
    *   **Respuesta HTTP:** `404 Not Found`
    *   **Cuerpo de Respuesta (ErrorResponseDTO):**
        ```json
        {
            "timestamp": "...",
            "status": 404,
            "error": "Not Found",
            "message": "El empleado con el email proporcionado no fue encontrado.",
            "path": "/api/auth/register"
        }
        ```

*   **9.2. Usuario ya Existe (por Empleado o Email)**
    *   **Condición:** El empleado ya tiene cuenta, o el email ya está en uso como usuario.
    *   **Excepción Java:** `UserAlreadyExistsException`
    *   **Respuesta HTTP:** `409 Conflict`
    *   **Cuerpo de Respuesta (ErrorResponseDTO):**
        ```json
        {
            "timestamp": "...",
            "status": 409,
            "error": "Conflict",
            "message": "[El empleado ya tiene una cuenta | El email ya está en uso]",
            "path": "/api/auth/register"
        }
        ```

## 10. Postcondiciones
*   Se crea una nueva fila en la tabla `usuarios`.
*   La nueva fila de `usuarios` está vinculada a una fila existente en la tabla `empleados`.

## 11. DTOs Involucrados
*   **Request DTO:** `RegistroUsuarioCreateDTO`
*   **Response DTO:** `UsuarioDTO`

## 12. Endpoint REST
*   **Método:** `POST`
*   **URL:** `/api/auth/register`

## 13. Referencias
*   **Diagrama de Secuencia:** `docs/diagrams/autenticacion/sequence_registro_usuario.puml`
*   **Diagrama de Clases:** `docs/diagrams/autenticacion/class_diagram_autenticacion.puml`
*   **Diagrama ER:** `docs/diagrams/general/er_diagram_completo.puml`

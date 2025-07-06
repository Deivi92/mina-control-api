# CU-TUR-001: Crear Tipo de Turno

## 1. Dominio
Turnos

## 2. Descripción
Permite a un usuario administrador o gerente crear y configurar un nuevo tipo de turno en el sistema, especificando su nombre, horario y descripción.

## 3. Actor(es)
*   Usuario Administrador
*   Usuario Gerente

## 4. Precondiciones
*   El usuario debe estar autenticado y tener el rol de `ADMINISTRADOR` o `GERENTE`.
*   No debe existir otro tipo de turno con el mismo nombre.

## 5. Seguridad y Autorización
*   **Autenticación Requerida:** Sí
*   **Roles Permitidos:** `ADMINISTRADOR`, `GERENTE`
*   **Condiciones de Propiedad:** N/A

## 6. Flujo Principal (Happy Path)
1.  El usuario (Administrador/Gerente) envía una solicitud `POST` a `/api/turnos` con un `TipoTurnoCreateDTO`.
2.  El sistema valida el formato del DTO (campos requeridos, formato de hora).
3.  El sistema verifica que no exista un tipo de turno con el mismo `nombre`.
4.  El sistema valida las reglas de negocio (ej. `horaInicio` debe ser anterior a `horaFin`).
5.  El sistema crea una nueva entidad `TipoTurno` a partir del DTO.
6.  El sistema persiste la nueva entidad en la base de datos.
7.  El sistema devuelve una respuesta `201 Created` con el `TipoTurnoDTO` del nuevo tipo de turno.

## 7. Flujos Alternativos
*   N/A

## 8. Validaciones

### 8.1. Validaciones de Formato/Sintaxis (Capa de DTO)
*   `nombre`: `@NotBlank`, `@Size(max=50)`
*   `horaInicio`: `@NotNull`
*   `horaFin`: `@NotNull`
*   `descripcion`: `@Size(max=255)`

### 8.2. Validaciones de Reglas de Negocio (Capa de Servicio)
*   El `nombre` del tipo de turno debe ser único.
*   `horaInicio` debe ser estrictamente anterior a `horaFin`.

## 9. Flujos de Excepción

*   **9.1. Datos de Entrada Inválidos**
    *   **Condición:** El DTO no cumple con las validaciones de formato o de reglas de negocio.
    *   **Excepción Java:** `MethodArgumentNotValidException`, `TurnoInvalidoException`
    *   **Respuesta HTTP:** `400 Bad Request`
    *   **Cuerpo de Respuesta (ErrorResponseDTO):**
        ```json
        {
            "timestamp": "...",
            "status": 400,
            "error": "Bad Request",
            "message": "Error de validación: [mensaje específico del campo]",
            "path": "/api/turnos"
        }
        ```

*   **9.2. Conflicto de Nombre**
    *   **Condición:** Ya existe un tipo de turno con el nombre proporcionado.
    *   **Excepción Java:** `TurnoAlreadyExistsException`
    *   **Respuesta HTTP:** `409 Conflict`
    *   **Cuerpo de Respuesta (ErrorResponseDTO):**
        ```json
        {
            "timestamp": "...",
            "status": 409,
            "error": "Conflict",
            "message": "Ya existe un tipo de turno con el nombre '...'."
        }
        ```

## 10. Postcondiciones
*   Se crea una nueva fila en la tabla `tipos_turno`.
*   El nuevo tipo de turno está disponible para ser asignado.

## 11. DTOs Involucrados
*   **Request DTO:** `TipoTurnoCreateDTO`
*   **Response DTO:** `TipoTurnoDTO`

## 12. Endpoint REST
*   **Método:** `POST`
*   **URL:** `/api/turnos`

## 13. Referencias
*   **Diagrama de Secuencia:** `docs/diagrams/turnos/sequence_crear_turno.puml`
*   **Diagrama de Clases:** `docs/diagrams/turnos/class_diagram_turnos_corregido.puml`
*   **Diagrama ER:** `docs/diagrams/general/er_diagram_completo.puml`

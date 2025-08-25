# CU-TUR-006: Asignar Empleado a Turno

## 1. Dominio
Turnos

## 2. Descripción
Permite a un administrador asignar un empleado específico a un tipo de turno para un rango de fechas.

## 3. Actor(es)
*   Usuario Administrador

## 4. Precondiciones
*   El usuario debe estar autenticado y tener rol `ADMINISTRADOR`.
*   Tanto el empleado como el tipo de turno deben existir y estar activos.
*   No debe haber conflictos de horario para el empleado en las fechas seleccionadas.

## 5. Seguridad y Autorización
*   **Autenticación Requerida:** Sí
*   **Roles Permitidos:** `ADMINISTRADOR`

## 6. Flujo Principal (Happy Path)
1.  El usuario envía `POST` a `/api/turnos/asignaciones` con un `AsignacionTurnoCreateDTO`.
2.  El sistema valida el DTO.
3.  Verifica la existencia y estado del empleado y del tipo de turno.
4.  Comprueba que no haya conflictos de horario para el empleado.
5.  Crea una nueva entidad `AsignacionTurno`.
6.  Persiste la asignación en la base de datos.
7.  Devuelve `201 Created` con el `AsignacionTurnoDTO` creado.

## 7. Flujos Alternativos
*   N/A

## 8. Validaciones
*   `empleadoId` y `tipoTurnoId` deben existir.
*   `fechaInicio` debe ser anterior o igual a `fechaFin`.
*   El empleado no debe tener otra asignación que se solape en el tiempo.

## 9. Flujos de Excepción

*   **9.1. Empleado o Turno No Encontrado**
    *   **Respuesta HTTP:** `404 Not Found`.

*   **9.2. Conflicto de Horario**
    *   **Excepción:** `AsignacionTurnoInvalidaException`
    *   **Respuesta HTTP:** `409 Conflict`.

## 10. Postcondiciones
*   Se crea una nueva fila en la tabla `asignaciones_turno`.

## 11. DTOs Involucrados
*   **Request DTO:** `AsignacionTurnoCreateDTO`
*   **Response DTO:** `AsignacionTurnoDTO`

## 12. Endpoint REST
*   **Método:** `POST`
*   **URL:** `/api/turnos/asignaciones`

## 13. Referencias
*   **Diagrama de Secuencia:** `docs/diagrams/turnos/sequence_asignar_empleado_turno.puml`
*   **Diagrama de Clases:** `docs/diagrams/turnos/class_diagram_turnos_corregido.puml`

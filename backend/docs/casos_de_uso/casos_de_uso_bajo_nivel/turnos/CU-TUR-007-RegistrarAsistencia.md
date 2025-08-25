# CU-TUR-007: Registrar Entrada/Salida de Asistencia

## 1. Dominio
Turnos

## 2. Descripción
Registra la hora de entrada o salida de un empleado para su turno asignado en la fecha actual.

## 3. Actor(es)
*   Usuario Administrador (o un sistema automatizado)

## 4. Precondiciones
*   El empleado debe tener un turno asignado para la fecha actual.
*   Si se registra una salida, debe existir un registro de entrada previo para el mismo día.

## 5. Seguridad y Autorización
*   **Autenticación Requerida:** Sí
*   **Roles Permitidos:** `ADMINISTRADOR`

## 6. Flujo Principal (Happy Path)
1.  El usuario envía `POST` a `/api/asistencia/registrar` con `RegistrarAsistenciaDTO` (incluye `empleadoId`, `tipoRegistro` 'ENTRADA'/'SALIDA').
2.  El sistema busca la asignación de turno del empleado para la fecha actual.
3.  **Si es ENTRADA:** Crea un nuevo `RegistroAsistencia` con la `horaEntrada` actual.
4.  **Si es SALIDA:** Busca el registro de entrada del día, actualiza la `horaSalida` y calcula las `horasTrabajadas`.
5.  Persiste la entidad `RegistroAsistencia`.
6.  Devuelve `200 OK` con el `RegistroAsistenciaDTO` actualizado.

## 7. Flujos Alternativos
*   N/A

## 8. Validaciones
*   El `empleadoId` debe tener un turno asignado hoy.
*   No se puede registrar una entrada si ya existe una para hoy.
*   No se puede registrar una salida sin una entrada previa.

## 9. Flujos de Excepción

*   **9.1. Asignación No Encontrada**
    *   **Excepción:** `AsignacionTurnoInvalidaException`
    *   **Respuesta HTTP:** `404 Not Found`
    *   **Mensaje:** "El empleado no tiene un turno asignado para hoy."

*   **9.2. Registro Inválido**
    *   **Excepción:** `RegistroAsistenciaInvalidoException`
    *   **Respuesta HTTP:** `409 Conflict`
    *   **Mensaje:** "Ya existe una entrada registrada hoy." o "No se puede registrar salida sin una entrada previa."

## 10. Postcondiciones
*   Se crea o actualiza una fila en la tabla `registros_asistencia`.

## 11. DTOs Involucrados
*   **Request DTO:** `RegistrarAsistenciaDTO`
*   **Response DTO:** `RegistroAsistenciaDTO`

## 12. Endpoint REST
*   **Método:** `POST`
*   **URL:** `/api/asistencia/registrar`

## 13. Referencias
*   **Diagrama de Secuencia:** `docs/diagrams/turnos/sequence_registrar_entrada_salida.puml`
*   **Diagrama de Clases:** `docs/diagrams/turnos/class_diagram_turnos_corregido.puml`

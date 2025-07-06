# CU-TUR-005: Eliminar Tipo de Turno

## 1. Dominio
Turnos

## 2. Descripción
Permite a un administrador o gerente eliminar un tipo de turno, siempre que no tenga empleados asignados.

## 3. Actor(es)
*   Usuario Administrador
*   Usuario Gerente

## 4. Precondiciones
*   El usuario debe estar autenticado y tener los roles adecuados.
*   El tipo de turno a eliminar debe existir.
*   El tipo de turno no debe tener asignaciones activas.

## 5. Seguridad y Autorización
*   **Autenticación Requerida:** Sí
*   **Roles Permitidos:** `ADMINISTRADOR`, `GERENTE`

## 6. Flujo Principal (Happy Path)
1.  El usuario envía una solicitud `DELETE` a `/api/turnos/{id}`.
2.  El sistema busca el `TipoTurno` por `id`.
3.  Verifica que no tenga asignaciones en `AsignacionTurnoRepository`.
4.  Si no hay asignaciones, elimina la entidad de la base de datos.
5.  Devuelve una respuesta `204 No Content`.

## 7. Flujos Alternativos
*   N/A

## 8. Validaciones
*   El tipo de turno no debe tener empleados asignados.

## 9. Flujos de Excepción

*   **9.1. Tipo de Turno No Encontrado**
    *   **Respuesta HTTP:** `404 Not Found`.

*   **9.2. Turno con Asignaciones**
    *   **Condición:** Se intenta eliminar un turno que tiene empleados asignados.
    *   **Excepción Java:** `IllegalStateException`
    *   **Respuesta HTTP:** `409 Conflict`.

## 10. Postcondiciones
*   La fila correspondiente se elimina de la tabla `tipos_turno`.

## 11. DTOs Involucrados
*   N/A

## 12. Endpoint REST
*   **Método:** `DELETE`
*   **URL:** `/api/turnos/{id}`

## 13. Referencias
*   **Diagrama de Secuencia:** `docs/diagrams/turnos/sequence_eliminar_turno.puml`
*   **Diagrama de Clases:** `docs/diagrams/turnos/class_diagram_turnos_corregido.puml`

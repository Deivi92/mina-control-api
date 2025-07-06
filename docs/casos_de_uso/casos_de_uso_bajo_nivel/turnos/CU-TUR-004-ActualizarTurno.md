# CU-TUR-004: Actualizar Tipo de Turno

## 1. Dominio
Turnos

## 2. Descripción
Permite a un administrador o gerente actualizar los detalles de un tipo de turno existente.

## 3. Actor(es)
*   Usuario Administrador
*   Usuario Gerente

## 4. Precondiciones
*   El usuario debe estar autenticado y tener los roles adecuados.
*   El tipo de turno a actualizar debe existir.

## 5. Seguridad y Autorización
*   **Autenticación Requerida:** Sí
*   **Roles Permitidos:** `ADMINISTRADOR`, `GERENTE`

## 6. Flujo Principal (Happy Path)
1.  El usuario envía una solicitud `PUT` a `/api/turnos/{id}` con un `TipoTurnoCreateDTO`.
2.  El sistema valida el DTO.
3.  El sistema busca el `TipoTurno` por `id`.
4.  Si existe, actualiza los datos de la entidad con los del DTO.
5.  El sistema persiste los cambios.
6.  El sistema devuelve `200 OK` con el `TipoTurnoDTO` actualizado.

## 7. Flujos Alternativos
*   N/A

## 8. Validaciones
*   Las mismas que en la creación (`CU-TUR-001`).
*   El `nombre` actualizado no debe entrar en conflicto con otro existente.

## 9. Flujos de Excepción

*   **9.1. Tipo de Turno No Encontrado**
    *   **Respuesta HTTP:** `404 Not Found`.

*   **9.2. Datos Inválidos o Conflicto de Nombre**
    *   **Respuesta HTTP:** `400 Bad Request` o `409 Conflict`.

## 10. Postcondiciones
*   La información del tipo de turno se actualiza en la base de datos.

## 11. DTOs Involucrados
*   **Request DTO:** `TipoTurnoCreateDTO`
*   **Response DTO:** `TipoTurnoDTO`

## 12. Endpoint REST
*   **Método:** `PUT`
*   **URL:** `/api/turnos/{id}`

## 13. Referencias
*   **Diagrama de Secuencia:** `docs/diagrams/turnos/sequence_actualizar_turno.puml`
*   **Diagrama de Clases:** `docs/diagrams/turnos/class_diagram_turnos_corregido.puml`

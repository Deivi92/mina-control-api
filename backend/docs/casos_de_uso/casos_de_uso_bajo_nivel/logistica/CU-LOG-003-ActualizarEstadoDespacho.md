# CU-LOG-003: Actualizar Estado de Despacho

## 1. Dominio
Logística

## 2. Descripción
Permite a un usuario autorizado actualizar el estado de un despacho existente (ej. de `PROGRAMADO` a `EN_TRANSITO`, o de `EN_TRANSITO` a `ENTREGADO`).

## 3. Actor(es)
*   Usuario Administrador
*   Usuario Supervisor

## 4. Precondiciones
*   El usuario debe estar autenticado y tener los roles adecuados.
*   El despacho debe existir en la base de datos.
*   La transición de estado debe ser válida (ej. no se puede pasar de `ENTREGADO` a `PROGRAMADO`).

## 5. Seguridad y Autorización
*   **Autenticación Requerida:** Sí
*   **Roles Permitidos:** `ADMINISTRADOR`, `SUPERVISOR`
*   **Condiciones de Propiedad:** N/A

## 6. Flujo Principal (Happy Path)
1.  El usuario envía una solicitud `PATCH` a `/api/logistica/despachos/{id}/estado` con el nuevo estado en el cuerpo de la solicitud.
2.  El sistema busca el `Despacho` por el `id` proporcionado.
3.  El servicio valida que la transición del estado actual al nuevo estado sea permitida.
4.  El servicio actualiza el campo `estado` de la entidad.
5.  Si el nuevo estado es `EN_TRANSITO`, se establece la `fechaSalida`.
6.  Si el nuevo estado es `ENTREGADO`, se establece la `fechaEntrega`.
7.  El sistema persiste los cambios en la base de datos.
8.  El sistema devuelve una respuesta `200 OK` con el `DespachoDTO` actualizado.

## 7. Flujos Alternativos
*   N/A

## 8. Validaciones

### 8.1. Validaciones de Formato/Sintaxis (Capa de Controller)
*   El nuevo estado proporcionado debe ser un valor válido del enum `EstadoDespacho`.

### 8.2. Validaciones de Reglas de Negocio (Capa de Servicio)
*   El `id` debe corresponder a un despacho existente.
*   La transición de estado debe seguir una lógica de negocio predefinida (ej. `PROGRAMADO` -> `EN_TRANSITO` -> `ENTREGADO`). Un despacho `CANCELADO` o `ENTREGADO` no puede cambiar de estado.

## 9. Flujos de Excepción

*   **9.1. Despacho No Encontrado**
    *   **Condición:** El `id` no corresponde a ningún despacho.
    *   **Excepción Java:** `DespachoNotFoundException`
    *   **Respuesta HTTP:** `404 Not Found`

*   **9.2. Transición de Estado Inválida**
    *   **Condición:** Se intenta realizar una actualización de estado no permitida.
    *   **Excepción Java:** `EstadoDespachoInvalidoException`
    *   **Respuesta HTTP:** `409 Conflict`
    *   **Mensaje:** "No se puede cambiar el estado de '{estadoActual}' a '{estadoNuevo}'."

## 10. Postcondiciones
*   El estado del despacho y, posiblemente, sus fechas de salida/entrega, son actualizados.

## 11. DTOs Involucrados
*   **Request DTO:** Un DTO simple con el nuevo estado, ej. `ActualizarEstadoDespachoDTO`
*   **Response DTO:** `DespachoDTO`

## 12. Endpoint REST
*   **Método:** `PATCH`
*   **URL:** `/api/logistica/despachos/{id}/estado`

## 13. Referencias
*   **Diagrama de Secuencia:** `docs/diagrams/logistica/sequence_actualizar_estado_despacho.puml`
*   **Diagrama de Clases:** `docs/diagrams/logistica/class_diagram_logistica.puml`
*   **Diagrama ER:** `docs/diagrams/general/er_diagram_completo.puml`

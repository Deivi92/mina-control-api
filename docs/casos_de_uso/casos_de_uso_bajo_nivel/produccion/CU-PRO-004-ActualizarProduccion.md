# CU-PRO-004: Actualizar Registro de Producción

## 1. Dominio
Producción

## 2. Descripción
Permite a un usuario modificar los datos de un registro de producción existente, siempre y cuando no haya sido validado previamente.

## 3. Actor(es)
*   Usuario Administrador
*   Usuario Supervisor

## 4. Precondiciones
*   El usuario debe estar autenticado y tener los roles adecuados.
*   El registro de producción a actualizar debe existir en la base de datos.
*   El registro de producción no debe tener el estado `validado` como `true`.

## 5. Seguridad y Autorización
*   **Autenticación Requerida:** Sí
*   **Roles Permitidos:** `ADMINISTRADOR`, `SUPERVISOR`
*   **Condiciones de Propiedad:** N/A

## 6. Flujo Principal (Happy Path)
1.  El usuario envía una solicitud `PUT` a `/api/produccion/{id}` con un `RegistroProduccionCreateDTO` conteniendo los nuevos datos.
2.  El sistema valida el formato del DTO.
3.  El sistema busca el `RegistroProduccion` por el `id` proporcionado.
4.  El servicio verifica que el registro encontrado no esté validado (`validado` es `false`).
5.  El servicio actualiza los campos de la entidad con los datos del DTO.
6.  El sistema persiste los cambios en la base de datos, actualizando el campo `updatedAt`.
7.  El sistema devuelve una respuesta `200 OK` con el `RegistroProduccionDTO` actualizado.

## 7. Flujos Alternativos
*   N/A

## 8. Validaciones

### 8.1. Validaciones de Formato/Sintaxis (Capa de DTO)
*   Similares a las de creación (`CU-PRO-001`), asegurando que los datos actualizados sean coherentes.

### 8.2. Validaciones de Reglas de Negocio (Capa de Servicio)
*   El `id` debe corresponder a un registro existente.
*   El registro no puede ser modificado si ya ha sido validado (`validado == true`).

## 9. Flujos de Excepción

*   **9.1. Datos de Entrada Inválidos**
    *   **Condición:** El DTO no cumple las validaciones.
    *   **Excepción Java:** `MethodArgumentNotValidException`
    *   **Respuesta HTTP:** `400 Bad Request`

*   **9.2. Registro No Encontrado**
    *   **Condición:** El `id` no corresponde a ningún registro.
    *   **Excepción Java:** `RegistroProduccionNotFoundException`
    *   **Respuesta HTTP:** `404 Not Found`

*   **9.3. Registro Ya Validado**
    *   **Condición:** Se intenta modificar un registro que ya ha sido validado.
    *   **Excepción Java:** `RegistroProduccionValidatedException`
    *   **Respuesta HTTP:** `409 Conflict`
    *   **Mensaje:** "El registro de producción con ID {id} ya ha sido validado y no puede ser modificado."

## 10. Postcondiciones
*   La información del registro de producción se actualiza en la base de datos.

## 11. DTOs Involucrados
*   **Request DTO:** `RegistroProduccionCreateDTO` (o un `UpdateDTO` específico)
*   **Response DTO:** `RegistroProduccionDTO`

## 12. Endpoint REST
*   **Método:** `PUT`
*   **URL:** `/api/produccion/{id}`

## 13. Referencias
*   **Diagrama de Secuencia:** `docs/diagrams/produccion/sequence_actualizar_produccion.puml`
*   **Diagrama de Clases:** `docs/diagrams/produccion/class_diagram_produccion.puml`
*   **Diagrama ER:** `docs/diagrams/general/er_diagram_completo.puml`

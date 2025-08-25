# CU-PRO-005: Eliminar Registro de Producción

## 1. Dominio
Producción

## 2. Descripción
Permite a un usuario con los permisos adecuados eliminar un registro de producción. Por seguridad, la eliminación solo es posible si el registro no ha sido validado.

## 3. Actor(es)
*   Usuario Administrador

## 4. Precondiciones
*   El usuario debe estar autenticado y tener el rol `ADMINISTRADOR`.
*   El registro de producción debe existir.
*   El registro de producción no debe estar validado (`validado` es `false`).

## 5. Seguridad y Autorización
*   **Autenticación Requerida:** Sí
*   **Roles Permitidos:** `ADMINISTRADOR`
*   **Condiciones de Propiedad:** N/A

## 6. Flujo Principal (Happy Path)
1.  El usuario envía una solicitud `DELETE` a `/api/produccion/{id}`.
2.  El sistema busca el `RegistroProduccion` por el `id` proporcionado.
3.  El servicio verifica que el registro encontrado no esté validado.
4.  Si no está validado, el servicio procede a eliminar la entidad de la base de datos.
5.  El sistema devuelve una respuesta `204 No Content` para indicar que la eliminación fue exitosa.

## 7. Flujos Alternativos
*   N/A

## 8. Validaciones

### 8.1. Validaciones de Reglas de Negocio (Capa de Servicio)
*   El `id` debe corresponder a un registro existente.
*   El registro no puede ser eliminado si ya ha sido validado (`validado == true`).

## 9. Flujos de Excepción

*   **9.1. Registro No Encontrado**
    *   **Condición:** El `id` no corresponde a ningún registro.
    *   **Excepción Java:** `RegistroProduccionNotFoundException`
    *   **Respuesta HTTP:** `404 Not Found`

*   **9.2. Registro Ya Validado**
    *   **Condición:** Se intenta eliminar un registro que ya ha sido validado.
    *   **Excepción Java:** `RegistroProduccionValidatedException`
    *   **Respuesta HTTP:** `409 Conflict`
    *   **Mensaje:** "El registro de producción con ID {id} ya ha sido validado y no puede ser eliminado."

## 10. Postcondiciones
*   La fila correspondiente al registro de producción es eliminada permanentemente de la base de datos.

## 11. DTOs Involucrados
*   **Request DTO:** N/A
*   **Response DTO:** N/A

## 12. Endpoint REST
*   **Método:** `DELETE`
*   **URL:** `/api/produccion/{id}`

## 13. Referencias
*   **Diagrama de Secuencia:** `docs/diagrams/produccion/sequence_eliminar_produccion.puml`
*   **Diagrama de Clases:** `docs/diagrams/produccion/class_diagram_produccion.puml`
*   **Diagrama ER:** `docs/diagrams/general/er_diagram_completo.puml`

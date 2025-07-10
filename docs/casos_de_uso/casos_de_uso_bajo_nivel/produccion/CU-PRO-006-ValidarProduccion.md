# CU-PRO-006: Validar Registro de Producción

## 1. Dominio
Producción

## 2. Descripción
Permite a un usuario con rol de Gerente o Administrador marcar un registro de producción como 'validado'. Esta acción confirma que los datos son correctos y bloquea el registro contra futuras modificaciones o eliminaciones, asegurando la integridad de los datos para la nómina y reportes.

## 3. Actor(es)
*   Usuario Administrador
*   Usuario Gerente

## 4. Precondiciones
*   El usuario debe estar autenticado y tener el rol `ADMINISTRADOR` o `GERENTE`.
*   El registro de producción debe existir.

## 5. Seguridad y Autorización
*   **Autenticación Requerida:** Sí
*   **Roles Permitidos:** `ADMINISTRADOR`, `GERENTE`
*   **Condiciones de Propiedad:** N/A

## 6. Flujo Principal (Happy Path)
1.  El usuario envía una solicitud `PATCH` a `/api/produccion/{id}/validar`.
2.  El sistema busca el `RegistroProduccion` por el `id` proporcionado.
3.  Si el registro existe y su campo `validado` es `false`, el servicio lo actualiza a `true`.
4.  El sistema persiste el cambio en la base de datos.
5.  El sistema devuelve una respuesta `200 OK` con el `RegistroProduccionDTO` actualizado, mostrando el nuevo estado de validación.

## 7. Flujos Alternativos
*   **7.1. Registro ya está validado**
    *   **Condición:** El registro encontrado ya tiene el campo `validado` como `true`.
    *   **Pasos:** El servicio no realiza ningún cambio en la base de datos y simplemente devuelve el registro actual con un código `200 OK` (la operación es idempotente).

## 8. Validaciones

### 8.1. Validaciones de Reglas de Negocio (Capa de Servicio)
*   El `id` debe corresponder a un registro de producción existente.

## 9. Flujos de Excepción

*   **9.1. Registro No Encontrado**
    *   **Condición:** El `id` no corresponde a ningún registro.
    *   **Excepción Java:** `RegistroProduccionNotFoundException`
    *   **Respuesta HTTP:** `404 Not Found`

## 10. Postcondiciones
*   El campo `validado` del registro de producción se establece en `true`.
*   El registro ya no puede ser modificado (`CU-PRO-004`) ni eliminado (`CU-PRO-005`).

## 11. DTOs Involucrados
*   **Request DTO:** N/A
*   **Response DTO:** `RegistroProduccionDTO`

## 12. Endpoint REST
*   **Método:** `PATCH`
*   **URL:** `/api/produccion/{id}/validar`

## 13. Referencias
*   **Diagrama de Clases:** `docs/diagrams/produccion/class_diagram_produccion.puml` (ver método `validarRegistro`)
*   **Diagrama ER:** `docs/diagrams/general/er_diagram_completo.puml` (ver campo `validado` en `RegistroProduccion`)

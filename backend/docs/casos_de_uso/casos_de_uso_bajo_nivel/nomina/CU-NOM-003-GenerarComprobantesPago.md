# CU-NOM-003: Generar Comprobantes de Pago

## 1. Dominio
Nómina

## 2. Descripción
Permite a un Gerente generar los comprobantes de pago en formato PDF para todos los empleados incluidos en un período de nómina que ya ha sido calculado.

## 3. Actor(es)
*   Usuario Gerente

## 4. Precondiciones
*   El usuario debe estar autenticado y tener el rol `GERENTE`.
*   El `PeriodoNomina` debe estar en estado `CALCULADO`.
*   No deben existir comprobantes de pago previos para este período.

## 5. Seguridad y Autorización
*   **Autenticación Requerida:** Sí
*   **Roles Permitidos:** `GERENTE`
*   **Condiciones de Propiedad:** N/A

## 6. Flujo Principal (Happy Path)
1.  El Gerente envía una solicitud `POST` a `/api/nomina/periodos/{id}/generar-comprobantes`.
2.  El sistema busca el `PeriodoNomina` y verifica que su estado sea `CALCULADO`.
3.  El sistema obtiene todos los `CalculoNomina` asociados a ese período.
4.  Para cada `CalculoNomina`:
    a. Se invoca un servicio de generación de documentos (ej. `PdfGeneratorService`).
    b. Se crea un archivo PDF con el desglose del pago.
    c. Se guarda el archivo en una ubicación segura.
    d. Se crea una entidad `ComprobantePago` con la ruta al archivo y otros metadatos.
5.  El sistema persiste todas las entidades `ComprobantePago`.
6.  El sistema actualiza el estado del `PeriodoNomina` a `PAGADO`.
7.  El sistema devuelve una respuesta `200 OK` con una lista de los `ComprobantePagoDTO` generados.

## 7. Flujos Alternativos
*   N/A

## 8. Validaciones

### 8.1. Validaciones de Reglas de Negocio (Capa de Servicio)
*   El `PeriodoNomina` debe existir y estar en estado `CALCULADO`.

## 9. Flujos de Excepción

*   **9.1. Período de Nómina No Encontrado o en Estado Inválido**
    *   **Condición:** El `id` del período no existe o su estado no es `CALCULADO`.
    *   **Excepción Java:** `PeriodoNominaInvalidoException`
    *   **Respuesta HTTP:** `404 Not Found` o `409 Conflict`

*   **9.2. Error en la Generación de PDF**
    *   **Condición:** Falla el servicio de generación de documentos.
    *   **Excepción Java:** `PdfGenerationException` (personalizada)
    *   **Respuesta HTTP:** `500 Internal Server Error`

## 10. Postcondiciones
*   Se crean archivos PDF para cada comprobante de pago.
*   Se crean nuevas filas en la tabla `comprobante_pago`.
*   El estado del `PeriodoNomina` se actualiza a `PAGADO`.

## 11. DTOs Involucrados
*   **Request DTO:** N/A
*   **Response DTO:** `List<ComprobantePagoDTO>`

## 12. Endpoint REST
*   **Método:** `POST`
*   **URL:** `/api/nomina/periodos/{id}/generar-comprobantes`

## 13. Referencias
*   **Diagrama de Secuencia:** `docs/diagrams/nomina/sequence_generar_comprobantes.puml`
*   **Diagrama de Clases:** `docs/diagrams/nomina/class_diagram_nomina_corregido.puml`
*   **Diagrama ER:** `docs/diagrams/general/er_diagram_completo.puml`

# CU-NOM-002: Ajustar Cálculo de Nómina

## 1. Dominio
Nómina

## 2. Descripción
Permite a un usuario Gerente realizar ajustes manuales (bonificaciones, descuentos, correcciones) a un cálculo de nómina individual antes de que el período sea cerrado o pagado.

## 3. Actor(es)
*   Usuario Gerente

## 4. Precondiciones
*   El usuario debe estar autenticado y tener el rol `GERENTE`.
*   Debe existir un `CalculoNomina` para el empleado y período a ajustar.
*   El `PeriodoNomina` asociado debe estar en estado `CALCULADO`.

## 5. Seguridad y Autorización
*   **Autenticación Requerida:** Sí
*   **Roles Permitidos:** `GERENTE`
*   **Condiciones de Propiedad:** N/A

## 6. Flujo Principal (Happy Path)
1.  El Gerente envía una solicitud `PATCH` a `/api/nomina/calculos/{id}/ajustar` con los detalles del ajuste.
2.  El sistema busca el `CalculoNomina` por su ID.
3.  El sistema verifica que el `PeriodoNomina` asociado esté en estado `CALCULADO`.
4.  El sistema aplica el ajuste (suma o resta al total bruto/neto) y actualiza los campos correspondientes.
5.  Se añade una nota en el campo `observaciones` con la justificación del ajuste.
6.  El sistema persiste la entidad `CalculoNomina` actualizada.
7.  El sistema devuelve una respuesta `200 OK` con el `CalculoNominaDTO` actualizado.

## 7. Flujos Alternativos
*   N/A

## 8. Validaciones

### 8.1. Validaciones de Formato/Sintaxis (Capa de DTO)
*   `concepto`: `@NotBlank`
*   `monto`: `@NotNull`, `@Positive`
*   `esDeduccion`: `@NotNull`
*   `justificacion`: `@NotBlank`

### 8.2. Validaciones de Reglas de Negocio (Capa de Servicio)
*   El `CalculoNomina` debe existir.
*   El `PeriodoNomina` no debe estar en estado `PAGADO` o `CERRADO`.

## 9. Flujos de Excepción

*   **9.1. Cálculo de Nómina No Encontrado**
    *   **Condición:** El `id` del cálculo no existe.
    *   **Excepción Java:** `CalculoNominaNotFoundException`
    *   **Respuesta HTTP:** `404 Not Found`

*   **9.2. Ajuste No Permitido**
    *   **Condición:** El período de nómina ya ha sido pagado o cerrado.
    *   **Excepción Java:** `AjusteNominaNoPermitidoException`
    *   **Respuesta HTTP:** `409 Conflict`
    *   **Mensaje:** "No se pueden realizar ajustes a una nómina que ya ha sido pagada o cerrada."

## 10. Postcondiciones
*   El registro `CalculoNomina` es modificado para reflejar el ajuste.
*   Queda un registro del ajuste en las observaciones para auditoría.

## 11. DTOs Involucrados
*   **Request DTO:** `AjusteNominaDTO`
*   **Response DTO:** `CalculoNominaDTO`

## 12. Endpoint REST
*   **Método:** `PATCH`
*   **URL:** `/api/nomina/calculos/{id}/ajustar`

## 13. Referencias
*   **Diagrama de Secuencia:** `docs/diagrams/nomina/sequence_ajustar_nomina.puml`
*   **Diagrama de Clases:** `docs/diagrams/nomina/class_diagram_nomina_corregido.puml`
*   **Diagrama ER:** `docs/diagrams/general/er_diagram_completo.puml`

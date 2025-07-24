# CU-NOM-004: Consultar Historial de Pagos

## 1. Dominio
Nómina

## 2. Descripción
Permite a un usuario consultar el historial de pagos. Un Gerente puede consultar el historial de cualquier empleado, mientras que un Empleado solo puede ver su propio historial.

## 3. Actor(es)
*   Usuario Gerente
*   Usuario Empleado

## 4. Precondiciones
*   El usuario debe estar autenticado.

## 5. Seguridad y Autorización
*   **Autenticación Requerida:** Sí
*   **Roles Permitidos:** `GERENTE`, `EMPLEADO`
*   **Condiciones de Propiedad:** Un `EMPLEADO` solo puede acceder a sus propios datos.

## 6. Flujo Principal (Happy Path)
1.  El usuario envía una solicitud `GET` a `/api/nomina/historial` con parámetros opcionales de filtrado (empleadoId, fechaInicio, fechaFin).
2.  **Si el rol es `EMPLEADO`**, el sistema ignora el `empleadoId` del query y usa el ID del usuario autenticado.
3.  El sistema consulta la tabla `calculo_nomina` aplicando los filtros correspondientes.
4.  El sistema mapea cada entidad `CalculoNomina` encontrada a un `CalculoNominaDTO`.
5.  El sistema devuelve una respuesta `200 OK` con una lista de `CalculoNominaDTO`.

## 7. Flujos Alternativos
*   N/A

## 8. Validaciones

### 8.1. Validaciones de Formato/Sintaxis (Capa de Controller)
*   Los parámetros de fecha deben tener un formato válido (ej. `YYYY-MM-DD`).

### 8.2. Validaciones de Reglas de Negocio (Capa de Servicio)
*   Si el rol es `GERENTE` y se provee un `empleadoId`, este debe ser válido.
*   `fechaInicio` no puede ser posterior a `fechaFin`.

## 9. Flujos de Excepción

*   **9.1. Empleado No Encontrado**
    *   **Condición:** (Solo para Gerente) El `empleadoId` proporcionado no existe.
    *   **Excepción Java:** `EmpleadoNotFoundException`
    *   **Respuesta HTTP:** `404 Not Found`

*   **9.2. Rango de Fechas Inválido**
    *   **Condición:** `fechaInicio` es posterior a `fechaFin`.
    *   **Excepción Java:** `IllegalArgumentException`
    *   **Respuesta HTTP:** `400 Bad Request`

## 10. Postcondiciones
*   Se devuelve una lista (posiblemente vacía) de los registros de pago que coinciden con los criterios de búsqueda.

## 11. DTOs Involucrados
*   **Request DTO:** N/A (parámetros de consulta)
*   **Response DTO:** `List<CalculoNominaDTO>`

## 12. Endpoint REST
*   **Método:** `GET`
*   **URL:** `/api/nomina/historial`

## 13. Referencias
*   **Diagrama de Secuencia:** `docs/diagrams/nomina/sequence_consultar_historial_nomina.puml`
*   **Diagrama de Clases:** `docs/diagrams/nomina/class_diagram_nomina_corregido.puml`
*   **Diagrama ER:** `docs/diagrams/general/er_diagram_completo.puml`

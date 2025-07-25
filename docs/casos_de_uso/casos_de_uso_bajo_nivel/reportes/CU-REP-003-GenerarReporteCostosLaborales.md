# CU-REP-003: Generar Reporte de Costos Laborales

## 1. Dominio
Reportes

## 2. Descripción
Permite a un `GERENTE` generar un reporte financiero que analiza los costos laborales en relación con la producción y las horas trabajadas, proporcionando KPIs clave como el costo por tonelada producida.

## 3. Actor(es)
*   Usuario Gerente

## 4. Precondiciones
*   El usuario debe estar autenticado y tener el rol `GERENTE`.
*   Los períodos de nómina para el rango de fechas solicitado deben estar en estado `PAGADO` o `CERRADO`.
*   Deben existir datos de producción para el mismo período.

## 5. Seguridad y Autorización
*   **Autenticación Requerida:** Sí
*   **Roles Permitidos:** `GERENTE`
*   **Condiciones de Propiedad:** N/A

## 6. Flujo Principal (Happy Path)
1.  El Gerente envía una solicitud `POST` a `/api/reportes/costos-laborales` con `ParametrosReporteDTO`.
2.  El sistema valida el DTO.
3.  El `ReporteService` invoca al `NominaService` para obtener los datos de los cálculos de nómina pagados en el período.
4.  El `ReporteService` invoca al `ProduccionService` para obtener el total de toneladas producidas en el mismo período.
5.  El servicio calcula los KPIs (ej. costo total de nómina / total de toneladas) y consolida los datos para el reporte.
6.  Se invoca al `GeneradorReporteService` para crear el archivo (PDF, Excel).
7.  Se crea y persiste la entidad `ReporteGenerado`.
8.  El sistema devuelve `201 Created` con el `ReporteDTO` y la URL de descarga.

## 7. Flujos Alternativos
*   N/A

## 8. Validaciones

### 8.1. Validaciones de Formato/Sintaxis (Capa de DTO)
*   `fechaInicio`: `@NotNull`
*   `fechaFin`: `@NotNull`
*   `formato`: `@NotNull`

### 8.2. Validaciones de Reglas de Negocio (Capa de Servicio)
*   `fechaInicio` no puede ser posterior a `fechaFin`.

## 9. Flujos de Excepción

*   **9.1. Datos Insuficientes**
    *   **Condición:** No se encuentran datos de nómina o producción para el período solicitado.
    *   **Excepción Java:** `DatosInsuficientesParaReporteException`
    *   **Respuesta HTTP:** `404 Not Found`
    *   **Mensaje:** "No hay suficientes datos de nómina o producción para generar el reporte en el período seleccionado."

*   **9.2. Error en la Generación del Archivo**
    *   **Excepción Java:** `ErrorGeneracionReporteException`
    *   **Respuesta HTTP:** `500 Internal Server Error`

## 10. Postcondiciones
*   Se crea un nuevo archivo de reporte de costos.
*   Se crea una nueva fila en la tabla `reporte_generado`.

## 11. DTOs Involucrados
*   **Request DTO:** `ParametrosReporteDTO`
*   **Response DTO:** `ReporteDTO`
*   **DTOs de Datos:** `ReporteCostosLaboralesDTO`, `CostoEmpleadoReporteDTO`, etc.

## 12. Endpoint REST
*   **Método:** `POST`
*   **URL:** `/api/reportes/costos-laborales`

## 13. Referencias
*   **Diagrama de Secuencia:** `docs/diagrams/reportes/sequence_reporte_costos_laborales.puml`
*   **Diagrama de Clases:** `docs/diagrams/reportes/class_diagram_reportes.puml`
*   **Diagrama ER:** `docs/diagrams/general/er_diagram_completo.puml`

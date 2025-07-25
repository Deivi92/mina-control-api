# CU-REP-004: Exportar Datos Operacionales

## 1. Dominio
Reportes

## 2. Descripción
Proporciona una funcionalidad para que un `GERENTE` pueda exportar datos brutos de diferentes dominios (Empleados, Producción, Asistencia, etc.) en formatos como CSV o JSON para análisis externo o copias de seguridad.

## 3. Actor(es)
*   Usuario Gerente

## 4. Precondiciones
*   El usuario debe estar autenticado y tener el rol `GERENTE`.

## 5. Seguridad y Autorización
*   **Autenticación Requerida:** Sí
*   **Roles Permitidos:** `GERENTE`
*   **Condiciones de Propiedad:** N/A

## 6. Flujo Principal (Happy Path)
1.  El Gerente envía una solicitud `POST` a `/api/reportes/exportar-datos` con un `DatosOperacionalesDTO`.
2.  El DTO especifica los `datasets` a exportar (ej. `["empleados", "produccion"]`), el `formato` (CSV, JSON) y un `rango de fechas`.
3.  El sistema valida el DTO.
4.  Para cada `dataset` solicitado, el `ReporteService` invoca al servicio del dominio correspondiente para obtener los datos brutos dentro del rango de fechas.
5.  El `GeneradorReporteService` recibe los datos y los convierte al formato solicitado.
6.  Si el formato es CSV y se solicitaron múltiples datasets, se genera un archivo ZIP que contiene varios archivos CSV.
7.  Se crea y persiste la entidad `ReporteGenerado` con los metadatos de la exportación.
8.  El sistema devuelve `201 Created` con el `ReporteDTO` y la URL de descarga del archivo (ZIP, CSV o JSON).

## 7. Flujos Alternativos
*   N/A

## 8. Validaciones

### 8.1. Validaciones de Formato/Sintaxis (Capa de DTO)
*   `datasets`: `@NotEmpty`, la lista no puede estar vacía.
*   `formato`: `@NotNull`, debe ser `CSV` o `JSON`.
*   `fechaInicio`: `@NotNull`
*   `fechaFin`: `@NotNull`

### 8.2. Validaciones de Reglas de Negocio (Capa de Servicio)
*   Los nombres en `datasets` deben corresponder a dominios exportables válidos.
*   `fechaInicio` no puede ser posterior a `fechaFin`.

## 9. Flujos de Excepción

*   **9.1. Dataset No Válido**
    *   **Condición:** Se solicita un dataset que no existe o no es exportable.
    *   **Excepción Java:** `ParametrosReporteInvalidosException`
    *   **Respuesta HTTP:** `400 Bad Request`
    *   **Mensaje:** "El dataset '[nombre]' no es válido para exportación."

*   **9.2. Error en la Generación del Archivo**
    *   **Excepción Java:** `ErrorGeneracionReporteException`
    *   **Respuesta HTTP:** `500 Internal Server Error`

## 10. Postcondiciones
*   Se crea un archivo (CSV, JSON, ZIP) con los datos brutos solicitados.
*   Se crea una nueva fila en la tabla `reporte_generado`.

## 11. DTOs Involucrados
*   **Request DTO:** `DatosOperacionalesDTO`
*   **Response DTO:** `ReporteDTO`

## 12. Endpoint REST
*   **Método:** `POST`
*   **URL:** `/api/reportes/exportar-datos`

## 13. Referencias
*   **Diagrama de Secuencia:** `docs/diagrams/reportes/sequence_exportar_datos.puml`
*   **Diagrama de Clases:** `docs/diagrams/reportes/class_diagram_reportes.puml`
*   **Diagrama ER:** `docs/diagrams/general/er_diagram_completo.puml`

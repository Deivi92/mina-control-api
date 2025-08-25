# CU-REP-001: Generar Reporte de Producción

## 1. Dominio
Reportes

## 2. Descripción
Permite a un usuario con rol de `GERENTE` generar un reporte consolidado de la producción de carbón en un rango de fechas específico, con la opción de exportarlo en diferentes formatos (PDF, Excel, CSV).

## 3. Actor(es)
*   Usuario Gerente

## 4. Precondiciones
*   El usuario debe estar autenticado y tener el rol `GERENTE`.
*   Deben existir registros de producción en el sistema para el rango de fechas solicitado.

## 5. Seguridad y Autorización
*   **Autenticación Requerida:** Sí
*   **Roles Permitidos:** `GERENTE`
*   **Condiciones de Propiedad:** N/A

## 6. Flujo Principal (Happy Path)
1.  El Gerente envía una solicitud `POST` a `/api/reportes/produccion` con un `ParametrosReporteDTO`.
2.  El DTO incluye el rango de fechas (`fechaInicio`, `fechaFin`), el formato deseado (`formato`) y opcionalmente filtros adicionales (ej. `empleadosIds`).
3.  El sistema valida el DTO (fechas válidas, formato soportado).
4.  El `ReporteService` invoca al `ProduccionService` para obtener los datos de producción relevantes para el período y filtros especificados.
5.  El `ReporteService` consolida y procesa los datos (cálculo de totales, promedios, etc.).
6.  Se invoca a un `GeneradorReporteService` que toma los datos procesados y el formato deseado.
7.  El `GeneradorReporteService` crea el archivo del reporte (ej. un PDF) y lo almacena en una ubicación persistente.
8.  El `ReporteService` crea una entidad `ReporteGenerado` con los metadatos del reporte (tipo, fecha, ruta del archivo, etc.) y la guarda en la base de datos.
9.  El sistema devuelve una respuesta `201 Created` con un `ReporteDTO` que contiene la información del reporte generado, incluyendo una URL de descarga.

## 7. Flujos Alternativos
*   N/A

## 8. Validaciones

### 8.1. Validaciones de Formato/Sintaxis (Capa de DTO)
*   `fechaInicio`: `@NotNull`
*   `fechaFin`: `@NotNull`
*   `formato`: `@NotNull`, debe ser un valor del enum `FormatoReporte`.

### 8.2. Validaciones de Reglas de Negocio (Capa de Servicio)
*   `fechaInicio` no puede ser posterior a `fechaFin`.
*   El rango de fechas no debe exceder un límite máximo (ej. 1 año) para evitar consultas excesivas.

## 9. Flujos de Excepción

*   **9.1. Datos de Entrada Inválidos**
    *   **Condición:** El DTO no cumple las validaciones.
    *   **Excepción Java:** `MethodArgumentNotValidException`, `ParametrosReporteInvalidosException`
    *   **Respuesta HTTP:** `400 Bad Request`

*   **9.2. Error en la Generación del Archivo**
    *   **Condición:** Falla la creación del archivo PDF, Excel o CSV.
    *   **Excepción Java:** `ErrorGeneracionReporteException`
    *   **Respuesta HTTP:** `500 Internal Server Error`

## 10. Postcondiciones
*   Se crea un nuevo archivo de reporte (PDF, Excel, etc.) en el sistema de archivos.
*   Se crea una nueva fila en la tabla `reporte_generado`.

## 11. DTOs Involucrados
*   **Request DTO:** `ParametrosReporteDTO`
*   **Response DTO:** `ReporteDTO`
*   **DTOs de Datos:** `ReporteProduccionDTO`, `ProduccionEmpleadoReporteDTO`, etc.

## 12. Endpoint REST
*   **Método:** `POST`
*   **URL:** `/api/reportes/produccion`

## 13. Referencias
*   **Diagrama de Secuencia:** `docs/diagrams/reportes/sequence_reporte_produccion.puml`
*   **Diagrama de Clases:** `docs/diagrams/reportes/class_diagram_reportes.puml`
*   **Diagrama ER:** `docs/diagrams/general/er_diagram_completo.puml`

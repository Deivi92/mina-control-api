# CU-REP-002: Generar Reporte de Asistencia

## 1. Dominio
Reportes

## 2. Descripción
Permite a un usuario `GERENTE` generar un reporte consolidado sobre la asistencia de los empleados, incluyendo puntualidad, ausentismo y horas trabajadas en un período determinado.

## 3. Actor(es)
*   Usuario Gerente

## 4. Precondiciones
*   El usuario debe estar autenticado y tener el rol `GERENTE`.
*   Deben existir registros de asistencia para el rango de fechas solicitado.

## 5. Seguridad y Autorización
*   **Autenticación Requerida:** Sí
*   **Roles Permitidos:** `GERENTE`
*   **Condiciones de Propiedad:** N/A

## 6. Flujo Principal (Happy Path)
1.  El Gerente envía una solicitud `POST` a `/api/reportes/asistencia` con `ParametrosReporteDTO`.
2.  El sistema valida el DTO.
3.  El `ReporteService` invoca al `TurnoService` (o `AsistenciaService`) para obtener los datos de asistencia del período.
4.  El `ReporteService` procesa los datos para calcular métricas clave (ej. porcentaje de asistencia, total de tardanzas, ausencias justificadas/injustificadas).
5.  Se invoca al `GeneradorReporteService` para crear el archivo en el formato solicitado (PDF, Excel).
6.  El `ReporteService` crea y persiste una entidad `ReporteGenerado` con los metadatos.
7.  El sistema devuelve `201 Created` con un `ReporteDTO` que incluye la URL de descarga.

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

*   **9.1. Datos de Entrada Inválidos**
    *   **Excepción Java:** `MethodArgumentNotValidException`, `ParametrosReporteInvalidosException`
    *   **Respuesta HTTP:** `400 Bad Request`

*   **9.2. Error en la Generación del Archivo**
    *   **Excepción Java:** `ErrorGeneracionReporteException`
    *   **Respuesta HTTP:** `500 Internal Server Error`

## 10. Postcondiciones
*   Se crea un nuevo archivo de reporte de asistencia.
*   Se crea una nueva fila en la tabla `reporte_generado`.

## 11. DTOs Involucrados
*   **Request DTO:** `ParametrosReporteDTO`
*   **Response DTO:** `ReporteDTO`
*   **DTOs de Datos:** `ReporteAsistenciaDTO`, `AsistenciaEmpleadoReporteDTO`, etc.

## 12. Endpoint REST
*   **Método:** `POST`
*   **URL:** `/api/reportes/asistencia`

## 13. Referencias
*   **Diagrama de Secuencia:** `docs/diagrams/reportes/sequence_reporte_asistencia.puml`
*   **Diagrama de Clases:** `docs/diagrams/reportes/class_diagram_reportes.puml`
*   **Diagrama ER:** `docs/diagrams/general/er_diagram_completo.puml`

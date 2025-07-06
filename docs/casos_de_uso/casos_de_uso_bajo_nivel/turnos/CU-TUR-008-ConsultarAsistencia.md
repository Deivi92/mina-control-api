# CU-TUR-008: Consultar Asistencia

## 1. Dominio
Turnos

## 2. Descripción
Permite consultar los registros de asistencia de un empleado para un rango de fechas determinado.

## 3. Actor(es)
*   Usuario Administrador
*   Usuario Gerente

## 4. Precondiciones
*   El usuario debe estar autenticado.

## 5. Seguridad y Autorización
*   **Autenticación Requerida:** Sí
*   **Roles Permitidos:** `ADMINISTRADOR`, `GERENTE`

## 6. Flujo Principal (Happy Path)
1.  El usuario envía `GET` a `/api/asistencia/consultar` con parámetros de consulta (`empleadoId`, `fechaInicio`, `fechaFin`).
2.  El sistema busca en `RegistroAsistenciaRepository` los registros que coincidan con los filtros.
3.  El servicio mapea la lista de entidades a una lista de `RegistroAsistenciaDTO`.
4.  Devuelve `200 OK` con la lista de DTOs.

## 7. Flujos Alternativos
*   Si no se proporciona `empleadoId`, se devuelven los registros de todos los empleados para el rango de fechas.

## 8. Validaciones
*   `fechaInicio` debe ser anterior o igual a `fechaFin`.

## 9. Flujos de Excepción
*   **9.1. Rango de Fechas Inválido**
    *   **Respuesta HTTP:** `400 Bad Request`

## 10. Postcondiciones
*   Se devuelve una lista de registros de asistencia.

## 11. DTOs Involucrados
*   **Request DTO:** N/A (parámetros de consulta)
*   **Response DTO:** `List<RegistroAsistenciaDTO>`

## 12. Endpoint REST
*   **Método:** `GET`
*   **URL:** `/api/asistencia/consultar`

## 13. Referencias
*   **Diagrama de Secuencia:** `docs/diagrams/turnos/sequence_consultar_asistencia.puml`
*   **Diagrama de Clases:** `docs/diagrams/turnos/class_diagram_turnos_corregido.puml`

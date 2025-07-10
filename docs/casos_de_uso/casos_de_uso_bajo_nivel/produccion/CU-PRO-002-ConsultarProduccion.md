# CU-PRO-002: Consultar Registros de Producción

## 1. Dominio
Producción

## 2. Descripción
Permite a los usuarios consultar la lista de registros de producción, con la capacidad de filtrar por un rango de fechas y/o por un empleado específico.

## 3. Actor(es)
*   Usuario Administrador
*   Usuario Gerente
*   Usuario Supervisor

## 4. Precondiciones
*   El usuario debe estar autenticado y tener los roles adecuados.

## 5. Seguridad y Autorización
*   **Autenticación Requerida:** Sí
*   **Roles Permitidos:** `ADMINISTRADOR`, `GERENTE`, `SUPERVISOR`
*   **Condiciones de Propiedad:** N/A

## 6. Flujo Principal (Happy Path) - Listar Todos
1.  El usuario envía una solicitud `GET` a `/api/produccion` sin parámetros de filtro.
2.  El sistema invoca al servicio para obtener todos los registros de producción.
3.  El servicio consulta el repositorio para obtener la lista completa de entidades `RegistroProduccion`.
4.  El servicio mapea cada entidad a su correspondiente `RegistroProduccionDTO`.
5.  El sistema devuelve una respuesta `200 OK` con una lista de `RegistroProduccionDTO`.

## 7. Flujos Alternativos

*   **7.1. Consultar por Rango de Fechas**
    *   **Condición:** El usuario envía la solicitud `GET` con los parámetros `fechaInicio` y `fechaFin`.
    *   **Pasos:** El servicio llama al método del repositorio que filtra los registros entre las fechas proporcionadas (`findByFechaBetween`).

*   **7.2. Consultar por Empleado**
    *   **Condición:** El usuario envía la solicitud `GET` con el parámetro `empleadoId`.
    *   **Pasos:** El servicio llama al método del repositorio que filtra los registros por el ID del empleado (`findByEmpleadoId`).

*   **7.3. Consultar por Empleado y Rango de Fechas**
    *   **Condición:** El usuario envía la solicitud `GET` con los parámetros `empleadoId`, `fechaInicio` y `fechaFin`.
    *   **Pasos:** El servicio combina los filtros para una búsqueda más específica.

## 8. Validaciones

### 8.1. Validaciones de Formato/Sintaxis (Capa de Controller/DTO)
*   Los parámetros de fecha (`fechaInicio`, `fechaFin`) deben tener un formato válido (ej. `YYYY-MM-DD`).
*   `empleadoId` debe ser un número válido.

### 8.2. Validaciones de Reglas de Negocio (Capa de Servicio)
*   Si ambas fechas están presentes, `fechaInicio` no puede ser posterior a `fechaFin`.

## 9. Flujos de Excepción

*   **9.1. Formato de Parámetro Inválido**
    *   **Condición:** Las fechas o el ID de empleado no tienen el formato correcto.
    *   **Excepción Java:** `MethodArgumentTypeMismatchException`
    *   **Respuesta HTTP:** `400 Bad Request`
    *   **Mensaje:** "El parámetro '...' tiene un formato inválido."

*   **9.2. Rango de Fechas Inválido**
    *   **Condición:** `fechaInicio` es posterior a `fechaFin`.
    *   **Excepción Java:** `IllegalArgumentException`
    *   **Respuesta HTTP:** `400 Bad Request`
    *   **Mensaje:** "La fecha de inicio no puede ser posterior a la fecha de fin."

## 10. Postcondiciones
*   El sistema devuelve una lista (posiblemente vacía) de registros de producción que cumplen con los criterios de filtrado.

## 11. DTOs Involucrados
*   **Request DTO:** N/A (parámetros de consulta)
*   **Response DTO:** `List<RegistroProduccionDTO>`

## 12. Endpoint REST
*   **Método:** `GET`
*   **URL:** `/api/produccion`
*   **Parámetros de Consulta:** `empleadoId` (opcional), `fechaInicio` (opcional), `fechaFin` (opcional).

## 13. Referencias
*   **Diagrama de Secuencia:** `docs/diagrams/produccion/sequence_consultar_produccion_empleado.puml`, `docs/diagrams/produccion/sequence_consultar_produccion_fecha.puml`
*   **Diagrama de Clases:** `docs/diagrams/produccion/class_diagram_produccion.puml`
*   **Diagrama ER:** `docs/diagrams/general/er_diagram_completo.puml`

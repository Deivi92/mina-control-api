# CU-EMP-002: Listar Empleados

## 1. Dominio
Empleados

## 2. Descripción
Permite a un usuario administrador consultar la lista de todos los empleados registrados en el sistema, con la posibilidad de filtrar por estado o buscar por cargo.

## 3. Actor(es)
*   Usuario Administrador

## 4. Precondiciones
*   El usuario debe tener el rol de `ADMINISTRADOR`.

## 5. Seguridad y Autorización
*   **Autenticación Requerida:** Sí
*   **Roles Permitidos:** `ADMINISTRADOR`
*   **Condiciones de Propiedad:** N/A

## 6. Flujo Principal (Happy Path)
1.  El Usuario Administrador envía una solicitud `GET` a `/api/empleados`.
2.  El sistema invoca el método `obtenerTodosEmpleados()` del servicio de empleados.
3.  El servicio consulta el repositorio para obtener todos los empleados de la base de datos.
4.  El repositorio devuelve una lista de entidades `Empleado`.
5.  El servicio convierte cada entidad `Empleado` a un `EmpleadoDTO`.
6.  El sistema devuelve una respuesta `200 OK` con una lista de `EmpleadoDTO`.

## 7. Flujos Alternativos
*   **7.1. Filtrar por Estado:**
    *   **Condición:** El Usuario Administrador envía una solicitud `GET` a `/api/empleados/activos` (o `/api/empleados?estado=ACTIVO`).
    *   **Pasos:**
        1.  El sistema invoca el método `obtenerEmpleadosActivos()` (o `obtenerEmpleadosPorEstado(ACTIVO)`) del servicio de empleados.
        2.  El servicio consulta el repositorio filtrando por el estado `ACTIVO`.
        3.  El repositorio devuelve una lista de entidades `Empleado` filtradas.
        4.  El servicio convierte cada entidad `Empleado` a un `EmpleadoDTO`.
        5.  El sistema devuelve una respuesta `200 OK` con la lista de `EmpleadoDTO` filtrada.

*   **7.2. Buscar por Cargo:**
    *   **Condición:** El Usuario Administrador envía una solicitud `GET` a `/api/empleados/buscar?cargo=operador`.
    *   **Pasos:**
        1.  El sistema invoca el método `buscarEmpleadosPorCargo("operador")` del servicio de empleados.
        2.  El servicio consulta el repositorio buscando empleados cuyo cargo contenga la cadena "operador" (ignorando mayúsculas/minúsculas).
        3.  El repositorio devuelve una lista de entidades `Empleado` que coinciden.
        4.  El servicio convierte cada entidad `Empleado` a un `EmpleadoDTO`.
        5.  El sistema devuelve una respuesta `200 OK` con la lista de `EmpleadoDTO` filtrada.

## 8. Validaciones

### 8.1. Validaciones de Formato/Sintaxis (Capa de DTO)
*   N/A (Los parámetros de consulta son manejados por el framework o validados en la capa de servicio si son complejos).

### 8.2. Validaciones de Reglas de Negocio (Capa de Servicio)
*   N/A (La lógica de filtrado es parte del flujo principal y no implica reglas de negocio que puedan fallar con una excepción específica).

## 9. Flujos de Excepción

*   **9.1. Error Interno del Servidor**
    *   **Condición:** Cualquier otro error inesperado durante el procesamiento (ej. problema de conexión a la BD).
    *   **Excepción Java:** `RuntimeException` (o subclase específica, manejada por `@ControllerAdvice`)
    *   **Respuesta HTTP:** `500 Internal Server Error`
    *   **Cuerpo de Respuesta (ErrorResponseDTO):**
        ```json
        {
            "timestamp": "...",
            "status": 500,
            "error": "Internal Server Error",
            "message": "Error interno del servidor.",
            "path": "/api/empleados"
        }
        ```

## 10. Postcondiciones
*   El sistema devuelve una lista (posiblemente vacía) de empleados que cumplen con los criterios de búsqueda o filtrado.

## 11. DTOs Involucrados
*   **Request DTO:** N/A
*   **Response DTO:** `EmpleadoDTO` (lista)

## 12. Endpoint REST
*   **Método:** `GET`
*   **URL:** `/api/empleados`, `/api/empleados/activos`, `/api/empleados/buscar`

## 13. Referencias
*   **Diagrama de Secuencia:** `docs/diagrams/empleados/sequence_listar_empleados.puml`
*   **Diagrama de Clases:** `docs/diagrams/general/class_diagram_completo.puml`
*   **Diagrama ER:** `docs/diagrams/general/er_diagram_completo.puml`

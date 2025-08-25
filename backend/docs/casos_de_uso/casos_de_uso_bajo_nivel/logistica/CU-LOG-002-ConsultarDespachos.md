# CU-LOG-002: Consultar Despachos

## 1. Dominio
Logística

## 2. Descripción
Permite a los usuarios consultar la lista de despachos, con la capacidad de filtrar por un rango de fechas, estado o destino.

## 3. Actor(es)
*   Usuario Administrador
*   Usuario Supervisor
*   Usuario Gerente

## 4. Precondiciones
*   El usuario debe estar autenticado y tener los roles adecuados.

## 5. Seguridad y Autorización
*   **Autenticación Requerida:** Sí
*   **Roles Permitidos:** `ADMINISTRADOR`, `SUPERVISOR`, `GERENTE`
*   **Condiciones de Propiedad:** N/A

## 6. Flujo Principal (Happy Path) - Listar Todos
1.  El usuario envía una solicitud `GET` a `/api/logistica/despachos` sin parámetros.
2.  El sistema invoca al servicio para obtener todos los despachos.
3.  El servicio consulta el repositorio para obtener la lista completa de entidades `Despacho`.
4.  El servicio mapea cada entidad a su `DespachoListDTO` correspondiente.
5.  El sistema devuelve una respuesta `200 OK` con una lista de `DespachoListDTO`.

## 7. Flujos Alternativos

*   **7.1. Filtrar por Rango de Fechas**
    *   **Condición:** La solicitud `GET` incluye `fechaInicio` y `fechaFin`.
    *   **Pasos:** El servicio filtra los despachos cuya `fechaProgramada` esté dentro del rango.

*   **7.2. Filtrar por Estado**
    *   **Condición:** La solicitud `GET` incluye el parámetro `estado`.
    *   **Pasos:** El servicio filtra los despachos que coincidan con el estado proporcionado.

*   **7.3. Filtrar por Destino**
    *   **Condición:** La solicitud `GET` incluye el parámetro `destino`.
    *   **Pasos:** El servicio realiza una búsqueda parcial (LIKE) por el destino.

## 8. Validaciones

### 8.1. Validaciones de Formato/Sintaxis (Capa de Controller)
*   Los parámetros de fecha deben tener un formato válido (ej. `YYYY-MM-DD`).
*   El `estado` debe ser uno de los valores permitidos por el enum `EstadoDespacho`.

### 8.2. Validaciones de Reglas de Negocio (Capa de Servicio)
*   Si ambas fechas están presentes, `fechaInicio` no puede ser posterior a `fechaFin`.

## 9. Flujos de Excepción

*   **9.1. Formato de Parámetro Inválido**
    *   **Condición:** Un parámetro de fecha o estado no tiene el formato correcto.
    *   **Excepción Java:** `MethodArgumentTypeMismatchException`
    *   **Respuesta HTTP:** `400 Bad Request`

*   **9.2. Rango de Fechas Inválido**
    *   **Condición:** `fechaInicio` es posterior a `fechaFin`.
    *   **Excepción Java:** `IllegalArgumentException`
    *   **Respuesta HTTP:** `400 Bad Request`

## 10. Postcondiciones
*   Se devuelve una lista (posiblemente vacía) de despachos que cumplen los criterios.

## 11. DTOs Involucrados
*   **Request DTO:** N/A (parámetros de consulta)
*   **Response DTO:** `List<DespachoListDTO>`

## 12. Endpoint REST
*   **Método:** `GET`
*   **URL:** `/api/logistica/despachos`
*   **Parámetros de Consulta:** `fechaInicio`, `fechaFin`, `estado`, `destino` (todos opcionales).

## 13. Referencias
*   **Diagrama de Secuencia:** `docs/diagrams/logistica/sequence_consultar_despachos.puml`
*   **Diagrama de Clases:** `docs/diagrams/logistica/class_diagram_logistica.puml`
*   **Diagrama ER:** `docs/diagrams/general/er_diagram_completo.puml`

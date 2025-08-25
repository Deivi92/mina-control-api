# CU-TUR-002: Listar Tipos de Turno

## 1. Dominio
Turnos

## 2. Descripción
Permite a los usuarios consultar la lista de todos los tipos de turno disponibles en el sistema, tanto activos como inactivos.

## 3. Actor(es)
*   Usuario Administrador
*   Usuario Gerente

## 4. Precondiciones
*   El usuario debe estar autenticado.

## 5. Seguridad y Autorización
*   **Autenticación Requerida:** Sí
*   **Roles Permitidos:** `ADMINISTRADOR`, `GERENTE`

## 6. Flujo Principal (Happy Path)
1.  El usuario envía una solicitud `GET` a `/api/turnos`.
2.  El sistema invoca al `TurnoService` para obtener todos los tipos de turno.
3.  El servicio consulta el `TipoTurnoRepository`.
4.  El repositorio devuelve una lista de todas las entidades `TipoTurno`.
5.  El servicio mapea cada entidad `TipoTurno` a un `TipoTurnoDTO`.
6.  El sistema devuelve una respuesta `200 OK` con una lista de `TipoTurnoDTO`.

## 7. Flujos Alternativos

*   **7.1. Listar solo turnos activos**
    *   **Condición:** El usuario envía una solicitud `GET` a `/api/turnos?activo=true`.
    *   **Pasos:** El servicio llama al método `findByActivo(true)` del repositorio y devuelve solo los turnos activos.

## 8. Validaciones
*   N/A

## 9. Flujos de Excepción

*   **9.1. Error Interno del Servidor**
    *   **Condición:** Ocurre un error inesperado en el servidor (ej. fallo de la base de datos).
    *   **Respuesta HTTP:** `500 Internal Server Error`.

## 10. Postcondiciones
*   Se devuelve una lista (posiblemente vacía) de los tipos de turno.

## 11. DTOs Involucrados
*   **Request DTO:** N/A
*   **Response DTO:** `List<TipoTurnoDTO>`

## 12. Endpoint REST
*   **Método:** `GET`
*   **URL:** `/api/turnos`

## 13. Referencias
*   **Diagrama de Secuencia:** `docs/diagrams/turnos/sequence_listar_turnos.puml`
*   **Diagrama de Clases:** `docs/diagrams/turnos/class_diagram_turnos_corregido.puml`

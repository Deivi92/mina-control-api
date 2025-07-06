# CU-TUR-003: Obtener Tipo de Turno por ID

## 1. Dominio
Turnos

## 2. Descripción
Permite a un usuario consultar los detalles de un tipo de turno específico utilizando su ID.

## 3. Actor(es)
*   Usuario Administrador
*   Usuario Gerente

## 4. Precondiciones
*   El usuario debe estar autenticado.
*   El tipo de turno debe existir en la base de datos.

## 5. Seguridad y Autorización
*   **Autenticación Requerida:** Sí
*   **Roles Permitidos:** `ADMINISTRADOR`, `GERENTE`

## 6. Flujo Principal (Happy Path)
1.  El usuario envía una solicitud `GET` a `/api/turnos/{id}`.
2.  El sistema busca el tipo de turno por el `id` proporcionado.
3.  Si se encuentra, el sistema convierte la entidad `TipoTurno` a un `TipoTurnoDTO`.
4.  El sistema devuelve una respuesta `200 OK` con el `TipoTurnoDTO`.

## 7. Flujos Alternativos
*   N/A

## 8. Validaciones
*   N/A

## 9. Flujos de Excepción

*   **9.1. Tipo de Turno No Encontrado**
    *   **Condición:** El `id` no corresponde a ningún tipo de turno existente.
    *   **Excepción Java:** `TurnoNoEncontradoException`
    *   **Respuesta HTTP:** `404 Not Found`

## 10. Postcondiciones
*   Se devuelve la información del tipo de turno solicitado.

## 11. DTOs Involucrados
*   **Request DTO:** N/A
*   **Response DTO:** `TipoTurnoDTO`

## 12. Endpoint REST
*   **Método:** `GET`
*   **URL:** `/api/turnos/{id}`

## 13. Referencias
*   **Diagrama de Secuencia:** `docs/diagrams/turnos/sequence_obtener_turno_por_id.puml`
*   **Diagrama de Clases:** `docs/diagrams/turnos/class_diagram_turnos_corregido.puml`

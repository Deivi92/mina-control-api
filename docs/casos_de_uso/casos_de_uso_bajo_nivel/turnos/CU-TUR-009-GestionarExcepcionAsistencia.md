# CU-TUR-009: Gestionar Excepción de Asistencia

## 1. Dominio
Turnos

## 2. Descripción
Permite a un administrador registrar una excepción de asistencia para un empleado en una fecha específica (ej. permiso, ausencia justificada), creando o actualizando un registro de asistencia sin necesidad de una entrada/salida manual.

## 3. Actor(es)
*   Usuario Administrador

## 4. Precondiciones
*   El usuario debe estar autenticado y tener rol `ADMINISTRADOR`.
*   El empleado debe existir.

## 5. Seguridad y Autorización
*   **Autenticación Requerida:** Sí
*   **Roles Permitidos:** `ADMINISTRADOR`

## 6. Flujo Principal (Happy Path)
1.  El usuario envía `POST` a `/api/asistencia/excepciones` con `ExcepcionAsistenciaDTO` (`empleadoId`, `fecha`, `tipoExcepcion`, `motivo`).
2.  El sistema valida el DTO.
3.  Busca si ya existe un registro de asistencia para ese empleado y fecha.
4.  **Si no existe:** Crea un nuevo `RegistroAsistencia` con el estado correspondiente (`PERMISO`, `AUSENTE`, etc.) y sin horas de entrada/salida.
5.  **Si ya existe:** Actualiza el estado y las observaciones del registro existente.
6.  Persiste la entidad y devuelve `200 OK` con el `RegistroAsistenciaDTO` modificado.

## 7. Flujos Alternativos
*   N/A

## 8. Validaciones
*   El `empleadoId` debe ser válido.
*   El `tipoExcepcion` debe ser un valor válido del enum `EstadoAsistencia`.

## 9. Flujos de Excepción

*   **9.1. Empleado No Encontrado**
    *   **Respuesta HTTP:** `404 Not Found`.

*   **9.2. Datos de Excepción Inválidos**
    *   **Respuesta HTTP:** `400 Bad Request`.

## 10. Postcondiciones
*   Se crea o actualiza un registro en la tabla `registros_asistencia` para reflejar la excepción.

## 11. DTOs Involucrados
*   **Request DTO:** `ExcepcionAsistenciaDTO`
*   **Response DTO:** `RegistroAsistenciaDTO`

## 12. Endpoint REST
*   **Método:** `POST`
*   **URL:** `/api/asistencia/excepciones`

## 13. Referencias
*   **Diagrama de Secuencia:** `docs/diagrams/turnos/sequence_gestionar_excepciones.puml`
*   **Diagrama de Clases:** `docs/diagrams/turnos/class_diagram_turnos_corregido.puml`

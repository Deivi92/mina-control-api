# CU-PRO-001: Registrar Producción

## 1. Dominio
Producción

## 2. Descripción
Permite a un usuario (Supervisor, Administrador) registrar la cantidad de material (carbón) extraído por un empleado durante un turno específico, en una fecha y ubicación determinadas.

## 3. Actor(es)
*   Usuario Administrador
*   Usuario Supervisor

## 4. Precondiciones
*   El usuario debe estar autenticado y tener el rol `ADMINISTRADOR` o `SUPERVISOR`.
*   El empleado y el tipo de turno deben existir y estar activos en la base de datos.
*   El empleado debe tener una asignación de turno válida para la fecha que se está registrando.

## 5. Seguridad y Autorización
*   **Autenticación Requerida:** Sí
*   **Roles Permitidos:** `ADMINISTRADOR`, `SUPERVISOR`
*   **Condiciones de Propiedad:** N/A

## 6. Flujo Principal (Happy Path)
1.  El usuario envía una solicitud `POST` a `/api/produccion` con un `RegistroProduccionCreateDTO`.
2.  El sistema valida el formato del DTO (campos requeridos, valores positivos, etc.).
3.  El sistema verifica que el `empleadoId` y `tipoTurnoId` proporcionados corresponden a registros existentes y activos.
4.  El sistema valida que no exista un registro de producción duplicado para el mismo empleado y turno en la misma fecha.
5.  El sistema crea una nueva entidad `RegistroProduccion` a partir de los datos del DTO, estableciendo el campo `validado` en `false` por defecto.
6.  El sistema persiste la nueva entidad en la base de datos.
7.  El sistema devuelve una respuesta `201 Created` con el `RegistroProduccionDTO` del registro recién creado.

## 7. Flujos Alternativos
*   N/A

## 8. Validaciones

### 8.1. Validaciones de Formato/Sintaxis (Capa de DTO)
*   `empleadoId`: `@NotNull`
*   `tipoTurnoId`: `@NotNull`
*   `fechaRegistro`: `@NotNull`, `@PastOrPresent`
*   `cantidadExtraidaToneladas`: `@NotNull`, `@Positive`
*   `ubicacionExtraccion`: `@NotBlank`, `@Size(max=100)`

### 8.2. Validaciones de Reglas de Negocio (Capa de Servicio)
*   El `empleadoId` debe corresponder a un `Empleado` existente y activo.
*   El `tipoTurnoId` debe corresponder a un `TipoTurno` existente y activo.
*   No debe existir otro registro con la misma combinación de `empleadoId`, `tipoTurnoId` y `fechaRegistro`.

## 9. Flujos de Excepción

*   **9.1. Datos de Entrada Inválidos**
    *   **Condición:** El DTO no cumple las validaciones de formato.
    *   **Excepción Java:** `MethodArgumentNotValidException`
    *   **Respuesta HTTP:** `400 Bad Request`

*   **9.2. Empleado o Turno No Encontrado**
    *   **Condición:** El `empleadoId` o `tipoTurnoId` no existen.
    *   **Excepción Java:** `EmpleadoNotFoundException`, `TurnoNotFoundException`
    *   **Respuesta HTTP:** `404 Not Found`
    *   **Mensaje:** "El empleado con ID [...] no fue encontrado." o "El tipo de turno con ID [...] no fue encontrado."

*   **9.3. Registro Duplicado**
    *   **Condición:** Ya existe un registro para el mismo empleado, turno y fecha.
    *   **Excepción Java:** `RegistroProduccionDuplicateException`
    *   **Respuesta HTTP:** `409 Conflict`
    *   **Mensaje:** "Ya existe un registro de producción para el empleado en la fecha y turno especificados."

## 10. Postcondiciones
*   Se crea una nueva fila en la tabla `registros_produccion`.
*   El campo `validado` del nuevo registro es `false`.

## 11. DTOs Involucrados
*   **Request DTO:** `RegistroProduccionCreateDTO`
*   **Response DTO:** `RegistroProduccionDTO`

## 12. Endpoint REST
*   **Método:** `POST`
*   **URL:** `/api/produccion`

## 13. Referencias
*   **Diagrama de Secuencia:** `docs/diagrams/produccion/sequence_registrar_produccion.puml`
*   **Diagrama de Clases:** `docs/diagrams/produccion/class_diagram_produccion.puml`
*   **Diagrama ER:** `docs/diagrams/general/er_diagram_completo.puml`

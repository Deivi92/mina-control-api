# CU-LOG-001: Registrar Despacho

## 1. Dominio
Logística

## 2. Descripción
Permite a un usuario administrador o supervisor registrar un nuevo despacho de material (carbón), especificando los detalles del conductor, vehículo, carga y destino.

## 3. Actor(es)
*   Usuario Administrador
*   Usuario Supervisor

## 4. Precondiciones
*   El usuario debe estar autenticado y tener el rol `ADMINISTRADOR` o `SUPERVISOR`.
*   El número de despacho generado debe ser único.

## 5. Seguridad y Autorización
*   **Autenticación Requerida:** Sí
*   **Roles Permitidos:** `ADMINISTRADOR`, `SUPERVISOR`
*   **Condiciones de Propiedad:** N/A

## 6. Flujo Principal (Happy Path)
1.  El usuario envía una solicitud `POST` a `/api/logistica/despachos` con un `DespachoCreateDTO`.
2.  El sistema valida el formato del DTO (campos requeridos, valores válidos).
3.  El sistema genera un número de despacho único (ej. `DES-2023-0001`).
4.  El sistema crea una nueva entidad `Despacho` a partir de los datos del DTO, estableciendo el estado por defecto a `PROGRAMADO`.
5.  El sistema persiste la nueva entidad en la base de datos.
6.  El sistema devuelve una respuesta `201 Created` con el `DespachoDTO` del despacho recién creado.

## 7. Flujos Alternativos
*   N/A

## 8. Validaciones

### 8.1. Validaciones de Formato/Sintaxis (Capa de DTO)
*   `nombreConductor`: `@NotBlank`, `@Size(max=100)`
*   `placaVehiculo`: `@NotBlank`, `@Size(max=10)`
*   `cantidadDespachadaToneladas`: `@NotNull`, `@Positive`
*   `destino`: `@NotBlank`, `@Size(max=200)`
*   `fechaProgramada`: `@NotNull`, `@FutureOrPresent`

### 8.2. Validaciones de Reglas de Negocio (Capa de Servicio)
*   El `numeroDespacho` generado debe ser único. Si hay una colisión (muy improbable), se debe reintentar la generación.

## 9. Flujos de Excepción

*   **9.1. Datos de Entrada Inválidos**
    *   **Condición:** El DTO no cumple las validaciones de formato.
    *   **Excepción Java:** `MethodArgumentNotValidException`
    *   **Respuesta HTTP:** `400 Bad Request`

*   **9.2. Conflicto al Guardar**
    *   **Condición:** Ocurre un error al intentar guardar el despacho (ej. violación de una restricción única que no fue pre-validada).
    *   **Excepción Java:** `DataIntegrityViolationException`
    *   **Respuesta HTTP:** `409 Conflict`
    *   **Mensaje:** "Error de integridad de datos al guardar el despacho."

## 10. Postcondiciones
*   Se crea una nueva fila en la tabla `despachos`.
*   El estado inicial del despacho es `PROGRAMADO`.

## 11. DTOs Involucrados
*   **Request DTO:** `DespachoCreateDTO`
*   **Response DTO:** `DespachoDTO`

## 12. Endpoint REST
*   **Método:** `POST`
*   **URL:** `/api/logistica/despachos`

## 13. Referencias
*   **Diagrama de Secuencia:** `docs/diagrams/logistica/sequence_registrar_despacho.puml`
*   **Diagrama de Clases:** `docs/diagrams/logistica/class_diagram_logistica.puml`
*   **Diagrama ER:** `docs/diagrams/general/er_diagram_completo.puml`

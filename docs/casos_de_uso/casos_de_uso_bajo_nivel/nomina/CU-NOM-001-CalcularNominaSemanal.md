# CU-NOM-001: Calcular Nómina Semanal

## 1. Dominio
Nómina

## 2. Descripción
Permite a un usuario Gerente o Administrador iniciar el cálculo de la nómina para un período semanal específico. El sistema recopila datos de asistencia y producción para generar los registros de cálculo de nómina para cada empleado activo.

## 3. Actor(es)
*   Usuario Gerente
*   Usuario Administrador

## 4. Precondiciones
*   El usuario debe estar autenticado y tener el rol `GERENTE` o `ADMINISTRADOR`.
*   Debe existir un `PeriodoNomina` abierto para la semana que se desea calcular.
*   Los registros de producción y asistencia relevantes deben estar validados y cerrados.

## 5. Seguridad y Autorización
*   **Autenticación Requerida:** Sí
*   **Roles Permitidos:** `GERENTE`, `ADMINISTRADOR`
*   **Condiciones de Propiedad:** N/A

## 6. Flujo Principal (Happy Path)
1.  El usuario envía una solicitud `POST` a `/api/nomina/calcular` con el ID del período de nómina.
2.  El sistema valida que el período exista y esté en estado `ABIERTO`.
3.  El sistema obtiene la lista de todos los empleados `ACTIVOS`.
4.  Para cada empleado:
    a. El `NominaService` solicita al `TurnoService` el total de horas trabajadas y horas extras en el período.
    b. El `NominaService` solicita al `ProduccionService` el total de toneladas producidas en el período.
    c. Se calcula el salario base, bonificaciones por producción y asistencia, y otros conceptos.
    d. Se crea una entidad `CalculoNomina` con el desglose y los totales.
5.  El sistema persiste todas las entidades `CalculoNomina` en la base de datos.
6.  El sistema actualiza el estado del `PeriodoNomina` a `CALCULADO`.
7.  El sistema devuelve una respuesta `200 OK` con un resumen del cálculo (ej. número de empleados, monto total).

## 7. Flujos Alternativos
*   N/A

## 8. Validaciones

### 8.1. Validaciones de Formato/Sintaxis (Capa de DTO)
*   `periodoId`: `@NotNull`

### 8.2. Validaciones de Reglas de Negocio (Capa de Servicio)
*   El `PeriodoNomina` debe existir y su estado debe ser `ABIERTO`.
*   No deben existir cálculos previos para ese período.

## 9. Flujos de Excepción

*   **9.1. Período de Nómina No Encontrado o Inválido**
    *   **Condición:** El `periodoId` no existe o el período no está en estado `ABIERTO`.
    *   **Excepción Java:** `PeriodoNominaInvalidoException`
    *   **Respuesta HTTP:** `404 Not Found` o `409 Conflict`
    *   **Mensaje:** "El período de nómina no existe o no se puede calcular."

*   **9.2. Error en la Recopilación de Datos**
    *   **Condición:** Falla la comunicación con `TurnoService` o `ProduccionService`.
    *   **Excepción Java:** `DataFetchException` (personalizada)
    *   **Respuesta HTTP:** `500 Internal Server Error`
    *   **Mensaje:** "Error al recopilar los datos de producción o asistencia."

## 10. Postcondiciones
*   Se crean nuevas filas en la tabla `calculo_nomina` para cada empleado.
*   El estado del `PeriodoNomina` se actualiza a `CALCULADO`.

## 11. DTOs Involucrados
*   **Request DTO:** `CalcularNominaRequestDTO`
*   **Response DTO:** `CalculoNominaResumenDTO`

## 12. Endpoint REST
*   **Método:** `POST`
*   **URL:** `/api/nomina/calcular`

## 13. Referencias
*   **Diagrama de Secuencia:** `docs/diagrams/nomina/sequence_calcular_nomina_semanal.puml`
*   **Diagrama de Clases:** `docs/diagrams/nomina/class_diagram_nomina_corregido.puml`
*   **Diagrama ER:** `docs/diagrams/general/er_diagram_completo.puml`

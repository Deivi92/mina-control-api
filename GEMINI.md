# GEMINI Project Constitution: MinaControl Pro

Este documento define las reglas, arquitecturas, patrones y convenciones que deben ser seguidas estrictamente para el desarrollo del proyecto MinaControl Pro. Sirve como el contexto fundamental y la guía para cualquier generación de código o modificación realizada por el asistente de IA.

## 1. Visión General del Proyecto

- **Nombre:** MinaControl Pro
- **Propósito:** Sistema de gestión de operaciones para una mina pequeña/mediana.
- **Dominios Clave:** Autenticación, Empleados, Turnos, Producción, Logística, Nómina y Reportes.

## 2. Tecnologías y Lenguaje

- **Backend:** Spring Boot
- **Lenguaje:** Java 17
- **Base de Datos:** PostgreSQL
- **Gestión de Dependencias:** Maven (`pom.xml`)
- **Frontend (Futuro):** React.js

## 3. Arquitectura del Sistema

Se debe seguir rigurosamente una **Arquitectura en Capas (Layered Architecture)**.

1.  **Capa de API/Controller (`@RestController`):**
    - Responsable de exponer los endpoints REST.
    - **NO** debe contener lógica de negocio.
    - Delega toda la lógica a la capa de Servicio.
    - Maneja la serialización/deserialización de DTOs.

2.  **Capa de Lógica de Negocio/Servicio (`@Service`):**
    - **Corazón de la aplicación.** Contiene toda la lógica de negocio y la implementación de los casos de uso.
    - Orquesta las operaciones llamando a los Repositorios.
    - Es transaccional (`@Transactional`).

3.  **Capa de Acceso a Datos/Persistencia (`@Repository`):**
    - Interfaces que extienden `JpaRepository`.
    - Responsable exclusivo de la comunicación con la base de datos.
    - **NUNCA** debe ser accedida directamente desde la capa de Controller.

**Regla de Oro:** El flujo de comunicación es estrictamente unidireccional: `Controller -> Service -> Repository`.

## 4. Patrones de Diseño Obligatorios

- **Controller-Service-Repository:** Es la base de la estructura del código.
- **Inyección de Dependencias (DI):** Utilizar la inyección por constructor de Spring en todos los componentes.
- **DTO (Data Transfer Object):**
    - **Regla Crítica:** Las entidades JPA (`@Entity`) **NUNCA** deben ser expuestas en la capa de API.
    - Siempre se deben usar DTOs para las solicitudes (requests) y respuestas (responses).
    - Convención de nombres: `...DTO` para respuestas, `...CreateDTO` o `...UpdateDTO` para solicitudes.
- **Repository Pattern:** Implementado a través de Spring Data JPA.
- **Manejo de Excepciones Centralizado:**
    - Crear excepciones personalizadas y específicas del dominio (ej. `EmpleadoNotFoundException`) que extiendan `RuntimeException`.
    - Gestionar estas excepciones en una clase global con `@ControllerAdvice` para devolver respuestas de error HTTP consistentes.

## 5. Convenciones de Código y Estilo

- **Nomenclatura:**
    - **Paquetes:** `com.minacontrol.dominio` (ej. `com.minacontrol.auth`).
    - **Clases:** `CamelCase` (ej. `EmpleadoService`).
    - **Métodos:** `lowerCamelCase` (ej. `crearEmpleado`).
    - **Columnas DB:** `snake_case` (ej. `numero_identificacion`).
- **DTOs:** Deben ser implementados usando **Java Records** para garantizar la inmutabilidad y la claridad de intención. Esto es preferible a las clases con anotaciones de Lombok para este propósito específico.
- **Entidades JPA:** Utilizar **Lombok** (`@NoArgsConstructor`, `@Getter`, `@Setter`, `@EqualsAndHashCode`, `@ToString`) para reducir el boilerplate y satisfacer los requisitos de persistencia.
- **Entidades JPA:** Anotadas con `@Entity` y `@Table(name = "nombre_en_snake_case")`.

## 6. Estrategia de Pruebas

El desarrollo debe seguir un enfoque **TDD/BDD (Test/Behavior-Driven Development)**.

- **Pruebas Unitarias:**
    - Deben probar la lógica en las clases de **Servicio**.
    - Usar **JUnit 5** y **Mockito**.
    - **Todas las dependencias** (ej. repositorios) deben ser **simuladas (mocked)**.
- **Pruebas de Integración:**
    - Deben probar el flujo completo desde el **Controlador** hasta la base de datos.
    - Usar `@SpringBootTest`.
    - Utilizar una base de datos de prueba real, preferiblemente gestionada por **Testcontainers**.

## 7. Diseño de API RESTful

- **Contrato:** La API debe ser definida usando la especificación **OpenAPI 3.0**.
- **Endpoints:** Usar sustantivos en plural para los recursos (ej. `/api/empleados`).
- **Verbos HTTP:** Usar `GET` (leer), `POST` (crear), `PUT` (actualizar), `DELETE` (borrar) de forma estándar.
- **Códigos de Estado HTTP:** Usar los códigos apropiados (200, 201, 400, 401, 403, 404, 500).

## 8. Flujo de Trabajo de Desarrollo Iterativo

El desarrollo se realizará de forma iterativa, enfocándose en completar un dominio clave antes de pasar al siguiente, siguiendo este proceso para cada dominio:

1.  **Definir/Actualizar Casos de Uso de Bajo Nivel para el Dominio:**
    *   Para el dominio actual, se detallarán todos los casos de uso de bajo nivel (puntos finales/operaciones específicas).
    *   Cada caso de uso de bajo nivel se documentará en un archivo Markdown (`.md`) separado, siguiendo la plantilla definida, especificando flujos (principal, alternativos, excepción), validaciones, DTOs, endpoints y referencias a diagramas.

2.  **Escribir Pruebas (Unitarias y de Integración):**
    *   Basadas en los casos de uso de bajo nivel definidos para el dominio.
    *   Estas pruebas deben fallar inicialmente (enfoque TDD/BDD).

3.  **Escribir el Código de Producción:**
    *   Implementar la lógica necesaria en las capas de Controller, Service y Repository para que todas las pruebas del dominio pasen.

4.  **Refactorizar:**
    *   Mejorar el código del dominio, asegurando que todas las pruebas sigan en verde.

5.  **Repetir:**
    *   Una vez completado y verificado el dominio actual, se pasará al siguiente dominio prioritario, repitiendo los pasos del 1 al 4.

## 9. Puntos Finales (Casos de Uso de Bajo Nivel) por Dominio

Esta sección detalla la cantidad de puntos finales (operaciones o casos de uso de bajo nivel) identificados por cada dominio, basados en los diagramas de secuencia existentes. Estos son los elementos que se documentarán y construirán de forma iterativa.

*   **Autenticación:** 6 (Login, Logout, Recuperar Contraseña, Refresh Token, Registro, Cambiar Contraseña)
*   **Empleados:** 7 (Actualizar Empleado, Cambiar Estado Empleado, Consultar Perfil Personal, Crear Empleado, Eliminar Empleado, Listar Empleados, Obtener Empleado por ID)
*   **General:** 0 (Contiene diagramas de alto nivel, no operaciones)
*   **Logística:** 3 (Actualizar Estado Despacho, Consultar Despachos, Registrar Despacho)
*   **Nómina:** 4 (Ajustar Nómina, Calcular Nómina Semanal, Consultar Historial Nómina, Generar Comprobantes)
*   **Producción:** 5 (Actualizar Producción, Consultar Producción Empleado, Consultar Producción Fecha, Eliminar Producción, Registrar Producción)
*   **Reportes:** 4 (Exportar Datos, Reporte Asistencia, Reporte Costos Laborales, Reporte Producción)
*   **Turnos:** 9 (Actualizar Turno, Asignar Empleado Turno, Consultar Asistencia, Crear Turno, Eliminar Turno, Gestionar Excepciones, Listar Turnos, Obtener Turno por ID, Registrar Entrada/Salida)

**Total de Puntos Finales (Casos de Uso de Bajo Nivel) identificados:** 38

## 10. Plantilla de Caso de Uso de Bajo Nivel

Cada caso de uso de bajo nivel se documentará en un archivo Markdown (`.md`) separado, siguiendo esta plantilla exhaustiva. Esta plantilla asegura la consistencia, la completitud y sirve como base directa para la escritura de pruebas y la implementación del código.

```
# CU-DOM-XXX: [Nombre del Caso de Uso]

## 1. Dominio
[Nombre del Dominio (ej. Autenticación, Empleados)]

## 2. Descripción
[Breve descripción de la funcionalidad que este caso de uso de bajo nivel implementa.]

## 3. Actor(es)
[Quién o qué interactúa con esta funcionalidad (ej. Administrador del Sistema, Empleado, Sistema Externo).]

## 4. Precondiciones
*   [Condición 1 que debe ser verdadera antes de que el caso de uso pueda comenzar.]
*   [Condición 2...]

## 5. Flujo Principal (Happy Path)

1.  [Paso 1: Acción del Actor o del Sistema.]
2.  [Paso 2: Respuesta del Sistema.]
3.  [Paso 3...]
    (Detallar la secuencia de interacciones esperada cuando todo sale bien.)

## 6. Flujos Alternativos
*   **6.1. [Nombre del Flujo Alternativo]:**
    *   [Condición que dispara este flujo.]
    *   [Pasos específicos de este flujo alternativo.]

## 7. Flujos de Excepción

*   **7.1. [Nombre de la Excepción/Error]:**
    *   **Condición:** [Qué situación provoca este error.]
    *   **Excepción:** `[NombreDeLaExcepcionPersonalizada]` (si aplica, extendiendo `RuntimeException`).
    *   **Respuesta HTTP:** [Código de estado HTTP (ej. 400 Bad Request, 404 Not Found, 409 Conflict, 500 Internal Server Error).]
    *   **Detalle:** [Mensaje de error específico o información adicional.]

## 8. Validaciones (Ejemplos)

*   `[nombreCampo]`: [Reglas de validación (ej. Obligatorio, no nulo, longitud min/max, formato específico, único).]
*   `[otroCampo]`: [Reglas de validación.]

## 9. Postcondiciones
*   [Estado del sistema después de que el caso de uso se completa exitosamente.]
*   [Qué datos se modifican o se crean.]

## 10. DTOs Involucrados

*   **Request DTO:** `[NombreDelRequestDTO]` (si aplica)
    *   `[campo1]: [Tipo]`
    *   `[campo2]: [Tipo]`
*   **Response DTO:** `[NombreDelResponseDTO]` (si aplica)
    *   `[campo1]: [Tipo]`
    *   `[campo2]: [Tipo]`

## 11. Endpoint REST

*   **Método:** `[GET, POST, PUT, DELETE]`
*   **URL:** `[/api/recurso]`
*   **Ejemplo de Request Body:** (si aplica)
    ```json
    {
        // ...
    }
    ```
*   **Ejemplo de Response (Código HTTP):**
    ```json
    {
        // ...
    }
    ```

## 12. Referencias
*   **Diagrama de Secuencia:** `docs/diagrams/[dominio]/sequence_[nombre_diagrama].puml`
*   **Diagrama de Clases:** `docs/diagrams/[dominio]/class_diagram_[dominio].puml` (o específico si existe)
*   **Diagrama ER:** `docs/diagrams/general/er_diagram_completo.puml` (o específico si existe)
*   **Caso de Uso de Alto Nivel:** `docs/casos_de_uso/[NombreCasoUsoAltoNivel].md` (si aplica)

```
**

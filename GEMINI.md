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

1.  **Definir/Actualizar Artefactos de Diseño:** Casos de Uso, Diagramas ER, de Clases y de Secuencia.
2.  **Crear/Actualizar Casos de Uso de Bajo Nivel:** Detallar todos los flujos, validaciones y errores.
3.  **Escribir Pruebas (Unitarias y de Integración):** Basadas en los casos de uso de bajo nivel. Estas pruebas deben fallar inicialmente.
4.  **Escribir el Código de Producción:** Implementar la lógica necesaria para que las pruebas pasen.
5.  **Refactorizar:** Mejorar el código manteniendo las pruebas en verde.
6.  **Repetir.**

# GEMINI Project Constitution: MinaControl Pro

Este documento define las reglas, arquitecturas, patrones y convenciones que deben ser seguidas estrictamente para el desarrollo del proyecto MinaControl Pro. Sirve como el contexto fundamental y la guía para cualquier generación de código o modificación realizada por el asistente de IA.

## 1. Visión General del Proyecto

- **Nombre:** MinaControl Pro
- **Propósito:** Sistema de gestión de operaciones para una mina pequeña/mediana.
- **Dominios Clave:** Autenticación, Empleados, Turnos, Producción, Logística, Nómina y Reportes.

## 2. Tecnologías y Lenguaje

- **Backend:** Spring Boot 3.2.0
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
- **Programación Orientada a Interfaces (Desacoplamiento):**
    - **Regla Crítica:** Los componentes de la capa de API (Controllers) **deben** depender de **interfaces** de servicio, no de sus implementaciones concretas.
    - **Estructura:** Para cada servicio, se creará una interfaz (ej. `IServicioAutenticacion`) en el paquete `service` y su implementación (ej. `ServicioAutenticacionImpl`) en un subpaquete `service/impl`.
- **DTO (Data Transfer Object):**
    - **Regla Crítica:** Las entidades JPA (`@Entity`) **NUNCA** deben ser expuestas ni recibidas directamente en la capa de API.
    - **Organización:** Los DTOs deben residir en un paquete `dto` dentro de su dominio y estar organizados en subpaquetes `request` y `response`.
- **Manejo de Excepciones Centralizado:**
    - Crear excepciones personalizadas que extiendan `RuntimeException`.
    - Gestionar estas excepciones en una clase global con `@ControllerAdvice`.
    - **Todos los errores** deben responder con un `ErrorResponseDTO` estándar y consistente.
      ```java
      // Ubicado en un paquete compartido, ej: com.minacontrol.shared.dto
      public record ErrorResponseDTO(
          Instant timestamp,
          int status,
          String error,       // ej. "Not Found"
          String message,     // Mensaje específico de la excepción
          String path         // URI de la solicitud
      ) {}
      ```

## 5. Convenciones de Código y Estilo

- **Nomenclatura:**
    - **Paquetes:** `com.minacontrol.dominio` (ej. `com.minacontrol.autenticacion`).
    - **Clases:** `CamelCase` (ej. `EmpleadoService`).
    - **Métodos:** `lowerCamelCase` (ej. `crearEmpleado`).
    - **Columnas DB:** `snake_case` (ej. `numero_identificacion`).
- **DTOs:** Deben ser implementados usando **Java Records**.
- **Entidades JPA:** Utilizar **Lombok** (`@Data`, `@NoArgsConstructor`, `@AllArgsConstructor`, `@Builder`) y anotadas con `@Entity` y `@Table(name = "nombre_en_snake_case")`.
- **Mensajes de Commit:** Todos los mensajes de commit deben ser escritos **exclusivamente en español**.

## 6. Estrategia de Pruebas (TDD/BDD)

El desarrollo se rige por un enfoque **TDD/BDD (Test/Behavior-Driven Development)**, donde los **casos de uso de bajo nivel** sirven como la especificación principal para la creación de pruebas. Se priorizan las pruebas unitarias para guiar el desarrollo de la lógica de negocio, seguidas por pruebas de integración para verificar el flujo completo.

- **Pruebas Unitarias:**
    - **Foco:** Lógica de negocio en la capa de **Servicio**, guiadas por los casos de uso.
    - **Herramientas:** **JUnit 5** y **Mockito**.
    - **Dependencias:** **Todas** las dependencias deben ser **simuladas (mocked)**.

- **Pruebas de Integración:**
    - **Foco:** Flujo completo desde el **Controlador** hasta la **Base de Datos**, verificando los happy paths y flujos de excepción definidos en los casos de uso.
    - **Herramientas:** `@SpringBootTest` con `MockMvc`.
    - **Base de Datos:** Se usará una base de datos en memoria **H2** para el perfil `test` local, y **Testcontainers** para entornos de CI/CD.

## 7. Convenciones Detalladas de Pruebas

### 7.1. Organización y Nomenclatura

- **Estructura de Paquetes:** Las pruebas deben reflejar la estructura del código fuente, usando subpaquetes `unit` e `integration`.
- **Nomenclatura de Clases:** `NombreClaseTest` para unitarias, `NombreClaseIT` para integración.
- **Nomenclatura de Métodos:** Descriptiva, siguiendo el patrón `shouldHacerAlgoWhenCondicion()`.

### 7.2. Estructura de Pruebas Unitarias

- **Anotaciones:** `@ExtendWith(MockitoExtension.class)`, `@Mock`, `@InjectMocks`.
- **Patrón:** Seguir estrictamente el patrón **Arrange-Act-Assert**.

### 7.3. Estructura de Pruebas de Integración

- **Anotaciones:** `@SpringBootTest`, `@AutoConfigureMockMvc`, `@ActiveProfiles("test")`.
- **Organización:** Para mejorar la legibilidad, las pruebas para cada endpoint del controlador **deben** agruparse en clases internas separadas usando la anotación `@Nested` de JUnit 5.
- **Limpieza:** La base de datos debe limpiarse antes de cada prueba con `@BeforeEach`.

### 7.4. Checklist Esencial para Pruebas de Integración

Para garantizar la cobertura y robustez, cada flujo de endpoint probado en una prueba de integración (`@SpringBootTest`) debe validar sistemáticamente los siguientes aspectos clave. Esta lista actúa como el estándar mínimo de verificación:

1.  **Happy Path (Código 200 OK / 201 Created):**
    -   Verificar que el endpoint responde con el código de estado correcto para una solicitud válida.
    -   Validar que la estructura y los datos del DTO de respuesta son los esperados.

2.  **Verificación del Estado de la Base de Datos:**
    -   Después de una operación de escritura (`POST`, `PUT`, `DELETE`), consultar directamente el repositorio para confirmar que el estado de la base de datos refleja el cambio de manera correcta.

3.  **Caso de Error: Not Found (Código 404):**
    -   Probar que el endpoint responde con un `404 Not Found` cuando se intenta acceder a un recurso que no existe.

4.  **Caso de Error: Bad Request (Código 400):**
    -   Probar que el endpoint responde con un `400 Bad Request` cuando la solicitud contiene datos inválidos según las validaciones del DTO.

5.  **Caso de Error: Conflict (Código 409):**
    -   (Si aplica) Probar que el endpoint responde con un `409 Conflict` cuando se viola una regla de negocio (ej. email duplicado).

6.  **Verificación del JSON de Error (`ErrorResponseDTO`):**
    -   Para todos los casos de error (4xx, 5xx), verificar que la respuesta se adhiere a la estructura estándar de `ErrorResponseDTO`.

### 7.5. Guía Detallada para Pruebas Unitarias

Esta sección detalla la metodología y las convenciones para la creación de pruebas unitarias en cada dominio, asegurando consistencia y alta calidad.

#### **1. QUÉ TESTEAR (Orden de Prioridad)**
```
1. ServiceTest (80% del esfuerzo) - Lógica de negocio
2. EntityTest - Validaciones de dominio
3. MapperTest - Conversiones DTO/Entity
```

#### **2. ESTRUCTURA DE ARCHIVOS**
```
src/test/java/com/minacontrol/{dominio}/unit/
├── {Dominio}ServiceTest.java    # Principal
├── {Dominio}Test.java          # Entidad
└── {Dominio}MapperTest.java    # Mapper
```

#### **3. TEMPLATE BASE - ServiceTest**
```java
 @ExtendWith(MockitoExtension.class)
class EmpleadoServiceTest {

    @Mock private EmpleadoRepository repository;
    @InjectMocks private EmpleadoServiceImpl service;

    @Nested @DisplayName("Crear Empleado")
    class CrearEmpleadoTests {

        @Test
        void should_CreateEmpleado_When_ValidData() {
            // Arrange - Preparar datos y mocks
            // Act - Ejecutar método
            // Assert - Verificar resultado
        }
    }

    @Nested @DisplayName("Validaciones")
    class ValidacionesTests { /* ... */ }

    @Nested @DisplayName("Excepciones")
    class ExcepcionesTests { /* ... */ }
}
```

#### **4. TIPOS DE TEST POR MÉTODO**
```
Por cada método del servicio:
□ Happy Path (flujo normal)
□ Validaciones (email, formato, etc.)
□ Excepciones (duplicados, not found)
□ Verify mocks (repository.save fue llamado)
```

#### **5. PATRÓN AAA (Siempre)**
```java
 @Test
void should_Action_When_Condition() {
    // Arrange - Preparar datos y mocks
    var dto = createDto();
    when(repository.save(any())).thenReturn(entity);

    // Act - Ejecutar
    var result = service.method(dto);

    // Assert - Verificar
    assertThat(result).isNotNull();
    verify(repository).save(any());
}
```

#### **6. CHECKLIST FINAL**
```
□ ServiceTest con @Nested
□ EntityTest básico
□ MapperTest básico
□ Patrón AAA en todos
□ Nombres descriptivos
□ Cobertura 80%+
```

**Flujo**: Caso de Uso → Identificar métodos → Template ServiceTest → AAA por método → EntityTest → MapperTest → Verificar checklist.

## 8. Flujo de Trabajo de Desarrollo Iterativo

El desarrollo se realizará de forma iterativa, enfocándose en completar un dominio clave antes de pasar al siguiente. El proceso para desarrollar cada caso de uso dentro de un dominio sigue un estricto flujo guiado por pruebas (TDD/BDD):

1.  **Definir/Actualizar Caso de Uso de Bajo Nivel:** Se documenta la funcionalidad esperada, validaciones y flujos de excepción en su archivo `.md` correspondiente. Este documento es la fuente de verdad para el desarrollo.
2.  **Escribir Pruebas Unitarias:** Basándose en el caso de uso, se crean las pruebas unitarias para la capa de servicio. Estas pruebas fallarán inicialmente.
3.  **Escribir el Código de Producción:** Se implementa la lógica de negocio en la capa de servicio con el único objetivo de hacer que las pruebas unitarias pasen.
4.  **Escribir Pruebas de Integración:** Una vez que el código de producción está funcionando y validado por las pruebas unitarias, se escriben pruebas de integración para verificar el flujo completo desde el controlador hasta la base de datos.
5.  **Refactorizar:** Se refactoriza tanto el código de producción como el de las pruebas para mejorar la claridad y el mantenimiento.
6.  **Repetir:** Se repite el ciclo para el siguiente caso de uso hasta completar el dominio.

### 8.2. Estrategia de Construcción de Dominios

El desarrollo seguirá un enfoque incremental basado en las dependencias funcionales entre los dominios. El objetivo es construir desde los dominios más fundamentales (aquellos de los que otros dependen) hacia los más complejos o de soporte.

El orden de construcción recomendado es el siguiente:

1.  **Autenticación:** (Completado) Es la puerta de entrada al sistema. Define quién puede interactuar con la aplicación.
2.  **Empleados:** Es el dominio central. La mayoría de las operaciones dependen de la existencia de un empleado.
3.  **Turnos:** Depende de Empleados. Gestiona los horarios y la asistencia, un prerrequisito para la producción.
4.  **Producción:** Depende de Empleados y Turnos. Registra el trabajo realizado.
5.  **Logística:** Generalmente ligado a la Producción. Gestiona el despacho del material producido.
6.  **Nómina:** Dominio complejo que depende de Empleados, Turnos y potencialmente Producción para los cálculos.
7.  **Reportes:** Dominio transversal que consume datos de todos los demás. Se construye al final para tener fuentes de datos que analizar.

### 8.3. Estrategia de Ejecución de Pruebas

Para mantener un ciclo de desarrollo ágil y seguro, se establece la siguiente estrategia de ejecución de pruebas:

1.  **Durante el Desarrollo (Pruebas Aisladas por Dominio):**
    -   **Objetivo:** Obtener retroalimentación rápida y enfocada.
    -   **Acción:** Al trabajar en un dominio específico (ej. `Empleados`), se deben ejecutar **únicamente** los tests correspondientes a ese dominio. Esto se logra especificando el paquete del dominio en el comando de Maven.
    -   **Comando de Ejemplo:** `mvn test -Dtest="com.minacontrol.dominio.**.*Test,com.minacontrol.dominio.**.*IT"`

2.  **Antes de Integrar Cambios (Suite Completa):**
    -   **Objetivo:** Garantizar que los nuevos cambios no han introducido regresiones o efectos secundarios en otras partes del sistema.
    -   **Acción:** Antes de fusionar una rama de funcionalidad (feature branch) a la rama principal (`main` o `develop`), es **obligatorio** ejecutar la suite de pruebas completa del proyecto.
    -   **Comando:** `mvn verify` (ejecuta tests unitarios y de integración) o `mvn test` (solo unitarios).

Esta doble aproximación asegura velocidad durante la fase de codificación y máxima seguridad antes de consolidar el código.

## 9. Plantilla de Caso de Uso de Bajo Nivel

(La plantilla de CU-DOM-XXX se mantiene sin cambios)

---
# CU-DOM-XXX: [Nombre del Caso de Uso]
... (resto de la plantilla) ...
---

## 10. Principios de Colaboración con la IA

Estas reglas definen la interacción esperada con el asistente de IA para garantizar un desarrollo eficiente y de alta calidad.

*   **Tono y Proactividad:** El asistente debe mantener un tono profesional y colaborativo. Si una solicitud es ambigua, perjudicial o puede mejorarse, el asistente debe señalarlo y proponer alternativas en lugar de proceder ciegamente o ser condescendiente.
*   **Resolución de Dudas:** Ante la duda, la inacción es preferible a la acción incorrecta. Si el asistente no está seguro de cómo implementar un requisito o si la información en este documento es insuficiente, **debe** detenerse, solicitar clarificación al usuario y, si es apropiado, buscar información pública en la web. No debe realizar acciones sin estar seguro.

## 11. Directrices para la Creación de Casos de Uso de Bajo Nivel

Al crear o actualizar un Caso de Uso de Bajo Nivel (CU-DOM-XXX), es **obligatorio** referenciar los diagramas relevantes para proporcionar un contexto visual completo. Estos diagramas deben ser mencionados en la sección "Referencias" del caso de uso.

La estructura de los diagramas es la siguiente:

*   **Diagramas Generales (ER y Clases):**
    *   **Diagrama ER Completo:** `docs/diagrams/general/er_diagram_completo.puml`
    *   **Diagrama de Clases Completo:** `docs/diagrams/general/class_diagram_completo.puml`
    Estos diagramas proporcionan una visión global de la base de datos y la estructura de clases del sistema, respectivamente.

*   **Diagramas de Secuencia por Dominio:**
    *   Para cada dominio (ej. `autenticacion`, `empleados`, `turnos`), los diagramas de secuencia específicos se encuentran en `docs/diagrams/{nombre_del_dominio}/sequence_*.puml`.
    *   Se debe referenciar el diagrama de secuencia que ilustre el flujo del caso de uso específico que se está documentando.

**Ejemplo de Referencias en un Caso de Uso:**
```
## 13. Referencias
*   **Diagrama de Secuencia:** `docs/diagrams/autenticacion/sequence_registro_usuario.puml`
*   **Diagrama de Clases:** `docs/diagrams/general/class_diagram_completo.puml`
*   **Diagrama ER:** `docs/diagrams/general/er_diagram_completo.puml`
```
Esta convención asegura que cada caso de uso esté debidamente contextualizado visualmente, facilitando la comprensión y el desarrollo.

## 12. Gestión de Entornos con Perfiles de Spring

Para facilitar el desarrollo y las pruebas manuales, el proyecto utiliza perfiles de Spring para gestionar la configuración de seguridad y de base de datos de forma separada para cada entorno.

- **Perfil `dev` (Desarrollo Local):**
    - **Propósito:** Agilizar el desarrollo y las pruebas manuales con herramientas como `cURL` o Postman.
    - **Activación:** `mvn spring-boot:run -Dspring-boot.run.profiles=dev`
    - **Comportamiento:**
        - **Seguridad:** Deshabilitada. Todas las peticiones a los endpoints son permitidas (`permitAll`).
        - **Base de Datos:** Se utiliza una base de datos en memoria H2 que se crea y destruye con cada ejecución.

- **Perfil `test` (Pruebas de Integración):**
    - **Propósito:** Asegurar que el código nuevo no rompe la funcionalidad existente.
    - **Activación:** Automática al ejecutar `mvn verify` o `mvn test`.
    - **Comportamiento:**
        - **Seguridad:** **Habilitada**. Se utiliza la configuración de seguridad real (`SecurityConfig.java`). Las pruebas deben simular la autenticación si es necesario.
        - **Base de Datos:** Se utiliza una base de datos en memoria H2 limpia para cada ejecución de prueba.

- **Perfil `prod` o por defecto (Producción):**
    - **Propósito:** Ejecución en el entorno de producción real.
    - **Activación:** Al ejecutar el JAR (`java -jar ...`) sin especificar un perfil, o especificando `prod`.
    - **Comportamiento:**
        - **Seguridad:** **Habilitada** con la configuración más estricta.
        - **Base de Datos:** Se conecta a la base de datos PostgreSQL de producción (requiere configuración externa).

**Regla de Oro:** Solo necesitas especificar el perfil `dev` explícitamente. Para todas las demás tareas (como `mvn verify`), el sistema aplicará automáticamente la configuración segura y correcta.
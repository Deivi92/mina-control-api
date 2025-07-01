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
- **Programación Orientada a Interfaces (Desacoplamiento):**
    - **Regla Crítica:** Los componentes de la capa de API (Controllers) **deben** depender de **interfaces** de servicio, no de sus implementaciones concretas.
    - **Estructura:** Para cada servicio, se creará una interfaz (ej. `IServicioAutenticacion`) en el paquete `service` y su implementación (ej. `ServicioAutenticacionImpl`) en un subpaquete `service/impl`. Esto es fundamental para cumplir con el Principio de Inversión de Dependencias (SOLID).
- **DTO (Data Transfer Object):**
    - **Regla Crítica:** Las entidades JPA (`@Entity`) **NUNCA** deben ser expuestas ni recibidas directamente en la capa de API.
    - **Organización:** Los DTOs deben residir en un paquete `dto` dentro de su dominio y estar organizados en subpaquetes `request` y `response`.
    - **Convención de Nombres:**
        - Solicitudes: `...CreateDTO`, `...UpdateDTO`, `...RequestDTO`.
        - Respuestas: `...DTO`, `...ResponseDTO`.
- **Repository Pattern:** Implementado a través de Spring Data JPA.
- **Manejo de Concurrencia (Bloqueo Optimista):**
    - Para prevenir condiciones de carrera, las entidades JPA susceptibles a modificaciones concurrentes (ej. `Turno`, `Empleado`) **deben** incluir un campo de versión (`@Version private Integer version;`).
    - Los DTOs de actualización (ej. `...UpdateDTO`) **deben** incluir este campo de versión.
    - La capa de servicio **debe** validar que la versión del DTO coincida con la de la entidad antes de actualizar, lanzando `ObjectOptimisticLockingFailureException` (resultando en `409 Conflict`) si no coinciden.
- **Manejo de Excepciones Centralizado:**
    - Crear excepciones personalizadas. Las excepciones específicas de un dominio deben extender `RuntimeException` y ubicarse en un paquete `exception` dentro de su dominio (ej. `com.minacontrol.autenticacion.exception.UsuarioYaExisteException`). Las excepciones transversales a múltiples dominios pueden ubicarse en un paquete compartido (ej. `com.minacontrol.shared.exception`).
    - Gestionar estas excepciones en una clase global con `@ControllerAdvice` ubicada en un paquete compartido (ej. `com.minacontrol.shared.exception`).
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

## 5.1. Mejores Prácticas para el Manejo de Excepciones Personalizadas

Para asegurar un manejo de errores robusto y consistente, se seguirán las siguientes directrices para la creación de excepciones personalizadas:

*   **Extender `RuntimeException`:** Todas las excepciones de negocio personalizadas deben extender `java.lang.RuntimeException`. Esto las clasifica como "unchecked exceptions", lo que simplifica el código al no requerir que los métodos declaren `throws` y permite un manejo centralizado a través de `@ControllerAdvice`.
*   **Constructores Estándar:** Cada excepción personalizada debe incluir al menos los siguientes constructores para flexibilidad y trazabilidad:
    *   `public MyCustomException()`: Constructor por defecto.
    *   `public MyCustomException(String message)`: Para proporcionar un mensaje descriptivo del error.
    *   `public MyCustomException(String message, Throwable cause)`: Para encadenar la excepción actual con una causa subyacente, preservando el stack trace completo.
    *   `public MyCustomException(Throwable cause)`: Para envolver una excepción existente sin un mensaje específico adicional.
*   **Mensajes Claros y Concisos:** Los mensajes de las excepciones deben ser informativos, explicando la causa del error de manera clara. Deben ser útiles tanto para la depuración como, si es necesario, para ser expuestos al usuario final a través del `ErrorResponseDTO`.
*   **Ubicación Consistente:** Las excepciones específicas de un dominio deben residir en un paquete `exception` dentro de su dominio (ej. `com.minacontrol.autenticacion.exception`). Las excepciones que son verdaderamente transversales a múltiples dominios pueden ubicarse en un paquete compartido (ej. `com.minacontrol.shared.exception`).
*   **Mapeo a HTTP Status:** Cada excepción de negocio debe tener un mapeo claro a un código de estado HTTP apropiado (ej. `400 Bad Request`, `404 Not Found`, `409 Conflict`, `401 Unauthorized`, etc.) que será gestionado por el `@ControllerAdvice` global.
*   **Evitar Lógica de Negocio en Excepciones:** Las clases de excepción deben ser simples contenedores de información sobre el error. La lógica para determinar si una excepción debe ser lanzada y cómo se maneja debe residir en la capa de servicio o en el `@ControllerAdvice`.


## 5. Convenciones de Código y Estilo

- **Nomenclatura:**
    - **Paquetes:** `com.minacontrol.dominio` (ej. `com.minacontrol.autenticacion`).
    - **Clases:** `CamelCase` (ej. `EmpleadoService`).
    - **Métodos:** `lowerCamelCase` (ej. `crearEmpleado`).
    - **Columnas DB:** `snake_case` (ej. `numero_identificacion`).
- **DTOs:** Deben ser implementados usando **Java Records** para garantizar la inmutabilidad y la claridad de intención.
- **Entidades JPA:** Utilizar **Lombok** (`@Data`, `@NoArgsConstructor`, `@AllArgsConstructor`, `@Builder`) para reducir el boilerplate.
- **Entidades JPA:** Anotadas con `@Entity` y `@Table(name = "nombre_en_snake_case")`.
- **Mensajes de Commit:** Todos los mensajes de commit deben ser escritos **exclusivamente en español**.

## 6. Estrategia de Pruebas

El desarrollo debe seguir un enfoque **TDD/BDD (Test/Behavior-Driven Development)**, priorizando las pruebas unitarias antes que las de integración para guiar el desarrollo de la lógica de negocio.

- **Pruebas Unitarias:**
    - **Prioridad:** Se escriben primero para guiar el desarrollo de la lógica de negocio.
    - **Alcance:** Deben probar la unidad más pequeña y aislada de código. El foco principal es la lógica de negocio en las clases de **Servicio**, pero también pueden aplicarse a otras unidades aisladas (ej. utilidades, componentes específicos).
    - **Herramientas:** Usar **JUnit 5** y **Mockito**.
    - **Dependencias:** **Todas las dependencias** (ej. repositorios) deben ser **simuladas (mocked)**.
- **Pruebas de Integración:**
    - **Momento:** Se escriben una vez que la lógica de negocio ha sido probada unitariamente.
    - **Alcance:** Deben probar el flujo completo desde el **Controlador** hasta la base de datos, verificando la interacción entre componentes.
    - **Herramientas:** Usar `@SpringBootTest`.
    - **Base de Datos:** Utilizar una base de datos de prueba real, gestionada por **Testcontainers**.

## 7. Convenciones Detalladas de Pruebas

Para asegurar la consistencia, legibilidad y mantenibilidad de las pruebas, se seguirán las siguientes convenciones y mejores prácticas:

### 7.1. Organización y Nomenclatura

*   **Estructura de Paquetes:**
    *   Los paquetes de prueba deben **reflejar la estructura de paquetes del código de producción**. Por ejemplo, si una clase de servicio está en `com.minacontrol.dominio.service.NombreService`, sus pruebas unitarias se ubicarán en `com.minacontrol.dominio.unit.NombreServiceTest`.
    *   Las pruebas de integración se ubicarán en un subpaquete `integration` dentro del dominio de prueba (ej. `com.minacontrol.dominio.integration.NombreControllerIT`).
*   **Nomenclatura de Clases de Prueba:**
    *   **Pruebas Unitarias:** Se añadirá `Test` al nombre de la clase que se está probando (ej. `NombreServiceTest`).
    *   **Pruebas de Integración:** Se añadirá `IT` o `IntegrationTest` al nombre de la clase principal del flujo (ej. `NombreControllerIT` o `NombreRepositoryIntegrationTest`).
*   **Nomenclatura de Métodos de Prueba:**
    *   Deben ser descriptivos, explicando el escenario y el resultado esperado.
    *   Patrones recomendados:
        *   `shouldDoSomethingWhenConditionIsMet()`
        *   `given_precondition_when_action_then_expectedResult()`
        *   `testMethodName_scenario_expectedResult()`
    *   Ejemplos: `shouldRegisterUserSuccessfully()`, `registerUser_existingEmail_throwsConflictException()`.

### 7.2. Estructura Interna de Pruebas Unitarias

*   **Foco:** Probar una única unidad de código (ej. un método en una clase de servicio) de forma aislada.
*   **Dependencias:** Todas las dependencias externas (repositorios, otros servicios, APIs externas) deben ser **mockeadas** (simuladas) utilizando Mockito.
*   **Anotaciones Clave:**
    *   `@ExtendWith(MockitoExtension.class)`: Habilita las anotaciones de Mockito.
    *   `@Mock`: Para crear mocks de las dependencias.
    *   `@InjectMocks`: Para inyectar los mocks en la instancia de la clase bajo prueba.
    *   `@BeforeEach`: Método de configuración que se ejecuta antes de cada prueba para inicializar objetos comunes.
    *   `@Test`: Marca un método como una prueba.
    *   `@DisplayName`: (Opcional) Para nombres más legibles en los reportes de prueba.
*   **Patrón AAA (Arrange-Act-Assert):**
    *   **Arrange (Preparar):** Configurar los datos de prueba, los mocks y el sistema bajo prueba.
    *   **Act (Actuar):** Ejecutar el método o la acción que se está probando.
    *   **Assert (Verificar):** Comprobar el resultado (valor de retorno, cambios de estado, interacciones con mocks).

### 7.3. Estructura Interna de Pruebas de Integración

*   **Foco:** Probar la interacción entre múltiples componentes (ej. Controlador -> Servicio -> Repositorio -> Base de Datos).
*   **Dependencias:** Se utilizan dependencias reales donde sea apropiado (ej. una base de datos real a través de Testcontainers). Los sistemas externos que no son el foco de la integración pueden ser mockeados.
*   **Anotaciones Clave:**
    *   `@SpringBootTest`: Carga el contexto completo de la aplicación Spring.
    *   `@AutoConfigureMockMvc`: Configura `MockMvc` para probar endpoints REST sin levantar un servidor HTTP real.
    *   `@ActiveProfiles("test")`: Para usar un perfil específico de pruebas.
    *   `@Testcontainers`: Habilita Testcontainers para gestionar contenedores de base de datos.
    *   `@Container`: Define un contenedor de Testcontainers (ej. `PostgreSQLContainer`).
    *   `@DynamicPropertySource`: Para configurar propiedades de Spring para usar la base de datos de Testcontainers.
*   **`MockMvc`:** Se utiliza para realizar solicitudes HTTP simuladas a los endpoints del controlador y verificar las respuestas.
*   **Limpieza de Datos:** Es crucial limpiar la base de datos antes de cada prueba (`@BeforeEach`) para asegurar la independencia de las pruebas.

### 7.4. Mejores Prácticas Generales

*   **Principio de Responsabilidad Única:** Cada método de prueba debe probar un escenario o aspecto específico de la funcionalidad.
*   **Pruebas Rápidas:** Las pruebas unitarias deben ser muy rápidas. Las de integración serán más lentas, pero deben optimizarse.
*   **Pruebas Independientes:** Las pruebas no deben depender del orden de ejecución ni del estado dejado por pruebas anteriores.
*   **Evitar Lógica en Tests:** La lógica de prueba debe ser simple. Si se encuentran bucles o condicionales complejos, podría indicar la necesidad de refactorizar el código de producción o dividir la prueba.
*   **Aserciones Significativas:** Utilizar aserciones específicas (ej. `assertEquals`, `assertTrue`, `assertThrows`).
*   **Datos de Prueba:** Usar datos realistas pero mínimos. Para unitarias, crear datos directamente en el método de prueba o `setUp()`. Para integración, persistir datos directamente en la base de datos de prueba.
*   **Pruebas de Manejo de Errores:** Probar explícitamente cómo el código maneja entradas inválidas, casos límite y excepciones esperadas.

## 8. Diseño de API RESTful

- **Contrato:** La API debe ser definida usando la especificación **OpenAPI 3.0**.
- **Endpoints:** Usar sustantivos en plural para los recursos (ej. `/api/empleados`).
- **Verbos HTTP:** Usar `GET`, `POST`, `PUT`, `DELETE` de forma estándar.
- **Códigos de Estado HTTP:** Usar los códigos apropiados (200, 201, 400, 401, 403, 404, 409, 500).

## 8. Flujo de Trabajo de Desarrollo Iterativo

El desarrollo se realizará de forma iterativa, enfocándose en completar un dominio clave antes de pasar al siguiente, siguiendo este proceso para cada dominio:

### 8.1. Orden de Construcción de Componentes por Dominio

Para cada dominio, la construcción de componentes debe seguir el siguiente orden recomendado, integrando el enfoque TDD/BDD:

1.  **DTOs (Request y Response):** Definición del contrato de la API.
2.  **Modelos/Entidades:** Representación de la estructura de datos del dominio.
3.  **Repositorios:** Interfaces para la interacción con la base de datos.
4.  **Interfaces de Servicio:** Establecimiento del contrato de la lógica de negocio.
5.  **Implementación de Servicios y Pruebas Unitarias (TDD):** Desarrollo de la lógica de negocio, guiado por tests.
6.  **Excepciones Personalizadas:** Definición de errores específicos del dominio.
7.  **Controladores:** Exposición de la API REST.
8.  **Pruebas de Integración:** Verificación del flujo completo de la aplicación.

1.  **Definir/Actualizar Casos de Uso de Bajo Nivel para el Dominio.**
2.  **Escribir Pruebas (Unitarias y de Integración).**
3.  **Escribir el Código de Producción.**
4.  **Refactorizar.**
5.  **Repetir.**

## 9. Puntos Finales por Dominio

- Autenticación simple: email del empleado
*   **Empleados:** 7
*   **Logística:** 3
*   **Nómina:** 4
*   **Producción:** 5
*   **Reportes:** 4
*   **Turnos:** 9
*   **Total:** 38

## 10. Plantilla de Caso de Uso de Bajo Nivel

Cada caso de uso se documentará en un archivo `.md` individual, usando esta plantilla como base para guiar el desarrollo y las pruebas.

---

# CU-DOM-XXX: [Nombre del Caso de Uso]

## 1. Dominio
[Nombre del Dominio (ej. Autenticación, Empleados)]

## 2. Descripción
[Breve descripción de la funcionalidad que este caso de uso de bajo nivel implementa.]

## 3. Actor(es)
[Quién o qué interactúa con esta funcionalidad (ej. Administrador, Empleado, Sistema Externo).]

## 4. Precondiciones
*   [Condición 1 que debe ser verdadera antes de que el caso de uso pueda comenzar.]
*   [Condición 2...]

## 5. Seguridad y Autorización
*   **Autenticación Requerida:** [Sí/No]
*   **Roles Permitidos:** [Lista de roles que pueden ejecutar el endpoint (ej. `ADMIN`, `SUPERVISOR`, `EMPLEADO`).]
*   **Condiciones de Propiedad:** [Reglas sobre la propiedad de los datos (ej. "Un `EMPLEADO` solo puede consultar su propio perfil").]

## 6. Flujo Principal (Happy Path)
1.  [Paso 1: Acción del Actor o del Sistema.]
2.  [Paso 2: Respuesta del Sistema.]
3.  [...]

## 7. Flujos Alternativos
*   **7.1. [Nombre del Flujo Alternativo]:**
    *   [Condición que dispara este flujo.]
    *   [Pasos específicos de este flujo alternativo.]

## 8. Validaciones

### 8.1. Validaciones de Formato/Sintaxis (Capa de DTO)
*Se resuelven con anotaciones de `jakarta.validation` en el DTO.*
*   `[nombreCampo]`: [Reglas (ej. `@NotNull`, `@Size(min=3, max=50)`).]
*   `[email]`: [`@Email`.]

### 8.2. Validaciones de Reglas de Negocio (Capa de Servicio)
*Requieren lógica en la capa de servicio, a menudo con consultas a la BD.*
*   [Regla 1 (ej. "No se puede registrar producción para un empleado sin turno activo").]
*   [Regla 2 (ej. "El email debe existir para poder recuperar la contraseña").]

## 9. Flujos de Excepción
*   **9.1. [Nombre de la Excepción/Error]:**
    *   **Condición:** [Qué situación provoca este error.]
    *   **Excepción Java:** `[NombreDeLaExcepcionPersonalizada]`
    *   **Respuesta HTTP:** `[Código]` `[Texto]` (ej. `404 Not Found`)
    *   **Cuerpo de Respuesta (ErrorResponseDTO):**
        ```json
        {
            "timestamp": "...",
            "status": 404,
            "error": "Not Found",
            "message": "[Mensaje de error específico.]",
            "path": "[URI de la solicitud]"
        }
        ```

## 10. Postcondiciones
*   [Estado del sistema después de que el caso de uso se completa exitosamente.]
*   [Qué datos se modifican, crean o eliminan.]

## 11. DTOs Involucrados
*   **Request DTO:** `[NombreDelRequestDTO]`
*   **Response DTO:** `[NombreDelResponseDTO]`

## 12. Endpoint REST
*   **Método:** `[GET, POST, PUT, DELETE]`
*   **URL:** `[/api/recurso/{id}]`

## 13. Referencias
*   **Diagrama de Secuencia:** `docs/diagrams/[dominio]/sequence_[...].puml`
*   **Diagrama de Clases:** `docs/diagrams/[dominio]/class_diagram_[...].puml`
*   **Diagrama ER:** `docs/diagrams/general/er_diagram_completo.puml`

---


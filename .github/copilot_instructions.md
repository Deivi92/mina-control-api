# Instrucciones para GitHub Copilot - Proyecto MinaControl Pro

## 🌍 Configuración de Idioma y Terminología

*   **Responder SIEMPRE en español.**
*   **CODIFICACIÓN PREDOMINANTEMENTE EN ESPAÑOL:**
    *   Todo el código nuevo específico del dominio del proyecto (nombres de clases de negocio, métodos de lógica de negocio, variables que representan conceptos del dominio, comentarios explicativos de la lógica) debe estar escrito en español para mantener la coherencia y facilitar la comprensión.
    *   Se priorizará la traducción progresiva del código existente que no cumpla esta directriz.
    *   **Excepciones por Convenciones de la Industria:**
        *   **Nombres de Paquetes de Capa:** Se permite el uso de nombres estándar en inglés para los paquetes que representan capas arquitectónicas comunes en Spring Boot, tales como: `controller`, `dto`, `model` (o `entity`), `repository`, `service`, `config`, `exception`.
        *   **Métodos de Acceso (Getters/Setters):** Se mantendrá la convención estándar de JavaBeans con prefijos `get` y `set` para los métodos de acceso a propiedades (ej. `getNombre()`, `setNombre()`).
        *   **Nombres de Clases de Frameworks/Librerías:** Al extender o implementar clases/interfaces de frameworks (ej. Spring, JUnit), se seguirán las convenciones de nomenclatura de dicho framework si es necesario.
        *   **Términos Técnicos Web y HTTP:** Mantener en inglés términos técnicos estándar en contextos web/API como:
            * `request`, `response`, `body`, `header`, `status`, `endpoint`
            * Métodos HTTP: `GET`, `POST`, `PUT`, `DELETE`
            * Códigos y frases de estado HTTP: `OK`, `CREATED`, `BAD_REQUEST`, `NOT_FOUND`, etc.
        *   **Términos Técnicos de Desarrollo:** Mantener en inglés términos como:
            * `timestamp`, `error`, `message` (en respuestas de error)
            * `path`, `page`, `size` (en contextos de paginación)
            * `id`, `query`, `value` (en parámetros y consultas)
            * `log`, `debug`, `info`, `warn`, `error` (en contextos de logging)
        *   **Variables de Infraestructura:** Mantener en inglés términos como:
            * `host`, `port`, `url`, `connection`, `stream`, `buffer`
            * `file`, `directory`, `path`
*   **Para anglicismos técnicos no cubiertos por excepciones:** Usar formato "anglicismo (explicación clara en español)".
*   **Ejemplos obligatorios (para términos generales):**
    *   endpoint (punto de acceso de API)
    *   commit (confirmación de cambios)
    *   branch (rama de código)
    *   merge (fusión de ramas)
    *   pull request (solicitud de integración)
    *   deploy (despliegue/publicación)
    *   build (construcción/compilación)
    *   pipeline (flujo de trabajo automatizado)
    *   testing (pruebas)
    *   debugging (depuración)
    *   refactoring (refactorización)
    *   framework (marco de trabajo)
    *   library (biblioteca)
    *   package (paquete de dominio, ej. `com.minacontrol.personal`)
    *   controller (controlador de API, paquete `controller`)
    *   service (servicio de negocio, paquete `service`)
    *   repository (repositorio de datos, paquete `repository`)
    *   model (modelo de datos, paquete `model`)
    *   entity (entidad de dominio, ej. `Empleado`)

### 1.1. Ejemplos de Aplicación de Convenciones de Idioma

**Ejemplo 1: Manejador de Excepciones (términos técnicos en inglés)**
```java
@ExceptionHandler(RecursoNoEncontradoException.class)
public ResponseEntity<Map<String, Object>> manejarExcepcionRecursoNoEncontrado(RecursoNoEncontradoException ex) {
    // Nombres de clases de dominio y métodos en español
    Map<String, Object> body = new HashMap<>(); // 'body' se mantiene en inglés por ser término técnico estándar
    body.put("timestamp", LocalDateTime.now().toString()); // 'timestamp' se mantiene en inglés
    body.put("status", HttpStatus.NOT_FOUND.value()); // 'status' se mantiene en inglés
    body.put("error", HttpStatus.NOT_FOUND.getReasonPhrase()); // 'error' se mantiene en inglés
    body.put("message", ex.getMessage()); // 'message' se mantiene en inglés
    
    return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
}
```

**Ejemplo 2: Controlador REST (mezcla de términos)**
```java
@RestController
@RequestMapping("/api/empleados") // URL en español para recursos de dominio
public class EmpleadoController { // Clases de dominio en español
    
    @PostMapping
    public ResponseEntity<EmpleadoResponseDTO> crearEmpleado(@Valid @RequestBody EmpleadoRequestDTO empleadoRequestDTO) {
        // Método en español, pero anotaciones técnicas como @RequestBody en inglés
        EmpleadoResponseDTO nuevoEmpleado = empleadoService.crearEmpleado(empleadoRequestDTO);
        return new ResponseEntity<>(nuevoEmpleado, HttpStatus.CREATED); // HttpStatus en inglés
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<EmpleadoResponseDTO> obtenerEmpleadoPorId(@PathVariable Long id) {
        // Método y parámetros en español, excepto 'id' que es término técnico
        return empleadoService.obtenerEmpleadoPorId(id)
                .map(empleado -> new ResponseEntity<>(empleado, HttpStatus.OK))
                .orElseThrow(() -> new RecursoNoEncontradoException("Empleado no encontrado con id: " + id));
    }
}
```

## 1. Resumen del Proyecto

*   **Nombre del Proyecto:** MinaControl Pro
*   **Objetivo:** Desarrollar un API REST backend para gestionar operaciones en una mina de carbón de tamaño mediano en Socha, Boyacá, Colombia.
*   **Fase Actual (Fase 1):** Creación de un esqueleto de API funcional básico, con gestión de datos en memoria.
*   **Funcionalidades Clave (Fase 1):** Digitalizar procesos manuales para personal (empleados), producción diaria. Nómina básica e inventario simple se considerarán en etapas posteriores o de forma simplificada si derivan directamente de los módulos principales.

## 2. Entorno de Desarrollo

*   **IDE:** Visual Studio Code
*   **Sistema Operativo para Desarrollo:** WSL Ubuntu (Ubuntu-24.04)
*   **Java SDK:** OpenJDK 17 (configurado en WSL)
*   **Maven:** Versión 3.8.x o superior (configurado en WSL)
*   **VS Code Extensions Esenciales:**
    *   `Extension Pack for Java` (de Microsoft)
    *   `Maven for Java` (de Microsoft)
*   **Configuración VS Code (`.vscode/settings.json`):**
    *   WSL Ubuntu como terminal por defecto.
    *   `java.home` y `maven.executable.path` apuntando a las instalaciones en WSL.
    *   Configuraciones para recarga automática de dependencias y construcción automática.

## 3. Tecnologías y Dependencias (Fase 1 - según `pom.xml`)

*   **Framework Principal:** Spring Boot (`spring-boot-starter-parent: 2.7.18`)
*   **Servidor Web y API REST:** `spring-boot-starter-web`
*   **Base de Datos (Desarrollo):** `h2database` (en memoria)
*   **Validación:** `spring-boot-starter-validation` (Bean Validation)
*   **Testing:** `spring-boot-starter-test` (JUnit 5, Mockito, Spring Test)
*   **Versión de Java:** 17

## 4. Arquitectura del Software (Fase 1)

*   **Patrón General:** Arquitectura Modular Orientada a Dominios (Features). El proyecto se organiza en módulos que representan las principales áreas de negocio (ej. `personal`, `registrosProduccion`). Cada módulo internamente sigue una organización por capas técnicas.
*   **Paquete Base:** `com.minacontrol`
*   **Estructura de Paquetes Principal (`src/main/java/com/minacontrol`):**
    *   `MinaControlApiApplication.java`: Clase principal de la aplicación Spring Boot.
    *   `config/`: Contiene clases de configuración global para la aplicación (ej. Beans, seguridad si aplica).
    *   `exception/`: Manejo global de excepciones (`@ControllerAdvice`) y clases de excepciones personalizadas para toda la aplicación.
    *   **Módulos de Dominio (ejemplos):**
        *   `personal/` (anteriormente `empleado`): Contiene toda la lógica y componentes relacionados con la gestión de personal.
            *   `controller/`: Controladores REST (`@RestController`) específicos del módulo de personal.
            *   `dto/`: Data Transfer Objects (`RequestDTO`, `ResponseDTO`) para el módulo de personal.
            *   `model/` (o `entity/`): Clases de entidad JPA o modelos de dominio para personal.
            *   `repository/`: Interfaces de repositorio (ej. Spring Data JPA) para el acceso a datos de personal.
            *   `service/`: Lógica de negocio e interfaces de servicio para el módulo de personal.
        *   `registrosProduccion/` (anteriormente `registroproduccion`): Contiene toda la lógica y componentes relacionados con los registros de producción.
            *   (estructura interna similar al módulo `personal` con `controller`, `dto`, `model`, `repository`, `service`)
*   **Estructura de Paquetes de Pruebas (`src/test/java/com/minacontrol`):** Sigue una estructura paralela a `src/main/java`, replicando los paquetes de los módulos de dominio y sus capas para las pruebas correspondientes.

### 4.1. Árbol de Directorios Principal (Fuente)

A continuación, se muestra una vista simplificada de la estructura de directorios clave del código fuente:

```text
mina-control-api/
├── pom.xml
├── docs/
│   └── ... (documentación y diagramas)
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── minacontrol/
│   │   │           ├── MinaControlApiApplication.java
│   │   │           ├── config/                 # Configuración transversal
│   │   │           ├── exception/              # Excepciones transversales
│   │   │           ├── personal/               # Módulo de Personal (Dominio)
│   │   │           │   ├── controller/
│   │   │           │   ├── dto/
│   │   │           │   ├── model/
│   │   │           │   ├── repository/
│   │   │           │   └── service/
│   │   │           └── registrosProduccion/    # Módulo de Registros de Producción (Dominio)
│   │   │               ├── controller/
│   │   │               ├── dto/
│   │   │               ├── model/
│   │   │               ├── repository/
│   │   │               └── service/
│   │   └── resources/
│   │       └── application.properties
│   └── test/
│       ├── java/
│       │   └── com/
│       │       └── minacontrol/
│       │           ├── personal/
│       │           └── registrosProduccion/
│       └── resources/
└── .github/
    └── copilot_instructions.md
```

### 4.2. Diagramas UML de Referencia (PlantUML)

Los siguientes diagramas UML se encuentran en la carpeta `docs/diagrams/` y proporcionan una representación visual de la arquitectura y los flujos principales para la Fase 1:

*   **Diagrama de Clases General:** `docs/diagrams/class_diagram_fase1.puml`
*   **Secuencias de Empleados:**
    *   Crear Empleado: `docs/diagrams/sequence_empleado_crear.puml`
    *   Obtener Todos los Empleados: `docs/diagrams/sequence_empleado_obtener_todos.puml`
    *   Obtener Empleado por ID: `docs/diagrams/sequence_empleado_obtener_id.puml`
    *   Actualizar Empleado: `docs/diagrams/sequence_empleado_actualizar.puml`
    *   Eliminar Empleado: `docs/diagrams/sequence_empleado_eliminar.puml`
*   **Secuencias de Registro de Producción:**
    *   Crear Registro: `docs/diagrams/sequence_registro_produccion_crear.puml`
    *   Obtener Todos los Registros: `docs/diagrams/sequence_registro_produccion_obtener_todos.puml`
    *   Obtener Registro por ID: `docs/diagrams/sequence_registro_produccion_obtener_id.puml`
    *   Actualizar Registro: `docs/diagrams/sequence_registro_produccion_actualizar.puml`
    *   Eliminar Registro: `docs/diagrams/sequence_registro_produccion_eliminar.puml`
    *   Obtener Registros por Fecha: `docs/diagrams/sequence_registro_produccion_por_fecha.puml`
    *   Obtener Registros por Empleado y Fecha: `docs/diagrams/sequence_registro_produccion_por_empleado_y_fecha.puml`

## 5. Módulos Principales y Flujos de Datos (Fase 1)

### 5.1. Módulo de Personal (Empleados)

*   **Entidad (`Empleado`):**
    *   `id` (Long, PK)
    *   `numeroDocumento` (String, único)
    *   `nombres` (String)
    *   `apellidos` (String)
    *   `fechaNacimiento` (LocalDate)
    *   `cargo` (String)
    *   `fechaIngreso` (LocalDate)
    *   `salarioBase` (BigDecimal)
    *   `estado` (String/Enum: ACTIVO, INACTIVO)
    *   `email` (String, opcional, validado)
    *   `telefono` (String, opcional)
*   **DTOs:** `EmpleadoRequestDTO`, `EmpleadoResponseDTO`.
*   **Endpoints y Flujos de Datos:**
    *   **`POST /api/empleados` (Crear Empleado):**
        *   Cliente envía `EmpleadoRequestDTO` -> Controller valida -> Service mapea a `Empleado`, persiste (memoria), devuelve `Empleado` creado -> Controller mapea a `EmpleadoResponseDTO`, devuelve `201 Created`.
    *   **`GET /api/empleados` (Obtener Todos):**
        *   Controller -> Service obtiene lista de `Empleado` -> Controller mapea lista a `List<EmpleadoResponseDTO>`, devuelve `200 OK`.
    *   **`GET /api/empleados/{id}` (Obtener por ID):**
        *   Controller -> Service busca `Empleado` por ID -> Si existe, Controller mapea a `EmpleadoResponseDTO`, devuelve `200 OK`. Si no, `ResourceNotFoundException` (global) -> `404 Not Found`.
    *   **`PUT /api/empleados/{id}` (Actualizar Empleado):**
        *   Cliente envía `EmpleadoRequestDTO` -> Controller valida -> Service busca `Empleado`, actualiza campos, persiste (memoria), devuelve `Empleado` actualizado -> Controller mapea a `EmpleadoResponseDTO`, devuelve `200 OK`. Si no existe, `ResourceNotFoundException`.
    *   **`DELETE /api/empleados/{id}` (Eliminar Empleado):**
        *   Controller -> Service elimina `Empleado` por ID (o cambia estado a INACTIVO) -> Controller devuelve `204 No Content`. Si no existe, `ResourceNotFoundException`.

### 5.2. Módulo de Producción Diaria (RegistroProduccion)

*   **Entidad (`RegistroProduccion`):**
    *   `id` (Long, PK)
    *   `fechaRegistro` (LocalDate)
    *   `toneladasProducidas` (BigDecimal)
    *   `tipoCarbon` (String/Enum: TERMICO, METALURGICO)
    *   `zonaExtraccion` (String)
    *   `responsableTurnoId` (Long, FK a Empleado)
    *   `observaciones` (String, opcional)
*   **DTOs:** `RegistroProduccionRequestDTO`, `RegistroProduccionResponseDTO`.
*   **Endpoints y Flujos de Datos:**
    *   **`POST /api/registros-produccion` (Crear Registro):**
        *   Similar a crear empleado, usando DTOs y entidad de producción.
    *   **`GET /api/registros-produccion` (Obtener Todos):**
        *   Similar a obtener todos los empleados.
    *   **`GET /api/registros-produccion/{id}` (Obtener por ID):**
        *   Similar a obtener empleado por ID.
    *   **`PUT /api/registros-produccion/{id}` (Actualizar Registro):**
        *   Similar a actualizar empleado.
    *   **`DELETE /api/registros-produccion/{id}` (Eliminar Registro):**
        *   Similar a eliminar empleado.

## 6. Estándares de Codificación y Patrones de Diseño (Fase 1)

*   **DTOs para API:** Obligatorio. No exponer entidades.
*   **Validación de Beans:** Anotaciones en DTOs y entidades (`@Valid` en controllers).
*   **Manejo Global de Excepciones:** `@ControllerAdvice` para respuestas HTTP estandarizadas.
*   **Inyección de Dependencias:** Por constructor.
*   **Interfaces para Servicios/Repositorios:** Promover desacoplamiento.
*   **Gestión de Datos en Memoria (Fase 1):** Colecciones en servicios/repositorios. Generar IDs únicos.
*   **Nombres:** Convenciones Java.
*   **Comentarios:** Cuando la lógica no sea trivial.

## 7. Estrategia de Base de Datos (Fase 1)

*   **Base de Datos:** H2 en memoria.
*   **Objetivo:** Enfocarse en lógica API.
*   **Futuro (Post-Fase 1):** Migración a PostgreSQL con Spring Data JPA.

## 8. Flujo de Trabajo de Desarrollo Sugerido (por módulo)

1.  Definir **Entidad** (`model`).
2.  Definir **DTOs** (`dto`).
3.  Crear interfaz **Repositorio** (`repository`) (implementación en memoria o en servicio para Fase 1).
4.  Crear interfaz **Servicio** (`service`).
5.  Implementar **Servicio** (inyecta Repositorio, lógica de negocio).
6.  Crear **Controlador** (`controller`) (inyecta Servicio, define endpoints).
7.  Implementar manejo de excepciones.
8.  Escribir **pruebas unitarias** (Servicios) e **integración** (Controladores).

## 9. Directrices Específicas para la Interacción con la IA (GitHub Copilot)

*   **Asumir Contexto:** Este archivo es la fuente de verdad.
*   **Seguir Estándares:** Adherirse estrictamente a lo aquí definido.
*   **Inyección por Constructor:** Utilizarla.
*   **Paquetes:** Colocar clases en paquetes correctos.
*   **Pruebas:** Sugerir/generar esqueletos de pruebas.
*   **Evitar Complejidad Innecesaria (Fase 1):** No introducir tecnologías no listadas para Fase 1.
*   **Consultar en Caso de Duda:** Pedir clarificación si la solicitud es ambigua o conflictiva.
*   **Decisiones sobre Términos Técnicos:**
    * **Regla General:** Cuando exista duda si un término debe estar en español o inglés, preguntarse: "¿Es un término técnico de programación o infraestructura ampliamente utilizado en la industria?" Si es así, mantenerlo en inglés.
    * **Para Nuevos Términos:** Si aparece un término técnico que no está explícitamente cubierto en las listas anteriores, evaluar si cumple alguno de estos criterios:
      1. Es un término técnico de una biblioteca o framework.
      2. Es un término común en patrones de diseño o arquitectura.
      3. Es una convención ampliamente aceptada en el mundo de desarrollo.
      
      Si cumple algún criterio, mantenerlo en inglés; de lo contrario, traducirlo al español.
    * **Consistencia:** Una vez tomada una decisión sobre un término, aplicarla de manera consistente en todo el código.

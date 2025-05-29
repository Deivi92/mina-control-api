# Instrucciones para GitHub Copilot - Proyecto MinaControl Pro

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

*   **Patrón General:** Arquitectura en Capas.
*   **Paquete Base:** `com.minacontrol`
*   **Estructura de Paquetes Principal (`src/main/java/com/minacontrol`):**
    *   `MinaControlApiApplication.java`: Clase principal.
    *   `config`: Clases de configuración.
    *   `controller`: Controladores REST (`@RestController`).
    *   `dto`: Data Transfer Objects (`RequestDTO`, `ResponseDTO`).
    *   `exception`: Manejo global de excepciones (`@ControllerAdvice`), excepciones personalizadas.
    *   `model` (o `entity`): Clases de entidad.
    *   `repository`: Interfaces de repositorio.
    *   `service`: Lógica de negocio.
*   **Estructura de Paquetes de Pruebas (`src/test/java/com/minacontrol`):** Estructura paralela.

### 4.1. Diagramas UML de Referencia (PlantUML)

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

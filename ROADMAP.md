# Hoja de Ruta del Proyecto: MinaControl Pro

Este documento describe los próximos pasos estratégicos y técnicos para el desarrollo del proyecto, adaptado a un entorno sin Docker/Testcontainers.

---

### **Fase 1: Consolidación y Endurecimiento del Backend (Entorno Local)**

**Objetivo:** Asegurar que el backend sea robusto, seguro y esté listo para producción, utilizando una base de datos PostgreSQL local.

1.  **Configurar la Conexión a PostgreSQL Local:**
    *   **Acción:** Modificar el archivo `src/main/resources/application.properties` para que la aplicación se conecte a la instancia local de PostgreSQL.
    *   **Detalles de Configuración:**
        ```properties
        spring.datasource.url=jdbc:postgresql://localhost:5432/minacontrol_db
        spring.datasource.username=minacontrol_user
        spring.datasource.password=password
        spring.datasource.driver-class-name=org.postgresql.Driver
        spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
        spring.jpa.hibernate.ddl-auto=update
        ```
    *   **Por qué:** Para validar que la aplicación funciona correctamente con la base de datos de producción (PostgreSQL) y detectar posibles inconsistencias.

2.  **Ejecutar Pruebas de Integración contra PostgreSQL Local:**
    *   **Acción:** Ejecutar la suite de pruebas de integración (`mvn verify` o `mvn test -Dtest="com.minacontrol.**.*IT"`). El perfil `test` de Spring (`application-test.properties`) debería seguir usando H2 para mantener la rapidez en las pruebas unitarias, pero se deben ejecutar pruebas manuales o crear un nuevo perfil de Spring (`integration-test`) que apunte a la BD PostgreSQL local.
    *   **Por qué:** Para asegurar que la lógica de negocio completa, desde los controladores hasta la base de datos, funciona como se espera en un entorno de base de datos real.

3.  **Refinamiento y Auditoría de Seguridad:**
    *   **Acción:** Revisar todas las anotaciones `@PreAuthorize` en los controladores para asegurar que los roles son correctos y cubren todos los casos de uso.
    *   **Por qué:** Para prevenir cualquier brecha de seguridad antes de exponer la API.

---

### **Fase 2: Habilitación de la API y Documentación**

**Objetivo:** Hacer que la API sea fácilmente consumible y comprensible.

1.  **Generación de Documentación con OpenAPI (Swagger):**
    *   **Acción:** Añadir la dependencia `springdoc-openapi-starter-webmvc-ui` al `pom.xml`. Anotar los controladores y DTOs con `@Operation`, `@ApiResponse`, `@Schema`, etc.
    *   **Por qué:** Crea una documentación interactiva (Swagger UI) que sirve como un contrato claro para los desarrolladores de frontend.

2.  **Implementación de CORS (Cross-Origin Resource Sharing):**
    *   **Acción:** Crear una configuración de seguridad (`WebMvcConfigurer`) que defina una política de CORS permisiva para el entorno de desarrollo.
    *   **Por qué:** Indispensable para permitir la comunicación entre el frontend y el backend cuando se ejecutan en diferentes puertos.

---

### **Fase 3: Preparación para el Despliegue**

**Objetivo:** Empaquetar la aplicación y prepararla para un despliegue manual.

1.  **Empaquetado de la Aplicación:**
    *   **Acción:** Utilizar Maven para crear el archivo JAR ejecutable del proyecto (`mvn clean package`).
    *   **Por qué:** Genera un artefacto único que contiene todas las dependencias y puede ser ejecutado en cualquier entorno con Java.

2.  **Documentación de Despliegue:**
    *   **Acción:** Crear un archivo `DEPLOYMENT.md` que detalle los pasos para desplegar la aplicación manualmente:
        1.  Requisitos (Java 17, PostgreSQL).
        2.  Pasos para configurar la base de datos.
        3.  Comando para ejecutar el JAR (`java -jar ...`).
        4.  Variables de entorno necesarias.
    *   **Por qué:** Facilita el despliegue y la configuración del sistema a otros desarrolladores o en un servidor.

---

### **Fase 4: Desarrollo del Frontend (React.js)**

**Objetivo:** Iniciar la construcción de la interfaz de usuario.

1.  **Scaffolding del Proyecto React:**
    *   **Acción:** Crear un nuevo proyecto React usando `Vite` o `Create React App`.
    *   **Por qué:** Proporciona una estructura de proyecto moderna y optimizada.

2.  **Conexión con la API:**
    *   **Acción:** Utilizar la documentación de Swagger UI para crear los servicios de cliente (`axios` o `fetch`) que consumirán los endpoints del backend.

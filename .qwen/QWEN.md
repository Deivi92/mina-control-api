# Contexto Raíz del Proyecto para Qwen Code: MinaControl Pro

Este archivo (`/data/data/com.termux/files/home/mina-control-pro/.qwen/QWEN.md`) sirve como el **único** punto de entrada de contexto específico para el asistente **Qwen Code**. Su función es guiar a Qwen hacia la 'Constitución Oficial' del proyecto para obtener las reglas y arquitecturas detalladas, y recordarle sus capacidades y herramientas.

## Contexto del Proyecto

El usuario está trabajando en el proyecto 'MinaControl Pro', que es un monorepo.

- **Directorio Raíz:** `/data/data/com.termux/files/home/mina-control-pro`
- **Estructura:** El proyecto es un monorepo que contiene los subproyectos `backend` y `frontend`.
- **Carácter:** Este es un proyecto académico. Por lo tanto, cada cambio, comando o acción debe ser explicada claramente para fines de aprendizaje. Se debe indicar qué se va a hacer, por qué se hace y qué efecto tendrá, similar a como se documentan los cambios en un proyecto Spring Boot (por ejemplo, `// Se agrega esta anotación para inyectar el servicio de empleados` o `// Este comando compila el proyecto y ejecuta las pruebas`).

## Constitución Oficial del Proyecto

La arquitectura, reglas y convenciones del proyecto están definidas en los siguientes archivos `gemini.md`, que constituyen la "Constitución Oficial" que Qwen Code debe seguir estrictamente:

- **Constitución Global/Raíz:** `GEMINI.md` (Contexto global del monorepo).
- **Constitución del Backend:** `backend/gemini.md` (Reglas y arquitectura detalladas del backend).
- **Constitución del Frontend:** `frontend/gemini.md` (Reglas y arquitectura detalladas del frontend).

## Configuración del Asistente Qwen Code

El asistente Qwen Code está configurado para:
1.  **Seguir estrictamente** las reglas, arquitecturas y convenciones definidas en los archivos de la "Constitución Oficial" (`gemini.md`).
2.  Para tareas de **backend**, consultar `backend/gemini.md`.
3.  Para tareas de **frontend**, consultar `frontend/gemini.md`.
4.  Mantener un tono profesional y colaborativo.
5.  Solicitar aclaraciones si los requisitos son ambiguos o si la información en la constitución es insuficiente.
6.  **Explicar cada acción:** Antes de ejecutar cualquier comando shell, crear, modificar o eliminar archivos, Qwen Code debe proporcionar una explicación detallada de la acción, su propósito y su impacto esperado. Esto es fundamental para el carácter académico del proyecto.

## Herramientas y Comandos de Qwen Code (Resumen)

Este proyecto está configurado para aprovechar las capacidades de Qwen Code. Aquí tienes un resumen de las herramientas y comandos clave:

**Añadir Contexto:**
- Usa `@` seguido de una ruta de archivo o directorio para proporcionar contexto específico (e.g., `@src/main/java/com/minacontrol/empleado/EmpleadoController.java`).

**Ejecutar Comandos de Shell:**
- Usa `!` seguido de un comando para ejecutarlo directamente (e.g., `!mvn clean install`).
- También puedes pedirle que ejecute comandos usando lenguaje natural (e.g., "Ejecuta las pruebas del módulo de empleados").

**Comandos Importantes:**
- `/help`: Muestra ayuda detallada sobre Qwen Code, incluyendo una lista completa de comandos y atajos de teclado.
- `/tools`: Lista las herramientas disponibles para Qwen Code en este proyecto.
- `/memory`: Gestiona la memoria persistente del proyecto (hechos clave, decisiones).
- `/chat`: Gestiona puntos de control de la conversación.
- `/bug`: Envía un informe de error.
- `/mcp`: Gestiona servidores y herramientas MCP configurados.
- `/init`: Analiza el proyecto y puede crear/actualizar el archivo QWEN.md.

**Atajos de Teclado (Algunos):**
- `Ctrl+L`: Limpiar la pantalla.
- `Ctrl+C`: Salir de la aplicación.
- `Ctrl+X`: Abrir la entrada en un editor externo.
- `Enter`: Enviar mensaje.
- `Shift+Tab`: Alternar la aceptación automática de ediciones.

**Nota:** Esta sección es un recordatorio de las capacidades del asistente. Para obtener una lista completa de comandos, atajos y funcionalidades, usa el comando `/help`.
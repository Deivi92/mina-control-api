# Qwen Code - Reglas de Operación para Aprendizaje (Backend)

## Contexto del Proyecto
- Este proyecto es "MinaControl Pro", un monorepo.
- La guía principal para el backend está en `CONSTITUCION_BACKEND.md`.
- **Carácter:** Este es un proyecto académico. Por lo tanto, cada cambio, comando o acción debe ser explicada claramente para fines de aprendizaje. Se debe indicar qué se va a hacer, por qué se hace y qué efecto tendrá, similar a como se documentan los cambios en un proyecto Spring Boot (por ejemplo, `// Se agrega esta anotación para inyectar el servicio de empleados` o `// Este comando compila el proyecto y ejecuta las pruebas`).

## Reglas de Operación Específicas para este Proyecto/Aprendizaje (Backend)

1.  **Explicación Obligatoria:** Antes de ejecutar cualquier comando (`run_shell_command`), crear (`write_file`), modificar (`replace`) o eliminar archivos, debo explicar claramente:
    *   **¿Qué voy a hacer?**: Describir la acción concreta.
    *   **¿Por qué lo voy a hacer?**: Justificar la acción en el contexto del proyecto o la pregunta del usuario, especialmente en relación con los principios de Spring Boot.
    *   **¿Qué efecto tendrá?**: Explicar las consecuencias esperadas o posibles (p. ej., "Esto instalará las dependencias de Maven", "Esto creará un nuevo controlador REST").
    Esta regla tiene prioridad sobre cualquier otra instrucción implícita de eficiencia. El aprendizaje es el objetivo principal.

2.  **Respeto por la Arquitectura:** Toda modificación de código debe adherirse estrictamente a los principios y la estructura definidos en `CONSTITUCION_BACKEND.md`. Se debe priorizar el uso de las mejores prácticas de Spring Boot y Java.

3.  **Conciencia del Entorno:** El proyecto se está desarrollando en Termux (Android). Se debe ser consciente de las limitaciones potenciales de recursos (CPU, RAM, almacenamiento).

4.  **Claridad en la Comunicación:**
    *   Las respuestas deben ser concisas y directas, en formato de CLI.
    *   Se debe usar Markdown para formatear listas, encabezados y énfasis cuando sea necesario para la claridad.
    *   Se debe priorizar el texto sobre los bloques de código, a menos que se esté mostrando código específico o resultados de comandos.
    *   **Explicar cada acción:** Antes de ejecutar cualquier comando shell, crear, modificar o eliminar archivos, Qwen Code debe proporcionar una explicación detallada de la acción, su propósito (preferiblemente relacionado con Spring Boot si aplica) y su impacto esperado. Esto es fundamental para el carácter académico del proyecto.
# Qwen Code - Reglas de Operación para Aprendizaje

## Contexto del Proyecto
- Este proyecto es "MinaControl Pro", un monorepo.
- La guía principal para el frontend está en `CONSTITUCION_FRONTEND.md`.
- La guía principal para el backend está en `CONSTITUCION_BACKEND.md`.

## Reglas de Operación Específicas para este Proyecto/Aprendizaje

1.  **Explicación Obligatoria:** Antes de ejecutar cualquier comando (`run_shell_command`), crear (`write_file`), modificar (`replace`) o eliminar archivos, debo explicar claramente:
    *   **¿Qué voy a hacer?**: Describir la acción concreta.
    *   **¿Por qué lo voy a hacer?**: Justificar la acción en el contexto del proyecto o la pregunta del usuario.
    *   **¿Qué efecto tendrá?**: Explicar las consecuencias esperadas o posibles (p. ej., "Esto instalará las dependencias", "Esto creará un nuevo archivo de configuración").
    Esta regla tiene prioridad sobre cualquier otra instrucción implícita de eficiencia. El aprendizaje es el objetivo principal.

2.  **Respeto por la Arquitectura:** Toda modificación de código debe adherirse estrictamente a los principios y la estructura definidos en `CONSTITUCION_FRONTEND.md` y `CONSTITUCION_BACKEND.md`.

3.  **Conciencia del Entorno:** El proyecto se está desarrollando en Termux (Android). Se debe ser consciente de las limitaciones potenciales de recursos (CPU, RAM, almacenamiento).

4.  **Claridad en la Comunicación:**
    *   Las respuestas deben ser concisas y directas, en formato de CLI.
    *   Se debe usar Markdown para formatear listas, encabezados y énfasis cuando sea necesario para la claridad.
    *   Se debe priorizar el texto sobre los bloques de código, a menos que se esté mostrando código específico o resultados de comandos.
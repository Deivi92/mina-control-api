
# Blueprint de UI/UX: Dominio de Turnos

**Versión:** 1.0
**Fecha:** 2024-05-22
**Autor:** Gemini

## 1. Propósito y Alcance

Este documento define la experiencia de usuario (UX) y el diseño de la interfaz de usuario (UI) para la gestión del dominio "Turnos" en la aplicación MinaControl Pro. El objetivo es proporcionar una herramienta clara y eficiente para que los administradores gestionen los tipos de turno y para que los supervisores o el personal de RRHH gestionen la asistencia y las asignaciones de los empleados.

El diseño se divide en dos funcionalidades principales, que se presentarán como pestañas en la página de "Gestión de Turnos":
1.  **Administración de Tipos de Turno:** CRUD para las plantillas de turnos (ej. "Turno Diurno", "Turno Nocturno").
2.  **Control de Asistencia y Asignaciones:** Gestión diaria de qué empleado trabaja en qué turno y registro de su asistencia.

## 2. Flujos de Usuario

### Flujo 2.1: Administrador crea un nuevo tipo de turno
1.  El administrador navega a la página de "Gestión de Turnos".
2.  Selecciona la pestaña "Tipos de Turno".
3.  Hace clic en el botón "Crear Nuevo Tipo de Turno".
4.  Se abre un modal/formulario donde introduce:
    *   Nombre del turno (ej. "Turno de Tarde").
    *   Hora de inicio.
    *   Hora de fin.
    *   Color para identificación visual.
5.  Hace clic en "Guardar".
6.  El nuevo tipo de turno aparece en la tabla. Se muestra una notificación de éxito.

### Flujo 2.2: Supervisor asigna un turno a un empleado
1.  El supervisor navega a la página de "Gestión de Turnos".
2.  Selecciona la pestaña "Control de Asistencia".
3.  Utiliza un selector de fecha para elegir el día a gestionar.
4.  Hace clic en el botón "Asignar Turno".
5.  Se abre un formulario donde selecciona:
    *   Un empleado de una lista desplegable.
    *   Un tipo de turno de una lista desplegable.
    *   La fecha (pre-seleccionada).
6.  Hace clic en "Asignar".
7.  La vista se actualiza mostrando la nueva asignación del empleado para ese día.

### Flujo 2.3: Operador registra su asistencia (Entrada/Salida)
1.  El operador (o un supervisor) va a la sección "Registrar Asistencia".
2.  Selecciona al empleado.
3.  El sistema muestra si tiene un turno asignado para hoy.
4.  Hace clic en "Registrar Entrada" o "Registrar Salida".
5.  El sistema guarda la hora actual. Se muestra una notificación de éxito.

## 3. Diseño de la Interfaz (Wireframes y Componentes)

### Página Principal: `TurnosPage.tsx`
-   Contendrá un componente de Pestañas (Tabs) de MUI.
-   **Pestaña 1: "Tipos de Turno"**
    -   Renderiza el componente `TiposTurnoTab.tsx`.
-   **Pestaña 2: "Control de Asistencia"**
    -   Renderiza el componente `AsistenciaTab.tsx`.

### Pestaña 1: `TiposTurnoTab.tsx`
-   **Componentes:**
    -   `Button`: "Crear Nuevo Tipo de Turno".
    -   `TiposTurnoTable.tsx`: Una tabla (MUI DataGrid) con columnas:
        -   `Nombre`
        -   `Hora Inicio`
        -   `Hora Fin`
        -   `Color` (un pequeño círculo con el color)
        -   `Acciones` (botones de icono para Editar y Eliminar).
    -   `TipoTurnoForm.tsx`: Un componente de formulario (usado en un Modal) para crear y editar.
        -   `TextField` para el nombre.
        -   `TimePicker` para las horas.
        -   Un selector de color simple.

### Pestaña 2: `AsistenciaTab.tsx`
-   **Componentes:**
    -   `DatePicker`: Para seleccionar la fecha a visualizar/gestionar.
    -   `AsignacionesDia.tsx`: Un componente que muestra las asignaciones del día seleccionado. Podría ser una lista o tarjetas.
        -   Por cada empleado asignado, muestra su nombre, el turno asignado y el estado de su asistencia (Puntual, Retraso, Ausente).
    -   `Button`: "Asignar Turno". Abre un modal con el formulario `AsignacionTurnoForm.tsx`.
    -   `AsignacionTurnoForm.tsx`:
        -   `Select` (Autocomplete de MUI) para buscar y seleccionar un empleado.
        -   `Select` para seleccionar un `TipoTurno`.
        -   `DatePicker` para la fecha.
    -   `RegistrarAsistenciaForm.tsx`:
        -   `Select` (Autocomplete) para buscar al empleado.
        -   Botones "Registrar Entrada" y "Registrar Salida".

## 4. Estructura de Carpetas y Archivos (Propuesta)

```
src/domains/turnos/
├── components/
│   ├── TiposTurnoTab.tsx
│   ├── TiposTurnoTable.tsx
│   ├── TipoTurnoForm.tsx
│   ├── AsistenciaTab.tsx
│   ├── AsignacionesDia.tsx
│   ├── AsignacionTurnoForm.tsx
│   └── RegistrarAsistenciaForm.tsx
├── hooks/
│   ├── useTiposTurno.ts       # Para el CRUD de Tipos de Turno
│   └── useAsistencia.ts       # Para asignaciones y registros de asistencia
├── services/
│   ├── turno.service.ts       # API calls para Tipos de Turno y Asignaciones
│   └── asistencia.service.ts  # API calls para Asistencia
└── types.ts                   # Interfaces y tipos de TypeScript
```

## 5. Criterios de Aceptación

1.  **Gestión de Tipos de Turno:**
    -   [ ] El usuario puede ver una lista de todos los tipos de turno.
    -   [ ] El usuario puede crear un nuevo tipo de turno a través de un formulario modal.
    -   [ ] El usuario puede editar un tipo de turno existente.
    -   [ ] El usuario puede eliminar un tipo de turno.
    -   [ ] El formulario debe mostrar validaciones de error (ej. nombre vacío, hora de fin anterior a la de inicio).
2.  **Control de Asistencia:**
    -   [ ] El usuario puede seleccionar una fecha y ver las asignaciones de ese día.
    -   [ ] El usuario puede asignar un turno a un empleado para una fecha específica.
    -   [ ] El usuario puede registrar la hora de entrada y salida de un empleado.
    -   [ ] El sistema debe mostrar feedback claro (spinners, notificaciones) durante las operaciones de API.

## 6. Adhesión a la Guía de Usabilidad (`USABILITY_GUIDE.md`)

-   **Feedback Inmediato:** Se usarán componentes `CircularProgress` de MUI en botones y `Snackbar` para notificaciones de éxito/error.
-   **Consistencia:** Se usarán componentes de MUI (`Button`, `TextField`, `DataGrid`, `Modal`) para mantener la consistencia visual con el resto de la aplicación.
-   **Prevención de Errores:** Los formularios incluirán validación en tiempo real y los botones de acciones peligrosas (Eliminar) mostrarán un diálogo de confirmación.
-   **Claridad:** La separación en pestañas ayuda a reducir la carga cognitiva, permitiendo al usuario enfocarse en una tarea a la vez (administrar plantillas vs. gestionar el día a día).

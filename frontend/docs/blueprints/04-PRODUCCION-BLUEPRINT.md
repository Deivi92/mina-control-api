# Blueprint de UI/UX: Dominio de Producción

## 1. Objetivo del Dominio

El dominio de Producción tiene como objetivo permitir a los administradores registrar, consultar, editar y validar la producción diaria de los empleados. Este dominio es crucial para el control de operaciones mineras y servirá como base para cálculos de nómina y generación de reportes.

## 2. Flujos de Usuario

### 2.1 Flujo Principal: Registro de Producción Diaria

1. El administrador accede a la sección de Producción
2. El sistema muestra la lista de registros de producción existentes
3. El administrador hace clic en "Registrar Nueva Producción"
4. Se abre un formulario con los siguientes campos:
   - Empleado (selector)
   - Fecha de registro (date picker, por defecto fecha actual)
   - Turno (selector con turnos asignados al empleado en esa fecha)
   - Cantidad extraída (número con decimales)
   - Ubicación de extracción (texto)
   - Observaciones (texto opcional)
5. El administrador completa los campos y guarda
6. El sistema valida los datos y los envía al backend
7. El sistema actualiza la lista con el nuevo registro

### 2.2 Flujo Secundario: Consulta y Filtrado

1. El administrador accede a la sección de Producción
2. El sistema muestra la lista de registros con controles de filtrado:
   - Por empleado
   - Por rango de fechas
   - Por turno
3. El administrador aplica filtros
4. El sistema actualiza la tabla con los resultados

### 2.3 Flujo de Validación

1. El administrador identifica un registro que requiere validación
2. El administrador selecciona el registro y hace clic en "Validar"
3. El sistema confirma la acción
4. El registro cambia su estado a "validado"
5. El sistema actualiza la interfaz para reflejar el cambio

## 3. Wireframes

### 3.1 Página Principal de Producción
```
[Encabezado de la aplicación]
[Menú de navegación]

[Título: "Producción Diaria"]
[Botón: "Registrar Nueva Producción"]

[Controles de Filtro]
┌─────────────────────────────────────────────────────────┐
│ Empleado: [Selector]  Fecha: [__.__.__ - .__.__.__]     │
│ Turno: [Selector]                                     │
│ [Aplicar Filtros] [Limpiar Filtros]                   │
└─────────────────────────────────────────────────────────┘

[Tabla de Registros de Producción]
┌─────────────────────────────────────────────────────────┐
│ Empleado │ Fecha │ Turno │ Cantidad (t) │ Validado │   │
├─────────────────────────────────────────────────────────┤
│ Juan P.  │ 12/10 │ Día   │ 15.5         │ [✓]      │ > │
│ María G. │ 12/10 │ Noche │ 12.3         │ [ ]      │ > │
└─────────────────────────────────────────────────────────┘
[Paginación: 1 de 5] [Anterior] 1 2 3 4 5 [Siguiente]

[Barra de estado: Mostrando 10 de 45 registros]
```

### 3.2 Formulario de Registro de Producción
```
[Encabezado: "Registro de Producción"]
[Botón: "Volver"] [Botón: "Guardar"]

┌─────────────────────────────────────────────────────────┐
│ Empleado: [Selector de Empleados] *                    │
│ Fecha: [__.__.__] * (por defecto: hoy)                │
│ Turno: [Selector de Turnos del Empleado] *             │
│ Cantidad Extraída (toneladas): [_______] *             │
│ Ubicación de Extracción: [____________________] *       │
│ Observaciones: [________________________________]      │
│                                                         │
│ [Cancelar] [Guardar]                                    │
└─────────────────────────────────────────────────────────┘
```

## 4. Componentes de UI

### 4.1 Componentes Principales
- `ProduccionPage`: Página principal que orquesta todos los demás componentes
- `RegistroProduccionTable`: Tabla que muestra los registros de producción
- `RegistroProduccionForm`: Formulario para crear/editar registros
- `RegistroProduccionFilter`: Componente de filtros de búsqueda

### 4.2 Componentes Secundarios
- `CantidadInput`: Input especializado para ingresar cantidades con decimales
- `ValidarRegistroDialog`: Diálogo de confirmación para validar registros

## 5. Criterios de Aceptación

### 5.1 Funcionalidad
- [ ] El sistema permite registrar nuevos registros de producción
- [ ] El sistema permite editar registros existentes
- [ ] El sistema permite eliminar registros existentes
- [ ] El sistema permite validar registros (cambiar estado de validación)
- [ ] El sistema permite filtrar registros por empleado, fecha y turno
- [ ] El sistema muestra mensajes claros de éxito o error en todas las operaciones

### 5.2 Usabilidad
- [ ] Los formularios tienen validación en cliente
- [ ] El sistema muestra indicadores de carga durante operaciones
- [ ] Los mensajes de error son claros y ayudan al usuario a corregir
- [ ] El sistema requiere confirmación para operaciones destructivas
- [ ] La interfaz es responsive y funciona en dispositivos móviles

### 5.3 Accesibilidad
- [ ] Los componentes tienen etiquetas adecuadas para lectores de pantalla
- [ ] El sistema es navegable completamente con teclado
- [ ] El sistema cumple con estándares de contraste de color

## 6. Adhesión a la Guía de Usabilidad (`USABILITY_GUIDE.md`)

### 6.1 Visibilidad del estado del sistema
- El sistema mostrará indicadores de carga (spinners) mientras se procesan operaciones.
- Los cambios se reflejarán inmediatamente en la interfaz (por ejemplo, al validad un registro).
- Los mensajes de éxito o error serán claros y visibles en la interfaz.

### 6.2 Control y libertad del usuario
- Se incluirán botones de "Cancelar" en todos los formularios.
- Se pedirá confirmación antes de operaciones destructivas (eliminar registros).
- Se permitirá deshacer ciertas operaciones críticas (como validación accidental).

### 6.3 Consistencia y estándares
- Se utilizarán los mismos patrones de diseño y componentes que en los dominios anteriores (Empleados, Turnos).
- Los colores, tipografía y estilos seguirán el design system ya establecido.
- Los íconos serán consistentes con los utilizados en otras partes de la aplicación.

### 6.4 Reconocimiento antes que recuerdo
- Los formularios tendrán etiquetas claras y campos con placeholders descriptivos.
- Los campos de selección (empleado, turno) mostrarán listas descriptivas.
- Los botones tendrán texto descriptivo (no solo íconos).

### 6.5 Prevención y recuperación de errores
- Se aplicarán validaciones en cliente antes de enviar datos al servidor.
- Los mensajes de error se mostrarán de forma clara y se enfocará el campo con error.
- Se validarán formatos de datos (por ejemplo, números decimales válidos).

### 6.6 Feedback inmediato y útil
- Los botones cambiarán de estado cuando se presionen (efecto visual).
- Se mostrarán mensajes de éxito después de operaciones exitosas.
- Las operaciones de carga tendrán indicadores visuales claros.

### 6.7 Curva de aprendizaje progresiva
- La interfaz será simple para usuarios nuevos pero eficiente para usuarios avanzados.
- Se incluirán atajos de teclado para operaciones comunes.

### 6.8 Lenguaje del usuario
- Todos los mensajes, títulos y labels estarán en español y usarán lenguaje cotidiano.
- No se mostrarán códigos de error técnicos al usuario final.
- Los mensajes de error explicarán claramente qué sucedió y cómo corregirlo.

### 6.9 Evaluación constante y basada en datos
- Se implementarán herramientas de logging para identificar problemas de usabilidad.
- Se realizarán pruebas con usuarios reales una vez implementada la interfaz.

### 6.10 Diseño centrado en tareas
- El flujo de registro de producción será directo y sin pasos innecesarios.
- Los controles de filtrado estarán fácilmente accesibles.
- La operación más común (registro de nueva producción) será la más prominente en la interfaz.
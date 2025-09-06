# Blueprint de UI/UX: Dominio de Empleados

## 1. Objetivo

Crear una interfaz de usuario robusta, intuitiva y eficiente para la gestión completa de los empleados de la mina, permitiendo a los administradores realizar operaciones CRUD (Crear, Leer, Actualizar, Desactivar) de forma clara y con retroalimentación constante.

## 2. Componentes Principales

-   `EmpleadosPage.tsx`: Orquestador principal que renderiza la tabla y el formulario.
-   `EmpleadoTable.tsx`: Componente para mostrar la lista de empleados con opciones de paginación, búsqueda, edición y desactivación.
-   `EmpleadoForm.tsx`: Formulario (probablemente en un Modal) para crear un nuevo empleado o editar uno existente.
-   `DeleteEmpleadoDialog.tsx`: Diálogo de confirmación para desactivar un empleado.

## 3. Wireframe (Esquema Visual)

### Vista Principal (`/empleados`)

```
+-------------------------------------------------------------------+
| MinaControl Pro - Empleados                                       |
+-------------------------------------------------------------------+
| [Búsqueda por nombre/apellido...] [Filtrar por Puesto v] [ACTIVO v] |
|                                        [+ Crear Nuevo Empleado]   |
+-------------------------------------------------------------------+
|                                                                   |
| | Nombre Completo  | Puesto      | Email           | Estado  | Acciones |
| |------------------|-------------|-----------------|---------|----------|
| | Juan Pérez       | Minero      | juan.p@mail.com | ACTIVO  | [E] [D]  |
| | Ana García       | Geóloga     | ana.g@mail.com  | ACTIVO  | [E] [D]  |
| | ...              | ...         | ...             | ...     | ...      |
|                                                                   |
|                                                                   |
| < 1, 2, 3 ... >                                                   |
+-------------------------------------------------------------------+
```

### Modal de Creación/Edición de Empleado

```
+-------------------------------------------+
| Crear Nuevo Empleado                      |
+-------------------------------------------+
|                                           |
| Nombre: [_________________] *             |
| Apellido: [_________________] *           |
| Email: [___________________] *            |
| Puesto: [__________________] *            |
| Salario: [_________________] *            |
| ... otros campos ...                      |
|                                           |
|                      [Cancelar] [Guardar] |
+-------------------------------------------+
```

## 4. Flujo de Usuario

1.  El usuario navega a la página de "Empleados".
2.  La `EmpleadoTable` muestra una lista paginada de los empleados activos por defecto. Un spinner indica la carga inicial.
3.  El usuario puede usar los filtros para buscar empleados específicos.
4.  Al hacer clic en el botón **[+ Crear Nuevo Empleado]**, se abre un modal con el `EmpleadoForm` vacío.
5.  El usuario rellena el formulario. La validación se realiza en tiempo real. El botón "Guardar" está deshabilitado hasta que los campos requeridos son válidos.
6.  Al guardar, se muestra un spinner en el botón y se cierra el modal al recibir la confirmación de la API. La tabla se actualiza automáticamente.
7.  Al hacer clic en el icono de editar **[E]** de una fila, se abre el mismo modal `EmpleadoForm`, pero precargado con los datos de ese empleado.
8.  Al hacer clic en el icono de desactivar **[D]**, se abre el `DeleteEmpleadoDialog` pidiendo confirmación. Al confirmar, el estado del empleado cambia y la tabla se refresca.

## 5. Análisis de Usabilidad (Cumplimiento de USABILITY_GUIDE.md)

-   **Visibilidad del estado del sistema:** Se usarán spinners durante la carga de datos y al guardar/actualizar. Se mostrarán notificaciones (toasts) de éxito o error tras cada operación.
-   **Control y libertad del usuario:** El usuario puede cancelar la creación/edición en cualquier momento. La desactivación requiere una confirmación para evitar acciones accidentales.
-   **Consistencia y estándares:** Se usarán componentes de Material-UI, manteniendo la consistencia visual con el resto de la aplicación. Los iconos (Editar, Eliminar) serán estándar.
-   **Prevención de errores:** La validación de formularios en tiempo real y la desactivación del botón "Guardar" previenen el envío de datos incorrectos.
-   **Reconocimiento antes que recuerdo:** Las etiquetas de los campos serán claras. No se requiere que el usuario memorice información.
-   **Feedback inmediato:** Cada acción (clic, escritura) tendrá una respuesta visual, como la actualización de la tabla o la aparición de un mensaje.

## 6. Criterios de Aceptación

-   [ ] Se puede ver una lista paginada de empleados.
-   [ ] Se puede buscar un empleado por su nombre o apellido.
-   [ ] Se puede filtrar a los empleados por su estado (Activo/Inactivo).
-   [ ] Se puede crear un nuevo empleado a través de un formulario modal.
-   [ ] El formulario de creación valida los campos correctamente.
-   [ ] Se puede editar la información de un empleado existente.
-   [ ] Se puede desactivar a un empleado (borrado lógico) previa confirmación.
-   [ ] La interfaz es responsive y se visualiza correctamente en dispositivos móviles.

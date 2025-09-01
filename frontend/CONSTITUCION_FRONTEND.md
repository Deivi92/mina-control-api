# Contexto del Frontend: MinaControl Pro (App React)

**Rol de este archivo:** Este documento contiene todas las reglas, arquitecturas y convenciones que deben seguirse estrictamente para el desarrollo de la aplicación de React.

**Navegación de Contexto:**
- **Contexto Global:** `../GEMINI.md`
- **Contexto Backend:** `../backend/GEMINI.md`

---
# CONSTITUCIÓN FRONTEND - MINA CONTROL

## Filosofía de Desarrollo

Este documento establece el proceso de desarrollo y la arquitectura para el frontend del proyecto Mina Control. La filosofía es combinar la disciplina y el orden de la arquitectura de backend con un flujo de trabajo moderno que garantice la máxima calidad en la interfaz y experiencia de usuario (UI/UX). Cada dominio de la aplicación se construirá como un bloque completo y probado antes de pasar al siguiente, asegurando un crecimiento robusto y mantenible del proyecto.

## Plan de Desarrollo por Dominios

Basado en el análisis del `openapi-spec.json`, se ha definido el siguiente plan de trabajo para la construcción del frontend. Esta será nuestra "lista de tareas" (To-Do List) a seguir.

-   [ ] **Dominio 1: Autenticación**
    -   *Objetivo:* Permitir a los usuarios iniciar y cerrar sesión. Proteger las rutas que lo requieran. Es la base de la seguridad.
-   [ ] **Dominio 2: Empleados**
    -   *Objetivo:* Crear la interfaz para listar, crear, editar y ver los empleados. Es un dominio central que se conectará con muchos otros.
-   [ ] **Dominio 3: Turnos**
    -   *Objetivo:* Gestionar los tipos de turno y asignarlos a los empleados.
-   [ ] **Dominio 4: Producción**
    -   *Objetivo:* Implementar el formulario para que los empleados registren su producción diaria.
-   [ ] **Dominio 5: Nómina**
    -   *Objetivo:* Configurar las tarifas y permitir el cálculo de la nómina. Depende de los dominios anteriores.
-   [ ] **Dominio 6: Logística**
    -   *Objetivo:* Administrar los despachos.
-   [ ] **Dominio 7: Reportes**
    -   *Objetivo:* Visualizar los datos consolidados de la operación.

Para cada dominio, se seguirá rigurosamente **"El Flujo de Desarrollo Detallado por Dominio"** descrito más abajo en esta misma constitución.

## Enfoque Pedagógico

Este es un proyecto con un fuerte enfoque en el aprendizaje. Por lo tanto, el asistente de IA (Gemini) debe seguir las siguientes directrices:

- **Explicación Exhaustiva:** Cada paso, comando o decisión técnica debe ser explicado en detalle. No se debe asumir ningún conocimiento previo por parte del usuario.
- **Justificación del "Porqué":** Además de explicar el "qué" se está haciendo, es crucial justificar el "porqué". Por ejemplo, al instalar una dependencia, se debe explicar qué problema resuelve y por qué fue elegida sobre otras alternativas si es relevante.
- **Analogías con Spring Boot:** Siempre que sea posible, los conceptos del ecosistema frontend (Node.js, Vite, React, etc.) deben ser explicados usando analogías y símiles con el ecosistema backend de Java/Spring Boot, dado que es la referencia conocida por el usuario.
- **Investigación Activa:** Ante cualquier duda o concepto desconocido, se debe realizar una investigación activa (usando las herramientas de búsqueda) para obtener y presentar la información necesaria. No se dará ningún conocimiento por sentado.
- **Transparencia Total:** Todas las acciones deben ser comunicadas antes de ser ejecutadas, detallando su propósito e impacto esperado.

## Stack Tecnológico

- **Framework:** React 18 + Vite + TypeScript.
- **Librería de Componentes:** Material-UI (MUI) v5.
- **Cliente HTTP:** Axios.
- **Enrutamiento:** React Router v6.
- **Gestión de Estado del Servidor:** React Query.
- **Gestión de Estado Global (UI):** React Context.
- **Pruebas:** Jest + React Testing Library + Playwright.
- **Calidad de Código:** ESLint + Prettier + Husky.

## Arquitectura de Carpetas

La estructura está diseñada para ser un espejo intuitivo de la arquitectura del backend, agrupando los archivos por su responsabilidad arquitectónica.

```plaintext
frontend/
├── public/
├── .env                    # Variables de entorno (URL de API, claves públicas)
├── package.json
├── vite.config.js
└── src/
    ├── app/
    │   ├── assets/         # Imágenes, fuentes, etc.
    │   ├── styles/         # Estilos globales (global.css, theme.css)
    │   ├── App.jsx         # Componente raíz de la aplicación
    │   └── main.jsx        # Punto de entrada (renderiza App)
    │
    ├── auth/               # Lógica de Autenticación y Seguridad
    │   ├── services/       # auth.service.js (login, logout, refresh)
    │   ├── hooks/          # useAuth.js (hook para acceder a datos del usuario)
    │   ├── utils/          # token.js (helpers para manejar el JWT)
    │   └── ProtectedRoute.jsx # Componente para proteger rutas
    │
    ├── config/             # Configuración de herramientas (ej. instancia de Axios)
    │   └── axios.js        # Instancia de Axios con interceptores para el JWT
    │
    ├── domains/            # LÓGICA DE NEGOCIO PRINCIPAL
    │   ├── empleado/       # Dominio '''empleado'''
    │   │   ├── components/ # Componentes de UI específicos de este dominio
    │   │   │   ├── EmpleadoTable.jsx
    │   │   │   ├── EmpleadoForm.jsx
    │   │   │   └── EmpleadoFilter.jsx
    │   │   ├── hooks/      # Lógica de negocio y estado (tu '''service''' de backend)
    │   │   │   └── useEmpleados.js
    │   │   ├── services/   # Comunicación con la API (tu '''repository''' de backend)
    │   │   │   └── empleado.service.js
    │   │   └── types.ts    # DTOs y modelos (Empleado, EmpleadoRequest, etc.)
    │   │
    │   ├── nomina/         # Dominio '''nomina''' (misma estructura interna)
    │   └── ...             # Otros dominios
    │
    ├── pages/              # Orquestadores de vistas (tus '''Controllers''')
    │   ├── HomePage.jsx
    │   ├── EmpleadosPage.jsx  # Orquesta los componentes del dominio '''empleado'''
    │   ├── NominaPage.jsx     # Orquesta los componentes del dominio '''nomina'''
    │   └── LoginPage.jsx
    │
    ├── router/             # Definición de rutas de la aplicación
    │   └── index.jsx
    │
    └── shared/             # Código 100% reutilizable
        ├── components/     # UI genérica (Button, Input, Modal, Spinner)
        ├── hooks/          # Hooks genéricos (useToggle, useDebounce)
        └── utils/          # Funciones genéricas (formatDate, capitalize)
```

## El Flujo de Desarrollo Detallado por Dominio

Este es el ciclo de vida completo, paso a paso, para implementar un nuevo dominio en el frontend (ej. "Empleados").

### Paso 0: Configuración Inicial del Proyecto y Dependencias
* **Intención:** Establecer una base sólida y uniforme para el desarrollo.
* **Acciones:**
  1. **Selección de tecnologías base:** React, Vite, TypeScript, React Query, MUI, etc.
  2. **Configuración de herramientas de calidad:** ESLint, Prettier, Husky, GitHub Actions.
  3. **Definición del Sistema de Diseño:** Crear y configurar el tema global de la aplicación en `src/app/styles/theme.ts` para definir la paleta de colores, tipografía y estilo general.
  4. **Creación de la Estructura de Activos:** Preparar el directorio `src/app/assets` y añadir los logos o iconos base de la aplicación.
  5. **Plantillas y generadores:** Crear snippets para VS Code y plantillas de archivos.

### Paso 1: Análisis del Contrato (API Spec)
* **Intención:** Establecer una única fuente de verdad.
* **Acciones:**
  1. Analizar `openapi-spec.json`.
  2. Identificar endpoints, modelos y DTOs del dominio.
  3. Generar automáticamente tipos TypeScript a partir del OpenAPI spec.

### Paso 2: Creación del Blueprint de UI/UX
* **Intención:** Diseñar una experiencia de usuario tangible y de alta calidad, aplicando los principios de la `USABILITY_GUIDE.md` al dominio específico.
* **Acciones:**
  1. **Crear un archivo de Blueprint:** Para cada dominio, se creará un archivo `docs/blueprints/XX-NOMBRE-BLUEPRINT.md`.
  2. **Definir el Diseño:** El blueprint debe contener los wireframes, la lista de componentes y los flujos de usuario.
  3. **Validación Explícita de Usabilidad:** El blueprint debe incluir una sección que explique cómo el diseño propuesto cumple con los principios clave de la `USABILITY_GUIDE.md`, poniendo dichos principios al servicio de las necesidades del dominio.
  4. **Definir Criterios de Aceptación:** El documento concluirá con una lista de criterios de aceptación claros.

### Paso 2.5: Creación del Plan de Acción (Desarrollo Basado en Especificación)
*   **Intención:** Traducir el flujo abstracto en un plan de acción concreto, adoptando un enfoque de **Desarrollo Basado en Especificación (Specification-Driven Development)**.
*   **Acciones:**
    1.  Antes de escribir código de implementación, el asistente debe crear la **especificación de pruebas completa** para todas las capas del dominio (Servicios, Hooks, Componentes).
    2.  Esto implica crear todos los archivos `.test.ts(x)` necesarios, cada uno con el esqueleto completo de bloques `describe` e `it` que definen la totalidad del comportamiento esperado, basándose en el OpenAPI spec y el Blueprint de UI/UX.
    3.  Este conjunto de pruebas se convierte en la especificación formal y la lista de tareas para la fase de implementación.
    4.  El asistente debe presentar este plan y obtener la aprobación del usuario antes de proceder.

### Paso 3: Desarrollo Guiado por Pruebas Unitarias (TDD)
* **Intención:** Validar cada pieza de forma aislada.
* **Acciones:**
  1. Crear archivos `.test.tsx` para cada componente, hook y servicio.
  2. Escribir pruebas unitarias que validen el comportamiento según el blueprint.
  3. Medir cobertura de código (>80%).

### Paso 4: Implementación del Código
* **Intención:** Escribir código limpio para hacer pasar las pruebas.
* **Acciones:**
  1. Implementar JSX, CSS y lógica.
  2. Ejecutar `npm test` hasta que todo esté en verde.
  3. Refactorizar y realizar auto-revisión de código.

### Paso 5: Pruebas de Integración
* **Intención:** Verificar que las piezas colaboran correctamente.
* **Acciones:**
  1. Escribir pruebas para las `Pages`, simulando interacciones complejas entre componentes.
  2. Mockear respuestas de la API para simular escenarios.

### Paso 6: Pruebas End-to-End (E2E)
* **Intención:** Simular el recorrido completo de un usuario real.
* **Acciones:**
  1. Usar Playwright para crear scripts automatizados para los "caminos felices".
  2. Probar escenarios de error (servidor caído, permisos insuficientes).

### Paso 7: Verificación Final y Cierre del Dominio
* **Intención:** Realizar la validación humana final.
* **Acciones:**
  1. Revisar consistencia del diseño, responsive, rendimiento y pulir detalles visuales.
  2. Hacer el `commit` final y considerar el dominio cerrado.

## Gestión de Estado Global y Local

### Estado del Servidor
- **Herramienta:** React Query.
- **Responsabilidad:** Gestionar todos los datos de la API.
- **Beneficios:** Caching, revalidación, manejo de `loading`/`error`.

### Estado Global de UI
- **Herramienta:** React Context.
- **Responsabilidad:** Estado global no persistente (info de usuario, tema, notificaciones).
- **Cuándo NO usarlo:** Para datos de la API o estado local.

### Estado Local de Componente
- **Herramienta:** `useState`, `useReducer`.
- **Responsabilidad:** Estado relevante solo para un componente (valores de un formulario, si un modal está abierto).

## Manejo de Errores

- **Errores de API:** Capturados por interceptors de Axios y manejados por React Query.
- **Errores de Componente:** Usar `ErrorBoundary` de React para atrapar errores y mostrar una UI de fallback.
- **Errores de Validación:** Mostrar mensajes de error específicos en los formularios.

## Estrategia de Pruebas Detallada

La arquitectura de la aplicación se divide en 3 capas por cada dominio, y la estrategia de testing se alinea con esta separación.

### 1. **Capas del Dominio**

-   **Servicios (Lógica de datos):** Responsables de la comunicación con la API.
    ```typescript
    // empleado.service.ts
    export const empleadoService = {
      obtenerEmpleados: () => axios.get('/api/empleados'),
      crearEmpleado: (data) => axios.post('/api/empleados', data)
    }
    ```
-   **Hooks (Manejo de estado):** Orquestan la lógica de estado del servidor (loading, error, data), usando los servicios.
    ```typescript
    // useEmpleados.ts
    export const useEmpleados = () => {
      // Usa empleadoService
      // Maneja loading, error, data
    }
    ```
-   **Componentes/Páginas (UI):** Responsables de la presentación, usan los hooks para mostrar los datos y el estado.
    ```typescript
    // EmpleadoTable.tsx, EmpleadosPage.tsx
    // Usan los hooks para mostrar datos
    ```

### 2. **Tipos de Pruebas**

#### Pruebas Unitarias (Aisladas)
Se crea un archivo de prueba por cada archivo en cada capa, mockeando sus dependencias externas.

-   `empleado.service.test.ts` ← **Mockea `axios`**. Se prueba que el servicio llama a la API correctamente.
-   `useEmpleados.test.ts` ← **Mockea el `empleado.service`**. Se prueba que el hook maneja los estados (loading, error, data) correctamente.
-   `EmpleadoTable.test.tsx` ← **Mockea el `useEmpleados` hook**. Se prueba que el componente renderiza la UI correcta para cada estado.

#### Pruebas de Integración (Colaboración)

*   **Propósito:** Verificar que todas las capas internas del frontend (`Page`, `Hook`, `Service`) colaboran correctamente para implementar un flujo de usuario completo. A diferencia de las pruebas unitarias que aíslan cada pieza, aquí se prueba el conjunto.

*   **Arquitectura y Alcance:** Se crea un archivo de prueba para la `Page` principal del flujo (ej. `LoginPage.tsx`, `EmpleadosPage.tsx`). Esta prueba renderiza todos los componentes reales, utiliza los `Hooks` reales y los `Services` reales.

*   **Límite y Mocking:** El único límite del sistema que se mockea es la red. Se utiliza un mock de `axios` para simular las respuestas de la API, permitiéndonos probar cómo reacciona la UI a los diferentes escenarios de la comunicación.

    ```plaintext
    Página → Hook Real → Service Real ← Mock de HTTP (axios)
    ```

*   **Estándar de Rigurosidad (Casos Obligatorios a Cubrir):** Para que una prueba de integración se considere completa, debe validar cómo reacciona la UI a los 4 posibles resultados de la interacción:

    1.  **Éxito (Happy Path):** Cuando la API responde como se espera (`2xx`). Se debe verificar que la UI se actualiza para mostrar el estado final correcto (datos en la tabla, redirección, mensaje de éxito, etc.).

    2.  **Carga (Loading State):** Mientras la respuesta de la API está en curso. Se debe verificar que la UI proporciona feedback inmediato de que algo está sucediendo (spinners, botones deshabilitados).

    3.  **Error de Validación (Input Errors):** Cuando la entrada del usuario es inválida (validación del lado del cliente) o la API la rechaza (`4xx`). Se debe verificar que la UI muestra mensajes de error claros y específicos.

    4.  **Error de Sistema (System Failure):** Cuando la API falla por completo (`5xx`) o hay un problema de red. Se debe verificar que la UI muestra un mensaje de error genérico y amigable, sin "romperse".

### 3. **Diagrama de Dependencias de Pruebas**

```plaintext
PRUEBAS UNITARIAS (aisladas):
Service     ← Mock de HTTP (axios)
Hook        ← Mock del Service
Componente  ← Mock del Hook

PRUEBA DE INTEGRACIÓN (colaboración):
Página → Hook Real → Service Real ← Mock de HTTP (axios)
```

### 4. Estructura y Legibilidad de Pruebas

Para asegurar que los archivos de prueba sean legibles y mantenibles, se establece la siguiente norma de estructura:

-   **Agrupación con `describe`:** Todas las pruebas para un archivo deben estar contenidas en un bloque `describe` principal (ej. `describe('AuthService', ...)`).
-   **Anidamiento por Funcionalidad:** Dentro del bloque principal, se deben usar bloques `describe` anidados para agrupar las pruebas de cada función o método exportado (ej. `describe('login', ...)`). Esta práctica es el equivalente directo a usar clases `@Nested` en JUnit 5 y es obligatoria para mantener el orden.
-   **Casos de Prueba Claros:** Cada caso de prueba individual debe ser definido con la función `it` y tener un nombre descriptivo de su comportamiento esperado (ej. `it('debería devolver los tokens en caso de éxito')`).

### 5. Estándar de Pruebas de Integración (Principio General)

Para asegurar la consistencia y rigurosidad a través de todos los dominios, se establece el siguiente principio general. Para cada flujo de usuario que implique una comunicación con la API, las pruebas de integración deben verificar cómo reacciona la UI a los 4 posibles resultados:

1.  **Éxito (Happy Path):**
    *   **Cuando:** La API responde como se espera (`2xx`).
    *   **Verificar:** La UI se actualiza para mostrar el estado final correcto (datos, redirección, etc.).

2.  **Carga (Loading State):**
    *   **Cuando:** La respuesta de la API está en curso.
    *   **Verificar:** La UI proporciona feedback inmediato de que algo está sucediendo (spinners, botones deshabilitados).

3.  **Error de Validación (Input Errors):**
    *   **Cuando:** La entrada del usuario es inválida (validación cliente) o la API la rechaza (`4xx`).
    *   **Verificar:** La UI muestra mensajes de error claros y específicos para que el usuario corrija su entrada.

4.  **Error de Sistema (System Failure):**
    *   **Cuando:** La API falla por completo (`5xx`) o hay un problema de red.
    *   **Verificar:** La UI muestra un mensaje de error genérico y amigable, sin "romperse".

## Estándares y Convenciones

### Convención de Nomenclatura
* **Componentes:** `PascalCase` (ej. `EmpleadoTable.tsx`)
* **Hooks:** `useCamelCase` (ej. `useEmpleados.ts`)
* **Servicios:** `camelCase.service.ts` (ej. `empleado.service.ts`)
* **Archivos de prueba:** `[nombreArchivo].test.tsx`. **Deben estar ubicados en el mismo directorio que el archivo que prueban.**

### Estándares de Código
* Todas las funciones deben tener JSDoc.
* Uso obligatorio de TypeScript.
* Preferir funciones puras.
* Utilizar `React.memo()` para optimizar.

### Principios de Diseño
* Accesibilidad (WCAG 2.1 AA).
* Diseño responsive (mobile-first).
* Interfaz minimalista y clara.
* Feedback inmediato al usuario.

### Gestión de Variables de Entorno
- Usar archivos `.env` para secretos y configuración.
- El prefijo `VITE_` es obligatorio para exponer variables al frontend.
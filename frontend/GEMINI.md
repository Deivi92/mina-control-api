# Contexto del Frontend: MinaControl Pro (App React)

**Rol de este archivo:** Este documento contiene todas las reglas, arquitecturas y convenciones que deben seguirse estrictamente para el desarrollo de la aplicación de React.

**Navegación de Contexto:**
- **Contexto Global:** `../GEMINI.md`
- **Contexto Backend:** `../backend/GEMINI.md`

---
# CONSTITUCIÓN FRONTEND - MINA CONTROL

## Filosofía de Desarrollo

Este documento establece el proceso de desarrollo y la arquitectura para el frontend del proyecto Mina Control. La filosofía es combinar la disciplina y el orden de la arquitectura de backend con un flujo de trabajo moderno que garantice la máxima calidad en la interfaz y experiencia de usuario (UI/UX). Cada dominio de la aplicación se construirá como un bloque completo y probado antes de pasar al siguiente, asegurando un crecimiento robusto y mantenible del proyecto.

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
    │   ├── empleado/       # Dominio 'empleado'
    │   │   ├── components/ # Componentes de UI específicos de este dominio
    │   │   │   ├── EmpleadoTable.jsx
    │   │   │   ├── EmpleadoForm.jsx
    │   │   │   └── EmpleadoFilter.jsx
    │   │   ├── hooks/      # Lógica de negocio y estado (tu 'service' de backend)
    │   │   │   └── useEmpleados.js
    │   │   ├── services/   # Comunicación con la API (tu 'repository' de backend)
    │   │   │   └── empleado.service.js
    │   │   └── types.ts    # DTOs y modelos (Empleado, EmpleadoRequest, etc.)
    │   │
    │   ├── nomina/         # Dominio 'nomina' (misma estructura interna)
    │   └── ...             # Otros dominios
    │
    ├── pages/              # Orquestadores de vistas (tus 'Controllers')
    │   ├── HomePage.jsx
    │   ├── EmpleadosPage.jsx  # Orquesta los componentes del dominio 'empleado'
    │   ├── NominaPage.jsx     # Orquesta los componentes del dominio 'nomina'
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
  3. **Plantillas y generadores:** Crear snippets para VS Code y plantillas de archivos.

### Paso 1: Análisis del Contrato (API Spec)
* **Intención:** Establecer una única fuente de verdad.
* **Acciones:**
  1. Analizar `openapi-spec.json`.
  2. Identificar endpoints, modelos y DTOs del dominio.
  3. Generar automáticamente tipos TypeScript a partir del OpenAPI spec.

### Paso 2: Diseño del Blueprint de UI/UX
* **Intención:** Traducir datos en una experiencia de usuario tangible y de alta calidad.
* **Acciones:**
  1. Crear Wireframes, lista de componentes, flujos de usuario y criterios de aceptación.
  2. **Validar el diseño contra los principios de la [Guía de Usabilidad](./docs/USABILITY_GUIDE.md).**
  3. Validar accesibilidad (WCAG).

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

## Estrategia de Pruebas

- **Pruebas Unitarias (Jest + React Testing Library):** Validar la lógica interna de componentes y hooks de forma aislada.
- **Pruebas de Integración (Jest + React Testing Library):** Verificar la interacción entre varios componentes dentro de un feature.
- **Pruebas End-to-End (Playwright):** Validar flujos completos de usuario en un entorno real o simulado.

## Estándares y Convenciones

### Convención de Nomenclatura
* **Componentes:** `PascalCase` (ej. `EmpleadoTable.tsx`)
* **Hooks:** `useCamelCase` (ej. `useEmpleados.ts`)
* **Servicios:** `camelCase.service.ts` (ej. `empleado.service.ts`)
* **Archivos de prueba:** `[nombreArchivo].test.tsx`

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

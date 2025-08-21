# Constitución del Proyecto MinaControl Pro (Monorepo)

Este documento define la arquitectura, patrones y convenciones para **TODO** el proyecto MinaControl Pro, incluyendo el backend y el frontend. Sirve como el contexto fundamental y la guía para cualquier generación de código o modificación.

## 1. Visión General y Principios Globales

- **Propósito del Proyecto:** Sistema de gestión de operaciones para una mina.
- **Filosofía:** Desarrollar una aplicación full-stack robusta, mantenible y escalable, siguiendo las mejores prácticas de la industria tanto para el backend como para el frontend.
- **Enfoque de Repositorio:** Se utiliza una arquitectura **Monorepo**, donde el backend y el frontend conviven en el mismo repositorio para facilitar el desarrollo, la compartición de tipos y la coherencia general.
- **Convenciones Globales:**
    - **Mensajes de Commit:** Deben ser escritos exclusivamente en español.

---

# PARTE I: BACKEND (API - Spring Boot)

*Esta sección detalla la constitución del servicio de backend. El contenido original se ha mantenido intacto para que sirva como una plantilla base robusta para proyectos Spring Boot.*

## 2. Tecnologías y Lenguaje (Backend)

- **Backend:** Spring Boot 3.2.0
- **Lenguaje:** Java 17
- **Base de Datos:** PostgreSQL
- **Gestión de Dependencias:** Maven (`pom.xml`)

## 3. Arquitectura del Sistema (Backend)

Se debe seguir rigurosamente una **Arquitectura en Capas (Layered Architecture)**.

1.  **Capa de API/Controller (`@RestController`):**
    - Responsable de exponer los endpoints REST.
    - **NO** debe contener lógica de negocio.
    - Delega toda la lógica a la capa de Servicio.
    - Maneja la serialización/deserialización de DTOs.

2.  **Capa de Lógica de Negocio/Servicio (`@Service`):**
    - **Corazón de la aplicación.** Contiene toda la lógica de negocio.
    - Orquesta las operaciones llamando a los Repositorios.
    - Es transaccional (`@Transactional`).

3.  **Capa de Acceso a Datos/Persistencia (`@Repository`):**
    - Interfaces que extienden `JpaRepository`.
    - Responsable exclusivo de la comunicación con la base de datos.
    - **NUNCA** debe ser accedida directamente desde la capa de Controller.

**Regla de Oro:** El flujo de comunicación es estrictamente unidireccional: `Controller -> Service -> Repository`.

## 4. Patrones de Diseño Obligatorios (Backend)
- **Controller-Service-Repository:** Es la base de la estructura del código.
- **Inyección de Dependencias (DI):** Utilizar la inyección por constructor de Spring.
- **Programación Orientada a Interfaces:** Los Controllers **deben** depender de **interfaces** de servicio, no de sus implementaciones concretas.
- **DTO (Data Transfer Object):** Las entidades JPA (`@Entity`) **NUNCA** deben ser expuestas en la capa de API.
- **Manejo de Excepciones Centralizado:** Usar `@ControllerAdvice` para un manejo de errores global y consistente.

(Para más detalles sobre cada patrón, referirse a las secciones originales del documento).

---

# PARTE II: FRONTEND (Aplicación React)

*Esta sección define la constitución de la aplicación frontend. El objetivo es construir una interfaz de usuario moderna, reactiva y fácil de mantener, que sirva como una excelente pieza de portafolio y una guía de aprendizaje práctica.*

## 5. Stack Tecnológico (Frontend)

- **Framework:** **React 18 + Vite + TypeScript**.
    - *Didáctica:* Usamos React con componentes funcionales y Hooks, el estándar moderno. Vite nos proporciona una experiencia de desarrollo ultra-rápida. TypeScript añade seguridad de tipos para prevenir errores comunes y mejorar el autocompletado.
- **Librería de Componentes:** **Material-UI (MUI) v5**.
    - *Didáctica:* En lugar de construir cada botón y tabla desde cero, usamos una librería profesional. Esto nos permite enfocarnos en la lógica de la aplicación y asegura una UI consistente y atractiva.
- **Cliente HTTP:** **Axios**.
    - *Didáctica:* Es el estándar para realizar llamadas a nuestra API. Crearemos una instancia centralizada para manejar la URL base y la inyección automática de tokens de autenticación.
- **Enrutamiento:** **React Router v6**.
    - *Didáctica:* La librería estándar para manejar la navegación entre las diferentes páginas de nuestra aplicación.
- **Gestión de Estado del Servidor:** **React Query**.
    - *Didáctica:* Esta es una herramienta clave. En lugar de manejar manualmente estados de `loading`, `error` y `data` con `useState/useEffect` cada vez que pedimos datos a la API, React Query lo hace por nosotros, gestionando el cache, las revalidaciones y mucho más. Simplifica enormemente el código.
- **Gestión de Estado Global (UI):** **React Context**.
    - *Didáctica:* Lo usaremos para estado que es verdaderamente global y no cambia a menudo, como la información del usuario autenticado o el tema (claro/oscuro) de la aplicación.
- **Pruebas:** **Jest + React Testing Library**.
    - *Didáctica:* Nos permiten probar nuestros componentes desde la perspectiva del usuario, asegurando que funcionen como se espera.

## 6. Arquitectura y Estructura de Carpetas (Frontend)

Adoptamos una arquitectura **Híbrida / Feature-Sliced**, que es altamente modular y escalable.

```
/mina-control-frontend/
├── src/
│   ├── shared/
│   │   ├── components/  # UI Kit: Botones, Modales, Tablas genéricas. 100% reutilizables.
│   │   ├── hooks/       # Hooks globales (ej. useDebounce).
│   │   ├── services/    # Configuración de servicios (instancia de Axios, interceptors).
│   │   ├── utils/       # Funciones de utilidad puras (formateo de fechas, etc.).
│   │   └── types/       # Tipos de TypeScript compartidos en toda la app.
│   │
│   ├── features/        # El corazón de la app. Cada carpeta es un dominio de negocio.
│   │   ├── auth/        # Módulo de autenticación.
│   │   │   ├── components/  # Componentes que SÓLO se usan en autenticación (LoginForm).
│   │   │   ├── pages/       # Páginas de este feature (LoginPage).
│   │   │   ├── services/    # Lógica de API para auth (authService.ts).
│   │   │   ├── hooks/       # Hooks específicos (useAuth).
│   │   │   └── ...
│   │   ├── empleados/   # Módulo de empleados. Contiene todo lo relacionado con ellos.
│   │   └── ...          # Y así para cada dominio (turnos, produccion, etc.).
│   │
│   ├── layouts/         # Define la estructura visual de las páginas (ej. con sidebar o centrada).
│   ├── pages/           # Páginas globales que no pertenecen a un feature (Dashboard, NotFound).
│   ├── routes/          # Configuración central de las rutas de la aplicación.
│   └── App.tsx          # Componente raíz que une todo.
...
```

## 7. Flujo de Datos y Lógica (Frontend)

Para que el aprendizaje sea claro, este es el flujo típico de una petición de datos:

`Componente UI` -> `Hook de React Query` -> `Función de Servicio` -> `Instancia de Axios` -> **API Backend**

1.  **Componente UI (ej. `EmpleadoTable.tsx`):** Solo se encarga de mostrar la UI. Llama a un hook para obtener los datos. No sabe de dónde vienen.
2.  **Hook (ej. `useEmpleados.ts`):** Usa React Query (`useQuery`) para invocar la función del servicio. Maneja el estado de carga, error y los datos recibidos. Provee esta información al componente.
3.  **Servicio (ej. `empleadosService.ts`)::** Define una función asíncrona (`getEmpleados`) que usa Axios para hacer la petición GET a `/api/v1/empleados`. Su única responsabilidad es comunicarse con la API.

## 8. Plan de Implementación por Fases (Frontend)

Esta es nuestra hoja de ruta oficial.

### Fase 0: Setup Inicial (1-2 días)
1. Crear proyecto con Vite + React + TypeScript.
2. Configurar la estructura de carpetas del monorepo.
3. Instalar y configurar dependencias base (Axios, React Router, MUI, React Query).
4. Configurar ESLint + Prettier + Husky para calidad de código.

### Fase 1: Fundación (3-5 días)
1. **Configurar servicios base**: Instancia de Axios con interceptors para el token JWT.
2. **Sistema de autenticación completo**: `AuthContext`, `LoginPage`, `ProtectedRoute` y llamadas a la API de auth.
3. **Layout principal**: `MainLayout` con sidebar, header y área de contenido.

### Fase 2: Primer Módulo - Empleados (1 semana)
1. Crear la estructura completa del feature `empleados`.
2. `EmpleadosPage` con una tabla de MUI (`DataGrid`) para listar, paginar y filtrar.
3. `EmpleadoForm` en un modal para crear y editar.
4. Integración completa con la API de empleados.

### Fase 3: Expansión Modular (2-3 semanas)
1. Implementar los módulos de `Turnos` y `Producción` siguiendo el patrón de `Empleados`.
2. Crear un `DashboardPage` con algunas tarjetas y gráficos de resumen.

### Fase 4: Refinamiento (1-2 semanas)
1. Añadir pruebas unitarias y de integración con Jest y React Testing Library.
2. Optimizar el rendimiento si es necesario.
3. Documentar los componentes más complejos.

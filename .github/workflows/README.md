# 🚀 GitHub Actions Workflows - MinaControl Pro

Este directorio contiene los flujos de trabajo (workflows) de GitHub Actions para automatizar CI/CD, análisis de calidad y gestión de dependencias del proyecto MinaControl Pro.

## 📋 Workflows Disponibles

### 1. 🔄 CI/CD Pipeline (`ci-cd.yml`)
**Trigger**: Push y Pull Requests a ramas principales
**Propósito**: Integración continua y validación de código

#### Características:
- ✅ **Detección inteligente de cambios** (backend/frontend)
- ✅ **Build y test automático** para ambos componentes
- ✅ **Caching optimizado** de dependencias (Maven + npm)
- ✅ **Tests unitarios e integración**
- ✅ **Validación de estructura del monorepo**
- ✅ **Generación de artefactos** (.jar y dist/)
- ✅ **Reportes de cobertura** de código

#### Jobs incluidos:
- `detect-changes`: Detecta qué partes del monorepo cambiaron
- `backend-ci`: Build, test y empaquetado del backend (Spring Boot)
- `frontend-ci`: Build, test y linting del frontend (React + TypeScript)
- `monorepo-validation`: Validación general de la estructura
- `build-summary`: Resumen final del pipeline

### 2. 🚀 Deploy & Release (`release.yml`)
**Trigger**: Tags de versión (v*.*.*) y manual
**Propósito**: Automatización de releases y despliegues

#### Características:
- ✅ **Detección automática de versión** desde tags
- ✅ **Build optimizado para producción**
- ✅ **Generación de Docker images** (preparado para el futuro)
- ✅ **Creación automática de GitHub Releases**
- ✅ **Notas de release automáticas** con changelog
- ✅ **Artefactos incluidos** en el release

#### Jobs incluidos:
- `prepare-release`: Extrae información de versión
- `build-for-release`: Build completo para ambos componentes
- `docker-build`: Construcción de imágenes Docker (opcional)
- `create-release`: Creación del release en GitHub
- `deployment-notification`: Notificación del resultado

### 3. 🔍 Code Quality & Security (`quality.yml`)
**Trigger**: Push, PR, semanal y manual
**Propósito**: Análisis continuo de calidad y seguridad

#### Características:
- ✅ **Análisis de calidad de código** (Checkstyle, ESLint)
- ✅ **Detección de bugs** (SpotBugs para Java)
- ✅ **Análisis de seguridad** (CodeQL, OWASP, TruffleHog)
- ✅ **Cobertura de código** (JaCoCo, Vitest)
- ✅ **Auditoría de dependencias**
- ✅ **Verificación de licencias**
- ✅ **Reportes unificados**

#### Jobs incluidos:
- `backend-quality`: Análisis completo del backend
- `frontend-quality`: Análisis completo del frontend
- `security-analysis`: Análisis de seguridad con CodeQL
- `secret-scanning`: Detección de secretos con TruffleHog
- `license-compliance`: Verificación de licencias
- `quality-summary`: Resumen consolidado

### 4. 📦 Dependency Updates (`dependencies.yml`)
**Trigger**: Semanal (lunes) y manual
**Propósito**: Gestión automática de dependencias

#### Características:
- ✅ **Actualización automática** de dependencias menores
- ✅ **PRs automáticos** con cambios
- ✅ **Validación completa** post-actualización
- ✅ **Auditoría de seguridad** de dependencias
- ✅ **Creación automática de issues** para vulnerabilidades
- ✅ **Soporte completo** para Maven y npm

#### Jobs incluidos:
- `update-backend-dependencies`: Actualización de Maven
- `update-frontend-dependencies`: Actualización de npm
- `security-audit`: Auditoría de seguridad
- `dependency-summary`: Resumen del estado

## 🛠️ Configuración y Requisitos

### Variables de Entorno
Los workflows utilizan las siguientes variables:
- `JAVA_VERSION: '17'` - Versión de Java para el backend
- `NODE_VERSION: '20'` - Versión de Node.js para el frontend
- `MAVEN_OPTS: -Xmx1024m` - Opciones de memoria para Maven

### Secrets Necesarios
Los workflows utilizan secretos estándar de GitHub:
- `GITHUB_TOKEN` - Token automático para operaciones de GitHub
- No se requieren secretos adicionales para funcionalidad básica

### Permisos Requeridos
- `actions: read` - Para leer información de workflows
- `contents: read/write` - Para leer/escribir código
- `security-events: write` - Para reportes de seguridad
- `pull-requests: write` - Para crear PRs automáticos
- `issues: write` - Para crear issues de seguridad

## 🎯 Cómo Usar los Workflows

### Desarrollo Diario
1. **Haz push a tu rama**: Los workflows de CI se ejecutan automáticamente
2. **Crear PR**: Se ejecutan validaciones completas
3. **Merge a main**: Deploy automático si está configurado

### Crear un Release
1. **Crear tag de versión**:
   ```bash
   git tag v1.0.0
   git push origin v1.0.0
   ```
2. **El workflow de release se ejecuta automáticamente**
3. **GitHub Release se crea con artefactos**

### Ejecutar Manualmente
Todos los workflows soportan ejecución manual:
1. Ve a **Actions** en GitHub
2. Selecciona el workflow deseado
3. Clic en **Run workflow**
4. Configura parámetros si es necesario

## 📊 Monitoreo y Reportes

### Artefactos Generados
- **Backend**: `.jar` empaquetado, reportes de tests
- **Frontend**: Carpeta `dist/` compilada, reportes de cobertura
- **Calidad**: Reportes de Checkstyle, ESLint, JaCoCo
- **Seguridad**: Reportes OWASP, CodeQL, audit

### Ubicación de Reportes
Los reportes se suben como artefactos y están disponibles en:
- **Actions** → **Workflow Run** → **Artifacts**
- Retención: 7-90 días según el tipo

### Notificaciones
- ✅ **Éxito**: Workflow completa sin errores
- ❌ **Fallo**: Notificación automática en GitHub
- 🔒 **Vulnerabilidades**: Issues automáticos creados

## 🔧 Personalización

### Modificar Triggers
Edita la sección `on:` en cada workflow:
```yaml
on:
  push:
    branches: [ main, develop, feature/* ]
  schedule:
    - cron: '0 9 * * 1'  # Lunes a las 9 AM
```

### Agregar Entornos
Modifica el workflow de release para incluir entornos:
```yaml
environment:
  name: production
  url: https://minacontrol.example.com
```

### Personalizar Calidad
Ajusta los parámetros de análisis en `quality.yml`:
- Niveles de severidad
- Herramientas adicionales
- Umbrales de cobertura

## 🚨 Solución de Problemas

### Workflows Fallan
1. **Revisa los logs** en la pestaña Actions
2. **Verifica configuración** de Java/Node en local
3. **Ejecuta los comandos** localmente primero
4. **Revisa dependencias** y versiones

### Artefactos No Se Generan
1. **Verifica rutas** en `upload-artifact`
2. **Confirma** que los builds terminan exitosamente
3. **Revisa permisos** del workflow

### PRs Automáticos No Se Crean
1. **Verifica permisos** de `GITHUB_TOKEN`
2. **Confirma** que hay cambios en las dependencias
3. **Revisa configuración** del workflow

## 📚 Referencias

- [GitHub Actions Documentation](https://docs.github.com/en/actions)
- [Maven GitHub Actions](https://github.com/actions/setup-java)
- [Node.js GitHub Actions](https://github.com/actions/setup-node)
- [CodeQL Analysis](https://codeql.github.com/)
- [OWASP Dependency Check](https://owasp.org/www-project-dependency-check/)

---

## 🤝 Contribución

Para modificar o agregar workflows:
1. Crea una rama feature
2. Modifica los archivos `.yml`
3. Prueba localmente con [act](https://github.com/nektos/act) si es posible
4. Crea PR con descripción detallada de los cambios

**Nota**: Los workflows se ejecutan automáticamente una vez que están en la rama principal.
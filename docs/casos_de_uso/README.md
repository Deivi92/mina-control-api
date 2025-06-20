# Casos de Uso - MinaControl Pro

## Descripción

Esta carpeta contiene la documentación de casos de uso del sistema MinaControl Pro, un sistema de gestión minera diseñado para operaciones básicas pero escalables.

## Contenido

### Documento Principal
- **`casos_de_uso_alto_nivel.md`**: Documento maestro que define toda la arquitectura funcional del sistema

## Estructura del Sistema

### 🎭 Roles Definidos
- **Empleado**: Operaciones básicas (asistencia, producción personal)
- **Administrador**: Gestión operativa completa 
- **Gerente**: Supervisión estratégica y nómina

### 🏗️ Dominios del Sistema
0. **Autenticación**: Control de acceso y seguridad
1. **Empleados**: Gestión de personal
2. **Turnos**: Control de asistencia y horarios
3. **Producción**: Registro y seguimiento de extracción de carbón
4. **Logística/Despachos**: Control de salidas de carbón
5. **Nómina**: Cálculo y gestión de pagos semanales
6. **Reportes**: Análisis e inteligencia de negocio

## Casos de Uso por Dominio

| Dominio | Cantidad CU | Actores Principales |
|---------|-------------|-------------------|
| Autenticación | 4 | Empleado, Administrador, Gerente |
| Empleados | 5 | Administrador, Gerente, Empleado |
| Turnos | 4 | Administrador, Empleado |
| Producción | 5 | Administrador, Empleado, Gerente |
| Logística | 3 | Administrador, Gerente |
| Nómina | 4 | Gerente, Empleado |
| Reportes | 4 | Gerente, Administrador |

## Flujos Integrados

### 🔄 Operación Diaria
Entrada → Producción → Salida → Supervisión

### 💰 Ciclo de Nómina 
Cálculo semanal → Ajustes → Comprobantes → Pago (sábados)

### 🚛 Gestión de Despachos
Planificación → Ejecución → Seguimiento → Entrega

### 📊 Análisis Gerencial
Recopilación → Reportes → Decisiones estratégicas

## Escalabilidad y Flexibilidad

### ✨ Características Escalables
- Arquitectura modular por dominios
- APIs REST para integración
- Base de datos normalizada
- Soporte para múltiples turnos y ubicaciones

### 🎯 MVP Priorizado
1. **Fase 1**: Empleados + Turnos (base operativa)
2. **Fase 2**: Producción (core del negocio)  
3. **Fase 3**: Nómina (automatización crítica)
4. **Fase 4**: Logística + Reportes (optimización)

## Tecnologías Base

- **Backend**: Spring Boot (Java 17)
- **Base de datos**: PostgreSQL
- **Autenticación**: Spring Security + JWT
- **Documentación**: OpenAPI 3.0

## Convenciones

### Nomenclatura de Casos de Uso
`CU-[DOMINIO]-[NÚMERO]`: Descripción breve

**Ejemplos**:
- `CU-AUTH-001`: Iniciar sesión
- `CU-EMP-001`: Registrar nuevo empleado
- `CU-PRO-003`: Consultar producción por fecha
- `CU-NOM-001`: Calcular nómina semanal

### Estados del Documento
- **Definición de Alto Nivel**: Arquitectura funcional completa
- **Próximo**: Análisis detallado por dominio

---

**💡 Nota**: Este sistema está diseñado para ser simple pero profesional, ideal para proyectos académicos que demuestren capacidades de desarrollo full-stack con arquitectura escalable.

**📅 Última actualización**: Junio 2025

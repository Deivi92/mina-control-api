# Dominio Producción - Documentación y Diagramas

## Propósito del Dominio

El dominio de **Producción** gestiona toda la información relacionada con la extracción y procesamiento de materiales en la mina. Es fundamental para:

- Registrar la producción diaria por turnos y áreas
- Controlar la calidad y tipos de material extraído
- Monitorear el rendimiento de equipos y empleados
- Generar métricas de productividad y eficiencia
- Facilitar la planificación y optimización de operaciones

## Casos de Uso (desde casos_de_uso_alto_nivel.md)

### CU-P01: Registrar Producción

- **Actor Principal**: Supervisor/Operador
- **Descripción**: Registrar la producción obtenida en un turno específico
- **Precondiciones**: Turno activo, empleado autorizado
- **Flujo Principal**: Seleccionar turno → Ingresar cantidades → Especificar material → Confirmar
- **Postcondiciones**: Registro de producción almacenado

### CU-P02: Consultar Producción

- **Actor Principal**: Gerente/Supervisor/Administrador
- **Descripción**: Visualizar reportes de producción por períodos y filtros
- **Precondiciones**: Usuario autenticado con permisos
- **Flujo Principal**: Definir filtros → Seleccionar período → Generar reporte → Exportar
- **Postcondiciones**: Información de producción presentada

### CU-P03: Gestionar Tipos de Material

- **Actor Principal**: Administrador/Gerente
- **Descripción**: Mantener catálogo de materiales y sus características
- **Precondiciones**: Permisos administrativos
- **Flujo Principal**: Crear/editar material → Definir unidades → Establecer precios → Activar
- **Postcondiciones**: Catálogo actualizado para uso en registros

### CU-P04: Monitorear Rendimiento

- **Actor Principal**: Gerente/Administrador
- **Descripción**: Analizar productividad por empleado, turno y área
- **Precondiciones**: Datos de producción históricos
- **Flujo Principal**: Seleccionar métricas → Aplicar filtros → Visualizar gráficos → Generar insights
- **Postcondiciones**: Métricas de rendimiento disponibles

## Entidades Principales

### RegistroProduccion
- **Propósito**: Registro específico de producción en un turno
- **Atributos Clave**: fecha, turnoId, empleadoId, cantidad, materialId, area
- **Relaciones**: ManyToOne con Turno, Empleado, TipoMaterial

### TipoMaterial
- **Propósito**: Catálogo de materiales extraíbles
- **Atributos Clave**: nombre, descripcion, unidadMedida, precioReferencia
- **Relaciones**: OneToMany con RegistroProduccion

### MetricaProduccion
- **Propósito**: Métricas calculadas y agregadas de producción
- **Atributos Clave**: periodo, area, totalCantidad, promedioDiario
- **Relaciones**: Aggregation de RegistroProduccion

## Endpoints API Planificados

```http
# Gestión de Registros de Producción
POST   /api/produccion/registros                    # Crear registro
GET    /api/produccion/registros                    # Listar registros
GET    /api/produccion/registros/{id}               # Obtener registro específico
PUT    /api/produccion/registros/{id}               # Actualizar registro
DELETE /api/produccion/registros/{id}               # Eliminar registro

# Consultas específicas
GET    /api/produccion/registros/turno/{turnoId}    # Producción de un turno
GET    /api/produccion/registros/empleado/{id}      # Producción de un empleado
GET    /api/produccion/registros/fecha/{fecha}      # Producción de una fecha
GET    /api/produccion/registros/periodo            # Producción por período
GET    /api/produccion/registros/area/{area}        # Producción por área

# Tipos de Material
POST   /api/produccion/materiales                   # Crear tipo material
GET    /api/produccion/materiales                   # Listar materiales
GET    /api/produccion/materiales/{id}              # Obtener material
PUT    /api/produccion/materiales/{id}              # Actualizar material
DELETE /api/produccion/materiales/{id}              # Eliminar material

# Métricas y Reportes
GET    /api/produccion/metricas/diarias             # Métricas diarias
GET    /api/produccion/metricas/mensuales           # Métricas mensuales
GET    /api/produccion/metricas/empleado/{id}       # Rendimiento empleado
GET    /api/produccion/metricas/area/{area}         # Rendimiento por área
GET    /api/produccion/reportes/consolidado         # Reporte consolidado
GET    /api/produccion/reportes/comparativo         # Reporte comparativo
```

## Relación con Diagramas Generales

### Diagrama ER Completo (`er_diagram_completo.puml`)
- **RegistrosProduccion**: Tabla principal con datos de extracción
- **TiposMaterial**: Catálogo de materiales
- **MetricasProduccion**: Tabla de métricas agregadas
- **Relaciones**: FK hacia Empleados, Turnos, índices de rendimiento

### Diagrama de Clases Completo (`class_diagram_completo.puml`)
- **Entidades**: RegistroProduccion, TipoMaterial, MetricaProduccion
- **Services**: ProduccionService, MaterialService, MetricaService
- **Controllers**: ProduccionController, MaterialController
- **DTOs**: RegistroProduccionDTO, MaterialDTO, MetricaDTO

### Arquitectura General (`architecture_overview.puml`)
- **Capa de Dominio**: Lógica de producción y cálculos
- **Capa de Aplicación**: Servicios de registro y métricas
- **Capa de Infraestructura**: Repositorios y agregaciones
- **Integración**: Con módulos de Turnos, Empleados y Reportes

## Diagramas Específicos del Dominio

### 1. `class_diagram_produccion.puml`
- **Estado**: ⏳ Pendiente
- **Contenido**: Clases específicas del dominio producción
- **Enfoque**: Entidades, DTOs, servicios, controladores y repositorios
- **Basado en**: Diagrama de clases completo (sección producción)

### 2. `sequence_registrar_produccion.puml`
- **Estado**: ⏳ Pendiente
- **Contenido**: Flujo para registrar producción de un turno
- **Actores**: Supervisor → Controller → Service → Repository → DB
- **Validaciones**: Turno activo, material válido, cantidades

### 3. `sequence_consultar_metricas.puml`
- **Estado**: ⏳ Pendiente
- **Contenido**: Proceso de consulta de métricas de producción
- **Actores**: Gerente → Controller → Service → Repository → DB
- **Agregaciones**: Cálculos de totales, promedios, comparaciones

### 4. `sequence_gestionar_materiales.puml`
- **Estado**: ⏳ Pendiente
- **Contenido**: CRUD de tipos de materiales
- **Actores**: Admin → Controller → Service → Repository → DB
- **Validaciones**: Nombres únicos, unidades válidas, precios

## Metodología de Construcción

1. **Análisis**: Revisar casos de uso y flujos de producción
2. **Diseño**: Crear diagrama de clases basado en el completo
3. **Flujos**: Diseñar secuencias para casos principales
4. **Validación**: Verificar alineación con arquitectura general
5. **Implementación**: Usar diagramas como guía para desarrollo
6. **Pruebas**: Validar cálculos y métricas según especificaciones

## Integración con Otros Dominios

- **Turnos**: Registros asociados a turnos específicos
- **Empleados**: Producción por empleado y equipos
- **Nómina**: Bonificaciones basadas en productividad
- **Reportes**: Fuente principal de datos para análisis

## Notas de Implementación

- Validaciones de consistencia entre turnos y registros
- Cálculos automáticos de métricas y agregaciones
- Manejo de diferentes unidades de medida
- Optimización de consultas para grandes volúmenes
- Cache de métricas frecuentemente consultadas
- Integración con sistemas de pesaje y medición

---
**Última actualización**: Enero 2024  
**Estado del dominio**: En planificación (diagramas pendientes)  
**Responsable**: Equipo de desarrollo MinaControl Pro

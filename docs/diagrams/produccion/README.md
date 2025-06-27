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



### CU-P04: Monitorear Rendimiento

- **Actor Principal**: Gerente/Administrador
- **Descripción**: Analizar productividad por empleado, turno y área
- **Precondiciones**: Datos de producción históricos
- **Flujo Principal**: Seleccionar métricas → Aplicar filtros → Visualizar gráficos → Generar insights
- **Postcondiciones**: Métricas de rendimiento disponibles

## Entidades Principales

### RegistroProduccion
- **Propósito**: Registro específico de producción en un turno
- **Atributos Clave**: fecha, turnoId, empleadoId, cantidad, area
- **Relaciones**: ManyToOne con Turno, Empleado



### MetricaProduccion
- **Propósito**: Métricas calculadas y agregadas de producción
- **Atributos Clave**: periodo, area, totalCantidad, promedioDiario
- **Relaciones**: Aggregation de RegistroProduccion

## Endpoints API Planificados



## Relación con Diagramas Generales

### Diagrama ER Completo (`er_diagram_completo.puml`)
- **RegistrosProduccion**: Tabla principal con datos de extracción
- **MetricasProduccion**: Tabla de métricas agregadas
- **Relaciones**: FK hacia Empleados, Turnos, índices de rendimiento

### Diagrama de Clases Completo (`class_diagram_completo.puml`)
- **Entidades**: RegistroProduccion, MetricaProduccion
- **Services**: ProduccionService, MetricaService
- **Controllers**: ProduccionController
- **DTOs**: RegistroProduccionDTO, MetricaDTO

### Arquitectura General (`architecture_overview.puml`)
- **Capa de Dominio**: Lógica de producción y cálculos
- **Capa de Aplicación**: Servicios de registro y métricas
- **Capa de Infraestructura**: Repositorios y agregaciones
- **Integración**: Con módulos de Turnos, Empleados y Reportes

## Diagramas Específicos del Dominio

### 1. `class_diagram_produccion.puml`
- **Estado**: ✅ Completado
- **Contenido**: Clases específicas del dominio producción
- **Enfoque**: Entidades, DTOs, servicios, controladores y repositorios
- **Basado en**: Diagrama de clases completo (sección producción)

### 2. `sequence_registrar_produccion.puml`
- **Estado**: ✅ Completado
- **Contenido**: Flujo para registrar producción de un turno
- **Actores**: Supervisor → Controller → Service → Repository → DB
- **Validaciones**: Turno activo, cantidades

### 3. `sequence_actualizar_produccion.puml`
- **Estado**: ✅ Completado
- **Contenido**: Flujo para actualizar un registro de producción existente.

### 4. `sequence_consultar_produccion_empleado.puml`
- **Estado**: ✅ Completado
- **Contenido**: Flujo para consultar la producción de un empleado específico.

### 5. `sequence_consultar_produccion_fecha.puml`
- **Estado**: ✅ Completado
- **Contenido**: Flujo para consultar la producción por una fecha determinada.

### 6. `sequence_eliminar_produccion.puml`
- **Estado**: ✅ Completado
- **Contenido**: Flujo para eliminar un registro de producción.

---
**Última actualización**: Junio 2025  
**Estado del dominio**: ✅ Completado  
**Responsable**: Equipo de desarrollo MinaControl Pro

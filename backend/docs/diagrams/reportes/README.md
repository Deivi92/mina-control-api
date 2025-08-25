# Diagramas del Dominio Reportes

## Propósito
Este dominio maneja la inteligencia de negocio y análisis operacional, generando reportes para la toma de decisiones gerenciales.

## Casos de Uso Base
- **CU-REP-001**: Generar reporte de producción
- **CU-REP-002**: Generar reporte de asistencia
- **CU-REP-003**: Generar reporte de costos laborales
- **CU-REP-004**: Exportar datos operacionales

## Diagramas Incluidos

### 1. sequence_reporte_produccion.puml
Diagrama de secuencia para el reporte de producción (CU-REP-001):
- Análisis de productividad por período
- Integración con datos de producción y empleados
- Generación de visualizaciones y documentos

### 2. sequence_reporte_asistencia.puml
Diagrama de secuencia para el reporte de asistencia (CU-REP-002):
- Análisis de puntualidad y ausentismo
- Cálculo de métricas de asistencia
- Dashboard interactivo y documentos exportables

### 3. sequence_reporte_costos_laborales.puml
Diagrama de secuencia para el reporte de costos laborales (CU-REP-003):
- Análisis de gastos en nómina y productividad
- Cálculo de ratios de eficiencia
- Comparativas con períodos anteriores

### 4. sequence_exportar_datos.puml
Diagrama de secuencia para exportación de datos (CU-REP-004):
- Extracción de datos para análisis externo
- Múltiples formatos (CSV, JSON, Excel)
- Consolidación de datasets de múltiples dominios

## Endpoints Representados
- `POST /api/reportes/produccion` - Generar reporte de producción
- `POST /api/reportes/asistencia` - Generar reporte de asistencia
- `POST /api/reportes/costos-laborales` - Generar reporte de costos
- `POST /api/reportes/exportar-datos` - Exportar datos operacionales

## Relación con Diagramas Generales
- Basado en el `class_diagram_completo.puml` y `er_diagram_completo.puml`
- Implementa los casos de uso definidos en `casos_de_uso_alto_nivel.md`
- Integra datos de todos los dominios del sistema

## Notas de Implementación
- Acceso restringido a usuarios con rol Gerente
- Integración con múltiples servicios para consolidación de datos
- Generación de documentos en múltiples formatos
- Capacidades de dashboard interactivo y exportación masiva
- **Flujo Principal**: Evaluar métricas → Comparar umbrales → Generar alerta → Notificar usuarios

## Entidades Principales

### Reporte

- **Propósito**: Definición y configuración de reportes
- **Atributos Clave**: nombre, tipo, parametros, frecuencia, destinatarios
- **Relaciones**: OneToMany con EjecucionReporte

### Dashboard

- **Propósito**: Configuración de dashboards personalizados
- **Atributos Clave**: nombre, layout, widgets, permisos
- **Relaciones**: OneToMany con Widget

### Metrica

- **Propósito**: Definición de métricas calculadas
- **Atributos Clave**: nombre, formula, unidad, fuente
- **Relaciones**: ManyToMany con Reporte, Dashboard

## Endpoints API Planificados

```http
# Gestión de Reportes
POST   /api/reportes                                # Crear reporte
GET    /api/reportes                                # Listar reportes
GET    /api/reportes/{id}/ejecutar                  # Ejecutar reporte
GET    /api/reportes/{id}/historial                 # Historial ejecuciones

# Dashboards
POST   /api/dashboards                              # Crear dashboard
GET    /api/dashboards                              # Listar dashboards
GET    /api/dashboards/{id}                         # Obtener dashboard
PUT    /api/dashboards/{id}                         # Actualizar dashboard

# Métricas
GET    /api/metricas/produccion                     # Métricas de producción
GET    /api/metricas/empleados                      # Métricas de empleados
GET    /api/metricas/turnos                         # Métricas de turnos
GET    /api/metricas/consolidado                    # Métricas consolidadas

# Exportación
GET    /api/reportes/{id}/export/pdf                # Exportar PDF
GET    /api/reportes/{id}/export/excel              # Exportar Excel
GET    /api/dashboards/{id}/export/pdf              # Dashboard a PDF
```

## Diagramas Específicos del Dominio

### 1. `class_diagram_reportes.puml`

- **Estado**: ✅ Completado
- **Contenido**: Clases de reportes, dashboards y métricas

### 2. `sequence_reporte_produccion.puml`

- **Estado**: ✅ Completado
- **Contenido**: Flujo de generación de reporte de producción.

### 3. `sequence_reporte_asistencia.puml`

- **Estado**: ✅ Completado
- **Contenido**: Flujo de generación de reporte de asistencia.

### 4. `sequence_reporte_costos_laborales.puml`

- **Estado**: ✅ Completado
- **Contenido**: Flujo de generación de reporte de costos laborales.

### 5. `sequence_exportar_datos.puml`

- **Estado**: ✅ Completado
- **Contenido**: Flujo para exportar datos operacionales.

## Integración con Otros Dominios

- **Empleados**: Métricas de personal y productividad
- **Turnos**: Análisis de cobertura y eficiencia
- **Producción**: Reportes de extracción y rendimiento
- **Nómina**: Análisis de costos laborales
- **Logística**: Métricas de inventario y transporte

## Características Especiales

- Generación de reportes en múltiples formatos (PDF, Excel, CSV)
- Dashboards interactivos con gráficos en tiempo real
- Sistema de alertas configurable
- Análisis predictivo y tendencias
- Exportación programada de reportes
- Permisos granulares por tipo de reporte

---
**Estado del dominio**: En planificación

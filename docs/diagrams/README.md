# Diagramas UML del Proyecto MinaControl Pro

Esta carpeta contiene los diagramas UML organizados por dominios para el proyecto MinaControl Pro.

## Estructura de carpetas

- **general**: Diagramas transversales del sistema (ER, clases completas, arquitectura)
- **empleados**: Diagramas específicos del dominio de empleados
- **turnos**: Diagramas específicos del dominio de turnos/asistencia
- **produccion**: Diagramas específicos del dominio de producción
- **nomina**: Diagramas específicos del dominio de nómina
- **logistica**: Diagramas específicos del dominio de logística/despachos  
- **reportes**: Diagramas específicos del dominio de reportes

## Convenciones de nomenclatura

Los diagramas siguen la siguiente convención de nomenclatura:

- `er_diagram_completo.puml`: Diagrama entidad-relación completo del sistema
- `class_diagram_completo.puml`: Diagrama de clases completo del sistema
- `architecture_overview.puml`: Vista general de la arquitectura
- `sequence_[operacion]_[objeto].puml`: Para diagramas de secuencia específicos
- `class_[dominio]_domain.puml`: Para diagramas de clases por dominio

## Estado actual

### ✅ Fase 1: Fundación (Completada)
- [x] ER Diagram Completo - Define modelo de datos completo
- [x] Class Diagram Completo - Define estructura de clases general
- [x] Architecture Overview - Define arquitectura general

### ✅ Fase 2: Diagramas de clase por dominio (Completada)
- [x] **Autenticación**: `class_diagram_autenticacion.puml` - Control de acceso y seguridad
- [x] **Empleados**: `class_diagram_empleados.puml` - Gestión integral del personal
- [x] **Turnos**: `class_diagram_turnos.puml` - Control de asistencia y horarios
- [x] **Producción**: `class_diagram_produccion.puml` - Registro y seguimiento de producción
- [x] **Logística**: `class_diagram_logistica.puml` - Control de despachos de carbón
- [x] **Nómina**: `class_diagram_nomina.puml` - Cálculo y gestión de pagos semanales
- [x] **Reportes**: `class_diagram_reportes.puml` - Inteligencia de negocio y análisis

### ✅ Fase 3: Diagramas de secuencia por endpoint (Completada)
- [x] **38 diagramas de secuencia** - Uno por cada endpoint de todos los dominios
- [x] Alineados con casos de uso de alto nivel y diagrama ER
- [x] Documentados en README de cada dominio

### 🔄 Validación y coherencia (Completada)
- [x] **Coherencia verificada** entre casos de uso, ER y diagramas de clases
- [x] **Simplificación aplicada** - Eliminadas sobre-especificaciones
- [x] **Alineación con MVP** - Solo funcionalidades definidas en casos de uso
- [x] **Metodología documentada** - Relación clara entre artefactos de diseño

## Principios aplicados

### Coherencia entre artefactos
- **Casos de uso de alto nivel** → Define QUÉ hacer y PARA QUÉ
- **Diagrama ER** → Define estructura de datos y relaciones
- **Diagramas de clases generales** → Define arquitectura base
- **Diagramas de clases por dominio** → Detalla implementación específica
- **Diagramas de secuencia** → Define flujos y interacciones

### Simplicidad y realismo
- Solo entidades y atributos presentes en el ER
- Solo funcionalidades definidas en casos de uso
- Sin sobre-especificación ni funcionalidades futuras no planificadas
- Alineación estricta con el MVP definido

## Visualización de diagramas

Estos diagramas están escritos en formato PlantUML y pueden ser visualizados con:

1. Extensiones de VS Code como "PlantUML"
2. El servidor web de PlantUML
3. Herramientas como IntelliJ IDEA con el plugin PlantUML

## Mantenimiento

Para mantener esta documentación:

1. Coloca los nuevos diagramas en la carpeta correspondiente a su módulo
2. Actualiza el archivo README.md de la carpeta cuando añadas nuevos diagramas
3. Sigue las convenciones de nomenclatura establecidas

## Estado Actual de los Diagramas por Dominio

- **empleados/**: ✅ Completado (class_diagram, 2 sequence_diagrams, README detallado)
- **turnos/**: ✅ Completado (class_diagram, 2 sequence_diagrams, README detallado)
- **produccion/**: ✅ Completado (class_diagram, 1 sequence_diagram, README detallado)
- **nomina/**: ✅ Completado (class_diagram, README básico) 
- **logistica/**: 📋 Pendiente (README creado, diagramas por desarrollar)
- **reportes/**: 📋 Pendiente (README creado, diagramas por desarrollar)

### Próximos Pasos

1. Completar diagramas de secuencia para nómina
2. Desarrollar diagramas completos para logística
3. Desarrollar diagramas completos para reportes
4. Validar que todos los diagramas estén alineados con los casos de uso
5. Iniciar implementación progresiva basada en los diagramas

## Importante: Metodología y Relación con Casos de Uso

Toda la documentación y los diagramas de esta carpeta se construyen de forma progresiva y ordenada:

1. **Casos de uso de alto nivel**: Son la base conceptual y funcional del sistema. Todo parte de aquí.
2. **Diagramas generales** (`general/`): Se crean a partir de los casos de uso y definen la estructura global (ER, clases, arquitectura).
3. **Diagramas por dominio**: Se desarrollan usando como referencia los diagramas generales, detallando la lógica y los flujos internos de cada área (empleados, logística, nómina, etc.).
4. **Implementación de código y pruebas**: Solo se programa lo que ya está modelado y documentado en los diagramas y casos de uso.

> **Recuerda:** Los diagramas de secuencia deben representar fielmente los puntos finales (endpoints) y flujos reales del sistema, sirviendo como referencia para la implementación y las pruebas.

Esta metodología garantiza coherencia, escalabilidad y evita retrabajos. Antes de avanzar, revisa siempre los casos de uso y los diagramas generales.

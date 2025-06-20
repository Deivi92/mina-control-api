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
- [x] Class Diagram Completo - Define estructura de clases
- [x] Architecture Overview - Define arquitectura general

### 🚧 Próximas fases
- **Fase 2**: Diagramas de clase por dominio
- **Fase 3**: Diagramas de secuencia por endpoint
- **Fase 4**: Flujos transversales integrados

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

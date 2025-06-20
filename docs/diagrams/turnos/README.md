# Dominio Turnos - Documentación y Diagramas

## Propósito del Dominio

El dominio de **Turnos** gestiona la programación y asignación de turnos de trabajo para los empleados de la mina. Es fundamental para:

- Organizar el trabajo por bloques de tiempo específicos
- Asignar empleados a turnos según sus roles y disponibilidad
- Controlar la rotación y cobertura de las operaciones
- Facilitar el seguimiento de horas trabajadas para nómina
- Garantizar continuidad operacional 24/7

## Casos de Uso (desde casos_de_uso_alto_nivel.md)

### CU-T01: Crear Turno

- **Actor Principal**: Administrador/Gerente
- **Descripción**: Crear un nuevo turno especificando horarios, tipo y capacidad
- **Precondiciones**: Usuario autenticado con permisos de gestión
- **Flujo Principal**: Definir horarios → Establecer capacidad → Configurar tipo → Guardar
- **Postcondiciones**: Turno disponible para asignaciones

### CU-T02: Asignar Empleado a Turno
- **Actor Principal**: Administrador/Gerente
- **Descripción**: Asignar empleados específicos a turnos programados
- **Precondiciones**: Turno creado, empleado activo
- **Flujo Principal**: Seleccionar turno → Elegir empleado → Verificar disponibilidad → Confirmar
- **Postcondiciones**: Empleado asignado, disponibilidad actualizada

### CU-T03: Consultar Horarios
- **Actor Principal**: Empleado/Administrador
- **Descripción**: Visualizar turnos asignados y programación
- **Precondiciones**: Usuario autenticado
- **Flujo Principal**: Seleccionar período → Filtrar por empleado/área → Mostrar calendario
- **Postcondiciones**: Información de turnos visualizada

### CU-T04: Gestionar Cambios de Turno
- **Actor Principal**: Administrador/Empleado
- **Descripción**: Procesar solicitudes de intercambio o modificación de turnos
- **Precondiciones**: Turnos asignados existentes
- **Flujo Principal**: Solicitar cambio → Validar disponibilidad → Aprobar → Actualizar
- **Postcondiciones**: Turnos intercambiados, historial actualizado

## Entidades Principales

### Turno
- **Propósito**: Representa un bloque de tiempo de trabajo específico
- **Atributos Clave**: fechaInicio, fechaFin, tipoTurno, capacidadMaxima, area
- **Relaciones**: ManyToMany con Empleado, OneToMany con AsignacionTurno

### AsignacionTurno
- **Propósito**: Relación específica entre un empleado y un turno
- **Atributos Clave**: empleadoId, turnoId, fechaAsignacion, estado
- **Relaciones**: ManyToOne con Empleado y Turno

### TipoTurno
- **Propósito**: Clasificación de turnos (diurno, nocturno, especial)
- **Atributos Clave**: nombre, horaInicio, horaFin, multiplicadorPago
- **Relaciones**: OneToMany con Turno

## Endpoints API Planificados

```
# Gestión de Turnos
POST   /api/turnos                     # Crear turno
GET    /api/turnos                     # Listar turnos
GET    /api/turnos/{id}                # Obtener turno específico
PUT    /api/turnos/{id}                # Actualizar turno
DELETE /api/turnos/{id}                # Eliminar turno

# Asignaciones
POST   /api/turnos/{id}/asignaciones   # Asignar empleado a turno
GET    /api/turnos/{id}/asignaciones   # Ver asignaciones del turno
DELETE /api/turnos/{id}/asignaciones/{empleadoId} # Remover asignación

# Consultas específicas
GET    /api/turnos/empleado/{id}       # Turnos de un empleado
GET    /api/turnos/fecha/{fecha}       # Turnos de una fecha
GET    /api/turnos/semana/{fecha}      # Turnos de una semana
GET    /api/empleados/{id}/turnos      # Historial de turnos del empleado

# Cambios y solicitudes
POST   /api/turnos/intercambios        # Solicitar intercambio
GET    /api/turnos/intercambios        # Ver solicitudes pendientes
PUT    /api/turnos/intercambios/{id}   # Aprobar/rechazar intercambio
```

## Relación con Diagramas Generales

### Diagrama ER Completo (`er_diagram_completo.puml`)
- **Turnos**: Tabla principal con horarios y configuración
- **AsignacionesTurnos**: Tabla de relación empleado-turno
- **TiposTurno**: Catálogo de tipos de turno
- **Relaciones**: FK hacia Empleados, conexión con RegistrosProduccion

### Diagrama de Clases Completo (`class_diagram_completo.puml`)
- **Entidades**: Turno, AsignacionTurno, TipoTurno
- **Services**: TurnoService, AsignacionTurnoService
- **Controllers**: TurnoController
- **DTOs**: TurnoDTO, AsignacionTurnoDTO, TurnoCreateDTO, etc.

### Arquitectura General (`architecture_overview.puml`)
- **Capa de Dominio**: Lógica de turnos y asignaciones
- **Capa de Aplicación**: Servicios de gestión de turnos
- **Capa de Infraestructura**: Repositorios y persistencia
- **Integración**: Con módulos de Empleados, Producción y Nómina

## Diagramas Específicos del Dominio

### 1. `class_diagram_turnos.puml`
- **Estado**: ⏳ Pendiente
- **Contenido**: Clases específicas del dominio turnos
- **Enfoque**: Entidades, DTOs, servicios, controladores y repositorios
- **Basado en**: Diagrama de clases completo (sección turnos)

### 2. `sequence_crear_turno.puml`
- **Estado**: ⏳ Pendiente
- **Contenido**: Flujo para crear un nuevo turno
- **Actores**: Administrador → Controller → Service → Repository → DB
- **Validaciones**: Horarios válidos, capacidad, conflictos

### 3. `sequence_asignar_empleado_turno.puml`
- **Estado**: ⏳ Pendiente
- **Contenido**: Proceso de asignación empleado-turno
- **Actores**: Administrador → Controller → Service → Repository → DB
- **Validaciones**: Disponibilidad, capacidad, restricciones

### 4. `sequence_consultar_turnos_empleado.puml`
- **Estado**: ⏳ Pendiente
- **Contenido**: Consulta de turnos asignados a un empleado
- **Actores**: Empleado/Admin → Controller → Service → Repository → DB
- **Filtros**: Por fecha, estado, tipo de turno

## Metodología de Construcción

1. **Análisis**: Revisar casos de uso y relaciones con otros dominios
2. **Diseño**: Crear diagrama de clases específico basado en el completo
3. **Flujos**: Diseñar diagramas de secuencia para casos principales
4. **Validación**: Verificar alineación con diagramas generales
5. **Implementación**: Usar diagramas como guía para código
6. **Pruebas**: Validar endpoints y funcionalidad según secuencias

## Integración con Otros Dominios

- **Empleados**: Turnos asignados a empleados específicos
- **Producción**: Registros de producción asociados a turnos
- **Nómina**: Cálculo de pagos basado en turnos trabajados
- **Reportes**: Estadísticas de cobertura y productividad por turno

## Notas de Implementación

- Los turnos manejan zonas horarias y horarios 24/7
- Validaciones de solapamiento y conflictos de horarios
- Sistema de notificaciones para cambios de turno
- Integración con calendario para visualización
- Restricciones de capacidad máxima por turno
- Historial completo de cambios y asignaciones

---
**Última actualización**: Enero 2024  
**Estado del dominio**: En planificación (diagramas pendientes)  
**Responsable**: Equipo de desarrollo MinaControl Pro

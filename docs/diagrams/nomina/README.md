# Diagramas del Dominio Nómina

## Propósito
Este dominio maneja el cálculo y gestión de pagos semanales basados en asistencia y producción.

## Casos de Uso Base
- **CU-NOM-001**: Calcular nómina semanal
- **CU-NOM-002**: Ajustar cálculos de nómina
- **CU-NOM-003**: Generar comprobantes de pago
- **CU-NOM-004**: Consultar histórico de pagos

## Diagramas Incluidos

### 1. class_diagram_nomina.puml
Diagrama de clases específico del dominio nómina, mostrando:
- Entidades principales (Nomina, AjusteNomina, ComprobantePago)
- DTOs de entrada y salida
- Servicios y controladores
- Relaciones con otros dominios

### 2. sequence_calcular_nomina_semanal.puml
Diagrama de secuencia para el cálculo de nómina semanal (CU-NOM-001):
- Integración con servicios de turnos y producción
- Cálculo automático basado en horas trabajadas y producción
- Generación de registros de pago por empleado

### 3. sequence_ajustar_nomina.puml
Diagrama de secuencia para ajustes de nómina (CU-NOM-002):
- Aplicación de bonos, descuentos o correcciones
- Validaciones de estado (no pagado)
- Registro de justificaciones para auditoría

### 4. sequence_generar_comprobantes.puml
Diagrama de secuencia para generación de comprobantes (CU-NOM-003):
- Creación de documentos PDF individuales
- Almacenamiento de referencias
- Cambio de estado de nómina

### 5. sequence_consultar_historial_nomina.puml
Diagrama de secuencia para consulta de histórico (CU-NOM-004):
- Filtros por empleado y fechas
- Control de permisos (Gerente vs Empleado)
- Formateo de datos según rol

## Endpoints Representados
- `POST /api/nomina/calcular` - Calcular nómina semanal
- `PATCH /api/nomina/{id}/ajustar` - Ajustar cálculos
- `POST /api/nomina/{id}/comprobantes` - Generar comprobantes
- `GET /api/nomina/historial` - Consultar histórico

## Relación con Diagramas Generales
- Basado en el `class_diagram_completo.puml` y `er_diagram_completo.puml`
- Implementa los casos de uso definidos en `casos_de_uso_alto_nivel.md`
- Integra con dominios de Empleados, Turnos y Producción

## Notas de Implementación
- Cálculos automáticos basados en métricas de otros dominios
- Control estricto de estados para evitar modificaciones post-pago
- Auditoría completa de ajustes y modificaciones
- Generación de documentos para cumplimiento legal
- **Relaciones**: ManyToOne con DetalleNomina

## Endpoints API Planificados

```http
# Gestión de Nóminas
POST   /api/nomina/calcular                         # Calcular nómina período
GET    /api/nomina/periodos                         # Listar períodos calculados
GET    /api/nomina/{periodo}                        # Obtener nómina de período
GET    /api/nomina/{periodo}/empleado/{id}          # Detalle empleado específico

# Comprobantes
GET    /api/nomina/{periodo}/comprobantes           # Generar comprobantes
GET    /api/nomina/{periodo}/comprobante/{empleadoId} # Comprobante individual
POST   /api/nomina/{periodo}/enviar-comprobantes    # Enviar por email

# Conceptos
POST   /api/nomina/conceptos                        # Crear concepto
GET    /api/nomina/conceptos                        # Listar conceptos
PUT    /api/nomina/conceptos/{id}                   # Actualizar concepto
DELETE /api/nomina/conceptos/{id}                   # Eliminar concepto

# Consultas
GET    /api/nomina/empleado/{id}/historico          # Histórico empleado
GET    /api/nomina/reportes/resumen                 # Reporte resumen
GET    /api/nomina/reportes/comparativo             # Reporte comparativo
```

## Diagramas Específicos del Dominio

### 1. `class_diagram_nomina.puml`

- **Estado**: ⏳ Pendiente
- **Contenido**: Clases específicas del dominio nómina
- **Enfoque**: Entidades, DTOs, servicios y controladores

### 2. `sequence_calcular_nomina.puml`

- **Estado**: ⏳ Pendiente
- **Contenido**: Flujo para calcular nómina mensual
- **Validaciones**: Período válido, empleados activos, conceptos

### 3. `sequence_generar_comprobantes.puml`

- **Estado**: ⏳ Pendiente
- **Contenido**: Proceso de generación de comprobantes
- **Salidas**: PDFs, envío de emails, archivado

## Integración con Otros Dominios

- **Empleados**: Datos básicos y salarios base
- **Turnos**: Horas trabajadas y turnos nocturnos
- **Producción**: Bonificaciones por productividad
- **Reportes**: Análisis de costos laborales

---
**Estado del dominio**: En planificación

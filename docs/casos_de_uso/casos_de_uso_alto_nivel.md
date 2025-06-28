# Casos de Uso de Alto Nivel - MinaControl Pro

## Introducción

MinaControl Pro es un sistema de gestión minera diseñado para optimizar las operaciones básicas de una mina pequeña a mediana. Este documento define los casos de uso de alto nivel organizados por dominios y roles, proporcionando una base sólida para el desarrollo iterativo del sistema.

## Roles del Sistema

### 1. Empleado
- **Responsabilidades**: Consultar información personal (asistencia, producción, nómina propias)
- **Nivel de acceso**: Operativo - Solo lectura de datos propios
- **Contexto**: Personal de campo, operadores, técnicos

### 2. Administrador
- **Responsabilidades**: Gestión completa de empleados, turnos, producción y logística
- **Nivel de acceso**: Administrativo - Gestión diaria completa
- **Contexto**: Supervisor de operaciones, jefe de turno, coordinador

### 3. Gerente
- **Responsabilidades**: Supervisión estratégica, reportes ejecutivos, nómina
- **Nivel de acceso**: Ejecutivo - Visión completa y reportes
- **Contexto**: Gerente de operaciones, director de mina

## Dominios y Casos de Uso

### Dominio 0: Autenticación
**Propósito**: Control de acceso y seguridad del sistema

#### Casos de Uso Principales:
- **CU-AUTH-001**: Iniciar sesión
  - **Actor**: Empleado, Administrador, Gerente
  - **Descripción**: Autenticarse en el sistema con credenciales (email/contraseña)
  - **Flujo**: Ingresar email/contraseña → Validar credenciales → Generar token JWT

- **CU-AUTH-002**: Cerrar sesión
  - **Actor**: Empleado, Administrador, Gerente
  - **Descripción**: Finalizar sesión activa de forma segura
  - **Flujo**: Solicitar cierre → Invalidar token → Redireccionar a login

- **CU-AUTH-003**: Cambiar contraseña
  - **Actor**: Empleado (solo propia), Administrador (cualquier empleado)
  - **Descripción**: Actualizar credenciales de acceso por seguridad
  - **Flujo**: Validar contraseña actual → Ingresar nueva → Confirmar cambio → Notificar

- **CU-AUTH-004**: Recuperar contraseña
  - **Actor**: Usuario
  - **Descripción**: Restablecer acceso cuando el usuario olvida su contraseña
  - **Flujo**: Usuario solicita (vía email) → Sistema envía enlace de reseteo → Usuario resetea contraseña

### Dominio 1: Empleados
**Propósito**: Gestión integral del personal de la mina

#### Casos de Uso Principales:
- **CU-EMP-001**: Registrar nuevo empleado
  - **Actor**: Administrador
  - **Descripción**: Crear perfil de empleado con datos básicos, contacto y rol
  - **Flujo**: Datos personales → Asignación de rol → Configuración de acceso

- **CU-EMP-002**: Actualizar información de empleado
  - **Actor**: Administrador
  - **Descripción**: Modificar datos existentes del empleado
  - **Flujo**: Buscar empleado → Editar campos → Validar cambios

- **CU-EMP-003**: Consultar empleados
  - **Actor**: Administrador, Gerente
  - **Descripción**: Listar y filtrar empleados activos/inactivos
  - **Flujo**: Aplicar filtros → Mostrar lista → Detalles opcionales

- **CU-EMP-004**: Gestionar estado del empleado
  - **Actor**: Administrador
  - **Descripción**: Activar/desactivar empleados
  - **Flujo**: Seleccionar empleado → Cambiar estado → Confirmar acción

- **CU-EMP-005**: Consultar información personal
  - **Actor**: Empleado
  - **Descripción**: Ver datos propios y historial básico
  - **Flujo**: Autenticación → Panel personal → Consulta de datos

### Dominio 2: Turnos
**Propósito**: Control de asistencia y gestión de horarios

#### Casos de Uso Principales:
- **CU-TUR-001**: Registrar entrada/salida
  - **Actor**: Administrador
  - **Descripción**: Registrar asistencia de empleados con timestamp automático
  - **Flujo**: Identificar empleado → Registrar entrada/salida → Confirmación

- **CU-TUR-002**: Configurar turnos
  - **Actor**: Administrador
  - **Descripción**: Definir horarios y asignar empleados
  - **Flujo**: Crear turno → Definir horarios → Asignar personal

- **CU-TUR-003**: Consultar asistencia
  - **Actor**: Administrador, Gerente, Empleado (solo propia)
  - **Descripción**: Ver registros de asistencia por período
  - **Flujo**: Seleccionar período → Aplicar filtros → Mostrar registros

- **CU-TUR-004**: Gestionar excepciones
  - **Actor**: Administrador
  - **Descripción**: Registrar permisos, faltas justificadas, ajustes
  - **Flujo**: Identificar situación → Registrar excepción → Actualizar cálculos

### Dominio 3: Producción
**Propósito**: Registro y seguimiento de la producción diaria

#### Casos de Uso Principales:
- **CU-PRO-001**: Registrar producción diaria
  - **Actor**: Administrador
  - **Descripción**: Capturar datos de producción por turno/empleado
  - **Flujo**: Seleccionar empleado/turno → Ingresar métricas → Validar datos

- **CU-PRO-002**: Consultar producción por empleado
  - **Actor**: Administrador, Gerente, Empleado (solo propia)
  - **Descripción**: Ver histórico de producción individual
  - **Flujo**: Seleccionar empleado → Definir período → Mostrar métricas

- **CU-PRO-003**: Consultar producción por fecha
  - **Actor**: Administrador, Gerente
  - **Descripción**: Ver producción agregada por período
  - **Flujo**: Seleccionar rango → Agrupar datos → Mostrar totales

- **CU-PRO-004**: Actualizar registros de producción
  - **Actor**: Administrador
  - **Descripción**: Corregir o ajustar registros existentes
  - **Flujo**: Localizar registro → Editar valores → Validar cambios

- **CU-PRO-005**: Eliminar registros incorrectos
  - **Actor**: Administrador
  - **Descripción**: Remover registros erróneos o duplicados
  - **Flujo**: Identificar registro → Confirmar eliminación → Actualizar totales

### Dominio 4: Logística/Despachos
**Propósito**: Control de salida y distribución de carbón

#### Casos de Uso Principales:
- **CU-LOG-001**: Registrar despacho
  - **Actor**: Administrador
  - **Descripción**: Documentar salida de carbón de la mina
  - **Flujo**: Datos del despacho → Ingresar conductor/vehículo → Especificar toneladas → Confirmar salida

- **CU-LOG-002**: Consultar despachos
  - **Actor**: Administrador, Gerente
  - **Descripción**: Ver histórico de despachos por período
  - **Flujo**: Filtrar por fecha/destino → Mostrar lista → Detalles opcionales

- **CU-LOG-003**: Actualizar estado de despacho
  - **Actor**: Administrador
  - **Descripción**: Cambiar estado (programado/en tránsito/entregado)
  - **Flujo**: Localizar despacho → Actualizar estado → Registrar timestamp

### Dominio 5: Nómina
**Propósito**: Cálculo y gestión de pagos semanales

#### Casos de Uso Principales:
- **CU-NOM-001**: Calcular nómina semanal
  - **Actor**: Gerente
  - **Descripción**: Procesar pagos basados en asistencia y producción
  - **Flujo**: Seleccionar semana → Calcular automático → Revisar resultados

- **CU-NOM-002**: Ajustar cálculos de nómina
  - **Actor**: Gerente
  - **Descripción**: Aplicar bonificaciones, descuentos o correcciones
  - **Flujo**: Identificar empleado → Aplicar ajuste → Justificar cambio

- **CU-NOM-003**: Generar comprobantes de pago
  - **Actor**: Gerente
  - **Descripción**: Crear documentos de pago individual
  - **Flujo**: Confirmar nómina → Generar PDFs → Distribuir comprobantes

- **CU-NOM-004**: Consultar histórico de pagos
  - **Actor**: Gerente, Empleado (solo propio)
  - **Descripción**: Ver registros de pagos anteriores
  - **Flujo**: Seleccionar período → Aplicar filtros → Mostrar histórico

### Dominio 6: Reportes
**Propósito**: Inteligencia de negocio y análisis operacional

#### Casos de Uso Principales:
- **CU-REP-001**: Generar reporte de producción
  - **Actor**: Gerente
  - **Descripción**: Análisis de productividad por período
  - **Flujo**: Configurar parámetros → Procesar datos → Exportar reporte

- **CU-REP-002**: Generar reporte de asistencia
  - **Actor**: Administrador, Gerente
  - **Descripción**: Análisis de puntualidad y ausentismo
  - **Flujo**: Seleccionar período → Calcular métricas → Mostrar resultados

- **CU-REP-003**: Generar reporte de costos laborales
  - **Actor**: Gerente
  - **Descripción**: Análisis de gastos en nómina y productividad
  - **Flujo**: Consolidar datos → Calcular ratios → Generar dashboard

- **CU-REP-004**: Exportar datos operacionales
  - **Actor**: Gerente
  - **Descripción**: Extraer datos para análisis externo
  - **Flujo**: Seleccionar dataset → Configurar formato → Descargar archivo

## Flujos Integrados y Transversales

### Flujo 1: Ciclo Diario Operativo
1. **Inicio de turno**: Empleado se reporta físicamente → Administrador registra entrada (CU-TUR-001)
2. **Durante turno**: Administrador registra producción del empleado (CU-PRO-001)
3. **Fin de turno**: Empleado se reporta físicamente → Administrador registra salida (CU-TUR-001)
4. **Supervisión**: Administrador revisa y ajusta registros si necesario

### Flujo 2: Proceso Semanal de Nómina
1. **Lunes**: Gerente inicia cálculo de nómina anterior (CU-NOM-001)
2. **Martes-Miércoles**: Revisión y ajustes necesarios (CU-NOM-002)
3. **Jueves**: Generación de comprobantes (CU-NOM-003)
4. **Sábado**: Pago efectivo a empleados

### Flujo 3: Gestión de Despachos
1. **Planificación**: Administrador registra despacho programado (CU-LOG-001)
2. **Ejecución**: Administrador actualiza estado del despacho (CU-LOG-003)
3. **Seguimiento**: Administrador/Gerente consulta progreso y confirma entrega (CU-LOG-002)

### Flujo 4: Análisis Gerencial Mensual
1. **Recopilación**: Gerente consulta datos de todos los dominios
2. **Análisis**: Gerente genera reportes integrados (CU-REP-001, CU-REP-002, CU-REP-003)
3. **Decisión**: Gerente usa información para mejoras operacionales

## Consideraciones de Escalabilidad

### Flexibilidad Futura
- **Múltiples turnos**: Extensión desde horario básico a 24/7
- **Múltiples ubicaciones**: Soporte para minas distribuidas
- **Nuevos roles**: Incorporación de especialistas (geólogo, ingeniero, etc.)
- **Integración externa**: APIs para sistemas contables, ERP, etc.

### Modularidad
- Cada dominio opera independientemente
- APIs REST permiten integración gradual
- Base de datos normalizada facilita expansión
- Arquitectura de microservicios lista para escalar

### Personalización
- Configuración de múltiples tipos de materiales (futuro - actualmente solo carbón)
- Esquemas de pago flexibles (por hora, por producción, mixtos)
- Campos personalizables en empleados y registros
- Reportes configurables según necesidades específicas
- Gestión completa de conductores/vehículos (futuro - actualmente campos simples)
- Múltiples turnos por empleado (futuro - actualmente un turno básico)

## Notas de Implementación

### Prioridad de Desarrollo (MVP)
1. **Fase 1**: Empleados + Turnos (base operativa)
2. **Fase 2**: Producción (core del negocio)
3. **Fase 3**: Nómina (automatización crítica)
4. **Fase 4**: Logística + Reportes (optimización)

### Tecnologías Base
- **Backend**: Spring Boot (Java 17)
- **Base de datos**: PostgreSQL
- **Frontend**: React.js (futuro)
- **Autenticación**: Spring Security + JWT
- **Documentación**: OpenAPI 3.0

### Restricciones Iniciales
- Pagos únicamente los sábados (configurable a futuro)
- Solo extracción de carbón (sin otros materiales)
- Conductores y vehículos como datos simples (no entidades complejas)
- Autenticación simple: username = número identificación del empleado
- Contraseña inicial temporal que debe cambiarse en primer acceso
- Roles fijos (no personalizables inicialmente)
- Reportes en formato PDF/Excel solamente
- Un solo turno básico por empleado (expandible a futuro)

---

**Versión**: 1.0  
**Fecha**: Junio 2025  
**Estado**: Definición de Alto Nivel  
**Próximos pasos**: Análisis detallado por dominio y diseño de APIs

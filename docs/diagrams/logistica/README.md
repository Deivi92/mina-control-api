# Dominio Logística - Documentación y Diagramas

## Propósito del Dominio

El dominio de **Logística** gestiona el transporte, almacenamiento y distribución de materiales extraídos, así como el inventario de equipos y suministros necesarios para las operaciones mineras.

## Casos de Uso Principales

### CU-L01: Gestionar Inventario
- **Actor Principal**: Encargado de Almacén
- **Descripción**: Controlar entradas, salidas y stock de materiales y equipos
- **Flujo Principal**: Registrar movimiento → Actualizar stock → Verificar niveles → Generar alertas

### CU-L02: Programar Transporte
- **Actor Principal**: Coordinador Logístico
- **Descripción**: Planificar y coordinar el transporte de materiales
- **Flujo Principal**: Crear solicitud → Asignar vehículo → Programar ruta → Confirmar entrega

### CU-L03: Gestionar Proveedores
- **Actor Principal**: Administrador
- **Descripción**: Mantener información de proveedores y gestionar compras
- **Flujo Principal**: Registrar proveedor → Crear orden compra → Hacer seguimiento → Recibir materiales

## Entidades Principales

### Inventario
- **Propósito**: Control de stock de materiales y equipos
- **Atributos Clave**: codigo, descripcion, cantidad, stockMinimo, ubicacion
- **Relaciones**: OneToMany con MovimientoInventario

### Transporte
- **Propósito**: Gestión de vehículos y rutas de transporte
- **Atributos Clave**: fecha, vehiculo, ruta, carga, estado
- **Relaciones**: ManyToOne con Vehiculo, OneToMany con DetalleTransporte

### Proveedor
- **Propósito**: Información de proveedores de materiales y servicios
- **Atributos Clave**: nombre, contacto, especialidad, calificacion
- **Relaciones**: OneToMany con OrdenCompra

## Endpoints API Planificados

```http
# Gestión de Inventario
GET    /api/logistica/inventario                    # Listar inventario
POST   /api/logistica/inventario/movimiento         # Registrar movimiento
GET    /api/logistica/inventario/alertas            # Stock bajo

# Gestión de Transporte
POST   /api/logistica/transporte                    # Programar transporte
GET    /api/logistica/transporte/programados        # Ver programación
PUT    /api/logistica/transporte/{id}/estado        # Actualizar estado

# Gestión de Proveedores
POST   /api/logistica/proveedores                   # Crear proveedor
GET    /api/logistica/proveedores                   # Listar proveedores
POST   /api/logistica/ordenes-compra                # Crear orden compra
```

## Diagramas Específicos del Dominio

### 1. `class_diagram_logistica.puml`
- **Estado**: ✅ Completado
- **Contenido**: Clases de inventario, transporte y proveedores

### 2. `sequence_registrar_despacho.puml`
- **Estado**: ✅ Completado
- **Contenido**: Flujo para registrar un nuevo despacho.

### 3. `sequence_consultar_despachos.puml`
- **Estado**: ✅ Completado
- **Contenido**: Proceso de consulta de despachos existentes.

### 4. `sequence_actualizar_estado_despacho.puml`
- **Estado**: ✅ Completado
- **Contenido**: Flujo para actualizar el estado de un despacho.

## Integración con Otros Dominios

- **Producción**: Materiales extraídos para transporte
- **Empleados**: Conductores y personal logístico
- **Reportes**: Análisis de costos logísticos y eficiencia

---
**Estado del dominio**: En planificación

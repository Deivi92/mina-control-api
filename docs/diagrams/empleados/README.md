# Diagramas del Dominio Empleados

## Propósito
Este dominio maneja la gestión integral del personal de la mina, desde el registro hasta la consulta de información personal.

## Casos de Uso Base
- **CU-EMP-001**: Registrar nuevo empleado
- **CU-EMP-002**: Actualizar información de empleado
- **CU-EMP-003**: Consultar empleados
- **CU-EMP-004**: Gestionar estado del empleado
- **CU-EMP-005**: Consultar información personal

## Diagramas Incluidos

### ✅ Diagrama de Clases del Dominio
- **Archivo**: `class_diagram_empleados.puml`
- **Propósito**: Define la estructura de clases específica del dominio de empleados
- **Contenido**: Entidades (Empleado, Usuario), DTOs, Servicios, Controladores, Repositorios y componentes de seguridad
- **Estado**: ✅ Completado y alineado con ER y casos de uso

### ✅ Diagramas de Secuencia

### 1. sequence_crear_empleado.puml
Diagrama de secuencia para el registro de nuevos empleados (CU-EMP-001):
- Flujo completo desde el controlador hasta la persistencia
- Creación de usuario y empleado
- Validaciones y respuestas

### 2. sequence_listar_empleados.puml
Diagrama de secuencia para listar empleados:
- Filtros por estado, rol, etc.
- Transformación a DTOs
- Respuesta estructurada

### 3. sequence_obtener_empleado_por_id.puml
Diagrama de secuencia para obtener un empleado por ID:
- Consulta de un empleado específico por su identificador
- Manejo de empleado no encontrado

### 4. sequence_actualizar_empleado.puml
Diagrama de secuencia para actualización de empleados (CU-EMP-002):
- Validación de existencia
- Actualización de campos
- Manejo de excepciones

### 5. sequence_cambiar_estado_empleado.puml
Diagrama de secuencia para cambiar el estado de un empleado (CU-EMP-004):
- Activación o desactivación de empleados
- Validaciones de estado

### 6. sequence_consultar_perfil_personal.puml
Diagrama de secuencia para consultar información personal (CU-EMP-005):
- Consulta de perfil por el propio empleado
- Restricciones de acceso

### 7. sequence_eliminar_empleado.puml
Diagrama de secuencia para eliminar un empleado:
- Eliminación lógica o física de un empleado
- Manejo de dependencias y restricciones

## Relación con Diagramas Generales
- Basado en el `class_diagram_completo.puml` y `er_diagram_completo.puml`
- Implementa los casos de uso definidos en `casos_de_uso_alto_nivel.md`
- Mantiene consistencia con la arquitectura general del sistema

## Endpoints Representados
- `POST /api/empleados` - Registrar empleado
- `GET /api/empleados` - Listar empleados
- `GET /api/empleados/{id}` - Obtener empleado específico
- `PUT /api/empleados/{id}` - Actualizar empleado
- `PATCH /api/empleados/{id}/estado` - Cambiar estado
- `GET /api/empleados/perfil` - Consultar perfil personal

## Notas de Implementación
- Autenticación JWT requerida para todos los endpoints
- Autorización por roles (ADMINISTRADOR para gestión, EMPLEADO para consulta personal)
- Validación de datos con Bean Validation
- Manejo global de excepciones

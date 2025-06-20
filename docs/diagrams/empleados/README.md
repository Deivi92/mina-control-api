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

### 1. empleado_class_diagram.puml
Diagrama de clases específico del dominio empleados, mostrando:
- Entidades principales (Empleado, Usuario)
- DTOs de entrada y salida
- Servicios y controladores
- Enums de estado y roles

### 2. empleado_sequence_registrar.puml
Diagrama de secuencia para el registro de nuevos empleados (CU-EMP-001):
- Flujo completo desde el controlador hasta la persistencia
- Creación de usuario y empleado
- Validaciones y respuestas

### 3. empleado_sequence_consultar.puml
Diagrama de secuencia para consulta de empleados (CU-EMP-003):
- Filtros por estado, rol, etc.
- Transformación a DTOs
- Respuesta estructurada

### 4. empleado_sequence_actualizar.puml
Diagrama de secuencia para actualización de empleados (CU-EMP-002):
- Validación de existencia
- Actualización de campos
- Manejo de excepciones

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

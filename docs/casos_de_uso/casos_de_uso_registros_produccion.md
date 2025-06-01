# Casos de Uso: Módulo de Registros de Producción

Este documento detalla los casos de uso para el módulo de Registros de Producción de MinaControl Pro, incluyendo la especificación de endpoints, validaciones y comportamiento esperado.

## Introducción

El módulo de Registros de Producción permite gestionar la información diaria sobre la producción de carbón en la mina, incluyendo toneladas extraídas, frentes de trabajo activos, responsables de turno y observaciones relevantes.

## Modelo de Datos

### Entidad `RegistroProduccion`

| Atributo          | Tipo      | Descripción                                      | Obligatorio |
|-------------------|-----------|--------------------------------------------------|-------------|
| id                | Long      | Identificador único del registro                 | Sí          |
| fechaRegistro     | LocalDate | Fecha del registro de producción                 | Sí          |
| turno             | String    | Turno (MAÑANA, TARDE, NOCHE)                     | Sí          |
| responsableTurnoId| Long      | ID del empleado responsable del turno            | Sí          |
| toneladasExtraidas| Double    | Cantidad de toneladas extraídas en el turno      | Sí          |
| frentesTrabajo    | String    | Frentes de trabajo activos durante el turno      | Sí          |
| observaciones     | String    | Observaciones adicionales sobre la producción     | No          |
| fechaCreacion     | LocalDateTime | Fecha y hora de creación del registro        | Sí (auto)   |
| fechaActualizacion| LocalDateTime | Fecha y hora de última actualización         | Sí (auto)   |

## Casos de Uso

### CU-REG-01: Crear registro de producción diaria

**Descripción:** Permite crear un nuevo registro de producción diaria.

**Endpoint:** `POST /api/registros-produccion`

**Implementación requerida:**
- **Controller:** `RegistroProduccionController.crearRegistro()`
- **Service:** `RegistroProduccionServiceImpl.crearRegistro()`
- **Repository:** `InMemoryRegistroProduccionRepositoryImpl.save()`
- **DTOs:** `RegistroProduccionRequestDTO`, `RegistroProduccionResponseDTO`
- **Modelo:** `RegistroProduccion`

**Cuerpo de la solicitud (Request):**
```json
{
  "fechaRegistro": "2025-06-01",
  "turno": "MAÑANA",
  "responsableTurnoId": 1,
  "toneladasExtraidas": 25.5,
  "frentesTrabajo": "Nivel 1, Sector A",
  "observaciones": "Condiciones normales de operación"
}
```

**Respuesta exitosa (Response):**
```json
{
  "id": 1,
  "fechaRegistro": "2025-06-01",
  "turno": "MAÑANA",
  "responsableTurnoId": 1,
  "responsableTurnoNombre": "Juan Pérez",
  "toneladasExtraidas": 25.5,
  "frentesTrabajo": "Nivel 1, Sector A",
  "observaciones": "Condiciones normales de operación",
  "fechaCreacion": "2025-06-01T08:30:15",
  "fechaActualizacion": "2025-06-01T08:30:15"
}
```

**Validaciones a implementar:**
- `fechaRegistro`: No debe ser nula y no debe ser una fecha futura
- `turno`: Debe ser un valor válido entre ["MAÑANA", "TARDE", "NOCHE"]
- `responsableTurnoId`: Debe existir un empleado con ese ID en el sistema
- `toneladasExtraidas`: Debe ser un valor positivo mayor a cero
- `frentesTrabajo`: No debe ser nulo ni estar vacío
- Verificar que no exista ya un registro para la misma fecha y turno

**Códigos de respuesta:**
- 201 (Created): Registro creado exitosamente
- 400 (Bad Request): Datos inválidos o incompletos
- 404 (Not Found): El empleado responsable no existe
- 409 (Conflict): Ya existe un registro para esa fecha y turno

**Comportamiento esperado:**
1. Validar los datos de entrada
2. Verificar la existencia del empleado responsable
3. Verificar que no exista un registro duplicado para la misma fecha y turno
4. Crear el registro en el repositorio
5. Retornar el registro creado con el ID asignado

### CU-REG-02: Consultar registro de producción por ID

**Descripción:** Permite obtener la información de un registro específico mediante su identificador.

**Endpoint:** `GET /api/registros-produccion/{id}`

**Implementación requerida:**
- **Controller:** `RegistroProduccionController.obtenerRegistroPorId()`
- **Service:** `RegistroProduccionServiceImpl.obtenerRegistroPorId()`
- **Repository:** `InMemoryRegistroProduccionRepositoryImpl.findById()`
- **DTOs:** `RegistroProduccionResponseDTO`
- **Modelo:** `RegistroProduccion`
- **Excepciones:** `RecursoNoEncontradoException`

**Parámetros de ruta:**
- `id`: ID del registro de producción a consultar

**Respuesta exitosa (Response):**
```json
{
  "id": 1,
  "fechaRegistro": "2025-06-01",
  "turno": "MAÑANA",
  "responsableTurnoId": 1,
  "responsableTurnoNombre": "Juan Pérez",
  "toneladasExtraidas": 25.5,
  "frentesTrabajo": "Nivel 1, Sector A",
  "observaciones": "Condiciones normales de operación",
  "fechaCreacion": "2025-06-01T08:30:15",
  "fechaActualizacion": "2025-06-01T08:30:15"
}
```

**Códigos de respuesta:**
- 200 (OK): Registro encontrado y devuelto
- 404 (Not Found): Registro no encontrado

**Comportamiento esperado:**
1. Buscar el registro en el repositorio por su ID
2. Si no existe, lanzar `RecursoNoEncontradoException`
3. Si existe, convertir a DTO y retornar

### CU-REG-03: Listar todos los registros de producción

**Descripción:** Permite obtener la lista completa de registros de producción.

**Endpoint:** `GET /api/registros-produccion`

**Implementación requerida:**
- **Controller:** `RegistroProduccionController.obtenerTodosLosRegistros()`
- **Service:** `RegistroProduccionServiceImpl.obtenerTodosLosRegistros()`
- **Repository:** `InMemoryRegistroProduccionRepositoryImpl.findAll()`
- **DTOs:** `RegistroProduccionResponseDTO`
- **Modelo:** `RegistroProduccion`

**Respuesta exitosa (Response):**
```json
[
  {
    "id": 1,
    "fechaRegistro": "2025-06-01",
    "turno": "MAÑANA",
    "responsableTurnoId": 1,
    "responsableTurnoNombre": "Juan Pérez",
    "toneladasExtraidas": 25.5,
    "frentesTrabajo": "Nivel 1, Sector A",
    "observaciones": "Condiciones normales de operación",
    "fechaCreacion": "2025-06-01T08:30:15",
    "fechaActualizacion": "2025-06-01T08:30:15"
  },
  {
    "id": 2,
    "fechaRegistro": "2025-06-01",
    "turno": "TARDE",
    "responsableTurnoId": 2,
    "responsableTurnoNombre": "María López",
    "toneladasExtraidas": 22.8,
    "frentesTrabajo": "Nivel 1, Sector B",
    "observaciones": "Interrupción de 30 minutos por mantenimiento",
    "fechaCreacion": "2025-06-01T16:45:22",
    "fechaActualizacion": "2025-06-01T16:45:22"
  }
]
```

**Códigos de respuesta:**
- 200 (OK): Lista de registros retornada (puede estar vacía)

**Comportamiento esperado:**
1. Obtener todos los registros del repositorio
2. Convertir cada registro a DTO
3. Retornar la lista completa

### CU-REG-04: Actualizar registro de producción

**Descripción:** Permite modificar los datos de un registro de producción existente.

**Endpoint:** `PUT /api/registros-produccion/{id}`

**Implementación requerida:**
- **Controller:** `RegistroProduccionController.actualizarRegistro()`
- **Service:** `RegistroProduccionServiceImpl.actualizarRegistro()`
- **Repository:** `InMemoryRegistroProduccionRepositoryImpl.update()`
- **DTOs:** `RegistroProduccionRequestDTO`, `RegistroProduccionResponseDTO`
- **Modelo:** `RegistroProduccion`
- **Excepciones:** `RecursoNoEncontradoException`

**Parámetros de ruta:**
- `id`: ID del registro de producción a actualizar

**Cuerpo de la solicitud (Request):**
```json
{
  "fechaRegistro": "2025-06-01",
  "turno": "MAÑANA",
  "responsableTurnoId": 1,
  "toneladasExtraidas": 27.5,
  "frentesTrabajo": "Nivel 1, Sector A y C",
  "observaciones": "Se amplió el frente de trabajo al Sector C"
}
```

**Respuesta exitosa (Response):**
```json
{
  "id": 1,
  "fechaRegistro": "2025-06-01",
  "turno": "MAÑANA",
  "responsableTurnoId": 1,
  "responsableTurnoNombre": "Juan Pérez",
  "toneladasExtraidas": 27.5,
  "frentesTrabajo": "Nivel 1, Sector A y C",
  "observaciones": "Se amplió el frente de trabajo al Sector C",
  "fechaCreacion": "2025-06-01T08:30:15",
  "fechaActualizacion": "2025-06-01T10:15:30"
}
```

**Validaciones a implementar:**
- `fechaRegistro`: No debe ser nula y no debe ser una fecha futura
- `turno`: Debe ser un valor válido entre ["MAÑANA", "TARDE", "NOCHE"]
- `responsableTurnoId`: Debe existir un empleado con ese ID en el sistema
- `toneladasExtraidas`: Debe ser un valor positivo mayor a cero
- `frentesTrabajo`: No debe ser nulo ni estar vacío
- Verificar que el registro a actualizar exista
- Verificar que al cambiar fecha y turno, no exista ya un registro con esa combinación

**Códigos de respuesta:**
- 200 (OK): Registro actualizado exitosamente
- 400 (Bad Request): Datos inválidos o incompletos
- 404 (Not Found): El registro o el empleado responsable no existe
- 409 (Conflict): Ya existe un registro diferente para esa fecha y turno

**Comportamiento esperado:**
1. Validar los datos de entrada
2. Verificar la existencia del registro a actualizar
3. Verificar la existencia del empleado responsable
4. Verificar que no exista un registro duplicado para la misma fecha y turno (excluyendo el actual)
5. Actualizar el registro en el repositorio
6. Actualizar la fecha de actualización
7. Retornar el registro actualizado

### CU-REG-05: Eliminar registro de producción

**Descripción:** Permite eliminar un registro de producción del sistema.

**Endpoint:** `DELETE /api/registros-produccion/{id}`

**Implementación requerida:**
- **Controller:** `RegistroProduccionController.eliminarRegistro()`
- **Service:** `RegistroProduccionServiceImpl.eliminarRegistro()`
- **Repository:** `InMemoryRegistroProduccionRepositoryImpl.deleteById()`
- **Excepciones:** `RecursoNoEncontradoException`

**Parámetros de ruta:**
- `id`: ID del registro de producción a eliminar

**Códigos de respuesta:**
- 204 (No Content): Registro eliminado exitosamente
- 404 (Not Found): El registro no existe

**Comportamiento esperado:**
1. Verificar la existencia del registro a eliminar
2. Si no existe, lanzar `RecursoNoEncontradoException`
3. Si existe, eliminarlo del repositorio
4. Retornar respuesta sin contenido

### CU-REG-06: Consultar registros por fecha

**Descripción:** Permite obtener todos los registros de producción de una fecha específica.

**Endpoint:** `GET /api/registros-produccion/fecha/{fecha}`

**Implementación requerida:**
- **Controller:** `RegistroProduccionController.obtenerRegistrosPorFecha()`
- **Service:** `RegistroProduccionServiceImpl.obtenerRegistrosPorFecha()`
- **Repository:** `InMemoryRegistroProduccionRepositoryImpl.findByFechaRegistro()`
- **DTOs:** `RegistroProduccionResponseDTO`
- **Modelo:** `RegistroProduccion`

**Parámetros de ruta:**
- `fecha`: Fecha para filtrar los registros (formato: yyyy-MM-dd)

**Respuesta exitosa (Response):**
```json
[
  {
    "id": 1,
    "fechaRegistro": "2025-06-01",
    "turno": "MAÑANA",
    "responsableTurnoId": 1,
    "responsableTurnoNombre": "Juan Pérez",
    "toneladasExtraidas": 27.5,
    "frentesTrabajo": "Nivel 1, Sector A y C",
    "observaciones": "Se amplió el frente de trabajo al Sector C",
    "fechaCreacion": "2025-06-01T08:30:15",
    "fechaActualizacion": "2025-06-01T10:15:30"
  },
  {
    "id": 2,
    "fechaRegistro": "2025-06-01",
    "turno": "TARDE",
    "responsableTurnoId": 2,
    "responsableTurnoNombre": "María López",
    "toneladasExtraidas": 22.8,
    "frentesTrabajo": "Nivel 1, Sector B",
    "observaciones": "Interrupción de 30 minutos por mantenimiento",
    "fechaCreacion": "2025-06-01T16:45:22",
    "fechaActualizacion": "2025-06-01T16:45:22"
  }
]
```

**Validaciones a implementar:**
- `fecha`: Debe tener un formato válido (yyyy-MM-dd)

**Códigos de respuesta:**
- 200 (OK): Lista de registros retornada (puede estar vacía)
- 400 (Bad Request): Formato de fecha inválido

**Comportamiento esperado:**
1. Validar el formato de la fecha
2. Buscar registros en el repositorio por fecha
3. Convertir cada registro a DTO
4. Retornar la lista (puede estar vacía si no hay registros para esa fecha)

### CU-REG-07: Consultar registros por empleado y fecha

**Descripción:** Permite obtener los registros de producción de un empleado en una fecha específica.

**Endpoint:** `GET /api/registros-produccion/empleado/{empleadoId}/fecha/{fecha}`

**Implementación requerida:**
- **Controller:** `RegistroProduccionController.obtenerRegistrosPorEmpleadoYFecha()`
- **Service:** `RegistroProduccionServiceImpl.obtenerRegistrosPorEmpleadoYFecha()`
- **Repository:** `InMemoryRegistroProduccionRepositoryImpl.findByResponsableTurnoIdAndFechaRegistro()`
- **DTOs:** `RegistroProduccionResponseDTO`
- **Modelo:** `RegistroProduccion`
- **Excepciones:** `RecursoNoEncontradoException` (si el empleado no existe)

**Parámetros de ruta:**
- `empleadoId`: ID del empleado responsable
- `fecha`: Fecha para filtrar los registros (formato: yyyy-MM-dd)

**Respuesta exitosa (Response):**
```json
[
  {
    "id": 1,
    "fechaRegistro": "2025-06-01",
    "turno": "MAÑANA",
    "responsableTurnoId": 1,
    "responsableTurnoNombre": "Juan Pérez",
    "toneladasExtraidas": 27.5,
    "frentesTrabajo": "Nivel 1, Sector A y C",
    "observaciones": "Se amplió el frente de trabajo al Sector C",
    "fechaCreacion": "2025-06-01T08:30:15",
    "fechaActualizacion": "2025-06-01T10:15:30"
  }
]
```

**Validaciones a implementar:**
- `empleadoId`: Debe corresponder a un empleado existente
- `fecha`: Debe tener un formato válido (yyyy-MM-dd)

**Códigos de respuesta:**
- 200 (OK): Lista de registros retornada (puede estar vacía)
- 400 (Bad Request): Formato de fecha inválido
- 404 (Not Found): El empleado no existe

**Comportamiento esperado:**
1. Validar el formato de la fecha
2. Verificar la existencia del empleado
3. Buscar registros en el repositorio por empleado y fecha
4. Convertir cada registro a DTO
5. Retornar la lista (puede estar vacía si no hay registros para ese empleado en esa fecha)

## DTOs (Data Transfer Objects)

### RegistroProduccionRequestDTO

```java
public class RegistroProduccionRequestDTO {
    @NotNull(message = "La fecha de registro no puede ser nula")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaRegistro;
    
    @NotNull(message = "El turno no puede ser nulo")
    @Pattern(regexp = "MAÑANA|TARDE|NOCHE", message = "El turno debe ser MAÑANA, TARDE o NOCHE")
    private String turno;
    
    @NotNull(message = "El ID del responsable de turno no puede ser nulo")
    private Long responsableTurnoId;
    
    @NotNull(message = "Las toneladas extraídas no pueden ser nulas")
    @Positive(message = "Las toneladas extraídas deben ser un valor positivo")
    private Double toneladasExtraidas;
    
    @NotBlank(message = "Los frentes de trabajo no pueden estar vacíos")
    private String frentesTrabajo;
    
    private String observaciones;
    
    // Getters y setters
}
```

### RegistroProduccionResponseDTO

```java
public class RegistroProduccionResponseDTO {
    private Long id;
    
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaRegistro;
    
    private String turno;
    private Long responsableTurnoId;
    private String responsableTurnoNombre;
    private Double toneladasExtraidas;
    private String frentesTrabajo;
    private String observaciones;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime fechaCreacion;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime fechaActualizacion;
    
    // Getters y setters
}
```

## Modelo

### RegistroProduccion

```java
public class RegistroProduccion {
    private Long id;
    private LocalDate fechaRegistro;
    private String turno;
    private Long responsableTurnoId;
    private Double toneladasExtraidas;
    private String frentesTrabajo;
    private String observaciones;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;
    
    // Constructores, getters y setters
}
```

## Consideraciones Adicionales

### Validaciones de Negocio

Además de las validaciones básicas en los DTOs, se deben implementar las siguientes validaciones de negocio:

1. **Unicidad de registros**: No debe permitirse más de un registro para la misma combinación de fecha y turno
2. **Verificación de empleado**: Al crear o actualizar un registro, verificar que el empleado responsable exista
3. **Fechas válidas**: No debe permitirse registrar producción en fechas futuras
4. **Validación de turno**: Restringir los valores de turno a "MAÑANA", "TARDE" y "NOCHE"

### Manejo de Errores

Se debe implementar un manejo adecuado de errores, utilizando el manejador global de excepciones ya existente:

- `RecursoNoEncontradoException`: Para registros o empleados que no existen
- `IllegalArgumentException`: Para validaciones de negocio fallidas (duplicados, fechas inválidas, etc.)
- `MethodArgumentNotValidException`: Capturada automáticamente para validaciones de Bean Validation

### Relaciones entre Entidades

La entidad `RegistroProduccion` tiene una relación con `Empleado` a través del campo `responsableTurnoId`:

- Al crear o actualizar un registro, se debe verificar que el empleado exista
- Al retornar un DTO de respuesta, se debe incluir el nombre del empleado responsable
- En futuras implementaciones con bases de datos reales, esta relación se modelará como una clave foránea

### Auditoría

Cada registro debe mantener información de auditoría:

- `fechaCreacion`: Timestamp de creación del registro (se genera automáticamente al crear)
- `fechaActualizacion`: Timestamp de la última actualización (se actualiza automáticamente)

## Resumen de Endpoints

| Método HTTP | Endpoint                                              | Descripción                                    |
|-------------|-------------------------------------------------------|------------------------------------------------|
| POST        | `/api/registros-produccion`                           | Crear nuevo registro de producción             |
| GET         | `/api/registros-produccion/{id}`                      | Obtener registro por ID                        |
| GET         | `/api/registros-produccion`                           | Listar todos los registros                     |
| PUT         | `/api/registros-produccion/{id}`                      | Actualizar registro existente                  |
| DELETE      | `/api/registros-produccion/{id}`                      | Eliminar registro                              |
| GET         | `/api/registros-produccion/fecha/{fecha}`             | Obtener registros por fecha                    |
| GET         | `/api/registros-produccion/empleado/{id}/fecha/{fecha}` | Obtener registros por empleado y fecha       |

# Casos de Uso: Módulo de Empleados

Este documento detalla los casos de uso para el módulo de Empleados de MinaControl Pro, incluyendo la especificación de endpoints, validaciones y comportamiento esperado.

## Introducción

El módulo de Empleados permite gestionar la información del personal que trabaja en la mina, facilitando el registro, consulta, actualización y eliminación de datos de los trabajadores.

## Modelo de Datos

### Entidad `Empleado`

| Atributo        | Tipo      | Descripción                                   | Obligatorio |
|-----------------|-----------|-----------------------------------------------|-------------|
| id              | Long      | Identificador único del empleado              | Sí          |
| nombre          | String    | Nombre completo del empleado                  | Sí          |
| tipoDocumento   | String    | Tipo de documento (CC, CE, etc.)              | Sí          |
| numeroDocumento | String    | Número de documento de identidad              | Sí          |
| fechaNacimiento | LocalDate | Fecha de nacimiento                           | Sí          |
| telefono        | String    | Número telefónico de contacto                 | No          |
| email           | String    | Correo electrónico                            | No          |
| direccion       | String    | Dirección de residencia                       | No          |
| cargo           | String    | Cargo o puesto de trabajo                     | Sí          |
| fechaIngreso    | LocalDate | Fecha de ingreso a la empresa                 | Sí          |
| fechaCreacion   | LocalDateTime | Fecha y hora de creación del registro     | Sí (auto)   |
| fechaActualizacion | LocalDateTime | Fecha y hora de última actualización   | Sí (auto)   |

## Casos de Uso

### CU-EMP-01: Registrar nuevo empleado

**Descripción:** Permite crear un nuevo registro de empleado en el sistema.

**Endpoint:** `POST /api/empleados`

**Implementación actual:**
- **Controller:** `EmpleadoController.crearEmpleado()`
- **Service:** `EmpleadoServiceImpl.crearEmpleado()`
- **Repository:** `InMemoryEmpleadoRepositoryImpl.save()`
- **DTOs:** `EmpleadoRequestDTO`, `EmpleadoResponseDTO`
- **Modelo:** `Empleado`

**Cuerpo de la solicitud (Request):**
```json
{
  "nombre": "Juan Pérez",
  "tipoDocumento": "CC",
  "numeroDocumento": "1098765432",
  "fechaNacimiento": "1990-05-15",
  "telefono": "3101234567",
  "email": "juan.perez@example.com",
  "direccion": "Calle 123, Socha, Boyacá",
  "cargo": "Minero",
  "fechaIngreso": "2024-01-10"
}
```

**Respuesta exitosa (Response):**
```json
{
  "id": 1,
  "nombre": "Juan Pérez",
  "tipoDocumento": "CC",
  "numeroDocumento": "1098765432",
  "fechaNacimiento": "1990-05-15",
  "telefono": "3101234567",
  "email": "juan.perez@example.com",
  "direccion": "Calle 123, Socha, Boyacá",
  "cargo": "Minero",
  "fechaIngreso": "2024-01-10",
  "fechaCreacion": "2025-06-01T08:30:15",
  "fechaActualizacion": "2025-06-01T08:30:15"
}
```

**Validaciones implementadas:**
- `nombre`: No debe ser nulo ni estar vacío
- `tipoDocumento`: Debe ser un valor válido (CC, CE, etc.)
- `numeroDocumento`: No debe ser nulo ni estar vacío, debe ser único en el sistema
- `fechaNacimiento`: No debe ser nula, debe corresponder a una persona mayor de edad
- `email`: Si se proporciona, debe tener un formato válido
- `cargo`: No debe ser nulo ni estar vacío
- `fechaIngreso`: No debe ser nula ni posterior a la fecha actual

**Códigos de respuesta:**
- 201 (Created): Empleado creado exitosamente
- 400 (Bad Request): Datos inválidos o incompletos
- 409 (Conflict): Ya existe un empleado con el mismo número de documento

**Comportamiento esperado:**
1. Validar los datos de entrada
2. Verificar que no exista otro empleado con el mismo número de documento
3. Crear el empleado en el repositorio
4. Retornar el empleado creado con el ID asignado

### CU-EMP-02: Consultar empleado por ID

**Descripción:** Permite obtener la información de un empleado específico mediante su identificador.

**Endpoint:** `GET /api/empleados/{id}`

**Implementación actual:**
- **Controller:** `EmpleadoController.obtenerEmpleadoPorId()`
- **Service:** `EmpleadoServiceImpl.obtenerEmpleadoPorId()`
- **Repository:** `InMemoryEmpleadoRepositoryImpl.findById()`
- **DTOs:** `EmpleadoResponseDTO`
- **Modelo:** `Empleado`
- **Excepciones:** `RecursoNoEncontradoException`

**Parámetros de ruta:**
- `id`: ID del empleado a consultar

**Respuesta exitosa (Response):**
```json
{
  "id": 1,
  "nombre": "Juan Pérez",
  "tipoDocumento": "CC",
  "numeroDocumento": "1098765432",
  "fechaNacimiento": "1990-05-15",
  "telefono": "3101234567",
  "email": "juan.perez@example.com",
  "direccion": "Calle 123, Socha, Boyacá",
  "cargo": "Minero",
  "fechaIngreso": "2024-01-10",
  "fechaCreacion": "2025-06-01T08:30:15",
  "fechaActualizacion": "2025-06-01T08:30:15"
}
```

**Códigos de respuesta:**
- 200 (OK): Empleado encontrado y devuelto
- 404 (Not Found): Empleado no encontrado

**Comportamiento esperado:**
1. Buscar el empleado en el repositorio por su ID
2. Si no existe, lanzar `RecursoNoEncontradoException`
3. Si existe, convertir a DTO y retornar

### CU-EMP-03: Listar todos los empleados

**Descripción:** Permite obtener la lista completa de empleados registrados en el sistema.

**Endpoint:** `GET /api/empleados`

**Implementación actual:**
- **Controller:** `EmpleadoController.obtenerTodosLosEmpleados()`
- **Service:** `EmpleadoServiceImpl.obtenerTodosLosEmpleados()`
- **Repository:** `InMemoryEmpleadoRepositoryImpl.findAll()`
- **DTOs:** `EmpleadoResponseDTO`
- **Modelo:** `Empleado`

**Respuesta exitosa (Response):**
```json
[
  {
    "id": 1,
    "nombre": "Juan Pérez",
    "tipoDocumento": "CC",
    "numeroDocumento": "1098765432",
    "fechaNacimiento": "1990-05-15",
    "telefono": "3101234567",
    "email": "juan.perez@example.com",
    "direccion": "Calle 123, Socha, Boyacá",
    "cargo": "Minero",
    "fechaIngreso": "2024-01-10",
    "fechaCreacion": "2025-06-01T08:30:15",
    "fechaActualizacion": "2025-06-01T08:30:15"
  },
  {
    "id": 2,
    "nombre": "María López",
    "tipoDocumento": "CC",
    "numeroDocumento": "1087654321",
    "fechaNacimiento": "1992-08-20",
    "telefono": "3157894561",
    "email": "maria.lopez@example.com",
    "direccion": "Vereda El Mortiño, Socha, Boyacá",
    "cargo": "Supervisora",
    "fechaIngreso": "2023-11-15",
    "fechaCreacion": "2025-06-01T09:15:30",
    "fechaActualizacion": "2025-06-01T09:15:30"
  }
]
```

**Códigos de respuesta:**
- 200 (OK): Lista de empleados retornada (puede estar vacía)

**Comportamiento esperado:**
1. Obtener todos los empleados del repositorio
2. Convertir cada empleado a DTO
3. Retornar la lista completa

### CU-EMP-04: Actualizar información de empleado

**Descripción:** Permite modificar los datos de un empleado existente en el sistema.

**Endpoint:** `PUT /api/empleados/{id}`

**Implementación actual:**
- **Controller:** `EmpleadoController.actualizarEmpleado()`
- **Service:** `EmpleadoServiceImpl.actualizarEmpleado()`
- **Repository:** `InMemoryEmpleadoRepositoryImpl.update()`
- **DTOs:** `EmpleadoRequestDTO`, `EmpleadoResponseDTO`
- **Modelo:** `Empleado`
- **Excepciones:** `RecursoNoEncontradoException`

**Parámetros de ruta:**
- `id`: ID del empleado a actualizar

**Cuerpo de la solicitud (Request):**
```json
{
  "nombre": "Juan Carlos Pérez",
  "tipoDocumento": "CC",
  "numeroDocumento": "1098765432",
  "fechaNacimiento": "1990-05-15",
  "telefono": "3101234567",
  "email": "juancarlos.perez@example.com",
  "direccion": "Calle 456, Socha, Boyacá",
  "cargo": "Minero Senior",
  "fechaIngreso": "2024-01-10"
}
```

**Respuesta exitosa (Response):**
```json
{
  "id": 1,
  "nombre": "Juan Carlos Pérez",
  "tipoDocumento": "CC",
  "numeroDocumento": "1098765432",
  "fechaNacimiento": "1990-05-15",
  "telefono": "3101234567",
  "email": "juancarlos.perez@example.com",
  "direccion": "Calle 456, Socha, Boyacá",
  "cargo": "Minero Senior",
  "fechaIngreso": "2024-01-10",
  "fechaCreacion": "2025-06-01T08:30:15",
  "fechaActualizacion": "2025-06-01T10:45:20"
}
```

**Validaciones implementadas:**
- `nombre`: No debe ser nulo ni estar vacío
- `tipoDocumento`: Debe ser un valor válido (CC, CE, etc.)
- `numeroDocumento`: No debe ser nulo ni estar vacío, debe ser único en el sistema (excluyendo el empleado actual)
- `fechaNacimiento`: No debe ser nula, debe corresponder a una persona mayor de edad
- `email`: Si se proporciona, debe tener un formato válido
- `cargo`: No debe ser nulo ni estar vacío
- `fechaIngreso`: No debe ser nula ni posterior a la fecha actual

**Códigos de respuesta:**
- 200 (OK): Empleado actualizado exitosamente
- 400 (Bad Request): Datos inválidos o incompletos
- 404 (Not Found): El empleado a actualizar no existe
- 409 (Conflict): Ya existe otro empleado con el mismo número de documento

**Comportamiento esperado:**
1. Validar los datos de entrada
2. Verificar la existencia del empleado a actualizar
3. Verificar que no exista otro empleado con el mismo número de documento (excluyendo el actual)
4. Actualizar el empleado en el repositorio
5. Actualizar la fecha de actualización
6. Retornar el empleado actualizado

### CU-EMP-05: Eliminar empleado

**Descripción:** Permite eliminar un empleado del sistema.

**Endpoint:** `DELETE /api/empleados/{id}`

**Implementación actual:**
- **Controller:** `EmpleadoController.eliminarEmpleado()`
- **Service:** `EmpleadoServiceImpl.eliminarEmpleado()`
- **Repository:** `InMemoryEmpleadoRepositoryImpl.deleteById()`
- **Excepciones:** `RecursoNoEncontradoException`

**Parámetros de ruta:**
- `id`: ID del empleado a eliminar

**Códigos de respuesta:**
- 204 (No Content): Empleado eliminado exitosamente
- 404 (Not Found): El empleado no existe

**Comportamiento esperado:**
1. Verificar la existencia del empleado a eliminar
2. Si no existe, lanzar `RecursoNoEncontradoException`
3. Si existe, eliminarlo del repositorio
4. Retornar respuesta sin contenido

## DTOs (Data Transfer Objects)

### EmpleadoRequestDTO

```java
public class EmpleadoRequestDTO {
    @NotBlank(message = "El nombre no puede estar vacío")
    private String nombre;
    
    @NotBlank(message = "El tipo de documento no puede estar vacío")
    private String tipoDocumento;
    
    @NotBlank(message = "El número de documento no puede estar vacío")
    private String numeroDocumento;
    
    @NotNull(message = "La fecha de nacimiento no puede ser nula")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaNacimiento;
    
    private String telefono;
    
    @Email(message = "El formato del email no es válido")
    private String email;
    
    private String direccion;
    
    @NotBlank(message = "El cargo no puede estar vacío")
    private String cargo;
    
    @NotNull(message = "La fecha de ingreso no puede ser nula")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @PastOrPresent(message = "La fecha de ingreso no puede ser futura")
    private LocalDate fechaIngreso;
    
    // Getters y setters
}
```

### EmpleadoResponseDTO

```java
public class EmpleadoResponseDTO {
    private Long id;
    private String nombre;
    private String tipoDocumento;
    private String numeroDocumento;
    
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaNacimiento;
    
    private String telefono;
    private String email;
    private String direccion;
    private String cargo;
    
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaIngreso;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime fechaCreacion;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime fechaActualizacion;
    
    // Getters y setters
}
```

## Modelo

### Empleado

```java
public class Empleado {
    private Long id;
    private String nombre;
    private String tipoDocumento;
    private String numeroDocumento;
    private LocalDate fechaNacimiento;
    private String telefono;
    private String email;
    private String direccion;
    private String cargo;
    private LocalDate fechaIngreso;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;
    
    // Constructores, getters y setters
}
```

## Consideraciones Adicionales

### Validaciones de Negocio

Además de las validaciones básicas en los DTOs, se implementan las siguientes validaciones de negocio:

1. **Unicidad de documento**: No debe permitirse más de un empleado con el mismo número de documento
2. **Mayoría de edad**: Verificar que los empleados sean mayores de edad (18 años o más)
3. **Fechas válidas**: La fecha de ingreso no debe ser posterior a la fecha actual
4. **Validación de correo**: Si se proporciona un correo electrónico, debe tener un formato válido

### Manejo de Errores

Se implementa un manejo adecuado de errores, utilizando el manejador global de excepciones:

- `RecursoNoEncontradoException`: Para empleados que no existen
- `IllegalArgumentException`: Para validaciones de negocio fallidas (duplicados, fechas inválidas, etc.)
- `MethodArgumentNotValidException`: Capturada automáticamente para validaciones de Bean Validation

### Auditoría

Cada registro debe mantener información de auditoría:

- `fechaCreacion`: Timestamp de creación del registro (se genera automáticamente al crear)
- `fechaActualizacion`: Timestamp de la última actualización (se actualiza automáticamente)

## Resumen de Endpoints

| Método HTTP | Endpoint                 | Descripción                         |
|-------------|--------------------------|-------------------------------------|
| POST        | `/api/empleados`         | Crear nuevo empleado                |
| GET         | `/api/empleados/{id}`    | Obtener empleado por ID            |
| GET         | `/api/empleados`         | Listar todos los empleados          |
| PUT         | `/api/empleados/{id}`    | Actualizar empleado existente       |
| DELETE      | `/api/empleados/{id}`    | Eliminar empleado                   |

# Instrucciones para GitHub Copilot - Proyecto MinaControl Pro

## 🌍 Configuración de Idioma y Terminología

**Responder SIEMPRE en español.** 

### Convenciones de Codificación:
- **Código de dominio en español:** Clases de negocio, métodos, variables de dominio, comentarios
- **Términos técnicos en inglés:** Paquetes de capas (`controller`, `service`, `repository`, `dto`, `model`), métodos HTTP, códigos de estado, términos de frameworks
- **Getters/Setters:** Mantener convención JavaBeans (`getNombre()`, `setNombre()`)

### Ejemplos de Aplicación:
```java
@RestController
@RequestMapping("/api/empleados")
public class EmpleadoController {
    
    private final EmpleadoService empleadoService;
    
    public EmpleadoController(EmpleadoService empleadoService) {
        this.empleadoService = empleadoService;
    }
    
    @PostMapping
    public ResponseEntity<EmpleadoResponseDTO> crearEmpleado(@Valid @RequestBody EmpleadoRequestDTO request) {
        EmpleadoResponseDTO nuevoEmpleado = empleadoService.crearEmpleado(request);
        return new ResponseEntity<>(nuevoEmpleado, HttpStatus.CREATED);
    }
}
```

## 1. Resumen del Proyecto

- **Nombre del Proyecto:** MinaControl Pro
- **Objetivo:** API REST backend para gestionar operaciones en una mina de carbón en Socha, Boyacá, Colombia
- **Fase Actual:** MVP con 7 dominios funcionales completos
- **Enfoque:** Solo extracción de carbón, gestión simplificada de conductores/vehículos
- **Estado:** Dominio Empleado completamente implementado con tests

## 2. Entorno de Desarrollo

- **IDE:** Visual Studio Code
- **Sistema Operativo:** WSL Ubuntu (Ubuntu-24.04)
- **Java:** OpenJDK 17
- **Build Tool:** Maven 3.8+
- **Base de Datos:** H2 (desarrollo), PostgreSQL (producción futura)

## 3. Tecnologías y Dependencias Actuales

- **Framework:** Spring Boot 2.7.18
- **Web:** `spring-boot-starter-web`
- **Base de Datos:** H2 Database (en memoria para desarrollo)
- **Validación:** `spring-boot-starter-validation`
- **Testing:** `spring-boot-starter-test`
- **Java:** 17
- **Sin Lombok:** Getters/Setters manuales por simplicidad y claridad

## 4. Arquitectura del Software

### Estructura de Paquetes (`src/main/java/com/minacontrol`)

```text
com.minacontrol/
├── MinaControlApiApplication.java
├── config/                    # Configuración global (futura)
├── exception/                 # Manejo global de excepciones
├── empleado/                  # Dominio 1: Empleados (COMPLETADO)
│   ├── controller/           # EmpleadoController
│   ├── dto/                  # EmpleadoRequestDTO, EmpleadoResponseDTO
│   ├── model/                # Empleado, EstadoEmpleado
│   ├── repository/           # EmpleadoRepository + InMemoryImpl
│   └── service/              # EmpleadoService + EmpleadoServiceImpl
├── turno/                     # Dominio 2: Turnos (PENDIENTE)
├── produccion/                # Dominio 3: Producción (PENDIENTE)
├── logistica/                 # Dominio 4: Logística/Despachos (PENDIENTE)
├── nomina/                    # Dominio 5: Nómina (PENDIENTE)
├── reporte/                   # Dominio 6: Reportes (PENDIENTE)
└── auth/                      # Dominio 0: Autenticación (FUTURO)
```

### Principios de Arquitectura

- **Arquitectura por Dominios:** Cada dominio es completamente independiente
- **Inyección por Constructor:** Obligatoria en todos los servicios y controladores
- **DTOs Obligatorios:** Nunca exponer entidades JPA directamente
- **Interfaces para Servicios:** Separar contrato de implementación
- **Manejo Centralizado de Excepciones:** `@ControllerAdvice` global
- **Tests Completos:** Unitarios (service) e integración (controller)

## 5. Dominios y Entidades Principales

### Dominio 1: Empleados (IMPLEMENTADO)

- **Empleado:** Información personal, cargo, salario, estado
- **Estados:** ACTIVO, INACTIVO
- **Campos principales:** numeroDocumento, nombres, apellidos, fechaNacimiento, cargo, fechaContratacion, salario, estado, email, telefono

### Dominio 2: Turnos (PENDIENTE)

- **TipoTurno:** Definición de horarios
- **AsignacionTurno:** Asignación empleado-turno
- **RegistroAsistencia:** Control de entrada/salida diaria

### Dominio 3: Producción (PENDIENTE)

- **RegistroProduccion:** Solo carbón en toneladas
- **Campos:** empleadoId, tipoTurnoId, fechaRegistro, cantidadExtraidaToneladas, ubicacionExtraccion

### Dominio 4: Logística/Despachos (PENDIENTE)

- **Despacho:** Salida de carbón con datos simples de conductor/vehículo
- **Campos inline:** nombreConductor, placaVehiculo, cantidadDespachadaToneladas

### Dominio 5: Nómina (PENDIENTE)

- **PeriodoNomina:** Periodos semanales de pago
- **CalculoNomina:** Cálculo basado en asistencia y producción
- **ComprobantePago:** Documentos PDF de pago

### Dominio 6: Reportes (PENDIENTE)

- **ReporteGenerado:** Reportes de producción, asistencia, costos laborales

## 6. Restricciones del MVP

- **Material:** Solo carbón (no múltiples materiales)
- **Conductores/Vehículos:** Datos simples (no entidades separadas)
- **Pagos:** Solo sábados
- **Base de Datos:** H2 en memoria (desarrollo), PostgreSQL (producción futura)
- **Autenticación:** Diferida hasta completar dominios core
- **Turnos:** Un turno básico por empleado
- **Reportes:** Solo PDF/Excel

## 7. Estándares de Codificación Obligatorios

### Inyección de Dependencias

```java
// ✅ CORRECTO: Constructor injection
@RestController
public class EmpleadoController {
    private final EmpleadoService empleadoService;
    
    public EmpleadoController(EmpleadoService empleadoService) {
        this.empleadoService = empleadoService;
    }
}

// ❌ INCORRECTO: Field injection
@Autowired
private EmpleadoService empleadoService;
```

### DTOs y Validación

```java
// ✅ CORRECTO: DTO con validaciones
public class EmpleadoRequestDTO {
    @NotBlank(message = "El nombre no puede estar vacío")
    private String nombre;
    
    @NotNull(message = "La fecha de contratación no puede ser nula")
    @PastOrPresent(message = "La fecha de contratación debe ser una fecha pasada o presente")
    private LocalDate fechaContratacion;
}

// Endpoint con validación
@PostMapping
public ResponseEntity<EmpleadoResponseDTO> crearEmpleado(@Valid @RequestBody EmpleadoRequestDTO request) {
    // ...
}
```

### Manejo de Excepciones

```java
// ✅ CORRECTO: Excepción personalizada
public class RecursoNoEncontradoException extends RuntimeException {
    public RecursoNoEncontradoException(String message) {
        super(message);
    }
}

// ✅ CORRECTO: Manejador global
@ControllerAdvice
public class ManejadorExcepcionesGlobal {
    @ExceptionHandler(RecursoNoEncontradoException.class)
    public ResponseEntity<Map<String, Object>> manejarExcepcionRecursoNoEncontrado(RecursoNoEncontradoException ex) {
        // ...
    }
}
```

### Otros Estándares

- **DTOs obligatorios:** No exponer entidades JPA nunca
- **Interfaces:** Para servicios y repositorios siempre
- **Naming:** Métodos y clases de dominio en español
- **Tests:** `@WebMvcTest` para controllers, `@ExtendWith(MockitoExtension.class)` para services

## 8. Flujo de Desarrollo por Módulo

**Seguir EXACTAMENTE este orden para cada nuevo dominio:**

1. **Entidad JPA** (`model/`) con campos según casos de uso
2. **DTOs** (`dto/`) - Request/Response separados con validaciones
3. **Repository** (`repository/`) - Interface Spring Data JPA + InMemory implementation
4. **Service** (`service/`) - Interface + Implementation con inyección por constructor
5. **Controller** (`controller/`) - Endpoints REST con validación `@Valid`
6. **Tests** - Unitarios (service) e integración (controller) con coverage completo

## 9. Patrones Implementados (Base: Dominio Empleado)

- **Repository Pattern:** `EmpleadoRepository` interface + `InMemoryEmpleadoRepositoryImpl`
- **Service Layer:** `EmpleadoService` interface + `EmpleadoServiceImpl`
- **DTO Pattern:** `EmpleadoRequestDTO` / `EmpleadoResponseDTO`
- **Global Exception Handling:** `ManejadorExcepcionesGlobal` con `@ControllerAdvice`
- **Bean Validation:** Validaciones en DTOs usando anotaciones estándar
- **Constructor Injection:** En todos los servicios y controladores

## 10. Directrices para GitHub Copilot

### OBLIGATORIO

- **Seguir patrones del dominio Empleado:** Es la referencia de implementación
- **Inyección por constructor:** Siempre, nunca `@Autowired` en fields
- **DTOs con validaciones:** Bean Validation en todos los DTOs
- **Tests completos:** Unitarios e integración para cada clase
- **Excepciones en español:** Mensajes de error claros para usuarios

### PROHIBIDO

- **Exponer entidades JPA:** Solo DTOs en controllers
- **Field injection:** Solo constructor injection
- **Agregar dependencias:** Solo usar las definidas en pom.xml
- **Código sin tests:** Todo código debe tener tests

### REFERENCIAR

- **Casos de uso:** Documentación en `docs/casos_de_uso/`
- **Diagramas:** Arquitectura en `docs/diagrams/general/`
- **Código existente:** Patrones en dominio `empleado/`

### IMPLEMENTAR

- **Coherencia:** Seguir naming y estructura del código existente
- **Validación:** Implementar en DTOs y usar `@Valid` en controllers
- **Manejo de errores:** Usar `RecursoNoEncontradoException` y manejador global

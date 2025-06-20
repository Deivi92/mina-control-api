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

## 1.1. Metodología de Desarrollo Progresivo

**MinaControl Pro se construye siguiendo una metodología PROGRESIVA y SECUENCIAL donde cada fase se fundamenta en la anterior, garantizando coherencia arquitectónica y evitando retrocesos:**

### 🏗️ Fases de Construcción (ORDEN OBLIGATORIO)

#### **Fase 1: Fundación Conceptual** ✅ COMPLETADA

1. **Casos de Uso de Alto Nivel** (`docs/casos_de_uso/casos_de_uso_alto_nivel.md`)
   - Define TODOS los dominios, roles y flujos del sistema
   - Establece la base funcional completa
   - **Base para:** Diagramas de arquitectura y diseño de base de datos

2. **Diagrama Entidad-Relación Completo** (`docs/diagrams/general/er_diagram_completo.puml`)
   - Diseña TODA la estructura de base de datos
   - Normaliza relaciones entre dominios
   - **Base para:** Diagramas de clases y entidades JPA

3. **Diagrama de Clases Completo** (`docs/diagrams/general/class_diagram_completo.puml`)
   - Define TODA la estructura de código Java
   - Establece DTOs, servicios, controllers y repositorios
   - **Base para:** Implementación de código actual y futura

4. **Diagrama de Arquitectura General** (`docs/diagrams/general/architecture_overview.puml`)
   - Define la arquitectura completa del sistema
   - Establece comunicación entre capas y componentes
   - **Base para:** Implementación de cada dominio

#### **Fase 2: Implementación por Dominios** 🚧 EN CURSO

1. **Dominio Empleado** ✅ COMPLETADA
   - Implementación completa con tests
   - **Patrón de referencia** para todos los demás dominios

2. **Próximos dominios** (siguiendo el patrón de Empleado):
   - Turnos → Producción → Nómina → Logística → Reportes → Autenticación

#### **Fase 3: Integración y Refinamiento** 📋 PLANIFICADA

- Integración entre dominios
- Optimizaciones transversales
- Frontend React (futuro)

### 🎯 Principios de la Metodología

1. **NO RETROCEDER**: Cada fase completa es inmutable y sirve de base
2. **COHERENCIA TOTAL**: Código debe seguir EXACTAMENTE los diagramas
3. **PROGRESIÓN ORDENADA**: No saltar fases ni implementar fuera de orden
4. **DOCUMENTACIÓN PRIMERO**: Casos de uso → Diagramas → Código
5. **DOMINIO EMPLEADO = PATRÓN**: Replicar su estructura en todos los dominios

### 📋 Estado Actual del Proyecto

```text
✅ Casos de Uso Alto Nivel      (Fundación funcional completa)
✅ Diagrama ER Completo         (Base de datos completa) 
✅ Diagrama Clases Completo     (Arquitectura de código completa)
✅ Diagrama Arquitectura        (Comunicación entre componentes)
✅ Dominio Empleado             (Patrón de implementación establecido)
🚧 Próximo: Dominio Turnos      (Siguiendo patrón de Empleado)
```

### ⚠️ REGLAS CRÍTICAS para GitHub Copilot

1. **SIEMPRE CONSULTAR** casos de uso antes de implementar funcionalidad
2. **SIEMPRE SEGUIR** los diagramas ER y de clases existentes
3. **SIEMPRE REPLICAR** el patrón del dominio Empleado
4. **NUNCA MODIFICAR** fases completadas sin justificación arquitectónica
5. **NUNCA IMPLEMENTAR** funcionalidad no definida en casos de uso

**Esta metodología garantiza un desarrollo profesional, escalable y sin contradicciones arquitectónicas.**

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

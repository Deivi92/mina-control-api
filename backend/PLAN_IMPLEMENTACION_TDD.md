# PLAN DE IMPLEMENTACIÓN TDD: CONFIGURACIÓN DINÁMICA DE TARIFAS PARA MINERÍA COLOMBIANA

## OBJETIVO
Implementar un sistema flexible que permita a los administradores configurar tarifas monetarias (salario por hora, bonos, etc.) sin necesidad de modificar código, cumpliendo con las regulaciones mineras colombianas.

## METODOLOGÍA TDD
1. Escribir pruebas unitarias que definan el comportamiento esperado
2. Implementar el código mínimo necesario para pasar las pruebas
3. Refactorizar manteniendo las pruebas en verde
4. Repetir hasta completar la funcionalidad

## TODO LIST - PASO A PASO

### FASE 1: ESTRUCTURA DE DATOS Y ENTIDADES

#### 1.1 Crear entidad ConfiguracionTarifas
- [x] Crear clase `ConfiguracionTarifas` en `com.minacontrol.nomina.entity`
- [x] Agregar campos: tarifaPorHora, bonoPorTonelada, moneda, fechaVigencia
- [x] Agregar campos de auditoría: creadoPor, fechaCreacion
- [x] Anotar con JPA y Lombok

#### 1.2 Crear repositorio
- [x] Crear interfaz `ConfiguracionTarifasRepository` extendiendo `JpaRepository`
- [x] Agregar método para obtener configuración vigente

### FASE 2: SERVICIO Y LÓGICA DE NEGOCIO

#### 2.1 Crear interfaz de servicio
- [x] Crear `IConfiguracionTarifasService`
- [x] Definir métodos: guardarConfiguracion, obtenerConfiguracionVigente

#### 2.2 Crear implementación
- [x] Crear `ConfiguracionTarifasServiceImpl`
- [x] Implementar lógica de negocio
- [x] Agregar seguridad: solo ADMIN puede modificar

### FASE 3: INTEGRACIÓN CON NÓMINA

#### 3.1 Modificar NominaService
- [x] Inyectar `ConfiguracionTarifasService`
- [x] Reemplazar valores hardcoded por configuración dinámica
- [x] Actualizar cálculos de salario y bonificaciones

#### 3.2 Actualizar pruebas unitarias de Nómina
- [x] Modificar `NominaServiceTest` para usar mocks de configuración
- [x] Agregar pruebas para diferentes configuraciones

### FASE 4: CONTROLADOR Y API REST

#### 4.1 Crear controlador
- [x] Crear `ConfiguracionTarifasController`
- [x] Endpoints: GET /configuracion/tarifas/vigentes, POST /configuracion/tarifas
- [x] Agregar seguridad con @PreAuthorize

#### 4.2 Actualizar DTOs
- [x] Crear `ConfiguracionTarifasDTO`
- [x] Crear `ConfiguracionTarifasCreateDTO`
- [x] Crear mappers correspondientes

### FASE 5: PRUEBAS DE INTEGRACIÓN

#### 5.1 Pruebas de API
- [x] Crear `ConfiguracionTarifasControllerIT`
- [x] Probar endpoints con diferentes roles
- [x] Verificar seguridad y permisos

#### 5.2 Pruebas de integración con Nómina
- [x] Crear `NominaConConfiguracionTarifasIT`
- [x] Verificar que los cálculos usan configuración dinámica

### FASE 6: REPORTES Y AUDITORÍA

#### 6.1 Actualizar reportes
- [x] Modificar `ReporteServiceImpl` para incluir información de tarifas
- [x] Actualizar formato de reportes con moneda COP

#### 6.2 Auditoría
- [x] Agregar registro de cambios en configuración
- [x] Crear endpoint para historial de cambios

### FASE 7: CONFIGURACIÓN INICIAL

#### 7.1 Datos de prueba
- [x] Crear data inicial con tarifas colombianas típicas
- [x] Script de inicialización para entorno dev

#### 7.2 Documentación
- [ ] Actualizar OpenAPI/Swagger
- [ ] Documentar nuevos endpoints

## PRUEBAS UNITARIAS ESPECÍFICAS REQUERIDAS

### ConfiguracionTarifasServiceTest
- [x] should_GuardarConfiguracion_When_AdminUser()
- [x] should_ThrowException_When_NonAdminUser()
- [x] should_ObtenerConfiguracionVigente()
- [x] should_ReturnDefaultConfig_When_NoConfigExists()

### NominaServiceTest (actualizadas)
- [x] should_UsarTarifasConfiguradas_When_CalcularNomina()
- [x] should_CalcularSalarioConTarifaDinamica()
- [x] should_AplicarBonoConfigurado()

## CRITERIOS DE ACEPTACIÓN

1. Solo usuarios con rol ADMIN pueden modificar tarifas
2. Todos los usuarios pueden consultar tarifas vigentes
3. Los cálculos de nómina usan tarifas configuradas
4. Los reportes muestran moneda en COP
5. Se mantiene historial de cambios
6. El sistema funciona con valores colombianos típicos
7. Todas las pruebas pasan (unitarias e integración)

## VALORES DE REFERENCIA PARA COLOMBIA (2025)

- Salario mínimo mensual: $1,300,000 COP
- Salario hora (8 horas/día): ~$6,823 COP
- Bono por tonelada (variable según mineral): $8,000-$15,000 COP

## ENTREGABLES FINALES

1. Código implementado y testeado
2. Pruebas unitarias e integración
3. Documentación de API actualizada
4. Guía de uso para administradores
5. Migración de datos existentes (si aplica)

## SIGUIENTE PASO
Todos los elementos del plan han sido implementados. El siguiente paso sería crear una guía de uso para administradores y actualizar la documentación de la API.
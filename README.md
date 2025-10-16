# MinaControl Pro - Sistema de Gestión Minera

<div align="center">

[![Java](https://img.shields.io/badge/Java-17-ED8B00?style=for-the-badge&logo=java&logoColor=white)](https://www.java.com/)
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.2.0-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)](https://spring.io/projects/spring-boot)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-316192?style=for-the-badge&logo=postgresql&logoColor=white)](https://www.postgresql.org/)
[![JWT](https://img.shields.io/badge/JWT-000000?style=for-the-badge&logo=JSON%20web%20tokens&logoColor=white)](https://jwt.io/)
[![React](https://img.shields.io/badge/React-20232A?style=for-the-badge&logo=react&logoColor=61DAFB)](https://reactjs.org/)
[![TypeScript](https://img.shields.io/badge/TypeScript-007ACC?style=for-the-badge&logo=typescript&logoColor=white)](https://www.typescriptlang.org/)

</div>

## 🚀 Descripción del Proyecto

**MinaControl Pro** es un sistema integral de gestión para operaciones mineras desarrollado con una arquitectura de pila completa (full-stack). El backend está construido con **Java y Spring Boot**, siguiendo arquitectura en capas, patrones de diseño y buenas prácticas de desarrollo. Este proyecto demuestra mi experiencia y habilidades como desarrollador backend Java, implementando soluciones robustas y escalables para escenarios empresariales reales.

> 🎯 **Proyecto desarrollado como portafolio profesional para demostrar experiencia en tecnologías Java/Spring Boot**

## ✨ Características Principales

- **Backend RESTful API**: Desarrollado con Spring Boot, Java 17 y principios de arquitectura limpias
- **Sistema de Autenticación Segura**: JWT (JSON Web Tokens) con seguridad Spring Security
- **Gestión Completa de Dominios**: Empleados, Turnos, Producción, Logística, Nómina y Reportes
- **Arquitectura en Capas**: Separación clara de responsabilidades (Controller, Service, Repository)
- **Persistencia de Datos**: JPA/Hibernate con PostgreSQL como base de datos principal
- **Pruebas Automatizadas**: Implementación de TDD/BDD con pruebas unitarias e integración
- **Documentación de API**: Swagger/OpenAPI integrado para documentación interactiva
- **Frontend Moderno**: React + TypeScript para una experiencia de usuario fluida
- **Casos de Uso Documentados**: Extensa documentación de casos de uso funcionales
- **Diagramas UML**: Representación visual de la arquitectura, clases y flujos del sistema

## 🛠️ Tecnologías y Herramientas

### Backend
- **Java 17**: Lenguaje principal del proyecto
- **Spring Boot 3.2.0**: Framework para desarrollo rápido y empotrado
- **Spring Security**: Autenticación y autorización
- **Spring Data JPA**: Acceso a datos con Hibernate
- **PostgreSQL**: Base de datos relacional principal
- **H2 Database**: Base de datos en memoria para pruebas
- **JWT**: Tokens web JSON para autenticación stateless
- **MapStruct**: Mapeo automático de objetos (DTOs)
- **Lombok**: Reducción de código boilerplate
- **SpringDoc OpenAPI**: Documentación automática de API
- **JUnit 5 & Mockito**: Pruebas unitarias e integración
- **Maven**: Gestión de dependencias y construcción de proyecto

### Frontend
- **React 19**: Biblioteca JavaScript para interfaces de usuario
- **TypeScript**: Tipado estático para mayor seguridad
- **Material UI**: Biblioteca de componentes de interfaz
- **React Hook Form**: Gestión de formularios
- **React Router DOM**: Navegación entre vistas
- **Vite**: Build tool moderno para desarrollo rápido
- **ESLint & Prettier**: Estilo y calidad de código

### Prueba y Calidad
- **TDD / BDD**: Desarrollo guiado por pruebas
- **Pruebas Unitarias**: Cobertura para lógica de negocio
- **Pruebas de Integración**: Validación de flujos completos
- **Validaciones**: Implementación de validaciones de entrada
- **Excepciones Personalizadas**: Manejo robusto de errores

## 📊 Dominios de Negocio Implementados

1. **Autenticación**: Registro, login, tokens JWT, recuperación de contraseñas
2. **Empleados**: Gestión completa de información de personal
3. **Turnos**: Asignación de horarios y control de asistencia
4. **Producción**: Registro de actividades mineras
5. **Logística**: Gestión de despachos y recursos
6. **Nómina**: Cálculo de salarios y generación de comprobantes
7. **Reportes**: Generación de reportes operacionales
8. **Seguridad**: Control de acceso y roles de usuario

## 📋 Documentación Técnica Detallada

El proyecto incluye una documentación extensa que demuestra un desarrollo profesional estructurado:

### Casos de Uso
- **Casos de Uso de Alto Nivel**: Arquitectura funcional del sistema
- **Casos de Uso de Bajo Nivel**: Implementaciones detalladas para cada dominio
- **Flujos de Seguridad y Autorización**: Control de acceso por roles
- **Precondiciones y Validaciones**: Especificaciones claras de cada operación

### Diagramas UML
- **Diagramas Entidad-Relación (ER)**: Estructura de la base de datos
- **Diagramas de Clases**: Arquitectura de objetos y relaciones
- **Diagramas de Secuencia**: Flujos de operaciones en cada dominio
- **Diagramas Generales**: Vista de arquitectura del sistema completo

## 🚀 Cómo Ejecutar el Proyecto

### Backend (Java Spring Boot)

```bash
# Navegar al directorio del backend
cd backend

# Ejecutar con Maven (desarrollo)
./mvnw spring-boot:run

# O con perfiles (desarrollo sin seguridad)
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
```

### Frontend (React + TypeScript)

```bash
# Navegar al directorio del frontend
cd frontend

# Instalar dependencias
npm install

# Ejecutar en modo desarrollo
npm run dev
```

## 👨‍💻 Experiencia Demostrada

Este proyecto es una demostración práctica de mis habilidades como desarrollador backend Java, particularmente en:

- **Diseño y arquitectura de APIs RESTful**
- **Implementación de buenas prácticas de desarrollo orientado a objetos**
- **Conocimiento profundo del framework Spring y su ecosistema**
- **Implementación de capas de seguridad robustas**
- **Gestión de persistencia de datos con JPA/Hibernate**
- **Desarrollo orientado a pruebas (TDD/BDD)**
- **Uso de herramientas modernas de desarrollo**
- **Gestión de excepciones y validaciones de datos**
- **Mapeo de objetos con MapStruct y reducción de código con Lombok**
- **Documentación y modelado de sistemas con UML**
- **Gestión de flujos de trabajo complejos con casos de uso detallados**

## 📈 Impacto y Aprendizaje

Este proyecto representa un hito importante en mi experiencia como desarrollador backend, demostrando la capacidad de:

- **Diseñar y desarrollar sistemas empresariales completos**
- **Aplicar principios de arquitectura limpias y patrones de diseño**
- **Trabajar con tecnologías modernas del ecosistema Java**
- **Implementar estrategias de prueba exhaustivas**
- **Gestionar proyectos full-stack con diferentes tecnologías**
- **Mantener una alta calidad de código y documentación**
- **Documentar sistemas de forma profesional con casos de uso y diagramas UML**

## 💼 ¿Por qué este proyecto demuestra mi experiencia como desarrollador backend?

Este proyecto es un ejemplo tangible de mi capacidad para:

### Diseñar e Implementar Sistemas Empresariales Completos
- Arquitectura en capas con separación clara de responsabilidades
- Implementación de buenas prácticas de desarrollo orientado a objetos
- Documentación extensa con casos de uso y diagramas UML

### Trabajar con Tecnologías Modernas del Ecosistema Java
- Desarrollo con Java 17 y frameworks Spring (Boot, Security, Data JPA)
- Implementación de seguridad robusta con JWT y Spring Security
- Persistencia de datos con JPA/Hibernate y PostgreSQL

### Aplicar Metodologías de Calidad y Control
- Desarrollo orientado a pruebas (TDD/BDD) con pruebas unitarias e integración
- Implementación de estrategias de validación y manejo de excepciones
- Uso de patrones de diseño y buenas prácticas de programación
- Documentación automática de API con Swagger/OpenAPI

## 📞 Cómo Contactarme

¿Interesado en evaluar mis habilidades técnicas con Java/Spring Boot? Puedes contactarme a través de mi perfil de GitHub o correo profesional.

---

<div align="center">

**MinaControl Pro** - Proyecto desarrollado por [Tu Nombre] | Portfolio de Desarrollador Java Backend

</div>
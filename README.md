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

**MinaControl Pro** es un sistema full-stack para la gestión de operaciones mineras. El backend está construido con Java y Spring Boot, aplicando una arquitectura en capas y patrones de diseño. El frontend es una aplicación de una sola página (SPA) construida con React y TypeScript.

> 🎯 **Este proyecto sirve como un portafolio profesional para demostrar habilidades en el desarrollo de aplicaciones empresariales con tecnologías Java/Spring Boot y React.**

## 🏛️ Arquitectura

La arquitectura general del sistema está documentada y se puede visualizar en el siguiente diagrama:

- **[Ver Diagrama de Arquitectura](./backend/docs/diagrams/general/architecture_overview.puml)**

## ✨ Características Principales

-   **Backend RESTful API**: Desarrollado con Spring Boot, Java 17 y principios de arquitectura limpia.
-   **Sistema de Autenticación Segura**: Implementación de JWT (JSON Web Tokens) con Spring Security.
-   **Gestión Completa de Dominios**: Empleados, Turnos, Producción, Logística, Nómina y Reportes.
-   **Arquitectura en Capas**: Clara separación de responsabilidades (Controller, Service, Repository).
-   **Persistencia de Datos**: JPA/Hibernate con PostgreSQL.
-   **Pruebas Automatizadas**: Desarrollo guiado por pruebas (TDD) con pruebas unitarias y de integración.
-   **Documentación de API**: Generación automática de documentación con SpringDoc OpenAPI.
-   **Frontend Moderno**: Interfaz de usuario reactiva construida con React y TypeScript.

## 🛠️ Tecnologías y Herramientas

### Backend
- **Java 17**
- **Spring Boot 3.2.0** (Spring Security, Spring Data JPA)
- **PostgreSQL** & **H2 Database** (para pruebas)
- **JWT** para autenticación stateless
- **MapStruct** para el mapeo de DTOs
- **Lombok** para la reducción de código boilerplate
- **JUnit 5 & Mockito** para pruebas
- **Maven** para la gestión de dependencias

### Frontend
- **React 19**
- **TypeScript**
- **Material UI**
- **React Hook Form** & **React Router DOM**
- **Vite** como build tool
- **ESLint & Prettier** para la calidad del código

## 📋 Prerrequisitos

Asegúrate de tener instalado el siguiente software:

-   Java 17 o superior
-   Maven 3.8 o superior
-   Node.js 18 o superior
-   npm 9 o superior
-   PostgreSQL (opcional, puedes usar la base de datos en memoria H2 para pruebas)

## 🚀 Cómo Empezar

### Backend (Java Spring Boot)

1.  **Navegar al directorio del backend:**
    ```bash
    cd backend
    ```
2.  **Ejecutar con Maven:**
    ```bash
    ./mvnw spring-boot:run
    ```
    La API estará disponible en `http://localhost:8080`.
    La documentación de Swagger UI se encontrará en `http://localhost:8080/swagger-ui/index.html`.

### Frontend (React + TypeScript)

1.  **Navegar al directorio del frontend:**
    ```bash
    cd frontend
    ```
2.  **Instalar dependencias:**
    ```bash
    npm install
    ```
3.  **Ejecutar en modo desarrollo:**
    ```bash
    npm run dev
    ```
    La aplicación estará disponible en `http://localhost:3000`.

### Pruebas

Para ejecutar las pruebas unitarias y de integración del backend, utiliza el siguiente comando desde el directorio `backend`:

```bash
./mvnw test
```

## 📚 Documentación

La documentación técnica detallada, incluyendo casos de uso y diagramas UML, se encuentra en el directorio `backend/docs`:

-   **[Casos de Uso](./backend/docs/casos_de_uso)**
-   **[Diagramas UML](./backend/docs/diagrams)**

## 📄 Licencia

Este proyecto está bajo la Licencia MIT. Consulta el archivo `LICENSE` para más detalles.

## 📞 Contacto

-   **Deivi Arismendi** - trianadeivi92@gmail.com
-   **Perfil de GitHub:** [Deivi92](https://github.com/Deivi92)

---

<div align="center">

**MinaControl Pro** - Un proyecto de portafolio de Deivi Arismendi

</div>

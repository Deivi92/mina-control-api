# Diagramas del Dominio Autenticación

## Propósito
Este dominio maneja el control de acceso y seguridad del sistema, incluyendo autenticación, autorización y gestión de contraseñas.

## Casos de Uso Base
- **CU-AUTH-001**: Iniciar sesión
- **CU-AUTH-002**: Cerrar sesión  
- **CU-AUTH-003**: Cambiar contraseña
- **CU-AUTH-004**: Recuperar contraseña

## Diagramas Incluidos

### ✅ Diagrama de Clases del Dominio
- **Archivo**: `class_diagram_autenticacion.puml`
- **Propósito**: Define la estructura de clases específica del dominio de autenticación
- **Contenido**: Entidades (Usuario), DTOs, Servicios, Controladores, Repositorios y componentes de seguridad
- **Estado**: ✅ Completado y alineado con ER y casos de uso

### ✅ Diagramas de Secuencia

### 1. sequence_login.puml
Diagrama de secuencia para el inicio de sesión (CU-AUTH-001):
- Autenticación con número de identificación y contraseña
- Generación de token JWT
- Manejo de credenciales incorrectas

### 2. sequence_logout.puml  
Diagrama de secuencia para el cierre de sesión (CU-AUTH-002):
- Invalidación del token actual
- Limpieza de sesión
- Confirmación de logout

### 3. sequence_cambiar_password.puml
Diagrama de secuencia para cambio de contraseña (CU-AUTH-003):
- Validación de contraseña actual
- Validación de nueva contraseña  
- Actualización segura de credenciales

### 4. sequence_recuperar_password.puml
Diagrama de secuencia para recuperación de contraseña (CU-AUTH-004):
- Solicitud de recuperación por parte del administrador
- Generación de contraseña temporal
- Notificación al empleado

### 5. sequence_refresh_token.puml
Diagrama de secuencia para renovación de token:
- Validación de refresh token
- Generación de nuevo access token
- Manejo de tokens expirados

### 6. sequence_registro.puml
Diagrama de secuencia para registro de usuarios:
- Creación de nuevo usuario en el sistema
- Validación de datos únicos
- Generación de credenciales iniciales

## Endpoints Representados
- `POST /auth/login` - Iniciar sesión
- `POST /auth/logout` - Cerrar sesión
- `POST /auth/cambiar-password` - Cambiar contraseña
- `POST /auth/recuperar-password` - Recuperar contraseña
- `POST /auth/refresh` - Renovar token
- `POST /auth/registro` - Registrar usuario

## Relación con Diagramas Generales
- Basado en el `class_diagram_completo.puml` y `er_diagram_completo.puml`
- Implementa los casos de uso definidos en `casos_de_uso_alto_nivel.md`
- Mantiene consistencia con la arquitectura de seguridad del sistema

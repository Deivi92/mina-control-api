# CU-PRO-003: Obtener Registro de Producción por ID

## 1. Dominio
Producción

## 2. Descripción
Permite a un usuario consultar los detalles completos de un registro de producción específico utilizando su ID único.

## 3. Actor(es)
*   Usuario Administrador
*   Usuario Gerente
*   Usuario Supervisor

## 4. Precondiciones
*   El usuario debe estar autenticado y tener los roles adecuados.
*   El registro de producción con el ID especificado debe existir en la base de datos.

## 5. Seguridad y Autorización
*   **Autenticación Requerida:** Sí
*   **Roles Permitidos:** `ADMINISTRADOR`, `GERENTE`, `SUPERVISOR`
*   **Condiciones de Propiedad:** N/A

## 6. Flujo Principal (Happy Path)
1.  El usuario envía una solicitud `GET` a `/api/produccion/{id}`.
2.  El sistema extrae el `id` de la URL.
3.  El servicio de producción busca en el repositorio un `RegistroProduccion` con ese `id`.
4.  Si se encuentra el registro, el servicio lo mapea a un `RegistroProduccionDTO`.
5.  El sistema devuelve una respuesta `200 OK` con el `RegistroProduccionDTO` solicitado.

## 7. Flujos Alternativos
*   N/A

## 8. Validaciones

### 8.1. Validaciones de Formato/Sintaxis (Capa de Controller)
*   El `id` proporcionado en la URL debe ser un tipo de dato numérico válido (Long).

### 8.2. Validaciones de Reglas de Negocio (Capa de Servicio)
*   El `id` debe corresponder a un registro de producción existente.

## 9. Flujos de Excepción

*   **9.1. Registro No Encontrado**
    *   **Condición:** No existe ningún registro de producción con el `id` proporcionado.
    *   **Excepción Java:** `RegistroProduccionNotFoundException`
    *   **Respuesta HTTP:** `404 Not Found`
    *   **Cuerpo de Respuesta (ErrorResponseDTO):**
        ```json
        {
            "timestamp": "...",
            "status": 404,
            "error": "Not Found",
            "message": "El registro de producción con ID {id} no fue encontrado.",
            "path": "/api/produccion/{id}"
        }
        ```

## 10. Postcondiciones
*   Se devuelve la información detallada del registro de producción solicitado.

## 11. DTOs Involucrados
*   **Request DTO:** N/A
*   **Response DTO:** `RegistroProduccionDTO`

## 12. Endpoint REST
*   **Método:** `GET`
*   **URL:** `/api/produccion/{id}`

## 13. Referencias
*   **Diagrama de Clases:** `docs/diagrams/produccion/class_diagram_produccion.puml`
*   **Diagrama ER:** `docs/diagrams/general/er_diagram_completo.puml`

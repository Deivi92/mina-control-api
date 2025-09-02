# Blueprint 01: Autenticación

Este documento describe el diseño y la experiencia de usuario para todos los flujos del dominio de Autenticación.

---

## Flujo 1: Iniciar Sesión (Login)

-   **Página:** `LoginPage.tsx`
-   **Diseño:** Un `Card` centrado con un ancho máximo de 400px.
-   **Componentes:** Título, campos de Email y Contraseña, botón de "Ingresar", spinner de carga y alerta de errores.
-   **Flujo de Usuario:**
    1.  Botón deshabilitado si los campos están vacíos.
    2.  Al enviar, se muestra un spinner y se deshabilita el botón (`Visibilidad del estado`).
    3.  En caso de éxito, se redirige al Dashboard.
    4.  En caso de fallo, se muestra un `Alert` con un error amigable (`Lenguaje del usuario`).
-   **Navegación:** Incluye enlaces a "Registrarse" y "¿Olvidaste tu contraseña?".

---

## Flujo 2: Registro de Usuario

-   **Página:** `RegisterPage.tsx`
-   **Diseño:** Un `Card` centrado, más ancho para acomodar más campos (ej. 600px).
-   **Componentes:** Título, campos para nombre, apellido, email, contraseña, etc., botón de "Registrarse", spinner y alertas.
-   **Flujo de Usuario:**
    1.  El botón se habilita solo cuando todos los campos requeridos están llenos (`Prevención de errores`).
    2.  Al enviar, se muestra un spinner.
    3.  En caso de éxito, el formulario se oculta y se muestra un `Alert` de éxito con un enlace para ir a la página de Login (`Feedback inmediato`).
    4.  En caso de fallo (ej. email duplicado), se muestra un `Alert` con el error específico.
-   **Navegación:** Incluye un enlace a "¿Ya tienes una cuenta? Inicia sesión".

---

## Flujo 3: Recuperación de Contraseña

Este flujo se divide en dos páginas.

### Paso 3.1: Solicitar Enlace de Recuperación

-   **Página:** `ForgotPasswordPage.tsx`
-   **Diseño:** Similar al Login, un `Card` simple y centrado.
-   **Componentes:** Título ("Recuperar Contraseña"), un campo de Email, botón de "Enviar Enlace", spinner y alertas.
-   **Flujo de Usuario:**
    1.  El usuario introduce su email y hace clic en enviar.
    2.  Se muestra un spinner.
    3.  **Siempre** se mostrará un mensaje de éxito genérico, como: "Si tu correo está registrado, recibirás un enlace en breve". Esto evita que un atacante pueda descubrir qué correos existen en el sistema (`Seguridad y Prevención de Errores`).

### Paso 3.2: Establecer Nueva Contraseña

-   **Página:** `ResetPasswordPage.tsx` (se accede desde el enlace en el correo).
-   **Diseño:** `Card` centrado.
-   **Componentes:** Título ("Nueva Contraseña"), campo para "Nueva Contraseña", campo para "Confirmar Contraseña", botón de "Guardar Contraseña", spinner y alertas.
-   **Flujo de Usuario:**
    1.  El botón de "Guardar" estará deshabilitado hasta que ambos campos estén llenos y sus valores coincidan (`Prevención de errores`).
    2.  Al enviar, se muestra un spinner.
    3.  En caso de éxito, se muestra un `Alert` de éxito y un enlace para ir al Login.
    4.  En caso de fallo (ej. token inválido), se muestra un error claro.

---

## Flujo 4: Cierre de Sesión (Logout)

-   **Página:** No es una página, es una acción.
-   **Diseño:** Debe ser un botón o una opción de menú claramente visible en las áreas protegidas de la aplicación (ej. en un menú de perfil en el header).
-   **Componentes:** `Button` o `MenuItem` con el texto "Cerrar Sesión".
-   **Flujo de Usuario:**
    1.  El usuario hace clic en "Cerrar Sesión".
    2.  La sesión local se destruye.
    3.  El usuario es redirigido inmediatamente a la `LoginPage` (`Control y libertad del usuario`).
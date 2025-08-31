# Blueprint 01: Autenticación

Este documento describe el diseño y la experiencia de usuario para el dominio de Autenticación, centrado en la `LoginPage`.

## 1. Wireframe y Diseño General

-   **Layout:** Una página con un `Card` de Material-UI centrado vertical y horizontalmente.
-   **Dimensiones:** El `Card` tendrá un ancho máximo de 400px para mantener la legibilidad en pantallas grandes.
-   **Estilo:** Minimalista, usando el tema por defecto de Material-UI.

## 2. Lista de Componentes

-   `Typography`: Para el título "Iniciar Sesión".
-   `TextField`: Un campo para "Correo Electrónico".
-   `TextField`: Un campo de tipo `password` para la "Contraseña".
-   `Button`: Un botón principal con el texto "Ingresar".
-   `CircularProgress`: Un spinner para indicar estados de carga.
-   `Alert`: Un componente para mostrar mensajes de error.

## 3. Flujo de Usuario y Criterios de Aceptación

1.  **Estado Inicial:** El usuario ve el formulario con los campos vacíos. El botón "Ingresar" está deshabilitado.
2.  **Interacción:** El usuario puede escribir en los campos. El botón "Ingresar" se habilita solo cuando ambos campos tienen contenido.
3.  **Envío:** Al hacer clic en "Ingresar", el botón se deshabilita y muestra un `CircularProgress` para indicar la carga.
4.  **Éxito:** Si las credenciales son válidas, el usuario es redirigido a la página principal (Dashboard).
5.  **Fallo:** Si las credenciales son inválidas, el `CircularProgress` desaparece, el botón se rehabilita y aparece un componente `Alert` con un mensaje de error claro y amigable.

## 4. Validación explícita de Usabilidad

Este diseño pone los principios de la `USABILITY_GUIDE.md` al servicio del dominio de Autenticación de la siguiente manera:

-   **Principio 1 (Visibilidad del estado):** Se cumple mediante el uso del `CircularProgress` y el estado deshabilitado del botón durante la carga. El usuario siempre sabe que el sistema está trabajando.
-   **Principio 3 (Consistencia y estándares):** Se cumple usando un diseño de formulario de login que es un estándar de facto en la web.
-   **Principio 4 (Reconocimiento antes que recuerdo):** Se cumple con etiquetas claras y un flujo obvio, sin requerir memorización.
-   **Principio 5 (Prevención de errores):** Se cumple deshabilitando el botón de envío si los campos están vacíos, previniendo errores innecesarios.
-   **Principio 8 (Lenguaje del usuario):** Se cumplirá mostrando mensajes de error como "Credenciales incorrectas" en lugar de jerga técnica.

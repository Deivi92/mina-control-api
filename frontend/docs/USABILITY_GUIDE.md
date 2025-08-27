# USABILITY_GUIDE.md

**Principios esenciales de usabilidad para el diseño de interfaces humanas efectivas**
*Basado en Nielsen, Shneiderman y Molich*


---

✅ **Principios Universales para una UI con Valor Humano**

1.  **Visibilidad del estado del sistema (Nielsen)**
    El sistema debe mantener al usuario informado sobre lo que ocurre, mediante retroalimentación inmediata y visual.
    > *Ejemplo: mostrar loading spinners, alertas, transiciones.*

2.  **Control y libertad del usuario (Nielsen, Shneiderman)**
    El usuario debe sentir que puede deshacer, rehacer, y elegir libremente su flujo de interacción.
    > *Ejemplo: botones de Deshacer, navegación no bloqueante, confirmaciones.*

3.  **Consistencia y estándares (Shneiderman, Molich)**
    Usar patrones visuales y de interacción ya conocidos por el usuario.
    > *Ejemplo: menús donde se esperan, colores coherentes, íconos universales.*

4.  **Reconocimiento antes que recuerdo (Nielsen)**
    Diseñar elementos que el usuario reconozca rápidamente, sin tener que memorizar.
    > *Ejemplo: etiquetas claras, autocompletado, botones con íconos conocidos.*

5.  **Prevención y recuperación de errores (Nielsen, Molich)**
    Anticipar errores antes que resolverlos, y cuando ocurran, ofrecer mensajes claros.
    > *Ejemplo: validaciones en formularios, mensajes sin tecnicismos, enfoque al campo con error.*

6.  **Feedback inmediato y útil (Shneiderman)**
    Cada acción del usuario debe generar una respuesta que indique que el sistema reaccionó.
    > *Ejemplo: mostrar "guardado exitoso", animación de botón presionado.*

7.  **Curva de aprendizaje progresiva (Nielsen, Shneiderman)**
    La interfaz debe ser amigable para novatos, pero eficiente para expertos.
    > *Ejemplo: teclas rápidas, menús ocultos para usuarios avanzados.*

8.  **Lenguaje del usuario (Molich)**
    Habla el idioma del usuario final. Nada de tecnicismos, códigos de error o jerga interna.
    > *Ejemplo: “Algo salió mal. Vuelve a intentarlo.” mejor que “ERROR 500 INTERNAL SERVER.”*

9.  **Evaluación constante y basada en datos (Molich)**
    No supongas que sabes lo que el usuario necesita: obsérvalo, pruébalo, mejóralo.
    > *Ejemplo: testeo temprano, heatmaps, entrevistas, logs de errores.*

10. **Diseño centrado en tareas, no en funciones**
    Prioriza las acciones que el usuario quiere completar, no lo que el sistema "puede hacer".
    > *Ejemplo: si el usuario quiere agendar una cita, no lo obligues a navegar por 4 secciones.*


---

⚡ **Aplicación práctica en React**

- Usa `useEffect`, `setState` y feedback visual para mantener visibilidad.
- Usa `react-toastify`, `alerts` o `snackbar` para mensajes útiles.
- Valida formularios con `Formik` + `Yup`, y enfoca campos con errores.
- Usa componentes reutilizables que respeten un design system coherente.
- Implementa `React Router` con navegación clara y no ambigua.
- Considera accesibilidad (`aria-*`, contraste, `tab focus`).
- Prepara `shortcuts` y `keyboard handlers` para flujos rápidos.
- Evalúa con usuarios (incluso no técnicos), revisa logs, adapta.


---

🎯 **Resultado esperado**

✅ Usuarios satisfechos
✅ Menor tasa de errores y soporte
✅ Mayor tasa de finalización de tareas
✅ Interfaz robusta y comprensible
✅ Producto más competitivo y valioso

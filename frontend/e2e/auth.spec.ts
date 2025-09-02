import { test, expect } from '@playwright/test';

test.describe('Autenticación', () => {
  // Hook para navegar a la página de login antes de cada test.
  // Asumimos que la página de login está en la raíz del sitio.
  test.beforeEach(async ({ page }) => {
    await page.goto('/');
  });

  // Escenario 1: Login Exitoso (Happy Path)
  test('debería permitir el login a un usuario con credenciales válidas y redirigir al dashboard', async ({ page }) => {
    // Interceptamos la llamada y forzamos una respuesta exitosa.
    await page.route('**/api/auth/login', async route => {
      await route.fulfill({
        status: 200,
        contentType: 'application/json',
        body: JSON.stringify({
          accessToken: 'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c',
          refreshToken: 'dummy-refresh-token'
        }),
      });
    });

    // Localizadores
    const emailInput = page.getByLabel('Correo Electrónico');
    const passwordInput = page.getByLabel('Contraseña');
    const loginButton = page.getByRole('button', { name: /ingresar/i });

    // Rellenamos el formulario y hacemos clic
    await emailInput.fill('usuario@valido.com');
    await passwordInput.fill('passwordCorrecta');
    await loginButton.click();

    // Verificación: Esperamos que la URL cambie a /dashboard
    // El router de la aplicación se encargará de esta redirección.
    await expect(page).toHaveURL('/dashboard');
    
    // Adicionalmente, verificamos que no haya un mensaje de error
    const errorMessage = page.getByRole('alert');
    await expect(errorMessage).not.toBeVisible();
  });

  // Escenario 2: Login Fallido (Credenciales Incorrectas)
  test('debería mostrar un mensaje de error con credenciales incorrectas', async ({ page }) => {
    // Interceptamos la llamada a la API de login y forzamos una respuesta de error 401.
    await page.route('**/api/auth/login', async route => {
      await route.fulfill({
        status: 401,
        contentType: 'application/json',
        body: JSON.stringify({
          timestamp: new Date().toISOString(),
          status: 401,
          error: 'Unauthorized',
          message: 'Usuario o contraseña incorrectos',
          path: '/api/auth/login'
        }),
      });
    });

    // Localizadores
    const emailInput = page.getByLabel('Correo Electrónico');
    const passwordInput = page.getByLabel('Contraseña');
    const loginButton = page.getByRole('button', { name: /ingresar/i });

    // Rellenamos el formulario y hacemos clic
    await emailInput.fill('usuario@incorrecto.com');
    await passwordInput.fill('passwordCualquiera');
    await loginButton.click();

    // Verificación
    const errorMessage = page.getByRole('alert');
    await expect(errorMessage).toBeVisible();
    await expect(errorMessage).toContainText('Usuario o contraseña incorrectos');
    
    // Verificamos que seguimos en la misma página
    await expect(page).toHaveURL('/');
  });

  // Escenario 3: Validación de Formulario (Prevención de Errores)
  test('debería tener el botón de login deshabilitado si los campos están vacíos', async ({ page }) => {
    // Localizadores de los elementos de la UI
    const emailInput = page.getByLabel('Correo Electrónico');
    const passwordInput = page.getByLabel('Contraseña');
    const loginButton = page.getByRole('button', { name: /ingresar/i });

    // 1. Verificar que el botón está deshabilitado al inicio
    await expect(loginButton).toBeDisabled();

    // 2. Rellenar solo el email y verificar que sigue deshabilitado
    await emailInput.fill('test@user.com');
    await expect(loginButton).toBeDisabled();

    // 3. Rellenar solo la contraseña (limpiando el email) y verificar
    await emailInput.clear();
    await passwordInput.fill('password123');
    await expect(loginButton).toBeDisabled();
    
    // 4. Rellenar ambos campos y verificar que el botón se habilita
    await emailInput.fill('test@user.com');
    await expect(loginButton).toBeEnabled();
  });

  // Escenario 4: Verificación del Estado de Carga (Visibilidad del Estado)
  test('debería mostrar un estado de carga durante el intento de login', async ({ page }) => {
    // Forzamos una respuesta lenta para tener tiempo de ver el estado de carga.
    await page.route('**/api/auth/login', async route => {
      // Introducimos un retraso para que el estado de carga sea visible.
      await new Promise(resolve => setTimeout(resolve, 300));
      route.fulfill({
        status: 401, // No importa si es éxito o error para este test
        contentType: 'application/json',
        body: JSON.stringify({ message: 'Error simulado' }),
      });
    });

    const emailInput = page.getByLabel('Correo Electrónico');
    const passwordInput = page.getByLabel('Contraseña');
    const loginButton = page.getByRole('button', { name: /ingresar/i });

    await emailInput.fill('test@user.com');
    await passwordInput.fill('password123');

    // Empezamos a esperar la respuesta de la API ANTES de hacer clic.
    const responsePromise = page.waitForResponse('**/api/auth/login');

    await loginButton.click();

    // Inmediatamente después del clic, verificamos el estado de carga.
    await expect(loginButton).toBeDisabled();
    await expect(page.getByRole('progressbar')).toBeVisible();

    // Esperamos a que la respuesta de la API llegue.
    await responsePromise;

    // Una vez que la respuesta ha llegado, el estado de carga debe desaparecer.
    await expect(page.getByRole('progressbar')).not.toBeVisible();
    await expect(loginButton).toBeEnabled();
  });
});

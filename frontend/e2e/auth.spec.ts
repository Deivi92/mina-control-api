import { test, expect } from '@playwright/test';

// Bloque principal para todo el dominio de Autenticación
test.describe('Dominio de Autenticación', () => {

  // --- Flujo de Login ---
  test.describe('Flujo de Login', () => {
    test.beforeEach(async ({ page }) => {
      await page.goto('/');
    });

    test('debería permitir el login y redirigir al dashboard', async ({ page }) => {
      await page.route('**/api/auth/login', route => route.fulfill({ status: 200, body: JSON.stringify({ accessToken: 'fake-token', refreshToken: 'fake-refresh-token' }) }));
      await page.getByLabel('Correo Electrónico').fill('user@test.com');
      await page.getByLabel('Contraseña').fill('password');
      await page.getByRole('button', { name: /ingresar/i }).click();
      await expect(page).toHaveURL('/dashboard');
    });

    test('debería mostrar un error con credenciales incorrectas', async ({ page }) => {
      await page.route('**/api/auth/login', route => route.fulfill({ status: 401, body: JSON.stringify({ message: 'Usuario o contraseña incorrectos' }) }));
      await page.getByLabel('Correo Electrónico').fill('user@test.com');
      await page.getByLabel('Contraseña').fill('wrong-password');
      await page.getByRole('button', { name: /ingresar/i }).click();
      await expect(page.getByRole('alert')).toContainText('Usuario o contraseña incorrectos');
    });
  });

  // --- Flujo de Registro ---
  test.describe('Flujo de Registro', () => {
    test.beforeEach(async ({ page }) => {
      await page.goto('/register');
    });

    test('debería permitir el registro de un nuevo usuario', async ({ page }) => {
      await page.route('**/api/auth/register', route => route.fulfill({ status: 201, body: JSON.stringify({ id: 1, email: 'new@user.com' }) }));
      await page.getByLabel('Nombre').fill('Nuevo');
      await page.getByLabel('Apellido').fill('Usuario');
      await page.getByLabel('Correo Electrónico').fill('new@user.com');
      await page.getByLabel('Contraseña').fill('password123');
      await page.getByLabel('Cédula').fill('123456');
      await page.getByLabel('Teléfono').fill('555-1234');
      await page.getByLabel('Cargo').fill('Tester');
      await page.getByRole('button', { name: /registrarse/i }).click();
      await expect(page.getByRole('alert')).toContainText('¡Registro exitoso!');
    });
  });

  // --- Flujo de Recuperar Contraseña ---
  test.describe('Flujo de Recuperar Contraseña', () => {
    test.beforeEach(async ({ page }) => {
      await page.goto('/forgot-password');
    });

    test('debería mostrar un mensaje de éxito genérico al solicitar recuperación', async ({ page }) => {
      await page.route('**/api/auth/forgot-password', route => route.fulfill({ status: 200 }));
      await page.getByLabel('Correo Electrónico').fill('any@user.com');
      await page.getByRole('button', { name: /enviar enlace/i }).click();
      await expect(page.getByRole('alert')).toContainText('recibirás un enlace para restablecer tu contraseña');
    });
    // Nota: Probar el flujo completo de reseteo (recibir email, hacer clic en enlace) 
    // está fuera del alcance de una prueba E2E estándar y requiere herramientas especializadas.
  });

  // --- Flujo de Logout ---
  test.describe('Flujo de Logout', () => {
    test('debería permitir a un usuario logueado cerrar sesión', async ({ page }) => {
      // 1. Primero, simulamos un login exitoso para tener una sesión.
      await page.goto('/');
      await page.route('**/api/auth/login', route => route.fulfill({ status: 200, body: JSON.stringify({ accessToken: 'fake-token', refreshToken: 'fake-refresh-token' }) }));
      await page.getByLabel('Correo Electrónico').fill('user@test.com');
      await page.getByLabel('Contraseña').fill('password');
      await page.getByRole('button', { name: /ingresar/i }).click();
      await expect(page).toHaveURL('/dashboard'); // Verificamos que estamos en el dashboard

      // 2. Ahora, hacemos clic en el botón de logout.
      await page.getByRole('button', { name: /cerrar sesión/i }).click();

      // 3. Verificamos que somos redirigidos a la página de login.
      await expect(page).toHaveURL('/');
      await expect(page.getByRole('heading', { name: /iniciar sesión/i })).toBeVisible();
    });
  });
});
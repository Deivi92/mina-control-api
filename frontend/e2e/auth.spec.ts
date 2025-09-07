import { test, expect } from '@playwright/test';

const testTimestamp = Date.now();
const uniqueRegisterEmail = `test.register.${testTimestamp}@test.com`;

test.describe('Autenticación E2E - Flujo Real', () => {

  test('debería permitir a un usuario válido hacer login y logout', async ({ page }) => {
    // --- FASE DE LOGIN ---
    await page.goto('/');
    await expect(page.getByRole('heading', { name: 'Iniciar Sesión' })).toBeVisible();

    // Usamos el usuario admin que fue creado por el globalSetup
    await page.getByLabel('Correo Electrónico').fill('admin@minacontrol.com');
    await page.getByLabel('Contraseña').fill('admin');
    await page.getByRole('button', { name: 'Ingresar' }).click();

    // Verificamos que la navegación post-login es exitosa
    await expect(page.getByRole('heading', { name: 'Dashboard' })).toBeVisible();
    await expect(page.getByRole('link', { name: 'Empleados' })).toBeVisible();

    // --- FASE DE LOGOUT ---
    await page.getByRole('button', { name: 'Cerrar Sesión' }).click();

    // Verificamos que somos redirigidos a la página de login
    await expect(page.getByRole('heading', { name: 'Iniciar Sesión' })).toBeVisible();
  });

  test('debería mostrar un error con credenciales incorrectas', async ({ page }) => {
    await page.goto('/');
    await page.getByLabel('Correo Electrónico').fill('admin@minacontrol.com');
    await page.getByLabel('Contraseña').fill('wrong-password');
    await page.getByRole('button', { name: 'Ingresar' }).click();

    // La API real debería devolver un error que el frontend muestre.
    // Asumimos que el error se muestra en un 'alert'.
    await expect(page.getByRole('alert')).toBeVisible();
  });

  test('debería permitir el registro de un nuevo usuario', async ({ page }) => {
    await page.goto('/register');
    
    // *** CAMPOS SIMPLIFICADOS ***
    await page.getByLabel('Correo Electrónico').fill(uniqueRegisterEmail);
    await page.getByLabel('Contraseña').fill('password123');
    await page.getByRole('button', { name: /registrarse/i }).click();

    // Esperar a que la redirección se complete y la página de login se cargue
    await page.waitForURL('**/login');
    
    // Después de un registro exitoso, la app debería redirigir a login
    // y mostrar un mensaje de éxito.
    await expect(page.getByRole('heading', { name: 'Iniciar Sesión' })).toBeVisible();
    // La prueba ahora también verifica el mensaje de éxito que se pasa en el estado de la navegación
    await expect(page.getByText('¡Registro exitoso! Por favor, inicia sesión.')).toBeVisible();
  });

});
import { test, expect } from '@playwright/test';

const testTimestamp = Date.now();
const uniqueRegisterEmail = `test.register.${testTimestamp}@test.com`;

test.describe('Autenticación E2E - Flujo Real', () => {

  test('debería permitir a un usuario válido hacer login y logout', async ({ page }) => {
    // --- FASE DE LOGIN ---
    await page.goto('/');
    await expect(page.getByRole('heading', { name: 'Iniciar Sesión' })).toBeVisible();

    // Usamos el usuario admin que fue creado por el globalSetup
    await page.getByLabel('Email').fill('admin@minacontrol.com');
    await page.getByLabel('Password').fill('admin');
    await page.getByRole('button', { name: 'Iniciar Sesión' }).click();

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
    await page.getByLabel('Email').fill('admin@minacontrol.com');
    await page.getByLabel('Password').fill('wrong-password');
    await page.getByRole('button', { name: 'Iniciar Sesión' }).click();

    // La API real debería devolver un error que el frontend muestre.
    // Asumimos que el error se muestra en un 'alert'.
    await expect(page.getByRole('alert')).toBeVisible();
  });

  test('debería permitir el registro de un nuevo usuario', async ({ page }) => {
    await page.goto('/register');
    
    await page.getByLabel('Nombre').fill('Test');
    await page.getByLabel('Apellido').fill('Register');
    await page.getByLabel('Email').fill(uniqueRegisterEmail);
    await page.getByLabel('Contraseña').fill('password123');
    await page.getByLabel('Cédula').fill('987654321');
    await page.getByLabel('Teléfono').fill('555-5555');
    await page.getByLabel('Cargo').fill('Registrado');
    await page.getByRole('button', { name: /registrarse/i }).click();

    // Después de un registro exitoso, la app debería redirigir a login
    // y mostrar un mensaje de éxito.
    await expect(page.getByRole('heading', { name: 'Iniciar Sesión' })).toBeVisible();
    await expect(page.getByText('¡Registro exitoso!')).toBeVisible();
  });

});

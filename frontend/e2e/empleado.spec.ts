import { test, expect } from '@playwright/test';

// Usamos un identificador único basado en el tiempo para cada ejecución del test.
// Esto asegura que no haya colisiones si los tests se ejecutan en paralelo o si datos de pruebas anteriores persisten.
const testTimestamp = Date.now();
const uniqueEmail = `test.empleado.${testTimestamp}@test.com`;
const uniqueId = `ID-${testTimestamp}`;

test.describe('Flujo de Gestión de Empleados', () => {
  
  // Antes de cada prueba en este fichero, nos aseguramos de estar logueados.
  // Esto asume que existe un usuario 'admin@minacontrol.com' con password 'admin' en la BBDD de pruebas.
  test.beforeEach(async ({ page }) => {
    await page.goto('/');
    await page.getByLabel('Correo Electrónico').fill('admin@minacontrol.com');
    await page.getByLabel('Contraseña').fill('admin');
    await page.getByRole('button', { name: 'Ingresar' }).click();
    // Esperamos a que el dashboard sea visible para confirmar que el login fue exitoso.
    await expect(page.getByRole('heading', { name: 'Dashboard' })).toBeVisible();
  });

  test('debería permitir crear, ver y eliminar un empleado', async ({ page }) => {
    // Asumimos que existe un enlace en la navegación para ir a la página de empleados.
    // Este es un punto que podría necesitar ajuste si el enlace tiene otro nombre.
    await page.getByRole('link', { name: 'Empleados' }).click();
    await expect(page.getByRole('heading', { name: 'Gestión de Empleados' })).toBeVisible();

    // --- FASE DE CREACIÓN ---
    await page.getByRole('button', { name: 'Crear Nuevo Empleado' }).click();

    // Rellenamos el formulario en el modal que debería haberse abierto.
    await expect(page.getByRole('heading', { name: 'Crear Nuevo Empleado' })).toBeVisible();
    await page.getByLabel('Nombre').fill('Test E2E');
    await page.getByLabel('Apellido').fill('Usuario');
    await page.getByLabel('Correo Electrónico').fill(uniqueEmail);
    await page.getByLabel('Número de Identificación').fill(uniqueId);
    await page.getByLabel('Fecha de Nacimiento').fill('1995-05-15');
    await page.getByLabel('Puesto').fill('Tester Automatizado');
    await page.getByLabel('Salario').fill('999');
    await page.getByRole('button', { name: 'Guardar' }).click();

    // --- FASE DE VERIFICACIÓN DE LA CREACIÓN ---
    // El modal debería cerrarse y el nuevo empleado debería ser visible en la tabla.
    // Usamos el email único para encontrarlo de forma fiable.
    await expect(page.getByRole('cell', { name: uniqueEmail })).toBeVisible();

    // --- FASE DE ELIMINACIÓN ---
    // Encontramos la fila del nuevo empleado y hacemos clic en su botón de eliminar.
    const newEmployeeRow = page.getByRole('row', { name: new RegExp(uniqueEmail) });
    await newEmployeeRow.getByRole('button', { name: 'eliminar' }).click();

    // Confirmamos la eliminación en el diálogo.
    await expect(page.getByRole('heading', { name: 'Confirmar Eliminación' })).toBeVisible();
    await page.getByRole('button', { name: 'Eliminar' }).click();

    // --- FASE DE VERIFICACIÓN DE LA ELIMINACIÓN ---
    // El diálogo debería cerrarse y el empleado ya no debería estar visible.
    await expect(page.getByRole('cell', { name: uniqueEmail })).not.toBeVisible();
  });
});

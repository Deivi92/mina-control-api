import axios from 'axios';

/**
 * Esta función se ejecuta una vez antes de que comience toda la suite de pruebas E2E.
 * Su propósito es asegurar que la base de datos de prueba esté en un estado conocido y limpio.
 */
async function globalSetup() {
  try {
    // La URL completa del backend cuando se ejecuta en el workflow de GitHub Actions.
    const setupEndpoint = 'http://localhost:8080/api/test-data/setup';
    
    console.log(`Ejecutando setup global: Llamando a ${setupEndpoint}...`);
    
    // Hacemos una llamada al backend para que siembre la base de datos.
    // Esto podría crear usuarios por defecto, limpiar tablas, etc.
    await axios.post(setupEndpoint);
    
    console.log('Setup global completado exitosamente.');
  } catch (error) {
    console.error('Error durante el setup global:', error);
    // Si el setup falla, es crítico que las pruebas no se ejecuten.
    // Lanzar un error aquí detendrá la ejecución de Playwright.
    throw new Error('El setup global de datos de prueba falló. Abortando tests.');
  }
}

export default globalSetup;

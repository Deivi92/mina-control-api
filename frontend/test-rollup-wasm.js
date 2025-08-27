// test-rollup-wasm.js
import { rollup } from '@rollup/wasm-node';

console.log('Intentando usar @rollup/wasm-node...');

// Una configuración mínima para probar
const inputOptions = {
  input: 'src/app/App.tsx' // Un archivo de entrada simple
};

const outputOptions = {
  file: 'dist/test-bundle.js',
  format: 'es'
};

async function build() {
  try {
    console.log('Creando bundle...');
    const bundle = await rollup(inputOptions);
    console.log('Bundle creado exitosamente.');
    await bundle.write(outputOptions);
    console.log('Bundle escrito a dist/test-bundle.js');
    await bundle.close();
  } catch (error) {
    console.error('Error en el build:', error);
  }
}

build();
/// <reference types="vitest" />
import { defineConfig } from 'vitest/config';
import react from '@vitejs/plugin-react';

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [react()],
  resolve: {
    alias: {
      '@mui/material/styles': '@mui/material/styles/index.js',
      '@mui/styled-engine': '@mui/styled-engine/index.js',
      '@mui/material/node': '@mui/material/node/index.js',
    },
  },
  define: {
    global: 'globalThis',
  },
  optimizeDeps: {
    exclude: ['@rollup/rollup-android-arm64'],
    include: [
      '@mui/material', 
      '@mui/material/Checkbox', 
      '@mui/material/Tooltip',
      '@mui/x-data-grid',
      '@mui/x-data-grid/utils',
      '@mui/x-data-grid/models',
      '@mui/x-data-grid/hooks',
      '@mui/x-data-grid/components',
      '@mui/x-data-grid/locales',
      '@mui/x-data-grid/colDef',
      '@mui/x-data-grid/constants',
    ],
  },
  test: {
    globals: true,
    environment: 'jsdom',
    setupFiles: ['./src/test/setup.ts'],
    exclude: ['node_modules', 'e2e/**'],
    testTimeout: 15000,
    hookTimeout: 15000,
    deps: {
      inline: [
        '@mui/material',
        '@mui/material/styles',
        '@mui/x-data-grid',
        '@mui',
      ],
    },
  },
  server: {
    proxy: {
      '/api': {
        target: 'http://localhost:8080',
        changeOrigin: true,
        secure: false,
      }
    }
  }
});
/// <reference types="vitest" />
import { defineConfig } from 'vitest/config';
import react from '@vitejs/plugin-react';

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [react()],
  optimizeDeps: {
    exclude: ['@rollup/rollup-android-arm64'],
  },
  test: {
    globals: true,
    environment: 'jsdom',
  },
});

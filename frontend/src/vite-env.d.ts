/// <reference types="vite/client" />

interface ImportMetaEnv {
  readonly VITE_BYPASS_AUTH: string;
  // Agrega otras variables de entorno aquí si las tienes
}

interface ImportMeta {
  readonly env: ImportMetaEnv;
}
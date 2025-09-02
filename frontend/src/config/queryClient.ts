import { QueryClient } from '@tanstack/react-query';

export const queryClient = new QueryClient({
  defaultOptions: {
    queries: {
      // Configuración por defecto para todas las queries
      staleTime: 1000 * 60 * 5, // 5 minutos
      refetchOnWindowFocus: false, // No refetch automático al volver al foco
      retry: 1, // Reintentar una vez si falla
    },
    mutations: {
      // Configuración por defecto para todas las mutations
      retry: 1, // Reintentar una vez si falla
    },
  },
});
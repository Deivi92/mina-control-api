import { useMutation } from '@tanstack/react-query';
import { register as registerService } from '../services/auth.service';
import type { RegistroUsuarioRequest, Usuario } from '../types';

export const useRegistration = () => {
  const {
    mutate: registerUser,
    isPending: isLoading,
    isSuccess,
    error,
  } = useMutation<Usuario, Error, RegistroUsuarioRequest>({
    mutationFn: registerService,
    // Opcional: aquí podríamos añadir lógica onSuccess, onError, etc.
    // Por ejemplo, para invalidar queries o mostrar notificaciones.
  });

  return {
    registerUser,
    isLoading,
    isSuccess,
    error,
  };
};

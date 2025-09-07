import { useMutation } from '@tanstack/react-query';
import { useNavigate } from 'react-router-dom';
import { register as registerService } from '../services/auth.service';
import type { RegistroUsuarioRequest, Usuario } from '../types';

export const useRegistration = () => {
  const navigate = useNavigate();

  const {
    mutate: registerUser,
    isPending: isLoading,
    isSuccess,
    error,
  } = useMutation<Usuario, Error, RegistroUsuarioRequest>({
    mutationFn: registerService,
    onSuccess: () => {
      // Redirigir a la página de login con un mensaje de éxito
      // Usar replace en lugar de navigate para evitar problemas de historial
      navigate('/login', {
        replace: true,
        state: { message: '¡Registro exitoso! Por favor, inicia sesión.' },
      });
    },
  });

  return {
    registerUser,
    isLoading,
    isSuccess,
    error,
  };
};

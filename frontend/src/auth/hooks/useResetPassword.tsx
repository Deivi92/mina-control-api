import { useMutation } from '@tanstack/react-query';
import { resetPassword as resetPasswordService } from '../services/auth.service';
import type { CambiarContrasenaRequest } from '../types';

export const useResetPassword = () => {
  const {
    mutate: resetPassword,
    isPending: isLoading,
    isSuccess,
    error,
  } = useMutation<void, Error, CambiarContrasenaRequest>({
    mutationFn: resetPasswordService,
  });

  return {
    resetPassword,
    isLoading,
    isSuccess,
    error,
  };
};

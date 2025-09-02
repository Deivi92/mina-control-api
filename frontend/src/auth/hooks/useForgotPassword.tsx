import { useMutation } from '@tanstack/react-query';
import { forgotPassword as forgotPasswordService } from '../services/auth.service';
import type { RecuperarContrasenaRequest } from '../types';

export const useForgotPassword = () => {
  const {
    mutate: forgotPassword,
    isPending: isLoading,
    isSuccess,
    error,
  } = useMutation<void, Error, RecuperarContrasenaRequest>({
    mutationFn: forgotPasswordService,
  });

  return {
    forgotPassword,
    isLoading,
    isSuccess,
    error,
  };
};

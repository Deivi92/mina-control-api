import { z } from 'zod';

export const resetPasswordSchema = z.object({
  newPassword: z.string().min(6, 'La nueva contraseña debe tener al menos 6 caracteres'),
  confirmPassword: z.string().min(1, 'Confirme la contraseña'),
});

export type ResetPasswordFormData = z.infer<typeof resetPasswordSchema>;
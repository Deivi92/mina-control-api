/**
 * DTO para la solicitud de login.
 * Coincide con LoginRequestDTO en el backend.
 */
export interface LoginRequest {
  email: string;
  password: string;
}
// Pequeño cambio para forzar la re-transpilación

/**
 * DTO para la respuesta de login.
 * Coincide con LoginResponseDTO en el backend.
 */
export interface LoginResponse {
  accessToken: string;
  refreshToken: string;
}

/**
 * DTO para el registro de un nuevo usuario.
 * Coincide con RegistroUsuarioCreateDTO en el backend.
 */
export interface RegistroUsuarioRequest {
  email: string;
  password: string;
}

/**
 * DTO para la información del usuario.
 * Coincide con UsuarioDTO en el backend.
 */
export interface Usuario {
  id: number;
  nombre: string;
  apellido: string;
  email: string;
  cedula: string;
  telefono: string;
  cargo: string;
}

/**
 * DTO para solicitar la actualización del token de acceso.
 * Coincide con RefreshTokenRequestDTO en el backend.
 */
export interface RefreshTokenRequest {
  refreshToken: string;
}

/**
 * DTO para la respuesta de la actualización del token.
 * Coincide con RefreshTokenResponseDTO en el backend.
 */
export interface RefreshTokenResponse {
  accessToken: string;
  refreshToken: string;
}

/**
 * DTO para la solicitud de recuperación de contraseña.
 * Coincide con RecuperarContrasenaRequestDTO en el backend.
 */
export interface RecuperarContrasenaRequest {
  email: string;
}

/**
 * DTO para la solicitud de logout.
 * Coincide con LogoutRequestDTO en el backend.
 */
export interface LogoutRequest {
  refreshToken: string;
}

/**
 * DTO para la solicitud de cambio de contraseña.
 * Coincide con CambiarContrasenaRequestDTO en el backend.
 */
export interface CambiarContrasenaRequest {
  oldPassword?: string;
  newPassword: string;
  token?: string;
}

export const TEST_EXPORT = 'hello';

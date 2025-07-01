package com.minacontrol.autenticacion.service;

import com.minacontrol.autenticacion.dto.request.LoginRequestDTO;
import com.minacontrol.autenticacion.dto.response.LoginResponseDTO;
import com.minacontrol.autenticacion.dto.request.RefreshTokenRequestDTO;
import com.minacontrol.autenticacion.dto.response.RefreshTokenResponseDTO;
import com.minacontrol.autenticacion.dto.request.RegistroUsuarioCreateDTO;
import com.minacontrol.autenticacion.dto.response.UsuarioDTO;

public interface IServicioAutenticacion {
    UsuarioDTO registrarUsuario(RegistroUsuarioCreateDTO registroUsuarioCreateDTO);
    LoginResponseDTO loginUsuario(LoginRequestDTO loginRequestDTO);
    void logoutUsuario(String refreshToken);
    RefreshTokenResponseDTO refreshToken(RefreshTokenRequestDTO refreshTokenRequestDTO);
}

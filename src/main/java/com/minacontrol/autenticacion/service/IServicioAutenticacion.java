package com.minacontrol.autenticacion.service;

import com.minacontrol.autenticacion.dto.LoginRequestDTO;
import com.minacontrol.autenticacion.dto.LoginResponseDTO;
import com.minacontrol.autenticacion.dto.RefreshTokenRequestDTO;
import com.minacontrol.autenticacion.dto.RefreshTokenResponseDTO;
import com.minacontrol.autenticacion.dto.RegistroUsuarioCreateDTO;
import com.minacontrol.autenticacion.dto.UsuarioDTO;

public interface IServicioAutenticacion {
    UsuarioDTO registrarUsuario(RegistroUsuarioCreateDTO registroUsuarioCreateDTO);
    LoginResponseDTO loginUsuario(LoginRequestDTO loginRequestDTO);
    void logoutUsuario(String refreshToken);
    RefreshTokenResponseDTO refreshToken(RefreshTokenRequestDTO refreshTokenRequestDTO);
}

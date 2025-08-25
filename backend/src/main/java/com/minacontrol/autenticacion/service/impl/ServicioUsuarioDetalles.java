package com.minacontrol.autenticacion.service.impl;

import com.minacontrol.autenticacion.model.Usuario;
import com.minacontrol.autenticacion.repository.UsuarioRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * Implementación de UserDetailsService de Spring Security.
 * Carga los datos del usuario desde la base de datos para la autenticación.
 */
@Service
public class ServicioUsuarioDetalles implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    public ServicioUsuarioDetalles(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con email: " + email));

        // Por simplicidad, asignamos un rol fijo.
        // En una implementación real, se obtendrían los roles del usuario desde la BD.
        // Aquí se asume que todos los usuarios tienen el rol EMPLEADO.
        // En el futuro, se podría ampliar para manejar múltiples roles basados en el empleado.
        List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_EMPLEADO"));

        return User.builder()
                .username(usuario.getEmail())
                .password(usuario.getPassword())
                .authorities(authorities)
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(false)
                .build();
    }
}
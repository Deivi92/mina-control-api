package com.minacontrol.shared.security.jwt;

import com.minacontrol.autenticacion.service.impl.ServicioUsuarioDetalles;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Filtro de Autenticación JWT personalizado.
 * Intercepta cada solicitud HTTP, extrae el token Bearer del encabezado Authorization,
 * lo valida y establece la autenticación en el contexto de seguridad de Spring.
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    private final JwtUtil jwtUtil;
    private final ServicioUsuarioDetalles servicioUsuarioDetalles;

    public JwtAuthenticationFilter(JwtUtil jwtUtil, ServicioUsuarioDetalles servicioUsuarioDetalles) {
        this.jwtUtil = jwtUtil;
        this.servicioUsuarioDetalles = servicioUsuarioDetalles;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        try {
            String jwt = parseJwt(request);

            if (jwt != null && jwtUtil.validateToken(jwt, servicioUsuarioDetalles.loadUserByUsername(jwtUtil.extractUsername(jwt)))) {
                String username = jwtUtil.extractUsername(jwt);

                UserDetails userDetails = servicioUsuarioDetalles.loadUserByUsername(username);
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            logger.error("No se pudo establecer la autenticación del usuario JWT: {}", e.getMessage());
        }

        filterChain.doFilter(request, response);
    }

    /**
     * Analiza el encabezado Authorization de la solicitud para extraer el token JWT.
     *
     * @param request La solicitud HTTP.
     * @return El token JWT si se encuentra y tiene el prefijo "Bearer ", de lo contrario null.
     */
    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");

        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7); // "Bearer " son 7 caracteres
        }

        return null;
    }
}
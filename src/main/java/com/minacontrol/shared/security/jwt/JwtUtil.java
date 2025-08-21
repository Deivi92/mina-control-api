package com.minacontrol.shared.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Utilidad para generar, validar y manipular tokens JWT.
 * Basada en la librería JJWT.
 */
@Component
public class JwtUtil {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);

    private final SecretKey secretKey;
    private final long accessTokenExpirationMs;
    private final long refreshTokenExpirationMs;

    public JwtUtil(
            @Value("${app.jwtSecret}") String jwtSecret,
            @Value("${app.jwtAccessTokenExpirationMs}") long accessTokenExpirationMs,
            @Value("${app.jwtRefreshTokenExpirationMs}") long refreshTokenExpirationMs
    ) {
        this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
        this.accessTokenExpirationMs = accessTokenExpirationMs;
        this.refreshTokenExpirationMs = refreshTokenExpirationMs;
    }

    // --- Generación de Tokens ---

    /**
     * Genera un Access Token JWT para un usuario.
     *
     * @param userDetails Los detalles del usuario autenticado.
     * @return El Access Token JWT firmado.
     */
    public String generateAccessToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        // Se pueden añadir claims personalizados aquí si es necesario
        return createToken(claims, userDetails.getUsername(), accessTokenExpirationMs);
    }

    /**
     * Genera un Refresh Token JWT para un usuario.
     *
     * @param userDetails Los detalles del usuario autenticado.
     * @return El Refresh Token JWT firmado.
     */
    public String generateRefreshToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        // Se pueden añadir claims personalizados aquí si es necesario
        return createToken(claims, userDetails.getUsername(), refreshTokenExpirationMs);
    }

    /**
     * Método auxiliar para crear un token JWT.
     *
     * @param claims            Claims personalizados.
     * @param subject           El sujeto del token (normalmente el username/email).
     * @param expirationTimeMs  Tiempo de expiración en milisegundos.
     * @return El token JWT firmado.
     */
    private String createToken(Map<String, Object> claims, String subject, long expirationTimeMs) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expirationTimeMs))
                .signWith(secretKey, SignatureAlgorithm.HS512)
                .compact();
    }

    // --- Validación y Extracción de Claims ---

    /**
     * Extrae el nombre de usuario del token JWT.
     *
     * @param token El token JWT.
     * @return El nombre de usuario contenido en el token.
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Extrae la fecha de expiración del token JWT.
     *
     * @param token El token JWT.
     * @return La fecha de expiración.
     */
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Método genérico para extraer un claim específico del token.
     *
     * @param token          El token JWT.
     * @param claimsResolver Función para resolver el claim.
     * @param <T>            Tipo del claim.
     * @return El valor del claim.
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Extrae todos los claims del token JWT.
     *
     * @param token El token JWT.
     * @return Un objeto Claims con todos los datos del token.
     */
    private Claims extractAllClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            logger.warn("JWT token expirado: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.warn("JWT token no soportado: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            logger.warn("JWT token mal formado: {}", e.getMessage());
        } catch (SignatureException e) {
            logger.warn("Firma JWT inválida: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.warn("JWT claims string vacía: {}", e.getMessage());
        }
        // Si ocurre una excepción, devolvemos null o lanzamos una excepción personalizada.
        // Para simplificar, dejamos que las excepciones JJWT se propaguen.
        // En una implementación más robusta, se crearían excepciones personalizadas.
        throw new RuntimeException("Error al parsear el JWT");
    }

    /**
     * Verifica si un token ha expirado.
     *
     * @param token El token JWT.
     * @return true si ha expirado, false en caso contrario.
     */
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Valida un token JWT.
     *
     * @param token        El token JWT.
     * @param userDetails  Los detalles del usuario para comparar.
     * @return true si el token es válido y pertenece al usuario, false en caso contrario.
     */
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}
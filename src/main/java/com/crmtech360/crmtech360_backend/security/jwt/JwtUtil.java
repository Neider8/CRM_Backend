package com.crmtech360.crmtech360_backend.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtUtil {

    private static final Logger log = LoggerFactory.getLogger(JwtUtil.class);

    @Value("${jwt.secret}")
    private String jwtSecretString;

    @Value("${jwt.expiration.ms}")
    private long jwtExpirationMs;

    private SecretKey jwtSecretKey;

    @PostConstruct
    public void init() {
        byte[] keyBytes = jwtSecretString.getBytes(StandardCharsets.UTF_8);
        // Para HS512, la clave debe tener 64 bytes (512 bits).
        // Keys.hmacShaKeyFor generará una clave segura si la original es muy corta,
        // pero es mejor proveer una clave de la longitud adecuada.
        if (keyBytes.length < 64) {
            log.warn("La clave secreta JWT configurada ({} bytes) es más corta de lo recomendado para HS512 (64 bytes). " +
                    "Se utilizará una clave derivada, pero se recomienda una clave fuerte y de longitud adecuada en producción.", keyBytes.length);
        }
        this.jwtSecretKey = Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(Authentication authentication) {
        UserDetails userPrincipal = (UserDetails) authentication.getPrincipal();
        return generateTokenFromUserDetails(userPrincipal);
    }

    public String generateTokenFromUserDetails(UserDetails userDetails) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationMs);

        List<String> authorities = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        log.debug("Generando token para usuario: {} con {} autoridades. Expiración: {}", userDetails.getUsername(), authorities.size(), expiryDate);

        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .claim("authorities", authorities)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(jwtSecretKey, SignatureAlgorithm.HS512)
                .compact();
    }

    public String getUsernameFromToken(String token) {
        return getClaims(token).getSubject();
    }

    @SuppressWarnings("unchecked")
    public List<String> getAuthoritiesFromToken(String token) {
        return getClaims(token).get("authorities", List.class);
    }

    public boolean validateToken(String authToken) {
        if (authToken == null || authToken.isBlank()) {
            log.warn("Intento de validar un token nulo o vacío.");
            return false;
        }
        try {
            Jwts.parserBuilder()
                    .setSigningKey(jwtSecretKey)
                    .build()
                    .parseClaimsJws(authToken);
            return true;
        } catch (SignatureException ex) {
            log.error("Validación JWT fallida: Firma inválida -> {}", ex.getMessage());
        } catch (MalformedJwtException ex) {
            log.error("Validación JWT fallida: Token malformado -> {}", ex.getMessage());
        } catch (ExpiredJwtException ex) {
            log.warn("Validación JWT fallida: Token expirado -> {}", ex.getMessage()); // Loguear expirado como WARN
        } catch (UnsupportedJwtException ex) {
            log.error("Validación JWT fallida: Token no soportado -> {}", ex.getMessage());
        } catch (IllegalArgumentException ex) {
            log.error("Validación JWT fallida: Argumento ilegal o claims vacíos -> {}", ex.getMessage());
        }
        return false;
    }

    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(jwtSecretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
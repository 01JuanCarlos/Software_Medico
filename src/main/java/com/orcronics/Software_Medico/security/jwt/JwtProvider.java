package com.orcronics.Software_Medico.security.jwt;

import com.orcronics.Software_Medico.security.dto.JwtDto;
import com.orcronics.Software_Medico.security.entity.UsuarioPrincipal;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.security.core.GrantedAuthority;


import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.authority.SimpleGrantedAuthority;


@Component
public class JwtProvider {

    private final static Logger logger = LoggerFactory.getLogger(JwtProvider.class);

    // Generación de una clave secreta segura para HS512 (512 bits)
    private final SecretKey secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS512);
    private static final long EXPIRATION_TIME = 28800000; // 8 horas (en milisegundos)
   //private static final long EXPIRATION_TIME = 20000; // 1 minuto (en milisegundos)

    private static final String TOKEN_PREFIX = "Bearer ";
    private static final String HEADER_STRING = "Authorization";

    // Método para generar un token JWT
    public String generateToken(Authentication authentication) {
        UsuarioPrincipal usuarioPrincipal = (UsuarioPrincipal) authentication.getPrincipal();
        List<String> roles = usuarioPrincipal.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return Jwts.builder()
                .setSubject(authentication.getName()) // Establece el nombre de usuario como el sujeto
                .claim("roles", roles) // Incluye los roles como claim

                .setIssuedAt(new Date()) // Fecha de emisión
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)) // Fecha de expiración
                .signWith(secretKey) // Firma con la clave secreta
                .compact();
    }

    // Método para obtener el nombre de usuario desde el token
    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(secretKey) // Establecer la clave secreta para verificar la firma
                .build()
                .parseClaimsJws(token.replace(TOKEN_PREFIX, ""))
                .getBody();
        return claims.getSubject(); // Obtener el sujeto (nombre de usuario)
    }

    // Método para verificar si el token ha expirado
    public boolean isTokenExpired(String token) {
        try {
            Date expirationDate = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token.replace(TOKEN_PREFIX, ""))
                    .getBody()
                    .getExpiration();
            return expirationDate.before(new Date());
        } catch (ExpiredJwtException e) {
            logger.error("Token expirado: {}", e.getMessage());
            return true;
        }
    }

    // Método para validar el token completo
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token.replace(TOKEN_PREFIX, "")); // Valida la firma y el contenido
            System.out.println("Token Validado y Firmado");
            return true;
        } catch (MalformedJwtException e) {
            logger.error("Token mal formado: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("Token no soportado: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("Token expirado: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("Token vacío o nulo: {}", e.getMessage());
        } catch (SignatureException e) {
            logger.error("Firma del token no válida: {}", e.getMessage());
        }
        return false; // Retorna falso si ocurre alguna excepción
    }

    public String refreshToken(JwtDto jwtDto) {
        // Obtén el token desde el DTO
        String token = jwtDto.getToken();

        // Analiza y valida el token existente
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(secretKey) // La clave secreta utilizada para firmar
                .build()
                .parseClaimsJws(token) // Analiza el token
                .getBody(); // Extrae las claims

        // Extrae la información relevante de las claims
        String nombreUsuario = claims.getSubject(); // El subject (nombre de usuario)
        List<String> roles = claims.get("roles", List.class); // Roles como una lista

        // Genera un nuevo token con la misma información pero nueva expiración
        return Jwts.builder()
                .setSubject(nombreUsuario)
                .claim("roles", roles)
                .setIssuedAt(new Date()) // Fecha actual
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)) // Nueva expiración
                .signWith(SignatureAlgorithm.HS512, secretKey) // Firma con HS512
                .compact();
    }
    public Authentication getAuthentication(String token) {
        // Obtener el nombre de usuario del token
        String username = getUsernameFromToken(token);

        // Obtener los roles del token (asumimos que los roles están en el claim "roles")
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token.replace(TOKEN_PREFIX, ""))
                .getBody();

        List<String> roles = claims.get("roles", List.class);

        // Convertir los roles en GrantedAuthority
        List<GrantedAuthority> authorities = roles.stream()
                .map(SimpleGrantedAuthority::new)  // Asumimos que los roles son simples strings
                .collect(Collectors.toList());

        // Crear y devolver un Authentication
        UserDetails userDetails = new User(username, "", authorities);  // Aquí puedes modificar si quieres agregar más detalles
        return new UsernamePasswordAuthenticationToken(userDetails, "", authorities);
    }



}

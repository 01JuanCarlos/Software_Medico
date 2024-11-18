package com.orcronics.Software_Medico.security.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsPasswordService;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JwtTokentFilter extends OncePerRequestFilter {

    private final static Logger logger = LoggerFactory.getLogger(JwtProvider.class);

    @Autowired
    JwtProvider jwtProvider;
    @Autowired
    UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain filterChain) throws ServletException, IOException {
        try {
            // Obtener el token de la cabecera de la solicitud
            String token = getToken(req);

            // Si el token no es nulo y es válido
            if (token != null && jwtProvider.validateToken(token)) {
                // Obtener el nombre de usuario desde el token
                String nombreUsuario = jwtProvider.getUsernameFromToken(token);  // Asegúrate de usar el nombre de método correcto

                // Cargar los detalles del usuario a partir del nombre de usuario
                UserDetails userDetails = userDetailsService.loadUserByUsername(nombreUsuario);

                // Crear el objeto de autenticación con los detalles del usuario y sus roles
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );

                // Establecer la autenticación en el contexto de seguridad
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        } catch (Exception e) {
            // Captura cualquier excepción y loguea el error
            logger.error("Error en el método doFilter: ", e);
        }

        // Continúa con la cadena de filtros
        filterChain.doFilter(req, res);
    }



    private String getToken(HttpServletRequest request){
        String header = request.getHeader("Authorization");
        if(header != null && header.startsWith("Bearer")){
            return  header.replace("Bearer","");
        }
        return null;
    }
}

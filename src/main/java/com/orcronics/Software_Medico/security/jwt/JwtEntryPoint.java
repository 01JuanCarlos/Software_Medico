package com.orcronics.Software_Medico.security.jwt;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JwtEntryPoint implements AuthenticationEntryPoint {

    private final static Logger logger = LoggerFactory.getLogger(JwtEntryPoint.class);

    @Override
    public void commence(HttpServletRequest req, HttpServletResponse res, AuthenticationException authException) throws IOException, ServletException {
        try {
            logger.error("fail en el método commence: {}", authException.getMessage());
            res.sendError(HttpServletResponse.SC_UNAUTHORIZED, "No autorizado");
        } catch (IOException e) {
            logger.error("Error al enviar la respuesta de error: {}", e.getMessage());
            throw new ServletException("Error interno del servidor", e); // O maneja según tu lógica
        }
    }

}

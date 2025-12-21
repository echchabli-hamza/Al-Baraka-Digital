package com.AlBaraka.demo.security.exeptions;

import com.AlBaraka.demo.dto.ApiErrorResponse;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.io.IOException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException
    ) throws IOException, java.io.IOException {

        String message = "Unauthorized";

        if (authException.getCause() instanceof ExpiredJwtException) {
            message = "JWT token expired";
        } else if (authException.getCause() instanceof JwtException) {
            message = "Invalid JWT token";
        }

        ApiErrorResponse error = new ApiErrorResponse();
        error.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        error.setError("UNAUTHORIZED");
        error.setMessage(message);
        error.setPath(request.getRequestURI());
        error.setTimestamp(LocalDateTime.now());

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.getWriter().write(
                new ObjectMapper().writeValueAsString(error)
        );
    }
}

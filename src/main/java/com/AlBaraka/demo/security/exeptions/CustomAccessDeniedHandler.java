package com.AlBaraka.demo.security.exeptions;

import com.AlBaraka.demo.dto.ApiErrorResponse;
import io.jsonwebtoken.io.IOException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(
            HttpServletRequest request,
            HttpServletResponse response,
            AccessDeniedException accessDeniedException
    ) throws IOException, java.io.IOException {

        ApiErrorResponse error = new ApiErrorResponse();
        error.setStatus(HttpServletResponse.SC_FORBIDDEN);
        error.setError("FORBIDDEN");
        error.setMessage("Access denied: insufficient permissions");
        error.setPath(request.getRequestURI());
        error.setTimestamp(LocalDateTime.now());

        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json");
        response.getWriter().write(
                new ObjectMapper().writeValueAsString(error)
        );
    }
}

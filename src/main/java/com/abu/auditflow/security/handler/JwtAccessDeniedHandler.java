package com.abu.auditflow.security.handler;

import java.io.IOException;

import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import com.abu.auditflow.security.dto.SecurityErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAccessDeniedHandler
        implements AccessDeniedHandler {

    private final ObjectMapper objectMapper;

    public JwtAccessDeniedHandler(
            ObjectMapper objectMapper) {

        this.objectMapper = objectMapper;
    }

    @Override
    public void handle(
            HttpServletRequest request,
            HttpServletResponse response,
            AccessDeniedException exception)
            throws IOException {

        response.setStatus(
                HttpServletResponse.SC_FORBIDDEN);

        response.setContentType(
                MediaType.APPLICATION_JSON_VALUE);

        objectMapper.writeValue(

                response.getOutputStream(),

                new SecurityErrorResponse(

                        HttpServletResponse.SC_FORBIDDEN,

                        "FORBIDDEN",

                        "Access denied",

                        request.getRequestURI()));
    }
}
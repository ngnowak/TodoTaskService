package com.rest.todo.infrastructure.configuration;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.time.LocalDateTime;

@Component
public class RequestResponseLoggingFilter implements HandlerInterceptor {
    private final Logger logger = LoggerFactory.getLogger(RequestResponseLoggingFilter.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // Log the request details here
        logger.info("[" + LocalDateTime.now() + "]" + "Request URL: " + request.getRequestURL());
        logger.info("Request Method: " + request.getMethod());

        // You can log additional information such as headers, parameters, etc.
        return true; // Return true to allow the request to proceed
    }
}

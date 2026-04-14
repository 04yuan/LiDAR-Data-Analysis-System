package com.training.lidar.config;

import com.training.lidar.common.ApiException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthHeaderInterceptor implements HandlerInterceptor {

    private final boolean authRequired;

    public AuthHeaderInterceptor(@Value("${app.auth.required:true}") boolean authRequired) {
        this.authRequired = authRequired;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String requestUri = request.getRequestURI();
        if (!authRequired || requestUri.startsWith("/actuator/health") || requestUri.startsWith("/error")) {
            return true;
        }

        String authorization = request.getHeader("Authorization");
        if (!StringUtils.hasText(authorization) || !authorization.startsWith("Bearer ")) {
            throw new ApiException(40100, "missing or invalid Authorization header");
        }
        return true;
    }
}

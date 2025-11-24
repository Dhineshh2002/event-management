package com.example.eventmanager.common.aspect;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Component
@Slf4j
public class ApiTraceInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse resp, Object handler) {
        log.info("Request start: {} {} [IP:{}]", req.getMethod(), req.getRequestURI(), req.getRemoteAddr());
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest req, HttpServletResponse resp, Object handler, ModelAndView mv) {
        log.info("Handler processed: {} {}", req.getMethod(), req.getRequestURI());
    }

    @Override
    public void afterCompletion(HttpServletRequest req, HttpServletResponse resp, Object handler, Exception ex) {
        log.info("Response: {} {}, Status: {}, Exception: {}",
                req.getMethod(), req.getRequestURI(), resp.getStatus(),
                ex != null ? ex.getMessage() : "None");
    }
}


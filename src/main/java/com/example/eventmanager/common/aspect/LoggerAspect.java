package com.example.eventmanager.common.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class LoggerAspect {

    @Before(
            "execution(* com.example.eventmanager.modules.user.service.impl.*.*(..)) || " +
                    "execution(* com.example.eventmanager.modules.registration.service.impl.*.*(..)) || " +
                    "execution(* com.example.eventmanager.modules.event.service.impl.*.*(..)) || " +
                    "execution(* com.example.eventmanager.modules.email.service.impl.*.*(..))"
    )
    public void methodCalled(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        log.info("{}: method was called", methodName);
    }
}
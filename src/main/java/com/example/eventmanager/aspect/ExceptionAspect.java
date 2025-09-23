package com.example.eventmanager.aspect;

import com.example.eventmanager.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class ExceptionAspect {

    private final EmailService emailService;

    @AfterThrowing(
            pointcut = "execution(* com.example.eventmanager.service.impl.*.*(..))"
                    + "&& !execution(* com.example.eventmanager.service.impl.EmailServiceImpl.sendExceptionToAdmin(..))",
            throwing = "e"
    )
    public void exceptionThrown(JoinPoint joinPoint, Exception e) {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        log.error("Exception thrown from {} - {}() - message: {}",
                className, methodName, e.getMessage(), e);
        emailService.sendExceptionToAdmin(className, methodName, e);
    }
}

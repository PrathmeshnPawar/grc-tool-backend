package com.grctool.config;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import com.grctool.model.BaseEntity;
import com.grctool.service.auditService.AuditLogService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class AuditLogAspect {

    private final AuditLogService auditLogService;

    // Senior Move: Target repositories but EXCLUDE the AuditLogRepository to prevent infinite recursion
    @AfterReturning(
        pointcut = "execution(* org.springframework.data.repository.Repository+.save(..)) " +
                   "&& !target(com.grctool.repository.AuditLogRepository)", 
        returning = "result"
    )
    public void logAutoPersistence(JoinPoint joinPoint, Object result) {
        try {
            if (result instanceof BaseEntity entity) { 
                auditLogService.logChange(
                    entity, 
                    "Automated persistence log for " + entity.getClass().getSimpleName(), 
                    null 
                );
                log.debug("AOP: Successfully captured {} save", entity.getClass().getSimpleName());
            }
        } catch (Exception e) {
            log.error("AOP Logging Failed: {}", e.getMessage());
        }
    }
}
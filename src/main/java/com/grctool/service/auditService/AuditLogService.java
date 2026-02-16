package com.grctool.service.auditService;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.grctool.model.AuditLog;
import com.grctool.model.BaseEntity;
import com.grctool.model.User;
import com.grctool.repository.AuditLogRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuditLogService {
    private final AuditLogRepository auditLogRepository;

    /**
     * Senior Tip: This runs on the Main Request Thread. 
     * We capture metadata here because RequestContext is available.
     */
    public void logChange(BaseEntity entity, String details, User user) {
        // 1. Capture metadata from the current request thread
        jakarta.servlet.http.HttpServletRequest request = 
            ((org.springframework.web.context.request.ServletRequestAttributes) 
            org.springframework.web.context.request.RequestContextHolder.getRequestAttributes())
            .getRequest();

        String ip = request.getRemoteAddr();
        String ua = request.getHeader("User-Agent");
        String sid = request.getSession().getId();

        // 2. Call the async method with all 8 required arguments
        logAction(
            entity.getClass().getSimpleName(), 
            entity.getId(), 
            "UPDATE", 
            details, 
            user, 
            ip, 
            ua, 
            sid
        );
    }

    /**
     * This runs on a separate Background Thread.
     */
    @Async
    public void logAction(String entityName, java.util.UUID entityId, String action, 
                          String details, User user, String ip, String ua, String sid) {
        AuditLog log = new AuditLog();
        log.setEntityName(entityName);
        log.setEntityId(entityId);
        log.setAction(action);
        log.setChangeDetails(details);
        log.setPerformedBy(user);
        
        // Use the metadata passed from the main thread
        log.setIpAddress(ip);
        log.setUserAgent(ua);
        log.setSessionId(sid);

        auditLogRepository.save(log);
    }
}
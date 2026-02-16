package com.grctool.config;

import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.grctool.service.auditService.AuditLogService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AuthEventsListener {
    private final AuditLogService auditLogService;

    @EventListener
    public void onFailure(AbstractAuthenticationFailureEvent event) {
        // 1. Capture request metadata on the current thread
        String[] metadata = getRequestMetadata();
        
        // 2. Pass all 8 arguments to the service
        auditLogService.logAction(
            "SYSTEM", 
            null, 
            "LOGIN_FAILURE", 
            "Reason: " + event.getException().getMessage(), 
            null, 
            metadata[0], // IP
            metadata[1], // UA
            metadata[2]  // SID
        );
    }

    @EventListener
    public void onSuccess(AuthenticationSuccessEvent event) {
        String[] metadata = getRequestMetadata();
        
        auditLogService.logAction(
            "SYSTEM", 
            null, 
            "LOGIN_SUCCESS", 
            "New session established", 
            null, 
            metadata[0], 
            metadata[1], 
            metadata[2]
        );
    }

    /**
     * Helper to safely extract metadata from the current web request.
     */
    private String[] getRequestMetadata() {
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attrs != null) {
            HttpServletRequest request = attrs.getRequest();
            return new String[] {
                request.getRemoteAddr(),
                request.getHeader("User-Agent"),
                request.getSession().getId()
            };
        }
        return new String[] {"unknown", "unknown", "unknown"};
    }
}
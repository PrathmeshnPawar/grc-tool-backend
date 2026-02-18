package com.grctool.config;

import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.grctool.model.User;
import com.grctool.repository.UserRepository;
import com.grctool.service.auditService.AuditLogService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class AuthEventsListener {

    private final AuditLogService auditLogService;
    private final UserRepository userRepository; // Added to identify the "Who"

    @EventListener
    public void onFailure(AbstractAuthenticationFailureEvent event) {
        String username = event.getAuthentication().getName();
        String[] metadata = getRequestMetadata();
        
        // Use "User" as entity name even on failure for consistent filtering
        auditLogService.logAction(
            "User", 
            null, 
            "AUTH_FAILURE", 
            "Failed login attempt for user: " + username + " | Reason: " + event.getException().getMessage(), 
            null, 
            metadata[0], 
            metadata[1], 
            metadata[2]
        );
        log.warn("Security: Authentication failure for user {} from IP {}", username, metadata[0]);
    }

    @EventListener
    public void onSuccess(AuthenticationSuccessEvent event) {
        String email = event.getAuthentication().getName();
        String[] metadata = getRequestMetadata();
        
        // Senior Move: Fetch the user entity to create a hard link in the Audit Log
        User user = userRepository.findByEmail(email).orElse(null);

        auditLogService.logAction(
            "User", 
            user != null ? user.getId() : null, 
            "AUTH_SUCCESS", 
            "User session established successfully", 
            user, 
            metadata[0], 
            metadata[1], 
            metadata[2]
        );
        log.info("Security: Authentication success for user {}", email);
    }

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
        return new String[] {"0.0.0.0", "Internal-System", "N/A"};
    }
}
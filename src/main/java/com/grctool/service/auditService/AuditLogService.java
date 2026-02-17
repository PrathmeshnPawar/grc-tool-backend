package com.grctool.service.auditService;

import com.grctool.dto.audit.AuditLogResponseDTO; // Updated import
import com.grctool.model.AuditLog;
import com.grctool.model.BaseEntity;
import com.grctool.model.User;
import com.grctool.repository.AuditLogRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuditLogService {

    private final AuditLogRepository auditLogRepository;

    public List<AuditLogResponseDTO> getAllLogs() {
        return auditLogRepository
            .findAll(Sort.by(Sort.Direction.DESC, "createdAt"))
            .stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    private AuditLogResponseDTO convertToDTO(AuditLog log) {
        return new AuditLogResponseDTO(
            log.getId(),
            log.getEntityName(),
            log.getEntityId(),
            log.getAction(),
            log.getChangeDetails(),
            log.getPerformedBy() != null
                ? log.getPerformedBy().getName()
                : "System",
            log.getIpAddress(),
            log.getUserAgent(),
            log.getCreatedAt()
        );
    }

    /**
     * Captures metadata from the current request thread.
     */
    public void logChange(BaseEntity entity, String details, User user) {
        jakarta.servlet.http.HttpServletRequest request = (
            (org.springframework.web.context.request.ServletRequestAttributes) org.springframework.web.context.request.RequestContextHolder.getRequestAttributes()
        ).getRequest();

        String ip = request.getRemoteAddr();
        String ua = request.getHeader("User-Agent");
        String sid = request.getSession().getId();

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
     * Persists the log on a separate background thread.
     */
    @Async
    public void logAction(
        String entityName,
        java.util.UUID entityId,
        String action,
        String details,
        User user,
        String ip,
        String ua,
        String sid
    ) {
        AuditLog log = new AuditLog();
        log.setEntityName(entityName);
        log.setEntityId(entityId);
        log.setAction(action);
        log.setChangeDetails(details);
        log.setPerformedBy(user);
        log.setIpAddress(ip);
        log.setUserAgent(ua);
        log.setSessionId(sid);

        auditLogRepository.save(log);
    }
}

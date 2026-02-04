package com.grctool.service.auditService;

import org.springframework.stereotype.Service;

import com.grctool.model.AuditLog;
import com.grctool.model.BaseEntity;
import com.grctool.model.User;
import com.grctool.repository.AuditLogRepository;

@Service
public class AuditLogService {
    private final AuditLogRepository auditLogRepository;

    public AuditLogService(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    public void logChange(BaseEntity entity, String details, User user) {
        AuditLog log = new AuditLog();
        log.setEntityName(entity.getClass().getSimpleName());
        log.setEntityId(entity.getId());
        log.setAction("UPDATE");
        log.setChangeDetails(details);
        log.setPerformedBy(user);
        auditLogRepository.save(log);
    }
}
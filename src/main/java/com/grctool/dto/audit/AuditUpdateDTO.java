package com.grctool.dto.audit;

import java.time.LocalDate;

public record AuditUpdateDTO(
        String name,
        LocalDate startDate,
        LocalDate endDate
) {}

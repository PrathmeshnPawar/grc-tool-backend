package com.grctool.enums;

public enum IncidentStatus {
    OPEN,
    REPORTED,        // Incident logged
    IN_PROGRESS,     // Being investigated
    MITIGATED,       // Temporary fix applied
    RESOLVED,        // Fully resolved
    CLOSED           // Reviewed & formally closed
}

package com.grctool.dto.compliance;

import java.util.Optional;

public record ComplianceFrameworkUpdateDTO(
        Optional<String> name,
        Optional<String> version,
        Optional<String> description

) {
}

package com.grctool.dto.risk;

import java.util.UUID;

import com.grctool.enums.RiskCategory;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RiskCreateDTO(
                @NotBlank(message = "Title is required") @Size(max = 200, message = "Title must be 200 characters or less") String title,
                @Size(max = 2000, message = "Description must be 2000 characters or less") String description,
                @NotNull(message = "Category is required") RiskCategory category,
                @Min(value = 1, message = "Impact must be between 1 and 5") @Max(value = 5, message = "Impact must be between 1 and 5") int impact,
                @Min(value = 1, message = "Likelihood must be between 1 and 5") @Max(value = 5, message = "Likelihood must be between 1 and 5") int likelihood,
                UUID ownerId,
                UUID incidentId

) {
}

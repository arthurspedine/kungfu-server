package com.spedine.server.api.dto;

import com.spedine.server.domain.entity.EBelt;
import jakarta.validation.constraints.NotNull;

public record BeltDataDTO(
        @NotNull
        EBelt type,
        @NotNull
        String achievedDate
) {
}

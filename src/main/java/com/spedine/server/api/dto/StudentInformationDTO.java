package com.spedine.server.api.dto;

import com.spedine.server.domain.entity.ESex;
import jakarta.validation.constraints.NotNull;

public record StudentInformationDTO(
        @NotNull
        String name,
        @NotNull
        String birthDate,
        @NotNull
        ESex sex
) {
}

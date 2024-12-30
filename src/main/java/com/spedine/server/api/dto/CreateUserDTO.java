package com.spedine.server.api.dto;

import com.spedine.server.domain.entity.ERole;
import jakarta.validation.constraints.NotNull;

public record CreateUserDTO(
        @NotNull
        StudentInformationDTO student,
        @NotNull
        ERole role,
        @NotNull
        BeltDataDTO belt,
        @NotNull
        LoginBodyDTO login
) {
}

package com.spedine.server.api.dto;

import com.spedine.server.domain.entity.ERole;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record CreateUserDTO(
        @NotNull
        StudentInformationDTO student,
        @NotNull
        ERole role,
        @NotNull
        List<BeltDataDTO> belts,
        @NotNull
        LoginBodyDTO login
) {
}

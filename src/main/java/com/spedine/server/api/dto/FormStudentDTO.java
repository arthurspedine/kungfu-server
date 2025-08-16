package com.spedine.server.api.dto;

import com.spedine.server.domain.entity.ERole;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public record FormStudentDTO(
        @NotNull
        StudentInformationDTO student,
        CreateUserDTO user,
        @NotNull
        List<BeltDataDTO> belts,
        UUID trainingCenterId
) {
        public record CreateUserDTO(
                @NotNull
                LoginBodyDTO login,
                @NotNull
                ERole role
        ) {}
}

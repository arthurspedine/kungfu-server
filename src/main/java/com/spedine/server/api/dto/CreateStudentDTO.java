package com.spedine.server.api.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CreateStudentDTO(
        @NotNull
        StudentInformationDTO student,
        @NotNull
        BeltDataDTO belt,
        @NotNull
        UUID trainingCenterId
) {
}

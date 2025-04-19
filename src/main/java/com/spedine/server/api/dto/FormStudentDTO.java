package com.spedine.server.api.dto;

import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public record FormStudentDTO(
        @NotNull
        StudentInformationDTO student,
        @NotNull
        List<BeltDataDTO> belts,
        @NotNull
        UUID trainingCenterId
) {
}

package com.spedine.server.dto;

import com.spedine.server.api.dto.StudentInformationDTO;

import java.util.UUID;

public record TrainingCenterStudentDTO (
        UUID studentId,
        StudentInformationDTO student,
        String currentBelt
) {
}

package com.spedine.server.dto;

import java.util.UUID;

public record StudentInfoDTO(
        UUID id,
        String name,
        String birthDate,
        Integer age,
        String sex,
        String currentBelt,
        Integer beltAgeMonths,
        String trainingCenter
) {
}

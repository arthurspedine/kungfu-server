package com.spedine.server.dto;

import java.util.UUID;

public record TrainingCenterSimpleInfoDTO(
        UUID id,
        String name,
        String teacherName
) {
}

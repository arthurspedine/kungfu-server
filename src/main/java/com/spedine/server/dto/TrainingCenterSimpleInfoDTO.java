package com.spedine.server.dto;

import com.spedine.server.domain.entity.TrainingCenter;

import java.util.UUID;

public record TrainingCenterSimpleInfoDTO(
        UUID id,
        String name,
        String teacherName
) {
    public static TrainingCenterSimpleInfoDTO build(TrainingCenter trainingCenter) {
        if (trainingCenter == null) {
            return null;
        }
        return new TrainingCenterSimpleInfoDTO(
                trainingCenter.getId(),
                trainingCenter.getName(),
                trainingCenter.getTeacher().getStudent().getName()
        );
    }
}

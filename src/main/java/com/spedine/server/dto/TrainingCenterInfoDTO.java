package com.spedine.server.dto;

import java.util.List;

public record TrainingCenterInfoDTO(
        TrainingCenterDTO trainingCenter,
        List<TrainingCenterStudentDTO> students
) {
}

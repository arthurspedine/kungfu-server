package com.spedine.server.dto;

import java.util.List;

public record TrainingCenterDetailsDTO(
        TrainingCenterInfoDTO trainingCenter,
        List<TrainingCenterStudentDTO> students
) {
}

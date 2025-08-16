package com.spedine.server.dto;

import com.spedine.server.api.dto.StudentInformationDTO;

import java.util.List;
import java.util.UUID;

public record StudentDetailsDTO(
        UUID id,
        StudentInformationDTO student,
        List<BeltInfoDTO> belts,
        TrainingCenterSimpleInfoDTO trainingCenter
) {
}

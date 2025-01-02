package com.spedine.server.dto;

import java.util.UUID;

public record TrainingCenterDTO(
        UUID id,
        TeacherDTO teacher,
        Integer studentsNumber,
        String name,
        String street,
        Integer number,
        String city,
        String state,
        String zipCode
) {
}


package com.spedine.server.dto;

import java.util.UUID;

public record TeacherDTO(
        UUID id, String name, String currentBelt, String sex
) {
}

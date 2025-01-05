package com.spedine.server.dto;

import java.util.UUID;

public record StudentBeltDTO(
        UUID id,
        UUID studentId,
        String belt,
        String achivedDate
) {
}
